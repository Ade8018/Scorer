package gd.zh.gamer.scorer.ui.activity;

import gd.zh.gamer.scorer.App;
import gd.zh.gamer.scorer.R;
import gd.zh.gamer.scorer.db.AccountDao;
import gd.zh.gamer.scorer.db.DaoMaster;
import gd.zh.gamer.scorer.db.DaoSession;
import gd.zh.gamer.scorer.db.PrinterDao;
import gd.zh.gamer.scorer.db.RecordDao;
import gd.zh.gamer.scorer.decode.QrDecodeStr;
import gd.zh.gamer.scorer.decode.QrPrinterBase;
import gd.zh.gamer.scorer.decode.QrPrinterValueTicket;
import gd.zh.gamer.scorer.entity.Account;
import gd.zh.gamer.scorer.entity.Printer;
import gd.zh.gamer.scorer.entity.Record;
import gd.zh.gamer.scorer.util.DaoUtil;
import gd.zh.gamer.scorer.util.QrCodeUtil;
import gd.zh.gamer.scorer.util.SpHelper;
import gd.zh.gamer.scorer.util.ToastUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ScanResultActivity extends Activity implements OnClickListener {
	public static final String INTENT_EXTRA_KEY_SCAN_RESULT = "scan_result";
	private Account account;
	private String scanText;
	private QrDecodeStr decoder;
	private TextView tv;
	private Button btn2Scan;
	private Button btn2Menu;
	private DaoMaster dm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan_result);

		dm = new DaoMaster(App.db);

		getResultAndCheck();

		initViews();

		decoder = QrDecodeStr.getInstance();

		account = DaoUtil.getCurrentAccount();

		if (!Account.isLogined(account)) {
			if (!onAuthOperator())
				print("未知的二维码");
			return;
		}

		if (Account.isManager(account) && onPrinter()) {
			return;
		}

		if (!onTicket()) {
			print("未知的二维码");
		}
	}

	private void getResultAndCheck() {
		scanText = getIntent().getStringExtra(INTENT_EXTRA_KEY_SCAN_RESULT);
		if (TextUtils.isEmpty(scanText)) {
			ToastUtil.shortToast(this, "扫描出错，请重试");
			finish();
			return;
		}
	}

	private void initViews() {
		tv = (TextView) findViewById(R.id.tv_scan_result);
		btn2Menu = (Button) findViewById(R.id.btn_scan_result_to_menu);
		btn2Menu.setOnClickListener(this);
		btn2Scan = (Button) findViewById(R.id.btn_scan_result_to_scan);
		btn2Scan.setOnClickListener(this);
	}

	/**
	 * @return return true if it's an authorization no matter it's successful or
	 *         not.
	 */
	private boolean onAuthOperator() {
		// account/password/printer sns
		String result = QrCodeUtil.authEncode(scanText);
		String[] arr = result.split(",");
		if (arr.length < 3) {
			return false;
		}
		String acc = null;
		if (arr[0].startsWith("!")) {
			acc = arr[0].substring(1);
		}
		String pwd = null;
		if (arr[1].startsWith("@")) {
			pwd = arr[1].substring(1);
		}
		String regCode = null;
		if (arr[2].startsWith("#")) {
			regCode = arr[2].substring(1);
		}
		List<Printer> ps = new ArrayList<Printer>();
		for (int i = 3; i < arr.length; i++) {
			String[] parr = arr[i].split("|");
			if (parr.length != 2) {
				return false;
			}
			ps.add(new Printer(null, parr[1], parr[0], 0));
		}

		if (!isRightRegisterCode(regCode)) {
			return false;
		}
		if (!Account.isValidAccountOrPwd(acc)
				|| !Account.isValidAccountOrPwd(pwd)) {
			return false;
		}

		Account a = new Account(null, acc, pwd, Account.TYPE_OPERATOR);
		DaoSession ds = dm.newSession();
		AccountDao accountDao = ds.getAccountDao();
		long insertResult = accountDao.insert(a);

		if (insertResult > 0 && savePrinters(ps)) {
			print("注册成功!");
			btn2Menu.setVisibility(View.VISIBLE);
		} else {
			print("注册失败，请重试!");
		}
		return true;
	}

	private boolean isRightRegisterCode(String regCode) {
		return ManagerBindActivity.REGISTER_CODE.equals(regCode);
	}

	private boolean savePrinters(List<Printer> ps) {
		if (ps.size() < 1) {
			return true;
		}
		DaoSession ds = dm.newSession();
		PrinterDao pd = ds.getPrinterDao();

		try {
			pd.insertInTx(ps);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private boolean onPrinter() {
		QrPrinterBase pb = decoder.decodeQrPrinterCode(scanText);
		if (pb == null)
			return false;

		print("识别到打印机：" + pb.getQrPrinterSn());

		String sn = pb.getQrPrinterSn();
		DaoSession ds = dm.newSession();
		PrinterDao pd = ds.getPrinterDao();
		if (pd.queryBuilder().where(PrinterDao.Properties.Sn.eq(sn)).count() > 0) {
			print("此打印机已绑定过");
			return true;
		}

		long time = getPrintTimeByTimeString(pb.getQrPrinterTime());
		if (time == 0) {
			print("无法绑定打印机，非法的二维码打印时间");
			return true;
		}

		String nickName = SpHelper.getAndIncreasePrinterNickName(this);
		if (nickName == null) {
			print("无法绑定打印机，请稍后再试");
			return true;
		}

		Printer printer = new Printer(null, sn, nickName, time);
		if (pd.insert(printer) > 0)
			print("绑定打印机" + nickName + "成功!");
		else
			print("绑定打印机失败,请稍后重试");
		return true;
	}

	private boolean onTicket() {
		QrPrinterValueTicket ticket = decoder.decodeQrValueCode(scanText);
		if (ticket == null) {
			return false;
		}

		print("识别到积分券：" + ticket.toString());

		long pid = getPrinterId(ticket.getQrPrinterSn());
		if (pid < 1 || !isRightPin(ticket.getPin())) {
			print("无法兑换积分券，未知的打印机");
			return true;
		}

		Record rec = new Record();
		rec.setPrinter_id(pid);
		rec.setExc_time(System.currentTimeMillis());
		rec.setPin(ticket.getPin());
		rec.setScore(ticket.getValue() * ticket.getRatioX()
				+ ticket.getAddtionValueN());
		long print_time = getPrintTimeByTimeString(ticket.getQrPrinterTime());
		if (print_time == 0) {
			print("无法兑换积分券，未知的日期");
			return true;
		}
		rec.setPrint_time(print_time);

		DaoSession ds = dm.newSession();
		RecordDao rd = ds.getRecordDao();
		if (rd.insert(rec) > 0) {
			print("兑换成功!");
		} else {
			print("兑换失败，请稍后重试。");
		}

		return true;
	}

	SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmss", Locale.CHINA);

	/**
	 * time string should be 161215235959 when it's 2016.12.15 23:59:59
	 * 
	 * @param timeStr
	 * @return
	 */
	private long getPrintTimeByTimeString(String timeStr) {
		if (TextUtils.isEmpty(timeStr) || timeStr.length() != 12) {
			return 0;
		}
		Date d = null;
		try {
			d = format.parse(timeStr);
		} catch (ParseException e) {
			e.printStackTrace();
			return 0;
		}
		return d.getTime();
	}

	private boolean isRightPin(String pin) {
		return TextUtils.equals(pin, SpHelper.getCachePin(this));
	}

	private long getPrinterId(String sn) {
		if (TextUtils.isEmpty(sn))
			throw new IllegalArgumentException();
		DaoSession ds = dm.newSession();
		PrinterDao pd = ds.getPrinterDao();
		List<Printer> printers = pd.queryBuilder()
				.where(PrinterDao.Properties.Sn.eq(sn)).list();
		int count = printers.size();
		if (count > 1 || count < 0)
			throw new RuntimeException();
		return printers.get(0).getId();
	}

	private void print(String text) {
		tv.setText(tv.getText() + "\n" + text);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_scan_result_to_menu:
			Intent intent = new Intent(this, RegisterActivity.class);
			startActivity(intent);
		case R.id.btn_scan_result_to_scan:
			finish();
			break;
		}
	}

}

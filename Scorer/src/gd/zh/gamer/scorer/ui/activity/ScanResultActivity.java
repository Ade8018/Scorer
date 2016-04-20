package gd.zh.gamer.scorer.ui.activity;

import gd.zh.gamer.scorer.App;
import gd.zh.gamer.scorer.R;
import gd.zh.gamer.scorer.db.AccountDao;
import gd.zh.gamer.scorer.db.DaoMaster;
import gd.zh.gamer.scorer.db.DaoSession;
import gd.zh.gamer.scorer.db.PrinterDao;
import gd.zh.gamer.scorer.decode.QrDecodeStr;
import gd.zh.gamer.scorer.decode.QrPrinterBase;
import gd.zh.gamer.scorer.decode.QrPrinterValueTicket;
import gd.zh.gamer.scorer.entity.Account;
import gd.zh.gamer.scorer.entity.Printer;
import gd.zh.gamer.scorer.entity.Record;
import gd.zh.gamer.scorer.util.DaoUtil;
import gd.zh.gamer.scorer.util.QrCodeUtil;
import gd.zh.gamer.scorer.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

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
	private AccountDao accountDao;
	private QrDecodeStr decoder;
	private TextView tv;
	private Button btn2Scan;
	private Button btn2Menu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan_result);

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
		List<String> pns = new ArrayList<String>();
		for (int i = 3; i < arr.length; i++) {
			pns.add(arr[i]);
		}

		if (!isRightRegisterCode(regCode)) {
			return false;
		}
		if (!Account.isValidAccountOrPwd(acc)
				|| !Account.isValidAccountOrPwd(pwd)) {
			return false;
		}

		Account a = new Account(null, acc, pwd, Account.TYPE_OPERATOR);
		long insertResult = accountDao.insert(a);
		if (insertResult > 0 && savePrinters(pns)) {
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

	private boolean savePrinters(List<String> pns) {
		// TODO save printers
		return false;
	}

	private boolean onPrinter() {
		QrPrinterBase pb = decoder.decodeQrPrinterCode(scanText);
		if (pb == null)
			return false;

		print("识别到打印机：" + pb.toString() + ",打印机未保存");
		// TODO

		return true;
	}

	private boolean onTicket() {
		QrPrinterValueTicket ticket = decoder.decodeQrValueCode(scanText);
		if (ticket == null) {
			return false;
		}

		print("识别到积分券：" + ticket.toString());

		long pid = getPrinterId(ticket.getQrPrinterSn());
		if (pid < 1) {
			return false;
		}

		Record rec = new Record();
		rec.setExc_time(System.currentTimeMillis());
		// ticket.getQrPrinterTime()
		// rec.setPrint_time();
		rec.setPrinter_id(pid);

		// TODO
		return true;
	}

	private long getPrinterId(String sn) {
		if (TextUtils.isEmpty(sn))
			throw new IllegalArgumentException();
		DaoMaster dm = new DaoMaster(App.db);
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
		tv.setText(text);
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

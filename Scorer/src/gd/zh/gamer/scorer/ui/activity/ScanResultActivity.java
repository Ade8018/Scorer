package gd.zh.gamer.scorer.ui.activity;

import gd.zh.gamer.scorer.App;
import gd.zh.gamer.scorer.db.AccountDao;
import gd.zh.gamer.scorer.db.DaoMaster;
import gd.zh.gamer.scorer.db.DaoSession;
import gd.zh.gamer.scorer.decode.QrDecodeStr;
import gd.zh.gamer.scorer.decode.QrPrinterBase;
import gd.zh.gamer.scorer.decode.QrPrinterValueTicket;
import gd.zh.gamer.scorer.entity.Account;
import gd.zh.gamer.scorer.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

public class ScanResultActivity extends Activity {
	public static final String INTENT_EXTRA_KEY_SCAN_RESULT = "scan_result";
	private Account account;
	private String scanText;
	private AccountDao accountDao;
	private QrDecodeStr decoder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		decoder = QrDecodeStr.getInstance();

		DaoMaster dm = new DaoMaster(App.db);
		DaoSession ds = dm.newSession();
		accountDao = ds.getAccountDao();
		List<Account> accs = accountDao.loadAll();
		if (accs != null && accs.size() == 1) {
			account = accs.get(0);
		}

		scanText = getIntent().getStringExtra(INTENT_EXTRA_KEY_SCAN_RESULT);
		if (TextUtils.isEmpty(scanText)) {
			ToastUtil.shortToast(this, "扫描出错，请重试");
			finish();
			return;
		}

		if (!isLogined(account)) {
			onAuthOperator();
			return;
		}
		if (isManager(account) && onPrinter()) {
			return;
		}
		if (!onTicket()) {
			ToastUtil.shortToast(this, "未知的二维码");
			finish();
		}
	}

	private void onAuthOperator() {
		// account/password/printer sns
		byte[] buf = scanText.getBytes();
		for (int i = 0; i < buf.length; i++) {
			buf[i] ^= i + 0x40;
		}
		String result = new String(buf);
		String[] arr = result.split(",");
		String acc = null;
		if (arr[0].startsWith("!@#$")) {
			acc = arr[0].substring(4);
		}
		String pwd = null;
		if (arr[1].startsWith("%^&*")) {
			pwd = arr[1].substring(4);
		}
		String regCode = null;
		if (arr[1].startsWith("()_+")) {
			regCode = arr[2].substring(4);
		}
		List<String> pns = new ArrayList<String>();
		for (int i = 3; i < arr.length; i++) {
			pns.add(arr[i]);
		}

		if (!ManagerBindActivity.REGISTER_CODE.equals(regCode)) {
			ToastUtil.shortToast(this, "注册操作员出错，注册码错误");
			finish();
			return;
		}
		if (!isValidAccountOrPwd(acc) || !isValidAccountOrPwd(pwd)) {
			ToastUtil.shortToast(this, "注册操作员出错，非法账号或密码");
			finish();
			return;
		}

		Account a = new Account(null, acc, pwd, Account.TYPE_OPERATOR);
		long insertResult = accountDao.insert(a);
		if (insertResult > 0) {
			ToastUtil.shortToast(this, "注册成功!");
			savePrinter(pns);

			Intent intent = new Intent(this, RegisterActivity.class);
			startActivity(intent);
			finish();
			return;
		} else {
			ToastUtil.shortToast(this, "注册失败，请重试");
			finish();
			return;
		}
	}

	private void savePrinter(List<String> pns) {
		// TODO save printer
	}

	public static boolean isValidAccountOrPwd(String text) {
		if (TextUtils.isEmpty(text) || text.length() < 4) {
			return false;
		}
		if (!text.matches("[A-Za-z0-9_]+")) {
			return false;
		}
		return true;
	}

	private boolean onPrinter() {
		QrPrinterBase pb = decoder.decodeQrPrinterCode(scanText);
		if (pb == null)
			return false;

		ToastUtil.shortToast(this, "扫描到打印机：" + pb.toString() + ",打印机未保存");
		// TODO

		return true;
	}

	private boolean onTicket() {
		QrPrinterValueTicket ticket = decoder.decodeQrValueCode(scanText);
		if (ticket == null) {
			return false;
		}
		ToastUtil.shortToast(this, "扫描到兑换券：" + ticket.toString() + ",兑换券未保存");
		// TODO
		return true;
	}

	public static boolean isLogined(Account account) {
		return account != null;
	}

	public static boolean isManager(Account account) {
		return isLogined(account) && account.getType() == Account.TYPE_MANAGER;
	}
}

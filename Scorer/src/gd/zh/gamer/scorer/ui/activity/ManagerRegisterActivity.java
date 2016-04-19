package gd.zh.gamer.scorer.ui.activity;

import gd.zh.gamer.scorer.App;
import gd.zh.gamer.scorer.R;
import gd.zh.gamer.scorer.db.AccountDao;
import gd.zh.gamer.scorer.db.DaoMaster;
import gd.zh.gamer.scorer.db.DaoSession;
import gd.zh.gamer.scorer.entity.Account;
import gd.zh.gamer.scorer.util.ToastUtil;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class ManagerRegisterActivity extends Activity implements
		OnClickListener {
	private EditText etAccount;
	private EditText etPwd;
	private EditText etRegCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_manager);

		etAccount = (EditText) findViewById(R.id.et_register_manager_account);
		etPwd = (EditText) findViewById(R.id.et_register_manager_pwd);
		etRegCode = (EditText) findViewById(R.id.et_register_manager_register_code);

		findViewById(R.id.btn_register_manager_register).setOnClickListener(
				this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_register_manager_register:
			onRegister();
			break;

		default:
			break;
		}
	}

	private void onRegister() {
		String account = etAccount.getText().toString();
		if (isValidAccountOrPwd(account)) {
			ToastUtil.shortToast(this, "请输入不少于4位的账号(数字、字母和下划线组合)");
			return;
		}
		String pwd = etPwd.getText().toString();
		if (isValidAccountOrPwd(pwd)) {
			ToastUtil.shortToast(this, "请输入不少于4位的密码(数字、字母和下划线组合)");
			return;
		}
		String regCode = etRegCode.getText().toString();
		if (TextUtils.isEmpty(regCode) || regCode.length() != 8) {
			ToastUtil.shortToast(this, "请输入8位的注册码");
			return;
		}

		Account acc = new Account(null, account, pwd, Account.TYPE_MANAGER);
		saveAccount(acc);
	}

	private void saveAccount(Account acc) {
		DaoMaster dm = new DaoMaster(App.db);
		DaoSession ds = dm.newSession();
		AccountDao ad = ds.getAccountDao();

		List<Account> accs = ad.queryBuilder()
				.where(AccountDao.Properties.Type.eq(Account.TYPE_MANAGER))
				.list();
		if (accs != null && accs.size() > 0) {
			ToastUtil.shortToast(this, "已注册过管理员，不允许再注册");
			return;
		}

		long result = ad.insert(acc);
		if (result > 0) {
			ToastUtil.shortToast(this, "注册成功");
			finish();
		} else {
			ToastUtil.shortToast(this, "注册失败");
		}
	}

	private boolean isValidAccountOrPwd(String text) {
		if (TextUtils.isEmpty(text) || text.length() < 4) {
			return false;
		}
		if (!text.matches("[A-Za-z0-9_]+")) {
			return false;
		}
		return true;
	}
}

package gd.zh.gamer.scorer.ui.activity;

import gd.zh.gamer.scorer.App;
import gd.zh.gamer.scorer.R;
import gd.zh.gamer.scorer.db.AccountDao;
import gd.zh.gamer.scorer.db.DaoMaster;
import gd.zh.gamer.scorer.db.DaoSession;
import gd.zh.gamer.scorer.entity.Account;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener {
	private EditText mEtAccount;
	private EditText mEtPwd;
	private Button mBtnLogin;
	private AccountDao ad;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		findViews();
		initDatas();
	}

	private void findViews() {
		mEtAccount = (EditText) findViewById(R.id.et_login_account);
		mEtPwd = (EditText) findViewById(R.id.et_login_password);
		mBtnLogin = (Button) findViewById(R.id.btn_login_login);
		mBtnLogin.setOnClickListener(this);
	}

	private void initDatas() {
		DaoMaster dm = new DaoMaster(App.db);
		DaoSession ds = dm.newSession();
		ad = ds.getAccountDao();
	}

	@Override
	public void onClick(View v) {
		String acc = mEtAccount.getText().toString();
		String pwd = mEtPwd.getText().toString();
		List<Account> as = ad.queryBuilder().where(AccountDao.Properties.Account.eq(acc), AccountDao.Properties.Password.eq(pwd)).list();
		if (as != null && as.size() > 0) {
			Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
//			SpHelper.login(as.get(0).getId());
			finish();
		} else {
			Toast.makeText(this, "登录失败，请检查账号和密码", Toast.LENGTH_SHORT).show();
		}
	}
}

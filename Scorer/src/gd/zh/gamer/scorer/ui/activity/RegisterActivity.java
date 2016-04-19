package gd.zh.gamer.scorer.ui.activity;

import java.util.List;

import gd.zh.gamer.scorer.App;
import gd.zh.gamer.scorer.R;
import gd.zh.gamer.scorer.db.AccountDao;
import gd.zh.gamer.scorer.db.DaoMaster;
import gd.zh.gamer.scorer.db.DaoSession;
import gd.zh.gamer.scorer.entity.Account;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class RegisterActivity extends Activity implements OnClickListener {
	private Button btnManager;
	private Button btnOperator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
	}

	@Override
	protected void onResume() {
		super.onResume();

		DaoMaster dm = new DaoMaster(App.db);
		DaoSession ds = dm.newSession();
		AccountDao ad = ds.getAccountDao();
		List<Account> accs = ad.queryBuilder()
				.where(AccountDao.Properties.Type.eq(Account.TYPE_MANAGER))
				.list();
		if (accs != null) {
			if (accs.size() > 1) {// 注册了多个管理员，此情况不应该出现
				throw new RuntimeException();
			} else if (accs.size() == 1) {// 已注册过管理员
				jumpToMenuActivity();
				finish();
				return;
			}
		}

		findViews();
	}

	private void jumpToMenuActivity() {
		Intent intent = new Intent(this, MenuActivity.class);
		startActivity(intent);
	}

	private void findViews() {
		btnManager = (Button) findViewById(R.id.btn_register_manager);
		btnOperator = (Button) findViewById(R.id.btn_register_operator);

		setOnClicks(btnManager, btnOperator);
	}

	private void setOnClicks(View... vs) {
		for (int i = 0; i < vs.length; i++) {
			vs[i].setOnClickListener(this);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_register_manager:
			Intent intent = new Intent(this, ManagerRegisterActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_register_operator:
			break;
		default:
			break;
		}
	}
}

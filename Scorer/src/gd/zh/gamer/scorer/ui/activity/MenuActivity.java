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

import com.google.zxing.client.android.CaptureActivity;

public class MenuActivity extends Activity implements OnClickListener {
	private Button btnAuth;
	private Account account;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);

		DaoMaster dm = new DaoMaster(App.db);
		DaoSession ds = dm.newSession();
		AccountDao ad = ds.getAccountDao();
		List<Account> accs = ad.loadAll();
		if (accs == null || accs.size() != 1) {
			throw new RuntimeException();
		}
		account = accs.get(0);

		findViews();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void findViews() {
		if (isManager()) {
			btnAuth = (Button) findViewById(R.id.btn_menu_authorize);
			btnAuth.setVisibility(View.VISIBLE);
			btnAuth.setOnClickListener(this);
		}

		findViewById(R.id.btn_menu_history).setOnClickListener(this);
		findViewById(R.id.btn_menu_scan).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_menu_authorize:
			Intent auth = new Intent(this, AuthorizeActivity.class);
			startActivity(auth);
			break;
		case R.id.btn_menu_history:
			onQuery();
			break;
		case R.id.btn_menu_scan:
			Intent scan = new Intent(this, CaptureActivity.class);
			startActivity(scan);
			break;
		}
	}

	private void onQuery() {
		if (isManager()) {
			Intent query = new Intent(this, QueryActivity.class);
			startActivity(query);
		} else {
			// TODO pop dialog to ask manager pwd

		}

	}

	private boolean isManager() {
		return Account.isManager(account);
	}
}

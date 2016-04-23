package gd.zh.gamer.scorer.ui.activity;

import gd.zh.gamer.scorer.App;
import gd.zh.gamer.scorer.R;
import gd.zh.gamer.scorer.db.AccountDao;
import gd.zh.gamer.scorer.db.DaoMaster;
import gd.zh.gamer.scorer.db.DaoSession;
import gd.zh.gamer.scorer.entity.Account;
import gd.zh.gamer.scorer.util.ToastUtil;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.client.android.CaptureActivity;

@SuppressLint("InflateParams")
public class MenuActivity extends Activity implements OnClickListener {
	private Button btnAuth;
	private Button btnPin;
	private Account account;
	private DaoMaster dm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);

		dm = new DaoMaster(App.db);
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

			btnPin = (Button) findViewById(R.id.btn_menu_pin);
			btnPin.setVisibility(View.VISIBLE);
			btnPin.setOnClickListener(this);
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
		case R.id.btn_menu_pin:
			Intent pin = new Intent(this, PinActivity.class);
			startActivity(pin);
			break;
		}
	}

	private void onQuery() {
		if (isManager()) {
			startQueryActivity();
		} else {
			AlertDialog.Builder builder = new Builder(this);
			final EditText et = (EditText) LayoutInflater.from(this).inflate(
					R.layout.password_edit, null, false);
			builder.setTitle("验证管理员密码");
			builder.setView(et);
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							String pwd = et.getText().toString();
							DaoSession ds = dm.newSession();
							AccountDao ad = ds.getAccountDao();
							if (ad.queryBuilder()
									.where(AccountDao.Properties.Password
											.eq(pwd)).count() == 1) {
								startQueryActivity();
							} else {
								ToastUtil
										.shortToast(MenuActivity.this, "密码错误！");
							}
							dialog.dismiss();
						}
					});

			builder.create().show();
		}

	}

	private void startQueryActivity() {
		Intent query = new Intent(this, QueryActivity.class);
		startActivity(query);
	}

	private boolean isManager() {
		return Account.isManager(account);
	}
}

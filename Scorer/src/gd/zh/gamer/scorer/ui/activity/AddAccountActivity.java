package gd.zh.gamer.scorer.ui.activity;

import gd.zh.gamer.scorer.App;
import gd.zh.gamer.scorer.R;
import gd.zh.gamer.scorer.db.AccountDao;
import gd.zh.gamer.scorer.db.DaoMaster;
import gd.zh.gamer.scorer.db.DaoSession;
import gd.zh.gamer.scorer.entity.Account;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class AddAccountActivity extends Activity implements OnClickListener, OnCheckedChangeListener {
	private EditText etAccount;
	private EditText etPassword;
	private EditText etName;
	private TextView tvManager;
	private Button btnAdd;
	private RadioButton rbManager;
	private RadioButton rbOperator;
	private AlertDialog dlgManager;
	private List<Account> managers;
	private int selectedManager = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_account);
		etAccount = (EditText) findViewById(R.id.et_account_account);
		etPassword = (EditText) findViewById(R.id.et_account_password);
		etName = (EditText) findViewById(R.id.et_account_name);
		tvManager = (TextView) findViewById(R.id.tv_account_manager);
		btnAdd = (Button) findViewById(R.id.btn_add_account);

		rbManager = (RadioButton) findViewById(R.id.rb_account_manager);
		rbOperator = (RadioButton) findViewById(R.id.rb_account_operator);
		rbManager.setOnCheckedChangeListener(this);
		rbOperator.setOnCheckedChangeListener(this);

		tvManager.setOnClickListener(this);
		btnAdd.setOnClickListener(this);

	}

	@Override
	protected void onResume() {
		super.onResume();
		refreshManagers();
	}

	private void refreshManagers() {
		DaoMaster dm = new DaoMaster(App.db);
		DaoSession ds = dm.newSession();
		AccountDao ad = ds.getAccountDao();
		managers = ad.queryBuilder().where(AccountDao.Properties.Type.eq(Integer.valueOf(0))).list();
		String[] items = new String[managers.size()];
		for (int i = 0; i < items.length; i++) {
			items[i] = managers.get(i).getName();
		}
		Builder builder = new Builder(this);
		builder.setCancelable(true);
		builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				selectedManager = which;
				tvManager.setText("隶属管理员：" + managers.get(selectedManager).getName());
				dlgManager.dismiss();
			}
		});
		dlgManager = builder.create();
	}

	@Override
	public void onClick(View v) {
		if (tvManager == v) {
			if (managers.size() > 0) {
				dlgManager.show();
			} else {
				Toast.makeText(this, "没有可选管理员，请先添加管理员", Toast.LENGTH_SHORT).show();
			}
		} else if (btnAdd == v) {
			if (rbManager.isChecked()) {
				addManager();
			} else {
				addOperator();
			}
		}
	}

	private void addOperator() {
		if (selectedManager < 0) {
			Toast.makeText(this, "请先选择管理员", Toast.LENGTH_SHORT).show();
			return;
		}
		String account = etAccount.getText().toString();
		String pwd = etPassword.getText().toString();
		String name = etName.getText().toString();
		if (account.length() < 1 || pwd.length() < 1 || name.length() < 1) {
			Toast.makeText(this, "请完整填写信息", Toast.LENGTH_SHORT).show();
			return;
		}
		DaoMaster dm = new DaoMaster(App.db);
		DaoSession ds = dm.newSession();
		AccountDao ad = ds.getAccountDao();
		Account a = new Account(null, account, pwd, name, 1, managers.get(selectedManager).getId());
		ad.insert(a);
		if (a.getId() != null && a.getId() > 0) {
			Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, "添加失败", Toast.LENGTH_SHORT).show();
		}
		clearData();
	}

	private void clearData() {
		etAccount.getText().clear();
		etPassword.getText().clear();
		etName.getText().clear();
		selectedManager = -1;
		tvManager.setText("选择管理员");
	}

	private void addManager() {
		String account = etAccount.getText().toString();
		String pwd = etPassword.getText().toString();
		String name = etName.getText().toString();
		if (account.length() < 1 || pwd.length() < 1 || name.length() < 1) {
			Toast.makeText(this, "请完整填写信息", Toast.LENGTH_SHORT).show();
			return;
		}
		DaoMaster dm = new DaoMaster(App.db);
		DaoSession ds = dm.newSession();
		AccountDao ad = ds.getAccountDao();
		Account a = new Account(null, account, pwd, name, 0, null);
		ad.insert(a);
		if (a.getId() != null && a.getId() > 0) {
			Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, "添加失败", Toast.LENGTH_SHORT).show();
		}
		clearData();
		refreshManagers();
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			if (buttonView == rbManager) {
				tvManager.setVisibility(View.GONE);
			} else {
				tvManager.setVisibility(View.VISIBLE);
			}
		}
	}
}

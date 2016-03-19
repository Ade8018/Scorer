package gd.zh.gamer.scorer.ui.activity;

import gd.zh.gamer.scorer.App;
import gd.zh.gamer.scorer.R;
import gd.zh.gamer.scorer.db.AccountDao;
import gd.zh.gamer.scorer.db.DaoMaster;
import gd.zh.gamer.scorer.db.DaoSession;
import gd.zh.gamer.scorer.db.PrinterDao;
import gd.zh.gamer.scorer.entity.Account;
import gd.zh.gamer.scorer.entity.Printer;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddPrinterActivity extends Activity implements OnClickListener {
	private EditText etSn;
	private EditText etName;
	private TextView tvManager;
	private Button btnAdd;
	private List<Account> managers;
	private int selectedManager = -1;
	private AlertDialog dlgManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_printer);
		etSn = (EditText) findViewById(R.id.et_printer_sn);
		etName = (EditText) findViewById(R.id.et_printer_name);
		tvManager = (TextView) findViewById(R.id.tv_printer_manager);
		btnAdd = (Button) findViewById(R.id.btn_printer_add);
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
		if (v == tvManager) {
			if (managers.size() > 0) {
				dlgManager.show();
			} else {
				Toast.makeText(this, "没有可选管理员，请先添加管理员", Toast.LENGTH_SHORT).show();
			}
		} else {
			add();
		}
	}

	private void add() {
		String sn = etSn.getText().toString();
		String name = etName.getText().toString();
		if (selectedManager < 0 || sn.length() < 1 || name.length() < 1) {
			Toast.makeText(this, "请完整填写信息", Toast.LENGTH_SHORT).show();
			return;
		}
		DaoMaster dm = new DaoMaster(App.db);
		DaoSession ds = dm.newSession();
		PrinterDao pd = ds.getPrinterDao();
		Printer p = new Printer(null, sn, name, managers.get(selectedManager).getId());
		pd.insert(p);
		if (p.getId() != null && p.getId() > 0) {
			Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, "添加失败", Toast.LENGTH_SHORT).show();
		}
		etSn.getText().clear();
		etName.getText().clear();
	}
}

package gd.zh.gamer.scorer.ui.activity;

import gd.zh.gamer.scorer.App;
import gd.zh.gamer.scorer.R;
import gd.zh.gamer.scorer.R.id;
import gd.zh.gamer.scorer.db.AccountDao;
import gd.zh.gamer.scorer.db.AuthorizationDao;
import gd.zh.gamer.scorer.db.DaoMaster;
import gd.zh.gamer.scorer.db.DaoSession;
import gd.zh.gamer.scorer.db.PrinterDao;
import gd.zh.gamer.scorer.entity.Account;
import gd.zh.gamer.scorer.entity.Authorization;
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
import android.widget.Toast;

public class AuthorizationActivity extends Activity implements OnClickListener {
	private EditText etPrinter;
	private EditText etOperator;
	private Button btnOk;
	private long idPrinter;
	private long idOperator;
	private Builder bOper;
	private Builder bPrin;
	private AccountDao ad;
	private PrinterDao pd;
	private AuthorizationDao authorD;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authorization);
		etPrinter = (EditText) findViewById(R.id.et_author_printer);
		etOperator = (EditText) findViewById(R.id.et_author_operator);
		btnOk = (Button) findViewById(R.id.btn_author_ok);
		etPrinter.setOnClickListener(this);
		etOperator.setOnClickListener(this);
		btnOk.setOnClickListener(this);
		bOper = new Builder(this);
		bOper.setCancelable(true);
		bPrin = new Builder(this);
		bPrin.setCancelable(true);
		DaoMaster dm = new DaoMaster(App.db);
		DaoSession ds = dm.newSession();
		ad = ds.getAccountDao();
		pd = ds.getPrinterDao();
		authorD = ds.getAuthorizationDao();
	}

	@Override
	public void onClick(View v) {
		if (etPrinter == v) {
			onPrinter();
		} else if (etOperator == v) {
			onOperator();
		} else if (btnOk == v) {
			onOk();
		}
	}

	private void onOk() {
		if (idOperator < 1 || idPrinter < 1) {
			Toast.makeText(this, "请填写完整信息", Toast.LENGTH_SHORT).show();
			return;
		}
		long count = authorD.queryBuilder().where(AuthorizationDao.Properties.Operator_id.eq(Long.valueOf(idOperator)), AuthorizationDao.Properties.Printer_id.eq(Long.valueOf(idPrinter))).count();
		if (count > 1) {
			throw new RuntimeException("存在多次授权");
		} else if (count == 1) {
			Toast.makeText(this, "该操作员与打印机已经过授权", Toast.LENGTH_SHORT).show();
			return;
		} else {
			Authorization au = new Authorization(null, idPrinter, idOperator);
			authorD.insert(au);
			if (au.getId() != null && au.getId() > 0) {
				Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void onOperator() {
		final List<Account> ops = ad.queryBuilder().where(AccountDao.Properties.Type.eq(Long.valueOf(1))).list();
		String[] items = new String[ops.size()];
		for (int i = 0; i < items.length; i++) {
			items[i] = ops.get(i).getName();
		}
		bOper.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				idOperator = ops.get(which).getId();
				etOperator.setText("操作员: " + ops.get(which).getName());
				dialog.cancel();
			}
		});
		bOper.create().show();
	}

	private void onPrinter() {
		final List<Printer> prints = pd.loadAll();
		String[] items = new String[prints.size()];
		for (int i = 0; i < items.length; i++) {
			items[i] = prints.get(i).getName();
		}
		bPrin.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				etPrinter.setText("打印机: " + prints.get(which).getName());
				idPrinter = prints.get(which).getId();
				dialog.cancel();
			}
		});
		bPrin.create().show();
	}
}

//package gd.zh.gamer.scorer.ui.activity;
//
//import gd.zh.gamer.scorer.App;
//import gd.zh.gamer.scorer.R;
//import gd.zh.gamer.scorer.db.AccountDao;
//import gd.zh.gamer.scorer.db.AuthorizationDao;
//import gd.zh.gamer.scorer.db.DaoMaster;
//import gd.zh.gamer.scorer.db.DaoSession;
//import gd.zh.gamer.scorer.db.PrinterDao;
//import gd.zh.gamer.scorer.db.RecordDao;
//import gd.zh.gamer.scorer.entity.Account;
//import gd.zh.gamer.scorer.entity.Authorization;
//import gd.zh.gamer.scorer.entity.Printer;
//import gd.zh.gamer.scorer.entity.Record;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.List;
//import java.util.Random;
//
//import android.app.Activity;
//import android.app.AlertDialog.Builder;
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
///**
// * 添加记录，分数随机1-1000，时间随机几天内。选择兑换员，打印机，添加数量
// * 
// */
//public class AddRecordActivity extends Activity implements OnClickListener {
//
//	private EditText etAccount;
//	private EditText etPrinter;
//	private EditText etCount;
//	private Button btnAdd;
//	private AccountDao ad;
//	private PrinterDao pd;
//	private RecordDao rd;
//	private AuthorizationDao authord;
//	private List<Account> accounts;
//	private Builder builder;
//	private long mCurrentAccountId;
//	private boolean isCurrentAccountManager;
//	private long mCurrentPrinterId;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_add_record);
//		etAccount = (EditText) findViewById(R.id.et_test_record_account);
//		etCount = (EditText) findViewById(R.id.et_test_record_count);
//		etPrinter = (EditText) findViewById(R.id.et_test_record_printer);
//		btnAdd = (Button) findViewById(R.id.btn_test_record_add);
//
//		etAccount.setOnClickListener(this);
//		etPrinter.setOnClickListener(this);
//		btnAdd.setOnClickListener(this);
//
//		DaoMaster dm = new DaoMaster(App.db);
//		DaoSession ds = dm.newSession();
//		ad = ds.getAccountDao();
//		pd = ds.getPrinterDao();
//		rd = ds.getRecordDao();
//		authord = ds.getAuthorizationDao();
//		accounts = ad.loadAll();
//		builder = new Builder(this);
//		builder.setCancelable(true);
//	}
//
//	@Override
//	public void onClick(View v) {
//		if (v == etAccount) {
//			onAccount();
//		} else if (v == etPrinter) {
//			onPrinter();
//		} else if (v == btnAdd) {
//			onAdd();
//		}
//	}
//
//	private void onAdd() {
//		if (mCurrentAccountId<=0 || mCurrentPrinterId<=0) {
//			Toast.makeText(this, "请填写完整信息", Toast.LENGTH_SHORT).show();
//			return;
//		}
//		String countStr = etCount.getText().toString();
//		int count = 0;
//		try {
//			count = Integer.valueOf(countStr);
//		} catch (Exception e) {
//		}
//		if (count == 0) {
//			Toast.makeText(this, "请输入大于0的数量", Toast.LENGTH_SHORT).show();
//			return;
//		}
//		Calendar c = Calendar.getInstance();
//		for (int i = 0; i < count; i++) {
//			long print = System.currentTimeMillis() - (new Random().nextInt(4 * 24 * 60 * 60 * 1000));
//			long exc = System.currentTimeMillis() + (new Random().nextInt(4 * 24 * 60 * 60 * 1000));
//			c.setTimeInMillis(print);
//			String printStr = c.get(Calendar.YEAR) + getDoubleString(c.get(Calendar.MONTH) + 1) + getDoubleString(c.get(Calendar.DAY_OF_MONTH)) + getDoubleString(c.get(Calendar.HOUR_OF_DAY))
//					+ getDoubleString(c.get(Calendar.MINUTE)) + getDoubleString(c.get(Calendar.SECOND));
//			c.setTimeInMillis(exc);
//			String excStr = c.get(Calendar.YEAR) + getDoubleString(c.get(Calendar.MONTH) + 1) + getDoubleString(c.get(Calendar.DAY_OF_MONTH)) + getDoubleString(c.get(Calendar.HOUR_OF_DAY))
//					+ getDoubleString(c.get(Calendar.MINUTE)) + getDoubleString(c.get(Calendar.SECOND));
//			Record r = new Record(null, new Random().nextInt(1000), Long.parseLong(printStr), Long.parseLong(excStr), mCurrentAccountId, mCurrentPrinterId);
//			rd.insert(r);
//		}
//	}
//
//	private String getDoubleString(int i) {
//		return i <= 9 ? ("0" + i) : ("" + i);
//	}
//
//	private List<Printer> printers;
//
//	private void onPrinter() {
//		if (mCurrentAccountId <= 0) {
//			Toast.makeText(this, "请先选择账户", Toast.LENGTH_SHORT).show();
//			return;
//		}
//		if (isCurrentAccountManager) {
//			printers = pd.queryBuilder().where(PrinterDao.Properties.Manager_id.eq(mCurrentAccountId)).list();
//		} else {
//			List<Authorization> aus = authord.queryBuilder().where(AuthorizationDao.Properties.Operator_id.eq(mCurrentAccountId)).list();
//			if (aus != null && aus.size() > 0) {
//				List<Long> pids = new ArrayList<Long>();
//				for (int i = 0; i < aus.size(); i++) {
//					pids.add(aus.get(i).getId());
//				}
//				printers = pd.queryBuilder().where(AuthorizationDao.Properties.Id.in(pids)).list();
//			}
//		}
//		if (printers == null || printers.size() <= 0) {
//			Toast.makeText(this, "该账户没有被授权的打印机", Toast.LENGTH_SHORT).show();
//			return;
//		}
//		String[] items = new String[printers.size()];
//		for (int i = 0; i < items.length; i++) {
//			items[i] = printers.get(i).getName();
//		}
//		builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				mCurrentPrinterId = printers.get(which).getId();
//				etPrinter.setText("选中打印机:" + printers.get(which).getName());
//				dialog.dismiss();
//			}
//		});
//		builder.create().show();
//	}
//
//	private void onAccount() {
//		if (accounts == null || accounts.size() <= 0) {
//			Toast.makeText(this, "没有可选择的账户，请先添加账户", Toast.LENGTH_SHORT).show();
//			return;
//		}
//		String[] items = new String[accounts.size()];
//		for (int i = 0; i < items.length; i++) {
//			items[i] = accounts.get(i).getName();
//		}
//		builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				Account a = accounts.get(which);
//				etAccount.setText("选中账户:" + a.getName());
//				mCurrentAccountId = a.getId();
//				isCurrentAccountManager = a.getType() == 0;
//				dialog.dismiss();
//			}
//		});
//		builder.create().show();
//	}
//}

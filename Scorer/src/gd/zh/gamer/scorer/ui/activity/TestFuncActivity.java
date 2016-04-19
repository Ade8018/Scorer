//package gd.zh.gamer.scorer.ui.activity;
//
//import gd.zh.gamer.scorer.App;
//import gd.zh.gamer.scorer.R;
//import gd.zh.gamer.scorer.db.DaoMaster;
//import gd.zh.gamer.scorer.db.DaoSession;
//import gd.zh.gamer.scorer.db.PrinterDao;
//import gd.zh.gamer.scorer.db.RecordDao;
//import gd.zh.gamer.scorer.entity.Printer;
//import gd.zh.gamer.scorer.entity.Record;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.List;
//import java.util.Random;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//
//public class TestFuncActivity extends Activity implements OnClickListener {
//	private Button mBtnAddAccount;
//	private Button mBtnAddPrinter;
//	private Button mBtnAuthor;
//	private Button mBtnAddRecord;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_testfunc);
//		mBtnAddAccount = (Button) findViewById(R.id.btn_add_account);
//		mBtnAddPrinter = (Button) findViewById(R.id.btn_add_printer);
//		mBtnAddRecord = (Button) findViewById(R.id.btn_add_record);
//		mBtnAuthor = (Button) findViewById(R.id.btn_author);
//		mBtnAddAccount.setOnClickListener(this);
//		mBtnAddPrinter.setOnClickListener(this);
//		mBtnAddRecord.setOnClickListener(this);
//		mBtnAuthor.setOnClickListener(this);
//	}
//
//	@Override
//	public void onClick(View v) {
//		Intent intent = null;
//		if (v == mBtnAddAccount) {
//			intent = new Intent(this, AddAccountActivity.class);
//			startActivity(intent);
//		} else if (v == mBtnAddPrinter) {
//			intent = new Intent(this, AddPrinterActivity.class);
//			startActivity(intent);
//		} else if (v == mBtnAddRecord) {
//			intent = new Intent(this, AddRecordActivity.class);
//			startActivity(intent);
//		}else if (v == mBtnAuthor) {
//			intent = new Intent(this, AuthorizationActivity.class);
//			startActivity(intent);
//		}
//	}
//
//	private void onRandomAdd() {
//		DaoMaster dm = new DaoMaster(App.db);
//		DaoSession ds = dm.newSession();
//		PrinterDao pd = ds.getPrinterDao();
//		List<String> psns = new ArrayList<String>();
//		for (int i = 0; i < 10; i++) {
//			String sn = "abcdefg" + i;
//			psns.add(sn);
//			Printer p = new Printer(null, sn, sn, 0l);
//			pd.insert(p);
//		}
//		RecordDao rd = ds.getRecordDao();
//		long print, exc;
//		Calendar c = Calendar.getInstance();
//		for (int i = 0; i < 100; i++) {
//			print = System.currentTimeMillis() - (new Random().nextInt(4 * 24 * 60 * 60 * 1000));
//			exc = System.currentTimeMillis() + (new Random().nextInt(4 * 24 * 60 * 60 * 1000));
//			c.setTimeInMillis(print);
//			String printStr = c.get(Calendar.YEAR) + getDoubleString(c.get(Calendar.MONTH) + 1) + getDoubleString(c.get(Calendar.DAY_OF_MONTH)) + getDoubleString(c.get(Calendar.HOUR_OF_DAY))
//					+ getDoubleString(c.get(Calendar.MINUTE)) + getDoubleString(c.get(Calendar.SECOND));
//			c.setTimeInMillis(exc);
//			String excStr = c.get(Calendar.YEAR) + getDoubleString(c.get(Calendar.MONTH) + 1) + getDoubleString(c.get(Calendar.DAY_OF_MONTH)) + getDoubleString(c.get(Calendar.HOUR_OF_DAY))
//					+ getDoubleString(c.get(Calendar.MINUTE)) + getDoubleString(c.get(Calendar.SECOND));
//			Record r = new Record(null, new Random().nextInt(1000), Long.parseLong(printStr), Long.parseLong(excStr), (long) (i + 1) % new Random().nextInt(10), 0l);
//			rd.insert(r);
//		}
//	}
//
//	private String getDoubleString(int i) {
//		return i <= 9 ? ("0" + i) : ("" + i);
//	}
//
//	private void onDeleteAll() {
//		DaoMaster dm = new DaoMaster(App.db);
//		DaoSession ds = dm.newSession();
//		PrinterDao pd = ds.getPrinterDao();
//		pd.deleteAll();
//		RecordDao rd = ds.getRecordDao();
//		rd.deleteAll();
//	}
//}

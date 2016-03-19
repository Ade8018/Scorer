package gd.zh.gamer.scorer.ui.activity;

import gd.zh.gamer.scorer.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SettingActivity extends Activity implements OnClickListener {
	private Button mBtnPrinter;
	private Button mBtnRecord;
	private Button mBtnAccount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		findViews();
		initDatas();
	}

	private void findViews() {
		mBtnPrinter = (Button) findViewById(R.id.btn_setting_printer);
		mBtnRecord = (Button) findViewById(R.id.btn_setting_record);
		mBtnAccount = (Button) findViewById(R.id.btn_setting_account);
		mBtnPrinter.setOnClickListener(this);
		mBtnRecord.setOnClickListener(this);
		mBtnAccount.setOnClickListener(this);
	}

	private void initDatas() {
	}

	@Override
	public void onClick(View v) {
		if (v == mBtnPrinter) {
			Intent intent = new Intent(this,PrinterActivity.class);
			startActivity(intent);
		}
	}
}

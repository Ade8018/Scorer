package gd.zh.gamer.scorer.ui.activity;

import gd.zh.gamer.scorer.R;
import gd.zh.gamer.scorer.util.SpHelper;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.zxing.client.android.CaptureActivity;

public class MainActivity extends Activity implements OnClickListener {
	private Button mBtnScan;
	private Button mBtnTestFunc;
	private Button mBtnLogin;
	private Button mBtnSetting;
	private Button mBtnLogout;
	private ProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mBtnScan = (Button) findViewById(R.id.btn_main_scanner);
		mBtnTestFunc = (Button) findViewById(R.id.btn_main_testfunc);
		mBtnLogin = (Button) findViewById(R.id.btn_main_login);
		mBtnSetting = (Button) findViewById(R.id.btn_main_setting);
		mBtnLogout = (Button) findViewById(R.id.btn_main_logout);
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setCancelable(true);
		mProgressDialog.setCanceledOnTouchOutside(false);
		mProgressDialog.setMessage("message");
		mProgressDialog.setButton(ProgressDialog.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mProgressDialog.dismiss();
			}
		});
		mBtnScan.setOnClickListener(this);
		mBtnTestFunc.setOnClickListener(this);
		mBtnLogin.setOnClickListener(this);
		mBtnSetting.setOnClickListener(this);
		mBtnLogout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == mBtnScan) {
			Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
			startActivity(intent);
		} else if (v == mBtnTestFunc) {
			Intent intent = new Intent(MainActivity.this, TestFuncActivity.class);
			startActivity(intent);
		} else if (v == mBtnLogin) {
			Intent intent = new Intent(MainActivity.this, LoginActivity.class);
			startActivity(intent);
		} else if (v == mBtnLogout) {
			SpHelper.logout();
			refreshState();
		} else if (v == mBtnSetting) {
			Intent intent = new Intent(MainActivity.this, SettingActivity.class);
			startActivity(intent);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		refreshState();
	}

	private void refreshState() {
		if (SpHelper.isLogin()) {
			mBtnLogin.setVisibility(View.GONE);
			mBtnLogout.setVisibility(View.VISIBLE);
			mBtnSetting.setVisibility(View.VISIBLE);
		} else {
			mBtnLogout.setVisibility(View.GONE);
			mBtnLogin.setVisibility(View.VISIBLE);
			mBtnSetting.setVisibility(View.GONE);
		}
	}

}

package gd.zh.gamer.scorer.ui.activity;

import com.google.zxing.client.android.CaptureActivity;

import gd.zh.gamer.scorer.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MenuActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);

	}

	@Override
	protected void onResume() {
		super.onResume();

		findViews();
	}

	private void findViews() {
		findViewById(R.id.btn_menu_authorize).setOnClickListener(this);
		findViewById(R.id.btn_menu_history).setOnClickListener(this);
		findViewById(R.id.btn_menu_scan).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_menu_authorize:
			break;
		case R.id.btn_menu_history:
			Intent query = new Intent(this, QueryActivity.class);
			startActivity(query);
			break;
		case R.id.btn_menu_scan:
			Intent scan = new Intent(this, CaptureActivity.class);
			startActivity(scan);
			break;
		}
	}
}

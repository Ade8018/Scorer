package gd.zh.gamer.scorer.ui.activity;

import gd.zh.gamer.scorer.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class RegisterActivity extends Activity implements OnClickListener {
	private Button btnManager;
	private Button btnOperator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		findViews();
	}

	private void findViews() {
		btnManager = (Button) findViewById(R.id.btn_register_manager);
		btnOperator = (Button) findViewById(R.id.btn_register_operator);

		setOnClicks(btnManager, btnOperator);
	}

	private void setOnClicks(View... vs) {
		for (int i = 0; i < vs.length; i++) {
			vs[i].setOnClickListener(this);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_register_manager:
			break;
		case R.id.btn_register_operator:
			break;
		default:
			break;
		}
	}
}

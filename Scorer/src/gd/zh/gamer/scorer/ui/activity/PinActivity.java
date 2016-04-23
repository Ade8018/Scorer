package gd.zh.gamer.scorer.ui.activity;

import gd.zh.gamer.scorer.R;
import gd.zh.gamer.scorer.util.SpHelper;
import gd.zh.gamer.scorer.util.ToastUtil;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class PinActivity extends Activity implements OnClickListener {
	private EditText etPin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pin);

		etPin = (EditText) findViewById(R.id.et_pin);
		findViewById(R.id.btn_pin_save).setOnClickListener(this);

		etPin.setText(SpHelper.getCachePin(this));
	}

	@Override
	public void onClick(View v) {
		String pin = etPin.getText().toString();
		if (!SpHelper.isValidPin(pin)) {
			ToastUtil.shortToast(this, "请输入4位数字作为PIN码");
			return;
		}

		if (!SpHelper.savePin(this, pin)) {
			ToastUtil.shortToast(this, "保存PIN码失败，请稍后再试");
		} else {
			ToastUtil.shortToast(this, "保存成功");
		}
	}
}

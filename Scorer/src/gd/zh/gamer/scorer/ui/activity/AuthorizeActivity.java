package gd.zh.gamer.scorer.ui.activity;

import gd.zh.gamer.scorer.R;
import gd.zh.gamer.scorer.db.AccountDao;
import gd.zh.gamer.scorer.entity.Account;
import gd.zh.gamer.scorer.util.DaoUtil;
import gd.zh.gamer.scorer.util.L;
import gd.zh.gamer.scorer.util.QrCodeUtil;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class AuthorizeActivity extends Activity {
	public static final String TAG = "AuthorizeActivity";
	private ImageView iv;
	private TextView tv;
	private Bitmap bm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authorize);

		iv = (ImageView) findViewById(R.id.iv_authorize);
		tv = (TextView) findViewById(R.id.tv_authorize);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		L.e(TAG, "onWindowFocusChanged " + hasFocus);
		if (hasFocus) {
			Account acc = DaoUtil.getCurrentManager();
			if (acc == null) {// there must be a manager to auth ...
				throw new RuntimeException();
			}
			tv.setText("当前管理员账号：" + acc.getAccount());

			String content = getAuthString(acc);

			int length = iv.getWidth();
			bm = QrCodeUtil.str2QrCodeBitmap(content, length);

			iv.setImageBitmap(bm);
		}
		super.onWindowFocusChanged(hasFocus);
	}

	private String getAuthString(Account acc) {

		String result = "!" + acc.getAccount() + ",@" + acc.getPassword()
				+ ",#" + ManagerBindActivity.REGISTER_CODE;
		L.e(TAG, "二维码信息：" + result);

		String encode = QrCodeUtil.authEncode(result);

		result = QrCodeUtil.authEncode(encode);

		L.e(TAG, "加密：" + encode);
		L.e(TAG, "解密：" + result);

		return encode;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (bm != null && !bm.isRecycled()) {
			bm.recycle();
			bm = null;
		}
	}
}

package gd.zh.gamer.scorer.ui.activity;

import gd.zh.gamer.scorer.App;
import gd.zh.gamer.scorer.R;
import gd.zh.gamer.scorer.db.AccountDao;
import gd.zh.gamer.scorer.db.DaoMaster;
import gd.zh.gamer.scorer.db.DaoSession;
import gd.zh.gamer.scorer.entity.Account;
import gd.zh.gamer.scorer.util.L;
import gd.zh.gamer.scorer.util.QrCodeUtil;

import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

public class AuthorizeActivity extends Activity {
	public static final String TAG = "AuthorizeActivity";
	private ImageView iv;
	private Bitmap bm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authorize);

		iv = (ImageView) findViewById(R.id.iv_authorize);

	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		L.e(TAG, "onWindowFocusChanged " + hasFocus);
		if (hasFocus) {
			int length = iv.getWidth();

			String content = getAuthString();

			bm = QrCodeUtil.str2QrCodeBitmap(content, length);

			iv.setImageBitmap(bm);
		}
		super.onWindowFocusChanged(hasFocus);
	}

	private String getAuthString() {
		DaoMaster dm = new DaoMaster(App.db);
		DaoSession ds = dm.newSession();
		AccountDao ad = ds.getAccountDao();

		List<Account> accs = ad.queryBuilder()
				.where(AccountDao.Properties.Type.eq(Account.TYPE_MANAGER))
				.list();
		if (accs == null || accs.size() != 1) {
			throw new RuntimeException();
		}

		Account acc = accs.get(0);

		String result = "!" + acc.getAccount() + ",@" + acc.getPassword()
				+ ",#" + ManagerBindActivity.REGISTER_CODE;
		L.e(TAG, "二维码信息：" + result);

		String encode = QrCodeUtil.authEncode(result);

		result = QrCodeUtil.authEncode(result);

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

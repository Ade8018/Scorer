package gd.zh.gamer.scorer.ui.activity;

import gd.zh.gamer.scorer.App;
import gd.zh.gamer.scorer.R;
import gd.zh.gamer.scorer.db.AccountDao;
import gd.zh.gamer.scorer.db.DaoMaster;
import gd.zh.gamer.scorer.db.DaoSession;
import gd.zh.gamer.scorer.entity.Account;
import gd.zh.gamer.scorer.util.ToastUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class AuthorizeActivity extends Activity {
	public static final String TAG = "AuthorizeActivity";
	private ImageView iv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authorize);

		iv = (ImageView) findViewById(R.id.iv_authorize);
		int length = iv.getWidth();

		String content = getAuthString();

		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		try {
			BitMatrix bitMatrix = new MultiFormatWriter().encode(content,
					BarcodeFormat.QR_CODE, length, length, hints);
			Bitmap bm = getMatrixBitmap(bitMatrix, length);

			iv.setImageBitmap(bm);
		} catch (WriterException e) {
			e.printStackTrace();
		}
	}

	private Bitmap getMatrixBitmap(BitMatrix bitMatrix, int length) {
		int[] pixels = new int[length * length];
		// 下面这里按照二维码的算法，逐个生成二维码的图片，
		// 两个for循环是图片横列扫描的结果
		for (int y = 0; y < length; y++) {
			for (int x = 0; x < length; x++) {
				if (bitMatrix.get(x, y)) {
					pixels[y * length + x] = 0xff000000;
				} else {
					pixels[y * length + x] = 0xffffffff;
				}
			}
		}
		// 生成二维码图片的格式，使用ARGB_8888
		Bitmap bitmap = Bitmap.createBitmap(length, length,
				Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, length, 0, 0, length, length);
		return bitmap;
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

		String result = acc.getAccount() + "," + acc.getPassword() + ","
				+ ManagerBindActivity.REGISTER_CODE;
		Log.e(TAG, "二维码信息：" + result);
		return result;
	}
}

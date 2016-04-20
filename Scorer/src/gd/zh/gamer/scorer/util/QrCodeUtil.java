package gd.zh.gamer.scorer.util;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

public class QrCodeUtil {
	public static String authEncode(String src) {
		if (TextUtils.isEmpty(src)) {
			throw new NullPointerException();
		}
		byte[] buf = src.getBytes();
		for (int i = 0; i < buf.length; i++) {
			buf[i] ^= (i + 0x40);
		}
		return new String(buf);
	}

	public static Bitmap str2QrCodeBitmap(String src, int bitmapEdgeLength) {
		if (TextUtils.isEmpty(src) || bitmapEdgeLength <= 0) {
			throw new IllegalArgumentException();
		}
		Bitmap bm = null;
		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		BitMatrix bitMatrix = null;
		try {
			bitMatrix = new MultiFormatWriter().encode(src,
					BarcodeFormat.QR_CODE, bitmapEdgeLength, bitmapEdgeLength,
					hints);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		bm = getMatrixBitmap(bitMatrix, bitmapEdgeLength);
		return bm;
	}

	private static Bitmap getMatrixBitmap(BitMatrix bitMatrix, int length) {
		if (bitMatrix == null || length <= 0) {
			throw new IllegalArgumentException();
		}
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
		// 生成二维码图片的格式，使用ARGB_4444
		Bitmap bitmap = Bitmap.createBitmap(length, length,
				Bitmap.Config.ARGB_4444);
		bitmap.setPixels(pixels, 0, length, 0, 0, length, length);
		return bitmap;
	}
}

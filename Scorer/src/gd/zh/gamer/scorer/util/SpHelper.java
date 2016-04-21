package gd.zh.gamer.scorer.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

public class SpHelper {
	public static String SP_NAME_MAIN = "sp_name_main";
	public static String SP_KEY_PIN = "abcqwe";
	private static String sPin;

	private static SharedPreferences getSp(Context context) {
		return context.getSharedPreferences(SP_NAME_MAIN, Context.MODE_PRIVATE);
	}

	public static void savePin(Context context, String pin) {
		if (!isValidPin(pin))
			throw new IllegalArgumentException();

		if (getSp(context).edit().putString(SP_KEY_PIN, pin).commit())
			sPin = pin;
		else
			ToastUtil.shortToast(context, "保存PIN码失败，请稍后再试");
	}

	public static String getCachePin(Context context) {
		if (sPin == null) {
			synchronized (SpHelper.class) {
				if (sPin == null) {
					sPin = getPin(context);
				}
			}
		}
		return sPin;
	}

	private static String getPin(Context context) {
		return getSp(context).getString(SP_KEY_PIN, null);
	}

	public static boolean isValidPin(String pin) {
		if (TextUtils.isEmpty(pin) || pin.length() != 4)
			return false;
		return pin.matches("[0-9]+");
	}

}

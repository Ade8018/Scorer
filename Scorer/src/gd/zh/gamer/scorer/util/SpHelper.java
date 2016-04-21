package gd.zh.gamer.scorer.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

public class SpHelper {
	public static String SP_NAME_MAIN = "sp_name_main";
	public static String SP_KEY_PIN = "abcq";
	public static String SP_KEY_PRINTER_INDEX = "asdf";// 由一个字母和两位数字组成，如A00
	public static String DEFAULT_PIN = "0000";
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
		return getSp(context).getString(SP_KEY_PIN, DEFAULT_PIN);
	}

	public static boolean isValidPin(String pin) {
		if (TextUtils.isEmpty(pin) || pin.length() != 4)
			return false;
		return pin.matches("[0-9]+");
	}

	/**
	 * 生成并增加打印机代号。以下代码已测试,勿动
	 * 
	 * @param context
	 * @return
	 */
	public static String getAndIncreasePrinterNickName(Context context) {
		SharedPreferences sp = getSp(context);
		String current = sp.getString(SP_KEY_PRINTER_INDEX, "A00");
		char c = current.charAt(0);
		int index = -1;
		try {
			index = Integer.parseInt(current.substring(1));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		if (index < 0 || index >= 100)
			return null;

		index++;
		if (index == 100) {
			index = 0;
			c++;
		}
		String next = "" + c;
		if (index < 10) {
			next += ("0" + index);
		} else {
			next += index;
		}
		if (!sp.edit().putString(SP_KEY_PRINTER_INDEX, next).commit())
			return null;

		return current;
	}

}

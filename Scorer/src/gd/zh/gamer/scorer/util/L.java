package gd.zh.gamer.scorer.util;

import android.util.Log;

public class L {
	private static boolean debug = true;

	public static void e(String tag, String msg) {
		if (debug) {
			Log.e(tag, msg);
		}
	}
}

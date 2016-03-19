package gd.zh.gamer.scorer.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SpHelper {
	public static String SP_NAME_MAIN = "sp_name_main";
	public static String SP_KEY_LOGIN_ACCOUNT_ID = "sp_key_login_account_id";
	private static SharedPreferences sSp;

	public static void init(Context context) {
		sSp = context.getSharedPreferences(SP_NAME_MAIN, Context.MODE_PRIVATE);
	}

	public static long getLoginAccountID() {
		return sSp.getLong(SP_KEY_LOGIN_ACCOUNT_ID, 0);
	}

	public static boolean isLogin() {
		return sSp.getLong(SP_KEY_LOGIN_ACCOUNT_ID, 0) > 0;
	}

	public static void logout() {
		sSp.edit().putLong(SP_KEY_LOGIN_ACCOUNT_ID, 0).commit();
	}
	
	public static void login(long id){
		sSp.edit().putLong(SP_KEY_LOGIN_ACCOUNT_ID, id).commit();
	}

}

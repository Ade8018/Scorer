package gd.zh.gamer.scorer.util;

import gd.zh.gamer.scorer.R;
import android.app.Activity;
import android.widget.TextView;

public class ActionBarUtil {
	public static void setTitle(Activity act, String title) {
		TextView tv = (TextView) act.findViewById(R.id.tv_title);
		tv.setText(title);
	}
}

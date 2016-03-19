package gd.zh.gamer.scorer.ui.dialog;

import android.app.ProgressDialog;
import android.content.Context;

public class MsgProgressDialog {
	private ProgressDialog mProgressDialog;

	public MsgProgressDialog(Context context, String msg) {
		setDefaultDialog(context, msg);
	}

	private void setDefaultDialog(Context context, String msg) {
		if (context == null) {
			return;
		}
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(context);
			mProgressDialog.setCancelable(false);
			mProgressDialog.setCanceledOnTouchOutside(false);
			mProgressDialog.setMessage(msg);
		}
	}

	public void show() {
		if (mProgressDialog != null && !mProgressDialog.isShowing()) {
			mProgressDialog.show();

		}
	}

	public void dismiss() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
	}
}

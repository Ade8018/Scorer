package gd.zh.gamer.scorer.ui.dialog;

import java.util.Calendar;
import java.util.Date;

import gd.zh.gamer.scorer.R;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.PopupWindow;

public class PickTimeWindow implements OnClickListener {
	public interface OnTimeSaveListener {
		public void onTimeSaved(PickTimeWindow ptw);
	}

	private PopupWindow mPopWin;
	private NumberPicker mNpYear;
	private NumberPicker mNpMonth;
	private NumberPicker mNpDay;
	private NumberPicker mNpHour;
	private NumberPicker mNpMinute;
	private NumberPicker mNpSecond;
	private Button mBtnOk;
	private Button mBtnCancel;
	private View mView;
	private Calendar mTime;
	private OnTimeSaveListener mListener;

	public PickTimeWindow(Context context, OnTimeSaveListener lis) {
		mListener = lis;
		mPopWin = new PopupWindow(context);
		mView = LayoutInflater.from(context).inflate(R.layout.picktime, null, false);
		findViews();
		mPopWin.setOutsideTouchable(false);
		mPopWin.setBackgroundDrawable(new BitmapDrawable());
		mPopWin.setContentView(mView);
		mPopWin.setWidth(LayoutParams.WRAP_CONTENT);
		mPopWin.setHeight(LayoutParams.WRAP_CONTENT);
		initData();
	}

	private void findViews() {
		mNpYear = (NumberPicker) mView.findViewById(R.id.np_picktime_year);
		mNpMonth = (NumberPicker) mView.findViewById(R.id.np_picktime_month);
		mNpDay = (NumberPicker) mView.findViewById(R.id.np_picktime_day);
		mNpHour = (NumberPicker) mView.findViewById(R.id.np_picktime_hour);
		mNpMinute = (NumberPicker) mView.findViewById(R.id.np_picktime_minute);
		mNpSecond = (NumberPicker) mView.findViewById(R.id.np_picktime_second);
		mBtnCancel = (Button) mView.findViewById(R.id.btn_picktime_cancel);
		mBtnOk = (Button) mView.findViewById(R.id.btn_picktime_ok);

		mBtnCancel.setOnClickListener(this);
		mBtnOk.setOnClickListener(this);
	}

	private void initData() {
		mNpYear.setMaxValue(2099);
		mNpYear.setMinValue(2000);
		mNpMonth.setMaxValue(12);
		mNpMonth.setMinValue(1);
		mNpDay.setMaxValue(31);
		mNpDay.setMinValue(1);
		mNpHour.setMaxValue(23);
		mNpHour.setMinValue(0);
		mNpMinute.setMaxValue(59);
		mNpMinute.setMinValue(0);
		mNpSecond.setMaxValue(59);
		mNpSecond.setMinValue(0);

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(System.currentTimeMillis()));
		mNpYear.setValue(cal.get(Calendar.YEAR));
		mNpMonth.setValue(cal.get(Calendar.MONTH) + 1);
		mNpDay.setValue(cal.get(Calendar.DAY_OF_MONTH));
		mNpHour.setValue(cal.get(Calendar.HOUR_OF_DAY));
		mNpMinute.setValue(cal.get(Calendar.MINUTE));
		mNpSecond.setValue(cal.get(Calendar.SECOND));
	}

	public void show() {
		mPopWin.showAtLocation(mView, Gravity.CENTER, 0, 0);
	}

	public void dismiss() {
		mPopWin.dismiss();
	}

	public void onOk() {
		mTime = Calendar.getInstance();
		mTime.set(mNpYear.getValue(), mNpMonth.getValue() - 1, mNpDay.getValue(), mNpHour.getValue(), mNpMinute.getValue(), mNpSecond.getValue());
		dismiss();
		mListener.onTimeSaved(this);
	}

	public void onCancel() {
		dismiss();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_picktime_cancel:
			onCancel();
			break;
		case R.id.btn_picktime_ok:
			onOk();
			break;
		}
	}

	public Calendar getTime() {
		return mTime;
	}
}

package gd.zh.gamer.scorer.ui.activity;

import gd.zh.gamer.scorer.R;
import gd.zh.gamer.scorer.ui.dialog.PickTimeWindow;
import gd.zh.gamer.scorer.ui.dialog.PickTimeWindow.OnTimeSaveListener;
import gd.zh.gamer.scorer.util.ActionBarUtil;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class QueryActivity extends Activity implements OnClickListener, OnTimeSaveListener {
	private TextView mTvPrintStartTime;
	private TextView mTvPrintEndTime;
	private TextView mTvExcStartTime;
	private TextView mTvExcEndTime;
	private TextView mTvPrinter;
	private Button mBtnQuery;
	private PickTimeWindow mPtPrintStart;
	private PickTimeWindow mPtPrintEnd;
	private PickTimeWindow mPtExcStart;
	private PickTimeWindow mPtExcEnd;
	private long mPrintStartTime;
	private long mPrintEndTime;
	private long mExcStartTime;
	private long mExcEndTime;
	private ArrayList<String> mPrinters = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_condition);
		ActionBarUtil.setTitle(this, "设置查询条件");
		findUI();
		setData();
	}

	private void findUI() {
		mTvPrintStartTime = (TextView) findViewById(R.id.tv_condition_print_starttime);
		mTvPrintEndTime = (TextView) findViewById(R.id.tv_condition_print_endtime);
		mTvExcStartTime = (TextView) findViewById(R.id.tv_condition_exc_starttime);
		mTvExcEndTime = (TextView) findViewById(R.id.tv_condition_exc_endtime);
		mTvPrinter = (TextView) findViewById(R.id.tv_condition_printer);
		mBtnQuery = (Button) findViewById(R.id.btn_condition_query);
		mTvPrintStartTime.setOnClickListener(this);
		mTvPrintEndTime.setOnClickListener(this);
		mTvExcEndTime.setOnClickListener(this);
		mTvExcStartTime.setOnClickListener(this);
		mTvPrinter.setOnClickListener(this);
		mBtnQuery.setOnClickListener(this);
	}

	private void setData() {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_condition_print_starttime:
			if (mPtPrintStart == null) {
				mPtPrintStart = new PickTimeWindow(this, this);
			}
			mPtPrintStart.show();
			break;
		case R.id.tv_condition_print_endtime:
			if (mPtPrintEnd == null) {
				mPtPrintEnd = new PickTimeWindow(this, this);
			}
			mPtPrintEnd.show();
			break;
		case R.id.tv_condition_exc_starttime:
			if (mPtExcStart == null) {
				mPtExcStart = new PickTimeWindow(this, this);
			}
			mPtExcStart.show();
			break;
		case R.id.tv_condition_exc_endtime:
			if (mPtExcEnd == null) {
				mPtExcEnd = new PickTimeWindow(this, this);
			}
			mPtExcEnd.show();
			break;
		case R.id.tv_condition_printer:
			break;
		case R.id.btn_condition_query:
			onQuery();
			break;
		}
	}

	private void onQuery() {
		Intent intent = new Intent(this, QueryResultActivity.class);
		intent.putExtra(QueryResultActivity.INTENT_EXTRA_KEY_PRINT_START_TIME, mPrintStartTime);
		intent.putExtra(QueryResultActivity.INTENT_EXTRA_KEY_PRINT_END_TIME, mPrintEndTime);
		intent.putExtra(QueryResultActivity.INTENT_EXTRA_KEY_EXC_START_TIME, mExcStartTime);
		intent.putExtra(QueryResultActivity.INTENT_EXTRA_KEY_EXC_END_TIME, mExcEndTime);
		startActivity(intent);
	}

	@Override
	public void onTimeSaved(PickTimeWindow ptw) {
		Calendar c = ptw.getTime();
		String timeStr = c.get(Calendar.YEAR) + getDoubleString(c.get(Calendar.MONTH) + 1) + getDoubleString(c.get(Calendar.DAY_OF_MONTH)) + getDoubleString(c.get(Calendar.HOUR_OF_DAY))
				+ getDoubleString(c.get(Calendar.MINUTE)) + getDoubleString(c.get(Calendar.SECOND));
		if (mPtPrintStart == ptw) {
			mPrintStartTime = Long.parseLong(timeStr);
			setTime(mTvPrintStartTime, ptw.getTime());
		} else if (mPtPrintEnd == ptw) {
			mPrintEndTime = Long.parseLong(timeStr);
			setTime(mTvPrintEndTime, ptw.getTime());
		} else if (mPtExcStart == ptw) {
			mExcStartTime = Long.parseLong(timeStr);
			setTime(mTvExcStartTime, ptw.getTime());
		} else if (mPtExcEnd == ptw) {
			mExcEndTime = Long.parseLong(timeStr);
			setTime(mTvExcEndTime, ptw.getTime());
		}
	}

	private void setTime(TextView tv, Calendar time) {
		tv.setText(String.format("%s年%s月%s日  %s:%s:%s", time.get(Calendar.YEAR), time.get(Calendar.MONTH) + 1, time.get(Calendar.DAY_OF_MONTH), time.get(Calendar.HOUR_OF_DAY),
				time.get(Calendar.MINUTE), time.get(Calendar.SECOND)));
	}

	private String getDoubleString(int i) {
		return i <= 9 ? ("0" + i) : ("" + i);
	}

}

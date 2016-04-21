package gd.zh.gamer.scorer.ui.activity;

import gd.zh.gamer.scorer.R;
import gd.zh.gamer.scorer.ui.adapter.ResultAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

public class QueryResultActivity extends Activity {
	public static final String INTENT_EXTRA_KEY_PRINT_START_TIME = "print_start";
	public static final String INTENT_EXTRA_KEY_PRINT_END_TIME = "print_end";
	public static final String INTENT_EXTRA_KEY_EXC_START_TIME = "exc_start";
	public static final String INTENT_EXTRA_KEY_EXC_END_TIME = "exc_end";
	public static final String INTENT_EXTRA_KEY_PRINTER_IDS = "printer_ids";
	private ListView mLv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_queryresult);
		findViews();
		setDatas();
	}

	private void findViews() {
		mLv = (ListView) findViewById(R.id.lv_queryresult);
	}

	private void setDatas() {
		Intent intent = getIntent();
		long printStart = intent.getLongExtra(
				INTENT_EXTRA_KEY_PRINT_START_TIME, 0);
		long printEnd = intent.getLongExtra(INTENT_EXTRA_KEY_PRINT_END_TIME, 0);
		long excStart = intent.getLongExtra(INTENT_EXTRA_KEY_EXC_START_TIME, 0);
		long excEnd = intent.getLongExtra(INTENT_EXTRA_KEY_EXC_END_TIME, 0);
		long[] ids = intent.getLongArrayExtra(INTENT_EXTRA_KEY_PRINTER_IDS);

		ResultAdapter adapter = new ResultAdapter(printStart, printEnd,
				excStart, excEnd, ids);
		mLv.setAdapter(adapter);
	}
}

package gd.zh.gamer.scorer.ui.adapter;

import gd.zh.gamer.scorer.App;
import gd.zh.gamer.scorer.R;
import gd.zh.gamer.scorer.db.DaoMaster;
import gd.zh.gamer.scorer.db.DaoSession;
import gd.zh.gamer.scorer.db.RecordDao;
import gd.zh.gamer.scorer.db.RecordDao.Properties;
import gd.zh.gamer.scorer.entity.Record;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import de.greenrobot.dao.query.QueryBuilder;

public class ResultAdapter extends BaseAdapter implements OnClickListener {
	private List<Record> mDatas;
	private long mPrintTimeStart;
	private long mPrintTimeEnd;
	private long mExcTimeStart;
	private long mExcTimeEnd;
	private long printerIds[];
	private RecordDao mRd;
	private SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm:ss",
			Locale.CHINA);

	public ResultAdapter(long ptStart, long ptEnd, long etStart, long etEnd,
			long ids[]) {
		printerIds = ids;
		mPrintTimeEnd = ptEnd;
		mPrintTimeStart = ptStart;
		mExcTimeEnd = etEnd;
		mExcTimeStart = etStart;

		DaoMaster dm = new DaoMaster(App.db);
		DaoSession ds = dm.newSession();
		mRd = ds.getRecordDao();
		mDatas = getBaseBuilder().list();
	}

	@Override
	public int getCount() {
		return mDatas.size() + 1;
	}

	@Override
	public Object getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.item_queryresult, parent, false);
			vh = new ViewHolder();
			vh.tvId = (TextView) convertView
					.findViewById(R.id.tv_queryresult_id);
			vh.tvPrinter = (TextView) convertView
					.findViewById(R.id.tv_queryresult_printersn);
			vh.tvScore = (TextView) convertView
					.findViewById(R.id.tv_queryresult_score);
			vh.tvPrintTime = (TextView) convertView
					.findViewById(R.id.tv_queryresult_printtime);
			vh.tvExcTime = (TextView) convertView
					.findViewById(R.id.tv_queryresult_exctime);
			vh.tvId.setOnClickListener(this);
			vh.tvPrinter.setOnClickListener(this);
			vh.tvScore.setOnClickListener(this);
			vh.tvPrintTime.setOnClickListener(this);
			vh.tvExcTime.setOnClickListener(this);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		vh.tvId.setTag(position);
		vh.tvPrinter.setTag(position);
		vh.tvScore.setTag(position);
		vh.tvPrintTime.setTag(position);
		vh.tvExcTime.setTag(position);
		if (position == 0) {
			vh.tvId.setText("积分券ID");
			vh.tvPrinter.setText("打印机");
			vh.tvScore.setText("积分");
			vh.tvPrintTime.setText("打印时间");
			vh.tvExcTime.setText("兑换时间");
		} else {
			position--;
			Record r = mDatas.get(position);
			vh.tvId.setText(r.getId() + "");
			vh.tvPrinter.setText(r.getPrinter().getNickname());
			vh.tvScore.setText(r.getScore() + "");
			vh.tvPrintTime.setText(format.format(new Date(r.getPrint_time())));
			vh.tvExcTime.setText(format.format(new Date(r.getExc_time())));
		}
		return convertView;
	}

	class ViewHolder {
		TextView tvId;
		TextView tvPrinter;
		TextView tvScore;
		TextView tvPrintTime;
		TextView tvExcTime;
	}

	@Override
	public void onClick(View v) {
		int position = (int) v.getTag();
		if (position != 0)
			return;
		switch (v.getId()) {
		case R.id.tv_queryresult_score:
			onScore();
			break;
		default:
			break;
		}
	}

	private void onScore() {
		mDatas.clear();
		mDatas.addAll(getBaseBuilder().orderAsc(Properties.Score).list());
		notifyDataSetChanged();
	}

	private QueryBuilder<Record> getBaseBuilder() {
		QueryBuilder<Record> qb = mRd.queryBuilder();
		if (printerIds.length > 1) {
			qb.where(RecordDao.Properties.Printer_id.in(printerIds));
		} else if (printerIds.length == 1) {
			qb.where(RecordDao.Properties.Printer_id.eq(printerIds[0]));
		} else {
			throw new RuntimeException();
		}
		if (mPrintTimeStart > 0) {
			qb.where(Properties.Print_time.gt(mPrintTimeStart));
		}
		if (mPrintTimeEnd > 0) {
			qb.where(Properties.Print_time.lt(mPrintTimeEnd));
		}
		if (mExcTimeStart > 0) {
			qb.where(Properties.Exc_time.gt(mExcTimeStart));
		}
		if (mExcTimeEnd > 0) {
			qb.where(Properties.Exc_time.lt(mExcTimeEnd));
		}

		return qb;
	}
}
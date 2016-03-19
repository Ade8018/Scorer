package gd.zh.gamer.scorer.util;

import gd.zh.gamer.scorer.App;
import gd.zh.gamer.scorer.db.DaoMaster;
import gd.zh.gamer.scorer.db.DaoSession;
import gd.zh.gamer.scorer.db.PrinterDao;
import gd.zh.gamer.scorer.db.RecordDao;
import gd.zh.gamer.scorer.entity.Printer;
import gd.zh.gamer.scorer.entity.Record;

import java.util.List;

public class DaoUtil {
	public List<Record> findRecords(int printer_id, long timeStart, long timeEnd) {
		DaoMaster dm = new DaoMaster(App.db);
		DaoSession ds = dm.newSession();
		RecordDao rd = ds.getRecordDao();
		return rd.queryBuilder().where(RecordDao.Properties.Printer_id.eq(printer_id), RecordDao.Properties.Exc_time.between(timeStart, timeEnd)).list();
	}

	public List<Printer> findAllPrinter() {
		DaoMaster dm = new DaoMaster(App.db);
		DaoSession ds = dm.newSession();
		PrinterDao pd = ds.getPrinterDao();
		return pd.loadAll();
	}
}

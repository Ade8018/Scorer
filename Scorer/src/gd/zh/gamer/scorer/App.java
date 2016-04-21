package gd.zh.gamer.scorer;

import gd.zh.gamer.scorer.db.DaoMaster;
import gd.zh.gamer.scorer.util.SpHelper;
import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

public class App extends Application {
	public static SQLiteDatabase db;

	@Override
	public void onCreate() {
		super.onCreate();
		db = new DaoMaster.DevOpenHelper(this, "scorer.db", null).getWritableDatabase();
	}

}

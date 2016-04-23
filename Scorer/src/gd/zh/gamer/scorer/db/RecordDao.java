package gd.zh.gamer.scorer.db;

import gd.zh.gamer.scorer.entity.Printer;
import gd.zh.gamer.scorer.entity.Record;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.internal.SqlUtils;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "RECORD".
*/
public class RecordDao extends AbstractDao<Record, Long> {

    public static final String TABLENAME = "RECORD";

    /**
     * Properties of entity Record.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Score = new Property(1, long.class, "score", false, "SCORE");
        public final static Property Print_time = new Property(2, long.class, "print_time", false, "PRINT_TIME");
        public final static Property Exc_time = new Property(3, long.class, "exc_time", false, "EXC_TIME");
        public final static Property Pin = new Property(4, String.class, "pin", false, "PIN");
        public final static Property Printer_id = new Property(5, long.class, "printer_id", false, "PRINTER_ID");
    };

    private DaoSession daoSession;


    public RecordDao(DaoConfig config) {
        super(config);
    }
    
    public RecordDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"RECORD\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"SCORE\" INTEGER NOT NULL ," + // 1: score
                "\"PRINT_TIME\" INTEGER NOT NULL ," + // 2: print_time
                "\"EXC_TIME\" INTEGER NOT NULL ," + // 3: exc_time
                "\"PIN\" TEXT NOT NULL ," + // 4: pin
                "\"PRINTER_ID\" INTEGER NOT NULL );"); // 5: printer_id
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"RECORD\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Record entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getScore());
        stmt.bindLong(3, entity.getPrint_time());
        stmt.bindLong(4, entity.getExc_time());
        stmt.bindString(5, entity.getPin());
        stmt.bindLong(6, entity.getPrinter_id());
    }

    @Override
    protected void attachEntity(Record entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Record readEntity(Cursor cursor, int offset) {
        Record entity = new Record( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getLong(offset + 1), // score
            cursor.getLong(offset + 2), // print_time
            cursor.getLong(offset + 3), // exc_time
            cursor.getString(offset + 4), // pin
            cursor.getLong(offset + 5) // printer_id
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Record entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setScore(cursor.getLong(offset + 1));
        entity.setPrint_time(cursor.getLong(offset + 2));
        entity.setExc_time(cursor.getLong(offset + 3));
        entity.setPin(cursor.getString(offset + 4));
        entity.setPrinter_id(cursor.getLong(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Record entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Record entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getPrinterDao().getAllColumns());
            builder.append(" FROM RECORD T");
            builder.append(" LEFT JOIN PRINTER T0 ON T.\"PRINTER_ID\"=T0.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected Record loadCurrentDeep(Cursor cursor, boolean lock) {
        Record entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        Printer printer = loadCurrentOther(daoSession.getPrinterDao(), cursor, offset);
         if(printer != null) {
            entity.setPrinter(printer);
        }

        return entity;    
    }

    public Record loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<Record> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<Record> list = new ArrayList<Record>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<Record> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<Record> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}

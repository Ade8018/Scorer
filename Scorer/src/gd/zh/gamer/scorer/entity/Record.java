package gd.zh.gamer.scorer.entity;

import gd.zh.gamer.scorer.db.DaoSession;
import gd.zh.gamer.scorer.db.PrinterDao;
import gd.zh.gamer.scorer.db.RecordDao;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "RECORD".
 */
public class Record {

    private Long id;
    private long score;
    private long print_time;
    private long exc_time;
    /** Not-null value. */
    private String pin;
    private long printer_id;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient RecordDao myDao;

    private Printer printer;
    private Long printer__resolvedKey;


    public Record() {
    }

    public Record(Long id) {
        this.id = id;
    }

    public Record(Long id, long score, long print_time, long exc_time, String pin, long printer_id) {
        this.id = id;
        this.score = score;
        this.print_time = print_time;
        this.exc_time = exc_time;
        this.pin = pin;
        this.printer_id = printer_id;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRecordDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public long getPrint_time() {
        return print_time;
    }

    public void setPrint_time(long print_time) {
        this.print_time = print_time;
    }

    public long getExc_time() {
        return exc_time;
    }

    public void setExc_time(long exc_time) {
        this.exc_time = exc_time;
    }

    /** Not-null value. */
    public String getPin() {
        return pin;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setPin(String pin) {
        this.pin = pin;
    }

    public long getPrinter_id() {
        return printer_id;
    }

    public void setPrinter_id(long printer_id) {
        this.printer_id = printer_id;
    }

    /** To-one relationship, resolved on first access. */
    public Printer getPrinter() {
        long __key = this.printer_id;
        if (printer__resolvedKey == null || !printer__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PrinterDao targetDao = daoSession.getPrinterDao();
            Printer printerNew = targetDao.load(__key);
            synchronized (this) {
                printer = printerNew;
            	printer__resolvedKey = __key;
            }
        }
        return printer;
    }

    public void setPrinter(Printer printer) {
        if (printer == null) {
            throw new DaoException("To-one property 'printer_id' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.printer = printer;
            printer_id = printer.getId();
            printer__resolvedKey = printer_id;
        }
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

}

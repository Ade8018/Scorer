package gd.zh.gamer.scorer.entity;

import android.text.TextUtils;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "ACCOUNT".
 */
public class Account {
	public static final int TYPE_MANAGER = 1;
	public static final int TYPE_OPERATOR = 2;

    private Long id;
    /** Not-null value. */
    private String account;
    /** Not-null value. */
    private String password;
    private int type;

    public Account() {
    }

    public Account(Long id) {
        this.id = id;
    }

    public Account(Long id, String account, String password, int type) {
        this.id = id;
        this.account = account;
        this.password = password;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getAccount() {
        return account;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setAccount(String account) {
        this.account = account;
    }

    /** Not-null value. */
    public String getPassword() {
        return password;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setPassword(String password) {
        this.password = password;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    
    public static boolean isLogined(Account account) {
		return account != null;
	}

	public static boolean isManager(Account account) {
		return isLogined(account) && account.getType() == Account.TYPE_MANAGER;
	}

	public static boolean isValidAccountOrPwd(String text) {
		if (TextUtils.isEmpty(text) || text.length() < 4)
			return false;
		if (!text.matches("[A-Za-z0-9_]+"))
			return false;
		return true;
	}

}

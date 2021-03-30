package com.example.alarm;

public class ContactDBCtrict {
    private ContactDBCtrict(){}
    public static final String TBL_CONTACT = "ALARM_LIST";
    public static final String COL_CONTENT_TITLE = "CONTENT_TITLE";
    public static final String COL_CONTENT = "CONTENT";
    public static final String COL_TIME  = "TIME";
    public static final String COL_DAY = "DAY";

    //CREATE TABLE IF NOT EXISTS ALARM_LIST (CONTENT_TITLE TEXT, CONTENT TEXT , TIME INTEGER NOT NULL)
    public static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TBL_CONTACT +
            " (" +
            COL_CONTENT_TITLE + " TEXT" + " ," +
            COL_CONTENT +       " TEXT" + " ," +
            COL_TIME +          " INTEGER NOT NULL , " +
            COL_DAY +           " TEXT" + ")";
    //INSERT OR REPLACE INTO ALARM_LIST (CONTENT_TITLE, CONTENT, TIME) VALUES (X, X, X)
    public static final String SQL_INSERT = "INSERT OR REPLACE INTO " + TBL_CONTACT + "("+
            COL_CONTENT_TITLE + ", " + COL_CONTENT + ", " + COL_TIME + ", " + COL_DAY+") VALUES";
    public static final String SQL_DELETE = "DELETE FROM " +TBL_CONTACT;
    public static final String SQL_SELECT = "SELECT * FROM " + TBL_CONTACT;
}

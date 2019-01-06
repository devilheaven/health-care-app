package com.example.user.medical6;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class dataBase extends SQLiteOpenHelper{
    //資料庫版本
    private final static int DATABASE_VERSION = 5;
    //資料表名稱
    public static final String TABLE_c = "customer";
    public static final String TABLE_e = "examine";
    //客戶資料表
    public static final String id = "id";
    public static final String subjectId = "subjectId";
    public static final String idnum1= "guid";
    public static final String lastName = "lastName";
    public static final String height = "height";
    public static final String sex = "gender";
    //檢驗資料表
    public static final String num = "_id";
    public static final String idnum= "guid";
    public static final String time = "timestamp";
    public static final String weight = "weight";
    public static final String sbp = "sbp";
    public static final String dbp = "dbp";
    public static final String hr = "hr";
    public static final String record_status = "record_status";
    private final static String DATABASE_NAME = "sql.db";  //資料庫名稱

    public dataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String customer = "CREATE TABLE " + TABLE_c + " (" +id + " INTEGER PRIMARY KEY AUTOINCREMENT , "+subjectId+ " VARCHAR(32), "+idnum1+ " VARCHAR(32), " +lastName+ " VARCHAR(32), " + height + " VARCHAR(32), " + sex + " VARCHAR(5));";
        //time TimeStamp NOT NULL DEFAULT (datetime('now','localtime')) 世界時間
        final String examine = "CREATE TABLE " + TABLE_e  + " (" +num + " INTEGER PRIMARY KEY AUTOINCREMENT , " + time + " TimeStamp , " + weight + " VARCHAR(32), "+ sbp + " VARCHAR(32), "+ dbp + " VARCHAR(32), "+ hr + " VARCHAR(32), " + idnum + " VARCHAR(32)," + record_status + " VARCHAR(32));";

        db.execSQL(customer);
        db.execSQL(examine);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_c;
        final String DROP_TABLE1 = "DROP TABLE IF EXISTS " + TABLE_e;

        db.execSQL(DROP_TABLE);
        db.execSQL(DROP_TABLE1);
        onCreate(db);
    }
}

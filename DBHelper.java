package com.mobileappsco.training.day4;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by admin on 2/29/2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Day4.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "CONTACTS";
    public static final String COLUMN_FIRSTNAME = "FirstName";
    public static final String COLUMN_LASTNAME = "LastName";
    public static final String COLUMN_COUNTRY = "CountryofOrigin";
    public static final String COLUMN_DATE = "DateofBirth";
    public static final String CREATE_TABLE_SQL =
            "CREATE TABLE IF NOT EXISTS "+TABLE_NAME + "(" +
            COLUMN_FIRSTNAME + " VARCHAR(20) NOT NULL, " +
            COLUMN_LASTNAME + " VARCAR(20) NOT NULL, " +
            COLUMN_COUNTRY + " VARCHAR(14) NOT NULL, " +
            COLUMN_DATE + " DATE);";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("MYAPP", CREATE_TABLE_SQL);
        db.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("some sql statement to do something");
    }

}

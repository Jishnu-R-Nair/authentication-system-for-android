package com.example.king.lock;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by king on 21/1/16.
 */
public class LockDbHelper extends SQLiteOpenHelper {

    private static final String Type = " FLOAT";
    private static final String Comma_Sep = ",";
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE "+ LockContract.LockEntry.Table_Name+" ("+ LockContract.LockEntry._COUNT+" INTEGER PRIMARY KEY, "+
            LockContract.LockEntry.Column_Touch_x+Type+Comma_Sep+ LockContract.LockEntry.Column_Touch_y+Type+Comma_Sep+ LockContract.LockEntry.Column_Touch_size+Type+Comma_Sep+
            LockContract.LockEntry.Column_Touch_time+Type+Comma_Sep+ LockContract.LockEntry.Column_Accelerometer_x+Type+Comma_Sep+
            LockContract.LockEntry.Column_Accelerometer_y+Type+Comma_Sep+ LockContract.LockEntry.Column_Accelerometer_z+Type+Comma_Sep+
            LockContract.LockEntry.Column_Gyroscope_x+Type+Comma_Sep+ LockContract.LockEntry.Column_Gyroscope_y+Type+Comma_Sep+
            LockContract.LockEntry.Column_Gyroscope_z+Type+" )";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "+ LockContract.LockEntry.Table_Name;
    public static final int Database_Version = 1;
    public static final String Database_Name = "LockScreenApp.db";
    private  Context context;

    public LockDbHelper(Context context) {
        super(context, Database_Name, null, Database_Version);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db,oldVersion,newVersion);
    }

}

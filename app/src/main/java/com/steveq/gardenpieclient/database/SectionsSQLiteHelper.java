package com.steveq.gardenpieclient.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.steveq.gardenpieclient.R;

/**
 * Created by Adam on 2017-08-05.
 */

public class SectionsSQLiteHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "alarms_db";
    public static final Integer DB_VERSION = 1;
    private static Context mContext;

    //CREATE TABLES
    private final String CREATE_SECTIONS_TABLE = "CREATE TABLE IF NOT EXISTS " +
            SectionsContract.SectionsEntry.TABLE_NAME + " (" +
            SectionsContract.SectionsEntry.COLUMN_SECTION_NUM + " INTEGER PRIMARY KEY ON CONFLICT FAIL, " +
            SectionsContract.SectionsEntry.COLUMN_ACTIVE + " INTEGER )";

    private final String CREATE_SECTIONS_DAYS_TABLE = "CREATE TABLE IF NOT EXISTS " +
            SectionsContract.SectionsDaysEntry.TABLE_NAME+ " (" +
            SectionsContract.SectionsDaysEntry.COLUMN_SECTION_ID + " INTEGER, " +
            SectionsContract.SectionsDaysEntry.COLUMN_DAY + " TEXT CHECK(" +
            SectionsContract.SectionsDaysEntry.COLUMN_DAY + " == '" + mContext.getResources().getStringArray(R.array.days)[0] + "' OR " +
            SectionsContract.SectionsDaysEntry.COLUMN_DAY + " == '" + mContext.getResources().getStringArray(R.array.days)[1] + "' OR " +
            SectionsContract.SectionsDaysEntry.COLUMN_DAY + " == '" + mContext.getResources().getStringArray(R.array.days)[2] + "' OR " +
            SectionsContract.SectionsDaysEntry.COLUMN_DAY + " == '" + mContext.getResources().getStringArray(R.array.days)[3] + "' OR " +
            SectionsContract.SectionsDaysEntry.COLUMN_DAY + " == '" + mContext.getResources().getStringArray(R.array.days)[4] + "' OR " +
            SectionsContract.SectionsDaysEntry.COLUMN_DAY + " == '" + mContext.getResources().getStringArray(R.array.days)[5] + "' OR " +
            SectionsContract.SectionsDaysEntry.COLUMN_DAY + " == '" + mContext.getResources().getStringArray(R.array.days)[6] + "' ), " +
            "FOREIGN KEY(" + SectionsContract.SectionsDaysEntry.COLUMN_SECTION_ID + ") REFERENCES " + SectionsContract.SectionsEntry.TABLE_NAME + "(" + SectionsContract.SectionsEntry.COLUMN_SECTION_NUM+ ")," +
            "PRIMARY KEY(" + SectionsContract.SectionsDaysEntry.COLUMN_SECTION_ID + "," + SectionsContract.SectionsDaysEntry.COLUMN_DAY + "))";

    private final String CREATE_SECTIONS_TIMES_TABLE = "CREATE TABLE IF NOT EXISTS " +
            SectionsContract.SectionsTimesEntry.TABLE_NAME+ " (" +
            SectionsContract.SectionsTimesEntry.COLUMN_SECTION_ID + " INTEGER, " +
            SectionsContract.SectionsTimesEntry.COLUMN_TIME + " TEXT, " +
            "FOREIGN KEY(" + SectionsContract.SectionsTimesEntry.COLUMN_SECTION_ID + ") REFERENCES " + SectionsContract.SectionsEntry.TABLE_NAME + "(" + SectionsContract.SectionsEntry.COLUMN_SECTION_NUM+ ")," +
            "PRIMARY KEY(" + SectionsContract.SectionsTimesEntry.COLUMN_SECTION_ID + "," + SectionsContract.SectionsTimesEntry.COLUMN_TIME + "))";

    private static SectionsSQLiteHelper instance;

    public static SectionsSQLiteHelper getInstance(Context context){
        mContext = context;
        if(instance == null){
            instance = new SectionsSQLiteHelper(context);
            return instance;
        }
        return instance;
    }

    private SectionsSQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SECTIONS_TABLE);
        db.execSQL(CREATE_SECTIONS_DAYS_TABLE);
        db.execSQL(CREATE_SECTIONS_TIMES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

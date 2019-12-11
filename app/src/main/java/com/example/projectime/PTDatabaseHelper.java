package com.example.projectime;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class PTDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "projectime";
    private static final int DB_VERSION = 1;

    public PTDatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "CREATE TABLE CALENDAR (" +
                        "_id INTEGER ," +
                        "NAME TEXT," +
                        "COLOR INTEGER);");
        sqLiteDatabase.execSQL(
                "CREATE TABLE TAB (" +
                        "_id INTEGER ," +
                        "CALENDAR_ID INTEGER," +
                        "NAME TEXT)");
        sqLiteDatabase.execSQL(
                "CREATE TABLE EVENT (" +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "TAB_ID INTEGER," +
                        "NAME TEXT," +
                        "TIME INTEGER," +
                        "URI TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

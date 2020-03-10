package com.example.androidbasics.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_FILE_NAME = "random_words.db";
    private static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_FILE_NAME, new SQLiteDatabase.OpenParams.Builder().build().getCursorFactory(), DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        RandomWordsTable.createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //currently not necessary
    }
}

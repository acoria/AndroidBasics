package com.example.androidbasics.db;

import android.database.sqlite.SQLiteDatabase;

public class RandomWordsTable {

    //eindeutige ID für eine Auflistung
    public static final int ITEM_LIST_ID = 100;
    //eindeutige ID für einen Datensatz
    public static final int ITEM_ID = 101;
    public static final String TABNAME = "random_words";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABNAME + " (\n" +
            "\t\"id\"\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
            "\t\"word\"\tTEXT NOT NULL,\n" +
            "\t\"create_date\"\tTEXT NOT NULL\n" +
            ")";

    static void createTable(SQLiteDatabase db){
        db.execSQL(CREATE_TABLE);
    }
}

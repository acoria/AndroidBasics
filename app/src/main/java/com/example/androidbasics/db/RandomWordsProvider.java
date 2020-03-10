package com.example.androidbasics.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Locale;

public class RandomWordsProvider extends ContentProvider {

    public static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    private DBHelper dbHelper = null;
    final String ID_WHERE = BaseColumns._ID + "=?";

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        //Uri auflösen
        final int uriType = URI_MATCHER.match(uri);
        String type = null;

        switch (uriType){
            case RandomWordsTable.ITEM_LIST_ID:
                type = RandomWordsContract.RandomWords.CONTENT_TYPE;
                break;
            case RandomWordsTable.ITEM_ID:
                type = RandomWordsContract.RandomWords.CONTENT_ITEM_TYPE;
                break;
            default :
                //Uri ist unbekannt
                throw new IllegalArgumentException(String.format(Locale.GERMANY, "Unbekannte URI: %s, uri"));
        }
        return type;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int uriType = URI_MATCHER.match(uri);

        Uri insertUri = null;
        long newItemId = -1;

        switch(uriType){
            case RandomWordsTable.ITEM_LIST_ID :
            case RandomWordsTable.ITEM_ID:
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                newItemId = db.insert(RandomWordsTable.TABNAME, null, values);
                db.close();
                break;
            default :
                //Uri ist unbekannt
                throw new IllegalArgumentException(String.format(Locale.GERMANY, "Unbekannte URI: %s, uri"));
        }
        //insert erfolgreich
        if(newItemId > 0){
            insertUri = ContentUris.withAppendedId(
                    RandomWordsContract.RandomWords.CONTENT_URI,
                    newItemId
            );

            //Benachrichtigung über die Änderung der Daten
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return insertUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int uriType = URI_MATCHER.match(uri);
        int deletedItems = 0;
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        switch(uriType){
            case RandomWordsTable.ITEM_LIST_ID :
                deletedItems = db.delete(RandomWordsTable.TABNAME, selection, selectionArgs);
                db.close();
                break;
            case RandomWordsTable.ITEM_ID:
                deletedItems = db.delete(RandomWordsTable.TABNAME, ID_WHERE, idAsArray(ContentUris.parseId(uri)));
                db.close();
                break;
            default :
                //Uri ist unbekannt
                throw new IllegalArgumentException(String.format(Locale.GERMANY, "Unbekannte URI: %s, uri"));
        }

        if(deletedItems > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return deletedItems;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int uriType = URI_MATCHER.match(uri);
        int updatedItems = 0;
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        switch(uriType){
            case RandomWordsTable.ITEM_LIST_ID :
                updatedItems = db.update(RandomWordsTable.TABNAME, values, selection, selectionArgs);
                db.close();
                break;
            case RandomWordsTable.ITEM_ID :
                updatedItems = db.update(RandomWordsTable.TABNAME, values, ID_WHERE, idAsArray(ContentUris.parseId(uri)));
                db.close();
            break;

            default :
                throw new IllegalArgumentException(String.format(Locale.GERMANY, "Unbekannte URI: %s", uri));
        }
        if(updatedItems > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return updatedItems;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final int uriType = URI_MATCHER.match(uri);
        Cursor data;
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        switch(uriType){
            case RandomWordsTable.ITEM_LIST_ID :
                data = db.query(RandomWordsTable.TABNAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case RandomWordsTable.ITEM_ID:
            data = db.query(RandomWordsTable.TABNAME, projection, ID_WHERE, idAsArray(ContentUris.parseId(uri)), null, null, null);
                break;

            default:
                throw new IllegalArgumentException(String.format(Locale.GERMANY, "Unbekannte URI: %s", uri));
        }

        if(data != null){
            data.setNotificationUri(getContext().getContentResolver(),uri);
        }
        return data;
    }

    private String[] idAsArray(long id){
        return new String[]{String.valueOf(id)};
    }

    //initialisierung der Lookup-Tabellen
    static{
        //Lookup für die Auflistung
        URI_MATCHER.addURI(
            RandomWordsContract.AUTHORITY,
            //Unterverzeichnis
            RandomWordsContract.RandomWords.CONTENT_DIRECTORY,
            RandomWordsTable.ITEM_LIST_ID
        );

        //Lookup für einen Datensatz
        URI_MATCHER.addURI(
            RandomWordsContract.AUTHORITY,
            //Unterverzeichnis mit ID (#) des Datensatzes
            RandomWordsContract.RandomWords.CONTENT_DIRECTORY + "/#",
            RandomWordsTable.ITEM_ID
        );
    }
}

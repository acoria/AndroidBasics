package com.example.androidbasics.persist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.example.androidbasics.db.RandomWordsContract;
import com.example.androidbasics.pojo.RandomWord;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class DBConnStore implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAB_FIELD_WORD = "word";
    private static final String TAB_FIELD_DATE = "create_date";
    public static final int LOADER_ID = 100;
    private static final String TAG = DBConnStore.class.getSimpleName();

    List<RandomWord> words = new ArrayList<>();
    BehaviorSubject<List<RandomWord>> randomWords = BehaviorSubject.create();
    Context context;

    public DBConnStore(Context context) {
        this.context = context;
    }

    public void put(RandomWord randomWord){
        saveInDB(randomWord);
        words.add(randomWord);
        randomWords.onNext(words);
    }

    private void saveInDB(RandomWord randomWord) {
        ContentValues values = new ContentValues();
        values.put(TAB_FIELD_WORD, randomWord.getWord());
        String dbDate = RandomWordsContract.Converter.format(randomWord.getDate());
        values.put(TAB_FIELD_DATE, dbDate);

        context.getContentResolver().insert(RandomWordsContract.RandomWords.CONTENT_URI,values);
    }

    public Observable<List<RandomWord>> getStream(){
        return randomWords.hide();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, @Nullable Bundle bundle) {
        CursorLoader loader = null;
        switch(loaderId){
            case LOADER_ID:
                loader = new CursorLoader(
                        context,
                        RandomWordsContract.RandomWords.CONTENT_URI,
                        null,
                        null,
                        null,
                        null
                );
                break;
        }
        return loader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        switch(loader.getId()){
            case LOADER_ID:
                    words = new ArrayList<>();
                    while(cursor.moveToNext()){
                        words.add(cursorToRandomWord(cursor));
                    }
                    randomWords.onNext(words);
                break;
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        switch(loader.getId()){
            case LOADER_ID:
                words = new ArrayList<>();
                randomWords.onNext(words);
                break;
        }
    }

    private RandomWord cursorToRandomWord(Cursor cursor) {
        String word = cursor.getString(cursor.getColumnIndex(RandomWordsContract.Columns.WORD));
        String dbDate = cursor.getString(cursor.getColumnIndex(RandomWordsContract.Columns.CREATE_DATE));
        RandomWord randomWord = null;
        try {
            Calendar date = RandomWordsContract.Converter.parse(dbDate);
            randomWord = new RandomWord(word, date);
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
        }
        return randomWord;
    }
}

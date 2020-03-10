package com.example.androidbasics;

import android.content.Context;
import android.support.v4.app.LoaderManager;

import com.example.androidbasics.persist.DBConnStore;
import com.example.androidbasics.pojo.RandomWord;

import java.util.List;

import io.reactivex.Observable;

public class DBConnUseCase {

    DBConnStore store;

    public DBConnUseCase(Context context) {
        store = new DBConnStore(context);
    }

    public void addRandomWord(RandomWord randomWord, Context context){
        store.put(randomWord);
    }

    public Observable<List<RandomWord>> getWords(){
        return store.getStream();
    }

    public LoaderManager.LoaderCallbacks getLoaderCallback(){
        return store;
    }
}

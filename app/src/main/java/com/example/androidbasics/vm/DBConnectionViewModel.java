package com.example.androidbasics.vm;

import com.example.androidbasics.DBConnUseCase;
import com.example.androidbasics.db.RandomWordsContract;
import com.example.androidbasics.pojo.RandomWord;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;

import io.reactivex.Observable;

public class DBConnectionViewModel {

    private final DBConnUseCase useCase;
    private static DateFormat dateFormatter;

    public DBConnectionViewModel(DBConnUseCase useCase) {
        this.useCase = useCase;
    }

    public Observable<List<RandomWord>> getWords(){
        return useCase.getWords();
    }

    private static String formatDate(Calendar currentTime){
        if(dateFormatter == null){
            dateFormatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
        }
        return dateFormatter.format(currentTime.getTime());
    }
}

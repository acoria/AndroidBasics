package com.example.androidbasics.pojo;

import java.util.Calendar;

public class RandomWord {
    String word;
    Calendar date;

    public RandomWord(String word, Calendar date) {
        this.word = word;
        this.date = date;
    }

    public RandomWord(String word) {
        this.word = word;
        this.date = Calendar.getInstance();
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }
}

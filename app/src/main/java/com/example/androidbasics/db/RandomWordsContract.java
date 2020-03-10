package com.example.androidbasics.db;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RandomWordsContract {

    //Eindeutiger Name des Providers innerhalb des Betriebssystems
    public static final String AUTHORITY = "com.example.androidbasics.provider";
    //Basis URI zum Content Provider
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    //Kontrakt für Wörter
    public static final class RandomWords{

        //Unterverzeichnis für die Daten
        public static final String CONTENT_DIRECTORY = "words";

        //Uri zu den Daten
        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, CONTENT_DIRECTORY);

        //Datentyp für die Auflistung der Daten
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_DIRECTORY;

        //Datentyp für einen einzelnen Datensatz
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_DIRECTORY;
    }
    public interface Columns extends BaseColumns{
        String WORD = "word";
        String CREATE_DATE = "create_date";
    }

    public static final class Converter{
        private static final String ISO_8601_PATTERN = "yyyy-MM-dd'T'HH:mm";

        public static final DateFormat DB_DATE_TIME_FORMATTER = new SimpleDateFormat(ISO_8601_PATTERN, Locale.GERMANY);

        public static Calendar parse(String dbTime) throws ParseException{
            Calendar date = Calendar.getInstance();
            date.setTime(DB_DATE_TIME_FORMATTER.parse(dbTime));
            return date;
        }
        public static String format(Calendar dateTime){
            return DB_DATE_TIME_FORMATTER.format(dateTime.getTime());
        }

    }
}


package com.example.sirth.moviedbappnanodegree.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;

/**
 * Created by sirth on 26/03/2018.
 */

public class MovieSqliteHelper extends SQLiteOpenHelper {

    public static final String DBNAME ="MOVIEDB";
    public static final int version=5;

    SQLiteOpenHelper dbhandler;
    SQLiteDatabase db;

    public static final String TABLE_NAME = "favorite";
    public static final String _id = "movie_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_USER_RATING = "vote_average";
    public static final String COLUMN_POSTER_PATH = "poster_path";
    public static final String COLUMN_SYNOPSIS = "overview";
    public static final String COLUMN_RELEASE_DATE="release_date";
    public static final String COLUMN_BACKDROP_PATH="backdrop_path";


    public MovieSqliteHelper (Context context){
        super(context,DBNAME,null,version);

   }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_FAVORITE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT NOT NULL, " +
                COLUMN_BACKDROP_PATH + " TEXT NOT NULL, "+
                COLUMN_USER_RATING + " REAL NOT NULL, " +
                COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                COLUMN_SYNOPSIS + " TEXT NOT NULL, " +
                COLUMN_RELEASE_DATE +" TEXT NOT NULL" +
                " ); ";
        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieSqliteHelper.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}

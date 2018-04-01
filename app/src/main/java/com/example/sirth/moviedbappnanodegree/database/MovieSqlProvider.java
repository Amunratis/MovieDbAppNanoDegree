package com.example.sirth.moviedbappnanodegree.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by sirth on 28/03/2018.
 */

public class MovieSqlProvider extends ContentProvider {

    MovieSqliteHelper movieSqliteHelper;

    private static final int ALL_MOVIES = 1;
    private static final int SINGLE_MOVIE_ID = 2;

    private static final UriMatcher uriMatcher;
    private static final String AUTHORITY = "moviesapp.data.MovieProvider";

    public static final Uri CONTENT_URI =Uri.parse("content://" + AUTHORITY + "/movies");

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "movies", ALL_MOVIES);
        uriMatcher.addURI(AUTHORITY, "movies/#", SINGLE_MOVIE_ID);
    }

    @Override
    public boolean onCreate() {
        movieSqliteHelper=new MovieSqliteHelper(getContext());
        return false;
    }

    // The query() method must return a Cursor object, or if it fails,
    // throw an Exception. If you are using an SQLite database as your data storage,
    // you can simply return the Cursor returned by one of the query() methods of the
    // SQLiteDatabase class. If the query does not match any rows, you should return a
    // Cursor instance whose getCount() method returns 0. You should return null only
    // if an internal error occurred during the query process.

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = movieSqliteHelper.getReadableDatabase();
        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
            case ALL_MOVIES:
                cursor = db.query(MovieSqliteHelper.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case SINGLE_MOVIE_ID:
                selection = MovieSqliteHelper._id + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = db.query(MovieSqliteHelper.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        SQLiteDatabase db = movieSqliteHelper.getWritableDatabase();
        long idInserted = 0;
        Uri uriInserted;


        switch (uriMatcher.match(uri)) {
            case ALL_MOVIES:
                idInserted = db.insert(MovieSqliteHelper.TABLE_NAME, null, contentValues);
                if (idInserted > 0) {
                    uriInserted = Uri.parse(MovieSqliteHelper._id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Error on uri" + uri);

        }
        getContext().getContentResolver().notifyChange(uri, null);
        return uriInserted;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = movieSqliteHelper.getWritableDatabase();

        int deletedMovie;
        if (selection == null) {
            selection = "1";
        }

        switch (uriMatcher.match(uri)) {
            case ALL_MOVIES:
                deletedMovie = db.delete(MovieSqliteHelper.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported URI" + uri);

        }

        if (deletedMovie != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return deletedMovie;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}

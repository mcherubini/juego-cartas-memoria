package com.example.michele.proyectojuegoparejas;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FeedReaderDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "puntuaciones_juego_cartas.db";

    public FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        Log.d("DB","BASE DE DATOS CREADA");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over

        if(oldVersion < 2){
            db.execSQL(SQL_DELETE_ENTRIES);
            Log.d("DB","BASE DE DATOS ACTUALIZADA");
            onCreate(db);
        }




    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    private static final String SQL_V2_ALTER =
            "ALTER TABLE " + FeedReaderContract.FeedEntry.TABLE_NAME +
            " ADD COLUMN " + FeedReaderContract.FeedEntry.COLUMN_NAME_DIFICULT + "INTEGER";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedReaderContract.FeedEntry.TABLE_NAME + "(" +
                    FeedReaderContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_SCORE + " INTEGER," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_PLAYER + " TEXT," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_RESULT + " INTEGER," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_DIFICULT +"INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry.TABLE_NAME;
}

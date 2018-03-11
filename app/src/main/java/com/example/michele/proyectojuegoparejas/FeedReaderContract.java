package com.example.michele.proyectojuegoparejas;


import android.provider.BaseColumns;

/**
 * Created by michele on 23/01/2018.
 */

public class FeedReaderContract {

    private FeedReaderContract() {}

    /* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "puntuaciones";
        public static final String COLUMN_NAME_SCORE = "puntuacion";
        public static final String COLUMN_NAME_PLAYER = "jugador";
        public static final String COLUMN_NAME_RESULT = "resultado";
        public static final String COLUMN_NAME_DIFICULT = "dificultad";
    }

}

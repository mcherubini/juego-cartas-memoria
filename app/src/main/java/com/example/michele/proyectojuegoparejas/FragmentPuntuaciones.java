package com.example.michele.proyectojuegoparejas;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


public class FragmentPuntuaciones extends Fragment {

    public FragmentPuntuaciones() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista=  inflater.inflate(R.layout.fragment_fragment_puntuaciones, container, false);
        ListView listadoItems = (ListView) vista.findViewById(R.id.listadoCasillas);

        FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(getActivity().getApplicationContext());
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

// Define a projection that specifies which columns from the database
// you will actually use after this query.

        String[] projection = {
                FeedReaderContract.FeedEntry._ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_SCORE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_PLAYER,
                FeedReaderContract.FeedEntry.COLUMN_NAME_RESULT
        };

        String sortOrder =
                FeedReaderContract.FeedEntry.COLUMN_NAME_SCORE + " DESC,"+
                        FeedReaderContract.FeedEntry.COLUMN_NAME_RESULT + " DESC";

        Cursor cursor = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        AdaptadorCursor todoAdapter = new AdaptadorCursor(getActivity().getApplicationContext(), cursor);
        listadoItems.setAdapter(todoAdapter);

        return vista;
    }

}

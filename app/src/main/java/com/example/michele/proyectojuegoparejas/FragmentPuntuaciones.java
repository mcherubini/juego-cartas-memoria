package com.example.michele.proyectojuegoparejas;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;


public class FragmentPuntuaciones extends Fragment implements RadioGroup.OnCheckedChangeListener{

    private AdaptadorCursor todoAdapter;
    private ListView listadoItems;
    private Cursor cursor;
    private final String where = FeedReaderContract.FeedEntry.COLUMN_NAME_DIFICULT + " = ?";
    private final String[] projection = {
            FeedReaderContract.FeedEntry._ID,
            FeedReaderContract.FeedEntry.COLUMN_NAME_SCORE,
            FeedReaderContract.FeedEntry.COLUMN_NAME_PLAYER,
            FeedReaderContract.FeedEntry.COLUMN_NAME_RESULT,
            FeedReaderContract.FeedEntry.COLUMN_NAME_DIFICULT,
            FeedReaderContract.FeedEntry.COLUMN_NAME_GAMETIME
    };;
    private final String[] valuesFacil = {"1"};
    private final String[] valuesNormal = {"2"};
    private final String[] valuesDificil = {"3"};
    private SQLiteDatabase db;
    private View vista;
    private String sortOrder;

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
        vista=  inflater.inflate(R.layout.fragment_fragment_puntuaciones, container, false);
        listadoItems = (ListView) vista.findViewById(R.id.listadoCasillas);
        RadioGroup group = (RadioGroup) vista.findViewById(R.id.grupo_filtro);
        group.setOnCheckedChangeListener(this);
        FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(getActivity().getApplicationContext());
        db = mDbHelper.getReadableDatabase();
        Log.d("DB","Version actual" + db.getVersion());
// Define a projection that specifies which columns from the database
// you will actually use after this query.


        sortOrder =
                FeedReaderContract.FeedEntry.COLUMN_NAME_SCORE + " DESC,"+
                        FeedReaderContract.FeedEntry.COLUMN_NAME_RESULT + " DESC";

        hacerConsulta(valuesNormal);
        todoAdapter = new AdaptadorCursor(getActivity().getApplicationContext(), cursor);
        listadoItems.setAdapter(todoAdapter);

        return vista;
    }

    private void filtroDificultad(int id){
        RadioButton botonSelec = (RadioButton) vista.findViewById(id);

        boolean checked = botonSelec.isChecked();

        switch(botonSelec.getId()) {
            case(R.id.filtro_facil):
                if(checked){
                    hacerConsulta(valuesFacil);
                }
                break;
            case(R.id.filtro_normal):
                if(checked){
                    hacerConsulta(valuesNormal);
                }
                break;
            case(R.id.filtro_dificil):
                if(checked){
                    hacerConsulta(valuesDificil);
                }
                break;
        }
    }

    private void hacerConsulta(String[] filtro){
        cursor = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                where,                                // The columns for the WHERE clause
                filtro,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        todoAdapter = new AdaptadorCursor(getActivity().getApplicationContext(),cursor);
        listadoItems.setAdapter(todoAdapter);
    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
        filtroDificultad(i);
        Log.d("filtro","nueva consulta");
    }
}

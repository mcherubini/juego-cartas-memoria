package com.example.michele.proyectojuegoparejas;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by michele on 24/01/2018.
 */

public class AdaptadorCursor extends CursorAdapter {

    private Context context;

    public AdaptadorCursor(Context context, Cursor cursor) {
        super(context, cursor, 0);
        this.context = context;
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView textoJugador = (TextView) view.findViewById(R.id.textoUsuario);
        TextView textoScore = (TextView) view.findViewById(R.id.textoScore);
        ImageView imagen = (ImageView) view.findViewById(R.id.imagenResultado);

        // Extract properties from cursor

        int score = cursor.getInt(1);
        String jugador = cursor.getString(2);
        int resultado = cursor.getInt(3);
        // Populate fields with extracted properties

        textoJugador.setText(context.getString(R.string.jugador) + ": " + jugador);
        textoScore.setText(context.getString(R.string.score) +  ": " + String.valueOf(score));

        //0 perder 1 empate 2 victoria
        switch (resultado){
            case 0:
                imagen.setImageResource(R.drawable.sad);
                break;
            case 1:
                imagen.setImageResource(R.drawable.sceptic);
                break;
            case 2:
                imagen.setImageResource(R.drawable.happy);
                break;
        }
    }
}
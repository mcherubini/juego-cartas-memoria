package com.example.michele.proyectojuegoparejas;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Debug;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPartida extends Fragment implements View.OnClickListener{

    private final int COLUMNAS = 8;
    private int filas;
    private int dificultad;
    private LinearLayout xmlPartida;
    private GridLayout gridLayout;
    private final int PUNTOS = 10;
    private int[] idsImagenes;
    private int[] imagenesPartida;
    private Partida partida;
    private boolean hiloEspera = false;
    private int scoreJugador = 0;
    private int scoreIA = 0;
    private String textoBaseJugador;
    private String textoBaseIA;
    private TextView scoreTextJugador;
    private TextView scoreTextIA;

    public FragmentPartida() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        xmlPartida = (LinearLayout) inflater.inflate(R.layout.fragment_partida, container, false);

        scoreTextJugador = (TextView) xmlPartida.findViewById(R.id.score_jugador);
        scoreTextJugador.setText(scoreTextJugador.getText().toString() + scoreJugador);

        textoBaseJugador = getResources().getString(R.string.tu_puntuacion);
        textoBaseIA = getResources().getString(R.string.puntuacion_contrincante);

        scoreTextIA = (TextView) xmlPartida.findViewById(R.id.score_ia);
        scoreTextIA.setText(scoreTextIA.getText().toString() + scoreIA);

        gridLayout = xmlPartida.findViewById(R.id.tablero);
        gridLayout.setColumnCount(COLUMNAS);
        gridLayout.setRowCount(filas);

        partida = new Partida(gridLayout,dificultad);
        /*final R.drawable drawableResources = new R.drawable();
        final Class<R.drawable> c = R.drawable.class;
        final Field[] fields = c.getDeclaredFields();*/

        idsImagenes = new int[50];
        Log.d("pene", "antes del for ");

        for (int i = 1, max = idsImagenes.length; i <= max; i++) {

            Log.d("mensaje", "despues del for ");
            int resourceId;
            String nombreImagen = "";

                if(i < 10){
                    nombreImagen = "imagen_tablero00" + i +"";
                }else if( i < 100){
                    nombreImagen = "imagen_tablero0" + i +"";
                }
                    Log.d("mensaje11",nombreImagen);
                resourceId = getContext().getResources().getIdentifier(nombreImagen, "drawable",
                        getContext().getApplicationContext().getPackageName());//recuperamos el id de cada imagen
                Log.d("mensaje7", "id imagen: " + resourceId + "" +nombreImagen);
                //Log.d("dificultad", Integer.toString(dificultad));

            idsImagenes[i-1] = resourceId;

        }//for

        generarInfoTablero();
        colocarCasillas();

        return xmlPartida;
    }//onCreateView

    @Override
    public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);

        Intent intent = getActivity().getIntent();
        dificultad = intent.getIntExtra("dificultad",2);

        switch (dificultad){
            case 1:
                filas = 1;
                break;
            case 2:
                filas = 2;
                break;
            case 3:
                filas = 4;
                break;
        }

        //Log.d("prueba2","Hola " + Integer.toString(R.drawable.imagen_tablero050));
    }// onCreate


    private void resultadoPartida(){

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast toast;
                if(scoreIA == scoreJugador){
                    toast = Toast.makeText(getContext(),getResources().getString(R.string.empate),Toast.LENGTH_LONG);

                }else if(scoreIA > scoreJugador){
                    toast = Toast.makeText(getContext(),getResources().getString(R.string.derrota),Toast.LENGTH_LONG);
                }else{
                    toast = Toast.makeText(getContext(),getResources().getString(R.string.victoria),Toast.LENGTH_LONG);
                }
                toast.show();
            }
        });


                SystemClock.sleep(2500);
                startActivity(new Intent(getContext(),MainActivity.class));



    }

    private void generarInfoTablero(){

        /*for(int i = 0; i < 50;i++){
            Log.d("mensaje10"," " + idsImagenes[i]);
        }*/

        imagenesPartida = new int[COLUMNAS*filas];
        Log.d("mensaje1","total de casillas:" + imagenesPartida.length + "totalImg" + idsImagenes.length);
        for(int i = 0; i < imagenesPartida.length;i += 2){
            boolean casillaAgregada = false;

            while (!casillaAgregada){
                //Log.d("mensaje2","entra al while i vale:" + i);
                int posicionImagen =  (int) (Math.random() * (double) idsImagenes.length);
                //Log.d("mensaje5","indice aleatorio vale:" + posicionImagen);
                boolean yaAgregado = false;

                for(int j = 0; j < imagenesPartida.length;j++){//comprueba si imagen ya existe en array de partida
                    Log.d("mensaje3","entra al for anidado vale:" + j);

                    if(idsImagenes[posicionImagen] == imagenesPartida[j]){
                        yaAgregado = true;
                        Log.d("mensaje4","idImagen vale" + idsImagenes[posicionImagen] + " y el de partida vale " +
                                imagenesPartida[j]);
                    }
                }

                if(!yaAgregado){
                    Log.d("mensaje1"+i,"Se agrega imagen al indice" + i);
                    imagenesPartida[i] = idsImagenes[posicionImagen];
                    imagenesPartida[i+1] = idsImagenes[posicionImagen];
                    casillaAgregada = true;
                }

            }//while
        }//for
    }//generar info tablero

    private void colocarCasillas(){

        boolean yaColocado;
        boolean yaExiste;

        int[] posicionCasillas = new int[imagenesPartida.length];

        Arrays.fill(posicionCasillas,-1);

        for(int i = 0; i < imagenesPartida.length;i++){

            yaColocado = false;

            while(!yaColocado){

                int posicion = (int) (Math.random() * (double) imagenesPartida.length);
                yaExiste = false;

                for(int k = 0; k < posicionCasillas.length;k++){

                    if(posicionCasillas[k] == posicion){
                        yaExiste = true;
                    }
                }

                if(!yaExiste){
                    posicionCasillas[i] = posicion;
                    GridLayout.LayoutParams params = new GridLayout.LayoutParams();

                    ImageView imagen = new ImageView(getContext());
                    imagen.setImageResource(R.drawable.reverso_carta);
                    imagen.setOnClickListener(this);
                    imagen.setLayoutParams(params);
                    imagen.getLayoutParams().height = 200;
                    imagen.getLayoutParams().width = 200;
                    Casilla casilla = new Casilla(imagenesPartida[posicion],i);
                    imagen.setTag(casilla);

                    GradientDrawable gw = new GradientDrawable();
                    gw.setShape(GradientDrawable.RECTANGLE);
                    gw.setStroke(2, Color.BLACK);
                    imagen.setBackgroundDrawable(gw);

                    gridLayout.addView(imagen);

                    yaColocado = true;
                }
            }//while
        }//for
    }

    @Override
    public void onClick(final View view) {

            if(!hiloEspera){

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            final Casilla casilla = (Casilla) view.getTag();
                            final ImageView imagen;

                            switch (partida.turno(view)){
                                case 0:
                                    Log.d("mensaje","carta ya levantada no valida");
                                    break;

                                case 1://primer turno imagen no alzada
                                    imagen = (ImageView) view;
                                    imagen.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            imagen.setImageResource(casilla.getIdImagen());

                                        }
                                    });
                                    Log.d("mensaje","primer turno se levanta carta");
                                    casilla.setEstaLevantada(true);
                                    break;
                                case 2://segundo turno imagenes iguales
                                    imagen = (ImageView) view;

                                    imagen.post(new Runnable() {
                                        @Override
                                        public void run() {

                                            imagen.setImageResource(casilla.getIdImagen());
                                            scoreJugador += PUNTOS;
                                            scoreTextJugador.setText(textoBaseJugador + scoreJugador);
                                        }
                                    });

                                    Log.d("mensaje","segundo turno se levanta carta se quedan levantadas");
                                    casilla.setEstaLevantada(true);

                                    if(partida.finPartida()){
                                        resultadoPartida();
                                    }

                                    moverIA();

                                    if(partida.finPartida()){
                                        resultadoPartida();
                                    }
                                    break;
                                case 3://segundo turno imagenes diferentes
                                    final ImageView primeraCarta = partida.getImagenPrimeraCarta();
                                    imagen = (ImageView) view;

                                    imagen.post(new Runnable() {
                                        @Override
                                        public void run() {

                                            imagen.setImageResource(casilla.getIdImagen());

                                        }
                                    });
                                    hiloEspera = true;
                                    Log.d("mensaje","segundo turno se levanta carta no son iguales");
                                    SystemClock.sleep(1000);

                                    imagen.post(new Runnable() {
                                        @Override
                                        public void run() {

                                            imagen.setImageResource(R.drawable.reverso_carta);
                                            casilla.setEstaLevantada(false);
                                            partida.getPrimeraCarta().setEstaLevantada(false);
                                            primeraCarta.setImageResource(R.drawable.reverso_carta);

                                        }
                                    });
                                    Log.d("mensaje","segundo turno se vuelven a esconder las cartas");
                                    hiloEspera = false;

                                    moverIA();
                                    if(partida.finPartida()){
                                        resultadoPartida();
                                    }

                                    break;
                            }//switch
                        }
                    }).start();

            }

        //Log.d("mensaje","turno llamado");

    }//onClick

    private void moverIA(){

        final View[] eleccionIA = partida.ia();

        final Casilla casilla1 = (Casilla) eleccionIA[0].getTag();
        final Casilla casilla2 = (Casilla) eleccionIA[1].getTag();

        if(casilla1.equals(casilla2)){//las dos cartas iguales
            eleccionIA[0].post(new Runnable() {
                @Override
                public void run() {
                    ((ImageView) eleccionIA[0]).setImageResource(casilla1.getIdImagen());
                    ((ImageView) eleccionIA[1]).setImageResource(casilla2.getIdImagen());
                    casilla1.setEstaLevantada(true);
                    casilla2.setEstaLevantada(true);
                    scoreIA += PUNTOS;
                    scoreTextIA.setText(textoBaseIA + scoreIA);
                }
            });
        }else{//cartas diferentes
            eleccionIA[0].post(new Runnable() {
                @Override
                public void run() {
                    ((ImageView) eleccionIA[0]).setImageResource(casilla1.getIdImagen());
                    ((ImageView) eleccionIA[1]).setImageResource(casilla2.getIdImagen());

                }
            });

            hiloEspera = true;
            SystemClock.sleep(1000);

            eleccionIA[0].post(new Runnable() {
                @Override
                public void run() {
                    ((ImageView) eleccionIA[0]).setImageResource(R.drawable.reverso_carta);
                    ((ImageView) eleccionIA[1]).setImageResource(R.drawable.reverso_carta);
                    casilla1.setEstaLevantada(false);
                    casilla2.setEstaLevantada(false);
                }
            });
            hiloEspera = false;
        }
    }//moverIA
}
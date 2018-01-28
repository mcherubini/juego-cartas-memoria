package com.example.michele.proyectojuegoparejas;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPartida extends Fragment implements View.OnClickListener{

    private final int COLUMNAS = 8;
    private final int PUNTOS = 10;//puntos que se suman cada vez que hay un acierto.
    private int filas;
    private int dificultad;
    private int scoreJugador = 0;
    private int scoreIA = 0;
    private boolean hiloEspera = false;
    private String jugador;
    private LinearLayout xmlPartida;
    private GridLayout gridLayout;
    private Partida partida;
    private String textoBaseJugador;
    private String textoBaseIA;
    private TextView scoreTextJugador;
    private TextView scoreTextIA;
    private MediaPlayer mediaPlayer = null;

    public FragmentPartida() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("sonido","ONCREATE");
        super.onCreate(savedInstanceState);

        Intent intent = getActivity().getIntent();
        dificultad = intent.getIntExtra("dificultad",2);//dificultad es normal si no tiene valor

        switch (dificultad){//segun la dificultad se establece las filas que tendra el tablero
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

    }// onCreate

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("sonido","ONCREATEVIEW");
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

        int[] idsImagenes = new int[50];//indice es total de imagenes tablero en carpeta drawable
        Log.d("pene", "antes del for ");

        for (int i = 1, max = idsImagenes.length; i <= max; i++) {
            /*se cargan los ids de la carpeta drawable en el array empieza contando desde uno
            * porque la primera imagen esta etiquetada como 001 */
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

            idsImagenes[i-1] = resourceId;

        }//for

        generarTablero(idsImagenes);

        if(mediaPlayer == null){
            mediaPlayer = MediaPlayer.create(getContext(),R.raw.ljones_mango_kimono);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
            Log.d("audio","audio creado");
        }

        return xmlPartida;//devolvemos la vista del xml asociado al fragment
    }//onCreateView

    @Override
    public void onStart() {
        super.onStart();
        if( mediaPlayer != null && !mediaPlayer.isPlaying()){
            mediaPlayer.start();
        }
        Log.d("sonido","ONSTART");
// add your code here which executes when the Fragment gets visible.
    }



    @Override public void onPause(){
        Log.d("sonido","ONPAUSE");
        super.onPause();
        if(mediaPlayer != null && mediaPlayer.isPlaying())
            mediaPlayer.pause();
    }

    private long insertar(SQLiteDatabase db, int score, String jugador, int resultado){
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SCORE, score);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_PLAYER, jugador);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_RESULT, resultado);

        // Insert the new row, returning the primary key value of the new row
        return db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
    }//insertar

    private void resultadoPartida(){//llamado cuando se acaba la partida


        mediaPlayer.release();
        mediaPlayer = null;

        /*Como es llamado desde el onClick que se encuentra ya en otro hilo
        * por eso se llama a runOnUiThread para que muestre el toast en el hilo principal*/

        FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(getActivity().getApplicationContext());
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        /*Al almacenar el resultado de la partida en la base de datos, el resultado pueden ser
        * tres posibles valores,0 si se pierde, 1 si se empata y 2 si se gana*/

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getResources().getString(R.string.nombre_jugador));

        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        builder.setPositiveButton("OK",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                jugador = input.getText().toString();

                /*getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {*/
                //modificado para que el resultado de  has hanado se muestre despues de pulsar el boton
                        Toast toast;

                        if(scoreIA == scoreJugador){
                            toast = Toast.makeText(getContext(),getResources().getString(R.string.empate),Toast.LENGTH_LONG);
                            insertar(db,scoreJugador,jugador,1);
                        }else if(scoreIA > scoreJugador){
                            insertar(db,scoreJugador,jugador,0);
                            toast = Toast.makeText(getContext(),getResources().getString(R.string.derrota),Toast.LENGTH_LONG);
                        }else{
                            insertar(db,scoreJugador,jugador,2);
                            toast = Toast.makeText(getContext(),getResources().getString(R.string.victoria),Toast.LENGTH_LONG);
                        }
                        db.close();
                        toast.show();

                    //}
                //});

                startActivity(new Intent(getContext(),MainActivity.class));
                getActivity().finish();
            }
        });//onClickListener

        builder.setView(input);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                builder.show();

            }
        });

    }//resultado partida

    private void generarTablero(int[] idsImagenes){

        //las imagenes de partida sera el numero de columnas * el numero de filas
        int[] imagenesPartida = new int[COLUMNAS*filas];//ids de imagenes de la partida actual

        Log.d("mensaje1","total de casillas:" + imagenesPartida.length + "totalImg" + idsImagenes.length);

        /*El contador se incrementa en dos porque en cada vuelta añade dos imagenes iguales*/
        for(int i = 0; i < imagenesPartida.length;i += 2){
            boolean imagenAgregada = false;

            while (!imagenAgregada){
                //Log.d("mensaje2","entra al while i vale:" + i);
                int posicionImagen =  (int) (Math.random() * (double) idsImagenes.length);
                //Log.d("mensaje5","indice aleatorio vale:" + posicionImagen);
                boolean yaAgregado = false;

                for(int j = 0; j < imagenesPartida.length;j++){//comprueba si imagen ya existe en array de partida
                    Log.d("mensaje3","entra al for anidado vale:" + j);

                    /*compara los ids de todas las imagenes con los que ya han sido agregados
                    * a la partida para que no agregue una imagen ya existente si la posicion generada
                    * de manera aleatoria no es valida*/
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
                    imagenAgregada = true;
                }

            }//while
        }//for

        colocarCasillas(imagenesPartida);
    }//generar info tablero

    private void colocarCasillas(int[] imagenesPartida){

        boolean yaColocado;
        boolean yaExiste;

        //el array contiene la posicion en la que se va a colocar cada imagen
        int[] posicionCasillas = new int[imagenesPartida.length];

        //se rellena con -1 para que no haya problemas con la posicion 0
        Arrays.fill(posicionCasillas,-1);

        for(int i = 0; i < imagenesPartida.length;i++){

            yaColocado = false;

            while(!yaColocado){
                /*genera un numero aleatorio que indica la posicion de la imagen, es decir
                * si la primera posicion de este array tiene un 4 va a agregar en la primera
                * posicion del grid la cuarta imagen y asi con todas.*/

                int posicion = (int) (Math.random() * (double) imagenesPartida.length);
                yaExiste = false;

                for(int k = 0; k < posicionCasillas.length;k++){
                    //comprueba si la posicion generada ya ha sido agregada anteriormente
                    if(posicionCasillas[k] == posicion){
                        yaExiste = true;
                    }
                }

                if(!yaExiste){
                    /*sino ha sido agregada la posicion generada, entonces agrega la imagen de esa
                    * posicion al tablero*/

                    posicionCasillas[i] = posicion;

                    //obtenemos los parametros para poder modificar las imagenes
                    GridLayout.LayoutParams params = new GridLayout.LayoutParams();

                    ImageView imagen = new ImageView(getContext());
                    //se pone una imagen que representa una carta boca abajo
                    imagen.setImageResource(R.drawable.reverso_carta);
                    imagen.setOnClickListener(this);
                    imagen.setLayoutParams(params);
                    imagen.getLayoutParams().height = (int)getResources().getDimension(R.dimen.medidas_casilla);
                    imagen.getLayoutParams().width = (int)getResources().getDimension(R.dimen.medidas_casilla);
                    /*el objeto casilla se agrega como tag a cada imageview del tablero, este
                    * contiene la información relacionada con la imagen que se supone que hay
                    * en esa posicion, asi como en que posicion del grid se encuentra, teniendo
                    * en cuenta que estamos utilizando el bucle for para identifcar cada casilla
                    * la posicion de la fila 2 y columna 4 por ejemplo seria 12*/
                    Casilla casilla = new Casilla(imagenesPartida[posicion],i);
                    imagen.setTag(casilla);

                    /*anadimos un borde negro al imageview*/
                    GradientDrawable gw = new GradientDrawable();
                    gw.setShape(GradientDrawable.RECTANGLE);
                    gw.setStroke(2, Color.BLACK);
                    imagen.setBackgroundDrawable(gw);

                    gridLayout.addView(imagen);

                    yaColocado = true;
                }//if
            }//while
        }//for
    }

    @Override
    public void onClick(final View view) {

        /*Se crea un segundo hilo ya que para mostrar la carta durante unos segundos y volver
        * a mostrar la imagen de reverso se ha utilizado un sleep para detener el hilo antes de
        * seguir ejecutando el resto del codigo, ya que android desaconseja detener el hilo principal*/

            if(!hiloEspera){//si el evento es llamado pero el hilo esta detenido no crea uno nuevo

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            final Casilla casilla = (Casilla) view.getTag();
                            final ImageView imagen;
                            /*se llama al metodo turno que comprueba en que estado
                            * se encuentra el tablero cuando pulsa sobre una imagen del grid
                            * cuando hay que modificar una imagen se llama al metodo post de la view
                            * llamada ya que solo se puede cambiar una imagen desde el hilo principal
                            * */

                            switch (partida.turno(view)){
                                //si se selecciona una carta que ya esta levantada
                                case 0:

                                    break;

                                case 1://primer paso del turno imagen no alzada
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
                                case 2://segundo paso del turno imagenes iguales
                                    imagen = (ImageView) view;
                                    /*Si las imagenes son iguales se muestran para el resto de la
                                    * partida*/

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
                                    /*al finalizar el movimiento del jugador comprueba si la
                                    * partida ha finalizado, y si es asi muestra el resultado,
                                    * sino llama al metodo ia que simula un contrincante, actuando
                                    * como si fuese su turno*/

                                    if(partida.finPartida()){
                                        resultadoPartida();
                                    }

                                    moverIA();

                                    if(partida.finPartida()){
                                        resultadoPartida();
                                    }
                                    break;

                                case 3://segundo paso del turno imagenes diferentes
                                    /*Si las imagenes no son iguales se muestran durante unos segundos
                                    * y se vuelve a colocar la imagen del reverso despues realiza
                                    * las mismas comprobaciones que el paso 2*/
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

        /*El metodo ia devuelve las dos imagenes que ha seleccionado*/
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
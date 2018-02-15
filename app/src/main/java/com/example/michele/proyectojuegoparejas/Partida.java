package com.example.michele.proyectojuegoparejas;

import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import java.util.Arrays;

public class Partida {

    /*Esta clase se ha creado para representar la logica y el siguimiento del estado de la partida*/

    private boolean primerPaso = false;//si el jugador esta en el paso 1 o 2 del turno
    private Casilla casillaPrimeraCarta;
    private ImageView imagenPrimeraCarta;
    private GridLayout gridLayout;
    private Casilla[] memoriaIA;//movimientos que la IA puede recordar
    private int contadorMemoria = 0;//contador cada vez que la ia recuerda un movimiento

    public Casilla getPrimeraCarta() {
        return casillaPrimeraCarta;
    }

    public Partida(GridLayout gridLayout,int dificultad){
        this.gridLayout = gridLayout;
        /*segun el nivel de dificultad la IA recuerda mas movimientos*/
        switch (dificultad){
            case 1:
                memoriaIA = new Casilla[2];
                break;
            case 2:
                memoriaIA = new Casilla[16];
                break;
            case 3:
                memoriaIA = new Casilla[16];
                break;
        }

        Arrays.fill(memoriaIA,null);

    }

    public ImageView getImagenPrimeraCarta() {
        return imagenPrimeraCarta;
    }

    public synchronized int turno(final View view){

        Log.d("mensaje", "turno llamado");

        int estado = 0; // 0 no valido, 1 primer turno 2 2º turno cartas iguales 3 2º turno
        //cartas diferentes

        Casilla casilla = (Casilla) view.getTag();

        boolean casillaExiste = false;

        if(!casilla.isEstaLevantada()){

            for(Casilla casillaMemoria: memoriaIA){
                /*comprueba si la casilla seleccionada este movimiento existe ya en la memoria
                * de la ia*/

                if(casilla.equals(casillaMemoria) && casillaMemoria.getPosicionTablero() == casilla.getPosicionTablero()){
                    casillaExiste = true;
                }
            }

            if(!casillaExiste){
                /*si la casilla no existe en la memoria de la ia la agrega
                * si el contador llega a la ultima posicion lo reinicia y sobreescribe la primera
                * casilla que hubiese agregado para simular que se va olvidando de los primeros
                * movimientos*/

                if(contadorMemoria == memoriaIA.length -1){
                    Log.d("memoria","MEMORIA LLENA");
                    contadorMemoria = 0;
                }
                Log.d("mensaje","agregado casilla a memoria");
                Log.d("mensaje","id imagen añadida a memoria" + casilla.getIdImagen());
                memoriaIA[contadorMemoria] = casilla;
                contadorMemoria++;
            }

        }

        Log.d("mensaje","casilla  vale" + casilla.getIdImagen());
        if(!primerPaso){//si el primer paso todavia no se ha realizado
            Log.d("mensaje","primer movimiento");
            estado = 0;
            if(!casilla.isEstaLevantada()){
                casillaPrimeraCarta = (Casilla) view.getTag();
                imagenPrimeraCarta = (ImageView) view;
                Log.d("mensaje","casillaPrimeraCarta vale" + casillaPrimeraCarta.getIdImagen());
                estado = 1;
                primerPaso = true;
            }
        }else{
            Log.d("mensaje","segundo movimiento");
            estado = 0;
            Log.d("mensaje", "casilla vale " + casilla.isEstaLevantada());
            if(!casilla.isEstaLevantada()) {
                final ImageView ultimaCarta = (ImageView) view;
                Log.d("mensaje", "carta no levantada " + casilla.isEstaLevantada());
                if (casilla.equals( casillaPrimeraCarta)) {
                    Log.d("mensaje", "cartas iguales encontradas");
                    estado = 2;

                } else {
                    estado = 3;
                    Log.d("mensaje", "cartas diferentes encontradas");

                }
                primerPaso = false;
                Log.d("mensaje", "turno acabado");

                    }//!if casilla is esta levantada
                // !if esta esperando
            }//else !primerPaso
        Log.d("turno","FIN METODO TURNO");
        return estado;
    }//turno

    public View[] ia(){

        View[] casillasElegidas = new View[2];
        Log.d("mensaje","empieza turno ia");
        /*compara cada posicion de la memoria con todas las demas que la suceden, en cada iteracion
        * va descartando las casillas que ya ha comparado en el primer for*/

        for(int i = 0; i < memoriaIA.length -1;i++){
            for(int j = i+1; j < memoriaIA.length;j++){

                if(memoriaIA[i] != null && memoriaIA[j] != null){//si las posiciones no estan vacias
                    //si las dos casillas son iguales y no estan levantadas
                    if(memoriaIA[i].equals(memoriaIA[j]) && !memoriaIA[i].isEstaLevantada() &&
                            !memoriaIA[j].isEstaLevantada()){
                        Log.d("mensaje","imagenes coinciden en memoria");
                        Log.d("mensaje","id imagenes memoria:" + memoriaIA[i].getIdImagen() + " - " +
                                memoriaIA[j].getIdImagen());
                        //devuelve las dos imagenes
                        casillasElegidas[0] = gridLayout.getChildAt(memoriaIA[i].getPosicionTablero());
                        casillasElegidas[1] = gridLayout.getChildAt(memoriaIA[j].getPosicionTablero());
                        return casillasElegidas;
                    }
                }
            }
        }
        //sino encuentra una concordancia en la memoria genera una posicion aleatoria
        View imagen1 = generarPosicion();

        Casilla casImagen1 = (Casilla) imagen1.getTag();
        boolean casillaCoincide = false;
        /*compara si la imagen generada coincide con alguna que tenga en memoria*/
        for(Casilla casilla: memoriaIA){
            /*si las dos imagenes son iguales pero estan en diferentes posiciones de tablero
            * entonces ha encontrado una pareja de imagenes*/
            if(casImagen1.equals(casilla) && casImagen1.getPosicionTablero() != casilla.getPosicionTablero()){
                Log.d("mensaje","una imagen coincide en memoria con otra generada");

                casillasElegidas[0] = gridLayout.getChildAt(casilla.getPosicionTablero());
                casillasElegidas[1] = gridLayout.getChildAt(casImagen1.getPosicionTablero());
                Log.d("mensaje","Su posicion es:" + casilla.getPosicionTablero() + " y " +
                casImagen1.getPosicionTablero());
                casillaCoincide = true;
            }
        }
        /*Si la imagen no coincide con ninguna en memoria entonces genera otra y las devuelve*/
        if(!casillaCoincide){
            Log.d("mensaje","dos imagenes generadas aleatorias");
            casillasElegidas[0] = imagen1;
            casillasElegidas[1] = generarPosicion();
            Log.d("mensaje","Su posicion es:" + ((Casilla) casillasElegidas[1].getTag()).getPosicionTablero() + " y " +
                    casImagen1.getPosicionTablero());
        }
        return casillasElegidas;
    }//ia

    private View generarPosicion(){
        /*genera una posicion aleatoria valida*/
        boolean posicionValida =false;
        int posicion = 0;
        View vistaImagen = null;

        while (!posicionValida){
            posicion = (int) (Math.random() * (double) gridLayout.getChildCount()) ;

            ImageView imagen = (ImageView) gridLayout.getChildAt(posicion);
            if(!((Casilla) imagen.getTag()).isEstaLevantada()){
                posicionValida = true;
                vistaImagen = gridLayout.getChildAt(posicion);
                Log.d("mensaje","imagen generada valida, id:" + ((Casilla) imagen.getTag()).getIdImagen());
            }
        }
        //se pone a true para que si tiene que generar otra imagen no genere la misma
        ((Casilla)  vistaImagen.getTag()).setEstaLevantada(true);
        return vistaImagen;
    }//generar posicion

    public boolean finPartida(){
        /*comprueba si todas las casillas ya estan levantadas, de manera que indica si la partida
        * esta acabada o no*/
        boolean partidaAcabada = true;
        Casilla casilla = null;

        for(int i = 0;  i < gridLayout.getChildCount();i++){
            casilla = (Casilla) gridLayout.getChildAt(i).getTag();
            Log.d("casilla","La casilla " + i + "esta " + casilla.isEstaLevantada());
            if(partidaAcabada){

                if(!casilla.isEstaLevantada()){
                    partidaAcabada = false;

                }
            }
        }
        Log.d("mensaje","partida acabada: " + partidaAcabada);
        return partidaAcabada;
    }//fin partida
}

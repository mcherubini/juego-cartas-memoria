package com.example.michele.proyectojuegoparejas;

import android.media.Image;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;

import java.util.Arrays;

public class Partida {

    private boolean primerPaso = false;
    private Casilla casillaPrimeraCarta;
    private ImageView imagenPrimeraCarta;
    private GridLayout gridLayout;
    private Casilla[] memoriaIA;
    private int contadorMemoria = 0;

    public Casilla getPrimeraCarta() {
        return casillaPrimeraCarta;
    }

    public void setPrimeraCarta(Casilla primeraCarta) {
        this.casillaPrimeraCarta = primeraCarta;
    }

    public Partida(GridLayout gridLayout,int dificultad){
        this.gridLayout = gridLayout;

        switch (dificultad){
            case 1:
                memoriaIA = new Casilla[2];
                break;
            case 2:
                memoriaIA = new Casilla[8];
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

    public void setImagenPrimeraCarta(ImageView imagenPrimeraCarta) {
        this.imagenPrimeraCarta = imagenPrimeraCarta;
    }

    public synchronized int turno(final View view){

        Log.d("mensaje", "turno llamado");

        int estado = 0; // 0 no valido, 1 primer turno 2 2º turno cartas iguales 3 2º turno
        //cartas diferentes

        Casilla casilla = (Casilla) view.getTag();

        boolean casillaExiste = false;

        if(!casilla.isEstaLevantada()){

            for(Casilla casillaMemoria: memoriaIA){
                if(casilla.equals(casillaMemoria) && casillaMemoria.getPosicionTablero() == casilla.getPosicionTablero()){
                    casillaExiste = true;
                }
            }

            if(!casillaExiste){

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
        if(!primerPaso){
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

        for(int i = 0; i < memoriaIA.length -1;i++){
            for(int j = i+1; j < memoriaIA.length;j++){

                if(memoriaIA[i] != null && memoriaIA[j] != null){
                    if(memoriaIA[i].equals(memoriaIA[j]) && !memoriaIA[i].isEstaLevantada() &&
                            !memoriaIA[j].isEstaLevantada()){
                        Log.d("mensaje","imagenes coinciden en memoria");
                        Log.d("mensaje","id imagenes memoria:" + memoriaIA[i].getIdImagen() + " - " +
                                memoriaIA[j].getIdImagen());
                        casillasElegidas[0] = gridLayout.getChildAt(memoriaIA[i].getPosicionTablero());
                        casillasElegidas[1] = gridLayout.getChildAt(memoriaIA[j].getPosicionTablero());
                        return casillasElegidas;
                    }
                }
            }
        }

        View imagen1 = generarPosicion();
        //comprobar si casilla de imagen1 esta en la memoria de la IA pero en diferente posicion
        //si es asi devolver las dos imagenes
        Casilla casImagen1 = (Casilla) imagen1.getTag();
        boolean casillaCoincide = false;
        for(Casilla casilla: memoriaIA){
            if(casImagen1.equals(casilla) && casImagen1.getPosicionTablero() != casilla.getPosicionTablero()){
                Log.d("mensaje","una imagen coincide en memoria con otra generada");

                casillasElegidas[0] = gridLayout.getChildAt(casilla.getPosicionTablero());
                casillasElegidas[1] = gridLayout.getChildAt(casImagen1.getPosicionTablero());
                casillaCoincide = true;
            }
        }

        if(!casillaCoincide){
            Log.d("mensaje","dos imagenes generadas aleatorias");
            casillasElegidas[0] = imagen1;
            casillasElegidas[1] = generarPosicion();
        }
        return casillasElegidas;
    }//ia

    private View generarPosicion(){

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
        ((Casilla)  vistaImagen.getTag()).setEstaLevantada(true);
        return vistaImagen;
    }//generar posicion

    public boolean finPartida(){

        boolean partidaAcabada = true;
        Casilla casilla = null;

        for(int i = 0;  i < gridLayout.getChildCount();i++){
            casilla = (Casilla) gridLayout.getChildAt(i).getTag();

            if(partidaAcabada){
                if(!casilla.isEstaLevantada()){
                    partidaAcabada = false;
                }
            }
        }

        return partidaAcabada;
    }//fin partida
}

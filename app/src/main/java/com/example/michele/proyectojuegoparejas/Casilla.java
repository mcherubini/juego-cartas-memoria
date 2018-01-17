package com.example.michele.proyectojuegoparejas;

/**
 * Created by Michele on 05/01/2018.
 */

public class Casilla {

    public int getIdImagen() {
        return idImagen;
    }

    public void setIdImagen(int idImagen) {
        this.idImagen = idImagen;
    }

    public boolean isEstaLevantada() {
        return estaLevantada;
    }

    public void setEstaLevantada(boolean estaLevantada) {
        this.estaLevantada = estaLevantada;
    }

    public int getPosicionTablero() {
        return posicionTablero;
    }

    public void setPosicionTablero(int posicionTablero) {
        this.posicionTablero = posicionTablero;
    }
    //prueba cambio github

    private int idImagen;
    private boolean estaLevantada;
    private int posicionTablero;

    public Casilla(int idImagen,int posicionTablero){
        this.idImagen = idImagen;
        this.posicionTablero = posicionTablero;
        estaLevantada = false;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Casilla casilla = (Casilla) o;

        return idImagen == casilla.idImagen;

    }

    @Override
    public int hashCode() {
        return idImagen;
    }
}

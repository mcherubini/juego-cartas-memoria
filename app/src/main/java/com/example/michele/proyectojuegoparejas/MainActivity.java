package com.example.michele.proyectojuegoparejas;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements FragmentMenu.ListenerMenuPartida {

    private boolean multiPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(findViewById(R.id.contenedor_fragmentos_main) != null){
            multiPanel = true;

            FragmentPuntuaciones fragment = new FragmentPuntuaciones();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contenedor_fragmentos_main, fragment).commit();

        }else{
            multiPanel = false;
        }
    }


    @Override
    public void cargarDificultad() {
        if(multiPanel){

            FragmentMenuDificultad fragment = new FragmentMenuDificultad();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contenedor_fragmentos_main, fragment).commit();
        }else{
            Intent i = new Intent(getApplicationContext(),ActivityDificultad.class);
            startActivity(i);
        }
    }

    @Override
    public void cargarOpciones() {
        if(multiPanel){

            FragmentOpciones fragment = new FragmentOpciones();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contenedor_fragmentos_main, fragment).commit();
        }else{
            Intent i = new Intent(getApplicationContext(),ActivityOpciones.class);
            startActivity(i);
        }
    }

    @Override
    public void cargarPuntuaciones() {
        if(multiPanel){

            FragmentPuntuaciones fragment = new FragmentPuntuaciones();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contenedor_fragmentos_main, fragment).commit();
        }else{
            Intent i = new Intent(getApplicationContext(),ActivityPuntuaciones.class);
            startActivity(i);
        }
    }
}

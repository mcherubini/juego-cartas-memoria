package com.example.michele.proyectojuegoparejas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMenu extends Fragment{

    private View view;

    public FragmentMenu() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_menu, container, false);
        Button btnJugar = (Button) view.findViewById(R.id.btn_jugar);

        btnJugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarDificultad();
            }
        });

        return view;
    }


    public void cargarDificultad(){

        Intent i = new Intent(getContext(),ActivityDificultad.class);
        startActivity(i);

        /*FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment fragment = fragmentManager.findFragmentById(R.id.menu_principal);

        fragmentTransaction.replace(R.id.prueba,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();*/

    }

}

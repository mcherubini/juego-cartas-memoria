package com.example.michele.proyectojuegoparejas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


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
        ImageButton btnJugar = (ImageButton) view.findViewById(R.id.imagen_play);
        ImageButton btnExit = (ImageButton) view.findViewById(R.id.imagen_exit);
        ImageButton btnOpciones = (ImageButton) view.findViewById(R.id.imagen_opciones);
        ImageButton btnScore = (ImageButton) view.findViewById(R.id.imagen_trofeo);

        btnJugar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                cargarDificultad();
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });


        return view;
    }


    public void cargarDificultad(){

        Intent i = new Intent(getContext(),ActivityDificultad.class);
        startActivity(i);
        getActivity().finish();


    }

}

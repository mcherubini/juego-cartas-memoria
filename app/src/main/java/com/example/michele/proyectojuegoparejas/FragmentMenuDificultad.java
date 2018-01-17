package com.example.michele.proyectojuegoparejas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMenuDificultad extends Fragment implements View.OnClickListener{

    public FragmentMenuDificultad() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu_dificultad, container, false);

        View[] botones = {view.findViewById(R.id.btn_facil),view.findViewById(R.id.btn_normal),
                view.findViewById(R.id.btn_dificil)};

        for(int i = 0; i < botones.length;i++){
            botones[i].setOnClickListener(this);
            Log.d("mensaje","añadido listener");
        }

        return view;
    }

    public void cargarPartida(View v){

        Intent i = new Intent(getContext(),ActivityPartida.class);
        Log.d("mensaje","metodo llamado");

        if(v.equals(getActivity().findViewById(R.id.btn_facil))){
            Log.d("mensaje","pulsado facil");
            i.putExtra("dificultad",1);
            startActivity(i);
        }else if(v.equals(getActivity().findViewById(R.id.btn_normal))){
            Log.d("mensaje","pulsado normal");
            i.putExtra("dificultad",2);
            startActivity(i);
        }else if(v.equals(getActivity().findViewById(R.id.btn_dificil))){
            Log.d("mensaje","pulsado dificil");
            i.putExtra("dificultad",3);
            startActivity(i);
        }
    }

    @Override
    public void onClick(View view) {
        cargarPartida(view);
    }
}

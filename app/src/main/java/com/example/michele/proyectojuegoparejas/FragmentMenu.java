package com.example.michele.proyectojuegoparejas;

import android.app.Activity;
import android.content.Context;
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
    private ListenerMenuPartida callBack;

    public FragmentMenu() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        callBack = (ListenerMenuPartida) context;
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
                callBack.cargarDificultad();
            }
        });
        btnOpciones.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                callBack.cargarOpciones();
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                getActivity().finish();
                getActivity().moveTaskToBack(true);
            }
        });

        //en vista para tablets se ha quitado el boton, por eso se realiza comprobacion
        if(btnScore !=null)
            btnScore.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                cargarPuntuaciones();
            }
        });


        return view;
    }

    public void cargarPuntuaciones(){
        Intent i = new Intent(getContext(),ActivityPuntuaciones.class);
        startActivity(i);
    }


    public interface ListenerMenuPartida{
        public void cargarDificultad();
        public void cargarOpciones();
    }
}

package com.example.michele.proyectojuegoparejas;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;


public class FragmentOpciones extends Fragment {

    private SharedPreferences sharedPref;

    public FragmentOpciones() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_opciones, container, false);

        Switch switchMusica = (Switch) view.findViewById(R.id.activarMusica);

        sharedPref = getActivity().getSharedPreferences(getString(R.string.fichero_opciones),
                Context.MODE_PRIVATE);

        switchMusica.setChecked(sharedPref.getBoolean(getString(R.string.is_musica_activa), true));

        switchMusica.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                SharedPreferences.Editor editor = sharedPref.edit();

                if (isChecked) {

                    editor.putBoolean(getString(R.string.is_musica_activa),true);
                    editor.commit();
                } else {
                    editor.putBoolean(getString(R.string.is_musica_activa),false);
                    editor.commit();
                }
            }
        });
        return view;
    }

}

package com.appreman.app.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appreman.app.Adapter.PreguntaAdapter;
import com.appreman.app.Database.DBHelper;
import com.appreman.app.Models.Pregunta;
import com.appreman.app.Repository.AppPreferences;
import com.appreman.appreman.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ElementosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ElementosFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String elementoNombre;

    public ElementosFragment() {
    }

    public static ElementosFragment newInstance(String param1, String nombreElemento) {
        ElementosFragment fragment = new ElementosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, nombreElemento);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            elementoNombre = getArguments().getString(ARG_PARAM2, "");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_elementos, container, false);

        RecyclerView recycler = fragment.findViewById(R.id.recyclerview);

        String numeroElemento = String.valueOf(mParam1);

        DBHelper v_db_helper = new DBHelper(requireActivity().getApplicationContext());

        List<Pregunta> preguntas = v_db_helper.getPreguntasElemento(numeroElemento);

        RecyclerView.LayoutManager lManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(lManager);

        // Obtener valores de SharedPreferences
        AppPreferences appPreferences = new AppPreferences(requireActivity().getApplicationContext());
        String nombreEmpresa = appPreferences.getNombreEmpresa();
        String nombreEncuestado = appPreferences.getNombreEncuestado();
        String cargoEncuestado = appPreferences.getCargoEncuestado();

        PreguntaAdapter adapter = new PreguntaAdapter(getContext(), preguntas, nombreEmpresa, nombreEncuestado, cargoEncuestado);
        recycler.setAdapter(adapter);

        getActivity().setTitle(elementoNombre);

        return fragment;
    }
}
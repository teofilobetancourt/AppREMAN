package com.appreman.app.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.appreman.app.Activity.EmpresasEncuestadasActivity;
import com.appreman.app.Activity.NuevaEmpresaActivity;
import com.appreman.app.Database.DBHelper;
import com.appreman.appreman.R;

public class SurveyFragment extends Fragment {

    private DBHelper dbHelper;

    public SurveyFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_survey, container, false);

        dbHelper = new DBHelper(getActivity());

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button btnNuevaEmpresa = view.findViewById(R.id.btnNuevaEmpresa);
        btnNuevaEmpresa.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), NuevaEmpresaActivity.class);
            startActivity(intent);
        });

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button btnListaEmpresa = view.findViewById(R.id.btnListaEmpresas);
        btnListaEmpresa.setOnClickListener(v -> listaEmpresasOnClick());

        return view;
    }

    private void listaEmpresasOnClick() {
        Intent intent = new Intent(getActivity(), EmpresasEncuestadasActivity.class);
        startActivity(intent);
    }
}

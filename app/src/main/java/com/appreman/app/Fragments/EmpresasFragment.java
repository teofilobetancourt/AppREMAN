package com.appreman.app.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.appreman.app.Activity.NuevaEmpresaActivity;
import com.appreman.app.Database.DBHelper;
import com.appreman.appreman.R;

public class EmpresasFragment extends Fragment {

    private DBHelper dbHelper;

    public EmpresasFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_empresas, container, false);

        dbHelper = new DBHelper(getActivity());

        Button btnNuevaEmpresa = view.findViewById(R.id.btnNuevaEmpresa);
        btnNuevaEmpresa.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), NuevaEmpresaActivity.class);
            startActivity(intent);
        });


        return view;
    }
}

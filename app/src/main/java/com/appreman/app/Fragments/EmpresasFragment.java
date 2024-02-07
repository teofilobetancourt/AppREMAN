package com.appreman.app.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appreman.app.Activity.NuevaEmpresaActivity;
import com.appreman.app.Adapter.EmpresaAdapter;
import com.appreman.app.Database.DBHelper;
import com.appreman.app.Models.Empresa;
import com.appreman.appreman.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class EmpresasFragment extends Fragment {

    private RecyclerView recyclerViewEmpresas;
    private DBHelper dbHelper;

    public EmpresasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_empresas, container, false);

        recyclerViewEmpresas = view.findViewById(R.id.recyclerViewEmpresas);
        dbHelper = new DBHelper(getActivity());

        FloatingActionButton btnNuevaEmpresa = view.findViewById(R.id.fabNuevaEmpresa);
        btnNuevaEmpresa.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), NuevaEmpresaActivity.class);
            startActivity(intent);
        });

        setupRecyclerView();

        return view;
    }

    private void setupRecyclerView() {
        List<Empresa> empresas = dbHelper.getAllEmpresas(); // Obtener la lista de empresas de la base de datos
        EmpresaAdapter adapter = new EmpresaAdapter(empresas);
        recyclerViewEmpresas.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewEmpresas.setAdapter(adapter);
    }
}

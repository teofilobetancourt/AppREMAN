package com.appreman.app.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appreman.app.Activity.EncuestasActivity;
import com.appreman.app.Adapter.EmpresaAdapter;
import com.appreman.app.Database.DBHelper;
import com.appreman.app.Models.Empresa;
import com.appreman.app.Repository.AppPreferences;
import com.appreman.appreman.R;

import java.util.List;

public class SurveyFragment extends Fragment {

    private RecyclerView recyclerViewEmpresas;
    private DBHelper dbHelper;
    private AppPreferences appPreferences;

    public SurveyFragment() {
        // Constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_survey, container, false);

        recyclerViewEmpresas = rootView.findViewById(R.id.recyclerViewEmpresas);
        dbHelper = new DBHelper(requireActivity());
        appPreferences = new AppPreferences(requireActivity());

        setupRecyclerView();

        return rootView;
    }

    private void setupRecyclerView() {
        List<Empresa> empresas = dbHelper.getAllEmpresas();

        for (Empresa empresa : empresas) {
            Log.d("SurveyFragment", "Empresa: " + empresa.getNombre() + ", ID: " + empresa.getId());
        }

        EmpresaAdapter adapter = new EmpresaAdapter(empresas);
        recyclerViewEmpresas.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerViewEmpresas.setAdapter(adapter);

        adapter.setEncuestaClickListener(position -> {
            if (position != RecyclerView.NO_POSITION) {
                Empresa empresa = empresas.get(position);
                String nombreEmpresaSeleccionada = empresa.getNombre();
                Log.d("SurveyFragment", "Nombre de empresa seleccionada: " + nombreEmpresaSeleccionada);

                // Guarda el nombre de la empresa en las preferencias
                appPreferences.setNombreEmpresa(nombreEmpresaSeleccionada);

                // Inicia EncuestasActivity con el nombre de la empresa
                EncuestasActivity.start(requireActivity());
            } else {
                Log.d("SurveyFragment", "Posición no válida seleccionada en RecyclerView");
            }
        });
    }
}

package com.appreman.app.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appreman.app.Activity.EncuestasActivity;
import com.appreman.app.Adapter.EmpresaAdapter;
import com.appreman.app.Adapter.EmpresaAdapter.EncuestaClickListener;
import com.appreman.app.Database.DBHelper;
import com.appreman.app.Models.Empresa;
import com.appreman.appreman.R;

import java.util.List;

public class SurveyFragment extends Fragment {

    private RecyclerView recyclerViewEmpresas;
    private DBHelper dbHelper;

    public SurveyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_survey, container, false);

        recyclerViewEmpresas = rootView.findViewById(R.id.recyclerViewEmpresas);
        dbHelper = new DBHelper(requireActivity());

        setupRecyclerView();

        return rootView;
    }

    private void setupRecyclerView() {
        List<Empresa> empresas = dbHelper.getAllEmpresas();

        EmpresaAdapter adapter = new EmpresaAdapter(empresas);
        recyclerViewEmpresas.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerViewEmpresas.setAdapter(adapter);

        // Configura el clic para abrir EncuestasActivity al hacer clic en un elemento del RecyclerView
        adapter.setEncuestaClickListener(new EncuestaClickListener() {
            @Override
            public void onEncuestaClick(int position) {
                // Obtiene la empresa seleccionada en la posición 'position'
                Empresa empresa = empresas.get(position);

                // Ahora puedes obtener el ID de la empresa
                int idEmpresaSeleccionada = empresa.getId();

                // Abre EncuestasActivity y pasa el ID de la empresa
                Intent intent = new Intent(requireContext(), EncuestasActivity.class);
                intent.putExtra("empresa_id", idEmpresaSeleccionada);
                startActivity(intent);
            }
        });
    }
}

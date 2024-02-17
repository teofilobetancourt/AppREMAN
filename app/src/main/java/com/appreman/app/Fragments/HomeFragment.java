package com.appreman.app.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.appreman.app.Database.DBHelper;
import com.appreman.app.ViewModel.PieChartView;
import com.appreman.appreman.R;

import java.util.List;

public class HomeFragment extends Fragment {

    private PieChartView pieChartView;
    private Spinner spinner;
    private DBHelper dbHelper;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ImageView imageView = view.findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.eman2);

        pieChartView = view.findViewById(R.id.pie_chart_view);
        dbHelper = new DBHelper(getActivity());

        // Busca el Spinner en el layout
        spinner = view.findViewById(R.id.spinner_empresas);

        // Configura el adaptador del Spinner y el listener
        List<String> empresaNames = dbHelper.getAllEmpresaNames();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, empresaNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        // Configura el listener del Spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Maneja el evento de selección del Spinner
                String selectedEmpresa = (String) parentView.getItemAtPosition(position);
                // Actualiza la descripción en PieChartView
                pieChartView.setSelectedEmpresa(selectedEmpresa);
                // Invalida la vista para forzar el redibujado
                pieChartView.invalidate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Maneja el caso donde no hay nada seleccionado
            }
        });

        return view;
    }
}

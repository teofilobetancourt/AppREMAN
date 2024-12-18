package com.appreman.app.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appreman.app.Activity.EmpresasEncuestadasActivity;
import com.appreman.app.Activity.MainActivity;
import com.appreman.app.Activity.NuevaEmpresaActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.appreman.app.Database.DBHelper;
import com.appreman.appreman.R;
import com.google.android.material.card.MaterialCardView;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SurveyFragment extends Fragment {

    private BarChart barChart1, barChart2, barChart3, barChart4, barChart5, barChart6;
    private DBHelper dbHelper;
    private String email;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_survey, container, false);

        // Obtener el email del Bundle
        if (getArguments() != null) {
            email = getArguments().getString("email");
        }

        barChart1 = view.findViewById(R.id.barChart1);
        barChart2 = view.findViewById(R.id.barChart2);
        barChart3 = view.findViewById(R.id.barChart3);
        barChart4 = view.findViewById(R.id.barChart4);
        barChart5 = view.findViewById(R.id.barChart5);
        barChart6 = view.findViewById(R.id.barChart6);
        dbHelper = new DBHelper(getContext());

        setupBarCharts();

        // Encuentra los CardView y configura los OnClickListener
        MaterialCardView cardNuevaEmpresa = view.findViewById(R.id.cardNuevaEmpresa);
        cardNuevaEmpresa.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), NuevaEmpresaActivity.class);
            intent.putExtra("email", email); // Pasar el email a NuevaEmpresaActivity
            startActivityForResult(intent, 1); // Usar un requestCode, por ejemplo, 1
        });

        MaterialCardView cardListaEmpresas = view.findViewById(R.id.cardListaEmpresas);
        cardListaEmpresas.setOnClickListener(v -> listaEmpresasOnClick());

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == getActivity().RESULT_OK) {
            // Redirigir al EmpresasFragment
            if (getActivity() instanceof MainActivity) {
                MainActivity activity = (MainActivity) getActivity();
                activity.displayFragment(new EmpresasFragment(), true, new Bundle());
            }
        }
    }

    private void setupBarCharts() {
        setupBarChart1();
        setupBarChart2();
        setupBarChart3();
        setupBarChart4();
        setupBarChart5();
        setupBarChart6();
    }

    private void setupBarChart1() {
        int totalEmpresas = dbHelper.getEmpresasCount();
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, totalEmpresas));

        BarDataSet dataSet = new BarDataSet(entries, "Total Empresas");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData data = new BarData(dataSet);

        barChart1.setData(data);
        barChart1.invalidate(); // refresh
    }

    private void setupBarChart2() {
        int empresasEncuestadas = dbHelper.getRespuestasEmpresasCount();
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, empresasEncuestadas));

        BarDataSet dataSet = new BarDataSet(entries, "Empresas Encuestadas");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData data = new BarData(dataSet);

        barChart2.setData(data);
        barChart2.invalidate(); // refresh
    }

    private void setupBarChart3() {
        int empresasCompletamenteEncuestadas = dbHelper.getEmpresasCompletamenteEncuestadasCount();
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, empresasCompletamenteEncuestadas));

        BarDataSet dataSet = new BarDataSet(entries, "Empresas Completamente Encuestadas");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData data = new BarData(dataSet);

        barChart3.setData(data);
        barChart3.invalidate(); // refresh
    }

    private void setupBarChart4() {
        int totalEmpresas = dbHelper.getEmpresasCount();
        int empresasEncuestadas = dbHelper.getRespuestasEmpresasCount();
        int empresasPorEncuestar = totalEmpresas - empresasEncuestadas;
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, empresasPorEncuestar));

        BarDataSet dataSet = new BarDataSet(entries, "Empresas por Encuestar");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData data = new BarData(dataSet);

        barChart4.setData(data);
        barChart4.invalidate(); // refresh
    }

    private void setupBarChart5() {
        Map<String, Long> tiempoPorDia = dbHelper.getTiempoPorDia();
        List<BarEntry> entries = new ArrayList<>();
        String[] dias = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};

        for (int i = 0; i < dias.length; i++) {
            long tiempo = tiempoPorDia.containsKey(dias[i]) ? tiempoPorDia.get(dias[i]) : 0L;
            entries.add(new BarEntry(i, tiempo));
            Log.d("BarChart5", "Día: " + dias[i] + ", Tiempo: " + tiempo); // Log para verificar los datos
        }

        BarDataSet dataSet = new BarDataSet(entries, "Tiempo dedicado por día");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData data = new BarData(dataSet);

        barChart5.setData(data);
        barChart5.invalidate(); // refresh
        Log.d("BarChart5", "Datos enviados al BarChart: " + entries.toString()); // Log para verificar los datos enviados
    }

    private void setupBarChart6() {
        int totalPreguntas = dbHelper.getTotalPreguntasCount();
        Map<String, Long> tiempoPorDia = dbHelper.getTiempoPorDia();
        long totalMinutos = 0;
        for (Long horas : tiempoPorDia.values()) {
            totalMinutos += horas * 60; // Convertir horas a minutos
        }
        double preguntasPorMinuto = totalMinutos > 0 ? (double) totalPreguntas / totalMinutos : 0;

        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, (float) preguntasPorMinuto));

        BarDataSet dataSet = new BarDataSet(entries, "Promedio de Preguntas por Minuto");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData data = new BarData(dataSet);

        barChart6.setData(data);
        barChart6.invalidate(); // refresh
    }

    private void listaEmpresasOnClick() {
        Intent intent = new Intent(getActivity(), EmpresasEncuestadasActivity.class);
        startActivity(intent);
    }
}
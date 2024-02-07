package com.appreman.app.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.appreman.appreman.R;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Encuentra la referencia al PieChartView en tu layout

        // Configura los datos para la gráfica de torta
        // Asume que la clase PieChartView ya maneja la lógica de la gráfica

        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}

package com.appreman.app.Fragments;

<<<<<<< HEAD
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
=======
import android.os.Bundle;
import android.view.LayoutInflater;
>>>>>>> a21008206cf1f372d46ed21e6732f650f9060c30
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
<<<<<<< HEAD
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.appreman.app.Activity.EncuestasActivity;
import com.appreman.app.Database.DBHelper;
import com.appreman.app.Repository.AppPreferences;
import com.appreman.app.Repository.WebSocketManager;
import com.appreman.appreman.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.card.MaterialCardView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment implements WebSocketManager.NotificationListener {

    private static final int REQUEST_WRITE_STORAGE = 112;
    private Spinner spinner;
    private DBHelper dbHelper;
    private AppPreferences appPreferences;
    private MaterialCardView cardActual;
    private MaterialCardView cardPotencial;
    private MaterialCardView cardAmbas;
    private MaterialCardView cardContinuar;
    private TextView textBalanceTotal;
    private BarChart barChart;
    private ImageView menuIcon;
    private ImageView notifi;
    private TextView notificationText;

    private WebSocketManager webSocketManager;

    private static final int TOTAL_PREGUNTAS = 377;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        dbHelper = new DBHelper(requireActivity());
        appPreferences = new AppPreferences(requireActivity());

        spinner = view.findViewById(R.id.spinner_empresas);
        List<String> empresaNames = dbHelper.getAllEmpresaNames();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireActivity(), R.layout.spinner_dropdown_item, empresaNames);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        cardActual = view.findViewById(R.id.card_actual);
        cardPotencial = view.findViewById(R.id.card_potencial);
        cardAmbas = view.findViewById(R.id.card_ambas);
        cardContinuar = view.findViewById(R.id.card_continuar);

        textBalanceTotal = view.findViewById(R.id.text_balance_total);
        barChart = view.findViewById(R.id.chart1);
        menuIcon = view.findViewById(R.id.menu_icon);
        notificationText = view.findViewById(R.id.notification_text);
        notifi = view.findViewById(R.id.notification_icon); // Asumiendo que has agregado el ImageView notifi en el layout XML

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedEmpresa = (String) parentView.getItemAtPosition(position);

                // Guarda el nombre de la empresa seleccionada en SharedPreferences
                appPreferences.setNombreEmpresa(selectedEmpresa);

                updateFinancialData(selectedEmpresa);
=======
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.appreman.app.Database.DBHelper;
import com.appreman.app.ViewModel.PieChartView;
import com.appreman.appreman.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
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
        dbHelper = new DBHelper(requireActivity());

        // Busca el Spinner en el layout
        spinner = view.findViewById(R.id.spinner_empresas);

        // Configura el adaptador del Spinner y el listener
        List<String> empresaNames = dbHelper.getAllEmpresaNames();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, empresaNames);
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
>>>>>>> a21008206cf1f372d46ed21e6732f650f9060c30
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Maneja el caso donde no hay nada seleccionado
            }
        });

<<<<<<< HEAD
        // Obtiene el nombre de la empresa guardada y actualiza la UI en consecuencia
        String lastSelectedEmpresa = appPreferences.getNombreEmpresa();
        if (lastSelectedEmpresa != null && !lastSelectedEmpresa.isEmpty()) {
            int spinnerPosition = spinnerAdapter.getPosition(lastSelectedEmpresa);
            spinner.setSelection(spinnerPosition);
        }

        cardContinuar.setOnClickListener(v -> iniciarEncuestasActivity());

        menuIcon.setOnClickListener(v -> showPopupMenu(v));

        // Initialize WebSocketManager with the NotificationListener implementation
        webSocketManager = new WebSocketManager(this); // Pass 'this' as NotificationListener
        webSocketManager.start();

        // Send a notification to all devices when the fragment is created
        webSocketManager.sendNotification("Nueva notificación para todos los dispositivos");

        return view;
    }

    @Override
    public void onNotificationReceived(int count) {
        if (notificationText != null) {
            notificationText.setText(String.valueOf(count));
            notificationText.setVisibility(View.VISIBLE); // Asegúrate de que el texto es visible
        }

        if (notifi != null) {
            notifi.setImageResource(R.drawable.notifi); // Asegúrate de que este recurso drawable es el correcto
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        // Actualiza los datos de la empresa seleccionada
        String selectedEmpresa = appPreferences.getNombreEmpresa();
        if (selectedEmpresa != null && !selectedEmpresa.isEmpty()) {
            updateFinancialData(selectedEmpresa);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (webSocketManager != null) {
            webSocketManager.close();
        }
    }

    private void updateFinancialData(String empresa) {
        Map<String, Double> resultados = dbHelper.obtenerContadoresYPorcentajes(empresa);

        // Obtener el número de respuestas respondidas para la empresa
        int respuestasCount = dbHelper.getRespuestasCount(empresa);

        // Obtener el total de preguntas (puedes reemplazar este valor con un método si lo tienes)
        int totalPreguntas = TOTAL_PREGUNTAS;

        double contadorActual = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            contadorActual = resultados.getOrDefault("Actual", 0.0);
        }
        double contadorPotencial = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            contadorPotencial = resultados.getOrDefault("Potencial", 0.0);
        }
        double contadorAmbas = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            contadorAmbas = resultados.getOrDefault("Ambas", 0.0);
        }

        // Agregar logs para verificar los valores obtenidos
        Log.d("HomeFragment", "contadorActual: " + contadorActual);
        Log.d("HomeFragment", "contadorPotencial: " + contadorPotencial);
        Log.d("HomeFragment", "contadorAmbas: " + contadorAmbas);

        double porcentajeActual = (contadorActual / totalPreguntas) * 100;
        double porcentajePotencial = (contadorPotencial / totalPreguntas) * 100;
        double porcentajeAmbas = (contadorAmbas / totalPreguntas) * 100;

        // Agregar logs para verificar los porcentajes calculados
        Log.d("HomeFragment", "porcentajeActual: " + porcentajeActual);
        Log.d("HomeFragment", "porcentajePotencial: " + porcentajePotencial);
        Log.d("HomeFragment", "porcentajeAmbas: " + porcentajeAmbas);

        TextView textActual = cardActual.findViewById(R.id.text_actual);
        TextView textPotencial = cardPotencial.findViewById(R.id.text_potencial);
        TextView textAmbas = cardAmbas.findViewById(R.id.text_ambas);

        textActual.setText(String.format("S/ %.2f (%.2f%%)", contadorActual, porcentajeActual));
        textPotencial.setText(String.format("S/ %.2f (%.2f%%)", contadorPotencial, porcentajePotencial));
        textAmbas.setText(String.format("S/ %.2f (%.2f%%)", contadorAmbas, porcentajeAmbas));

        double total = contadorActual + contadorPotencial + contadorAmbas;
        double totalPorcentaje = (total / totalPreguntas) * 100;

        // Agregar logs para verificar el total y el porcentaje total
        Log.d("HomeFragment", "total: " + total);
        Log.d("HomeFragment", "totalPorcentaje: " + totalPorcentaje);

        textBalanceTotal.setText(String.format("S/ %.2f (%.2f%%)", total, totalPorcentaje));

        // Actualiza el TextView con la cantidad de preguntas respondidas y el total
        TextView textViewPreguntas = getView().findViewById(R.id.text_view_preguntas);
        textViewPreguntas.setText(String.format("%d de %d", respuestasCount, totalPreguntas));

        setupChart(barChart, contadorActual, contadorPotencial, contadorAmbas);
    }

    private void setupChart(BarChart chart, double actual, double potencial, double ambas) {
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, (float) actual));
        entries.add(new BarEntry(1f, (float) potencial));
        entries.add(new BarEntry(2f, (float) ambas));

        BarDataSet dataSet = new BarDataSet(entries, "Distribución");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        BarData barData = new BarData(dataSet);
        chart.setData(barData);

        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(new String[]{"Actual", "Potencial", "Ambas"}));
        xAxis.setGranularity(1f);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setGranularity(1f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum((float) TOTAL_PREGUNTAS);

        chart.getAxisRight().setEnabled(false);
        chart.getDescription().setEnabled(false);

        // Configuración para 3D
        chart.setDrawBarShadow(true);
        chart.setDrawValueAboveBar(true);
        chart.setMaxVisibleValueCount(60);
        chart.setPinchZoom(false);
        chart.setDrawGridBackground(false);

        chart.invalidate();
    }

    private void iniciarEncuestasActivity() {
        String selectedEmpresa = (String) spinner.getSelectedItem();

        if (selectedEmpresa != null && !selectedEmpresa.isEmpty()) {
            Intent intent = new Intent(getActivity(), EncuestasActivity.class);
            intent.putExtra("empresa_nombre", selectedEmpresa);
            startActivity(intent);
        }
    }

    private void sendEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "teoedmundo@gmail.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Asunto del correo");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Contenido del correo");

        if (emailIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(Intent.createChooser(emailIntent, "Enviar correo..."));
        }
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(requireActivity(), view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_options, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_download) {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
                } else {
                    String selectedEmpresa = appPreferences.getNombreEmpresa();
                    if (selectedEmpresa != null && !selectedEmpresa.isEmpty()) {
                        File file = dbHelper.guardarRespuestasEnArchivo(selectedEmpresa, requireContext());
                        dbHelper.descargarArchivo(file, requireContext());
                    } else {
                        Toast.makeText(requireContext(), "No se ha seleccionado ninguna empresa", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            } else if (itemId == R.id.menu_send_email) {
                sendEmail();
                return true;
            } else {
                return false;
            }
        });
        popupMenu.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                String selectedEmpresa = appPreferences.getNombreEmpresa();
                if (selectedEmpresa != null && !selectedEmpresa.isEmpty()) {
                    File file = dbHelper.guardarRespuestasEnArchivo(selectedEmpresa, requireContext());
                    dbHelper.descargarArchivo(file, requireContext());
                } else {
                    Toast.makeText(requireContext(), "No se ha seleccionado ninguna empresa", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(requireContext(), "Permiso denegado para escribir en el almacenamiento externo", Toast.LENGTH_SHORT).show();
            }
        }
    }
=======
        // Busca el FloatingActionButton (botón flotante) en el layout
        FloatingActionButton fabDescargar = view.findViewById(R.id.fabDescargar);

        // Configura el listener del FloatingActionButton
        fabDescargar.setOnClickListener(v -> {
            // Llama al método para guardar en el archivo y obtener el archivo
            String selectedEmpresa = (String) spinner.getSelectedItem();
            File archivoGuardado = dbHelper.guardarRespuestasEnArchivo(selectedEmpresa, requireContext());
            // Llama al método para descargar el archivo
            dbHelper.descargarArchivo(archivoGuardado, requireContext());
        });

        return view;
    }
>>>>>>> a21008206cf1f372d46ed21e6732f650f9060c30
}

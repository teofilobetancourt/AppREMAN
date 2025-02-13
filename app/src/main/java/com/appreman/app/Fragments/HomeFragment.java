package com.appreman.app.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.appreman.app.Activity.EncuestasActivity;
import com.appreman.app.Database.DBHelper;
import com.appreman.app.Email.MailSender;
import com.appreman.app.Email.PendingEmail;
import com.appreman.app.Models.Asignar;
import com.appreman.app.Repository.AppPreferences;
import com.appreman.app.Repository.WebSocketManager;
import com.appreman.appreman.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import android.widget.ProgressBar;

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
    private LineChart lineChart;
    private ImageView menuIcon;
    private ImageView notifi;
    private TextView notificationText;
    private ProgressBar progressBar;
    private AlertDialog progressDialog;
    private boolean primerIntentoSinConexion = true;
    private View notificationBadge;
    private String notificationMessage;
    // Variable para almacenar las notificaciones
    private List<String> notificationMessages = new ArrayList<>();
    private String email; // Variable para almacenar el email
    private WebSocketManager webSocketManager;
    private int idOperador;

    public static HomeFragment newInstance(String email, int idOperador, List<Asignar> asignarList) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString("email", email);
        args.putInt("id_operador", idOperador);
        args.putSerializable("asignar_list", (Serializable) asignarList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        if (getArguments() != null) {
            String email = getArguments().getString("email");
            int idOperador = getArguments().getInt("id_operador");
            List<Asignar> asignarList = (List<Asignar>) getArguments().getSerializable("asignar_list");

            dbHelper = new DBHelper(requireActivity());
            appPreferences = new AppPreferences(getContext());

            spinner = view.findViewById(R.id.spinner_empresas);

            Log.d("HomeFragment", "📌 ID del Operador obtenido: " + idOperador);

            // Filtrar asignaciones por el operador actual
            List<String> empresaNames = new ArrayList<>();
            if (asignarList != null && !asignarList.isEmpty()) {
                for (Asignar asignar : asignarList) {
                    if (asignar.getIdOperador() == idOperador) {
                        empresaNames.add(asignar.getNombreEmpresa());
                    }
                }
            }
            Log.d("HomeFragment", "📌 Empresas asignadas al operador: " + empresaNames);

            // Configurar el Spinner con los nombres de empresas
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireActivity(), R.layout.spinner_dropdown_item, empresaNames);
            spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            spinner.setAdapter(spinnerAdapter);

            // Variables para los CardView
            cardActual = view.findViewById(R.id.card_actual);
            cardPotencial = view.findViewById(R.id.card_potencial);
            cardAmbas = view.findViewById(R.id.card_ambas);
            cardContinuar = view.findViewById(R.id.card_continuar);

            // Elementos de la interfaz
            textBalanceTotal = view.findViewById(R.id.text_balance_total);
            lineChart = view.findViewById(R.id.chart1);
            menuIcon = view.findViewById(R.id.menu_icon);
            notifi = view.findViewById(R.id.notification_icon);
            notificationBadge = view.findViewById(R.id.notification_badge);

            // Inicializar el punto rojo como invisible
            notificationBadge.setVisibility(View.GONE);

            notifi.setOnClickListener(v -> {
                if (notificationBadge.getVisibility() == View.VISIBLE) {
                    notificationBadge.setVisibility(View.GONE);

                    View popupView = LayoutInflater.from(requireContext()).inflate(R.layout.popup_notifications, null);
                    PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

                    actualizarMensajeNotificacion(email, requireContext());

                    TextView notificationText = popupView.findViewById(R.id.notification_text);
                    notificationText.setText(notificationMessage);

                    popupWindow.setOutsideTouchable(true);
                    popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    popupWindow.showAsDropDown(notifi);
                    notificationMessage = "";
                }
            });

            // Configuración del menú emergente
            menuIcon.setOnClickListener(this::showPopupMenu);

            // Configuración del Spinner
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    String selectedEmpresa = (String) parentView.getItemAtPosition(position);
                    appPreferences.setNombreEmpresa(selectedEmpresa);
                    updateFinancialData(view, selectedEmpresa);

                    // Guardar ID de la empresa basado en la posición del Spinner
                    int idEmpresa = position;
                    appPreferences.setIdEmpresa(idEmpresa);

                    Log.d("HomeFragment", "📌 ID de la Empresa seleccionada: " + idEmpresa);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {}
            });

            // Seleccionar la última empresa elegida en el Spinner
            String lastSelectedEmpresa = appPreferences.getNombreEmpresa();
            if (lastSelectedEmpresa != null && !lastSelectedEmpresa.isEmpty()) {
                int spinnerPosition = spinnerAdapter.getPosition(lastSelectedEmpresa);
                if (spinnerPosition >= 0) {
                    spinner.setSelection(spinnerPosition);
                }
            }

            // Botón de continuar
            cardContinuar.setOnClickListener(v -> iniciarEncuestasActivity());

            // Iniciar WebSocketManager
            webSocketManager = new WebSocketManager(this);
            webSocketManager.start();
        }

        return view;
    }


    @Override
    public void onNotificationReceived(int count) {
        if (notificationText != null) {
            notificationText.setText(String.valueOf(count));
            notificationText.setVisibility(View.VISIBLE);
        }

        if (notifi != null) {
            notifi.setImageResource(R.drawable.notifi);
        }

        // Mostrar el punto rojo cuando haya nuevas notificaciones
        if (count > 0) {
            notificationBadge.setVisibility(View.VISIBLE);
        }
    }

    // Método para actualizar el mensaje de notificación
    public void actualizarMensajeNotificacion(String email, Context context) {
        if (isConnectedToInternet(context)) {
            notificationMessage = "Archivo csv enviado a \n" + email;
        } else {
            notificationMessage = "No se pudo enviar el archivo csv \nverifique su conexion e intente nuevamente";
        }
    }

    // Método para verificar la conexión a Internet
    private boolean isConnectedToInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    @Override
    public void onResume() {
        super.onResume();
        String selectedEmpresa = appPreferences.getNombreEmpresa();
        if (selectedEmpresa != null && !selectedEmpresa.isEmpty()) {
            View view = getView(); // Obtener la vista actual del fragmento
            if (view != null) {
                updateFinancialData(view, selectedEmpresa); // Pasar la vista y la empresa al método
            }
        }
    }

    private void showProgressDialog() {
        // Crear un LinearLayout programáticamente
        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 50, 50, 50);
        layout.setGravity(Gravity.CENTER);

        // Crear un ProgressBar programáticamente
        ProgressBar progressBar = new ProgressBar(requireContext());
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);

        // Crear un TextView programáticamente
        TextView textView = new TextView(requireContext());
        textView.setText("Enviando 0%");
        textView.setTextSize(18);
        textView.setPadding(0, 20, 0, 0);
        textView.setGravity(Gravity.CENTER);

        // Añadir el ProgressBar y el TextView al LinearLayout
        layout.addView(progressBar);
        layout.addView(textView);

        // Crear el AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setCancelable(false); // Evitar que el diálogo se cierre al tocar fuera de él
        builder.setView(layout); // Establecer el LinearLayout como la vista del diálogo

        progressDialog = builder.create();
        progressDialog.show();

        // Simular progreso
        new Thread(() -> {
            for (int i = 0; i <= 100; i++) {
                int progress = i;
                requireActivity().runOnUiThread(() -> textView.setText("Enviando " + progress + "%"));
                try {
                    Thread.sleep(50); // Simular tiempo de envío
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            requireActivity().runOnUiThread(this::hideProgressDialog);
        }).start();
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("HomeFragment", "onDestroy called");
    }

    private void updateFinancialData(View view, String empresa) {
        Map<String, Double> resultados = dbHelper.obtenerContadoresYPorcentajes(empresa);

        // Obtener el número de respuestas respondidas para la empresa
        int respuestasCount = dbHelper.getRespuestasCount(empresa);

        // Obtener el total de preguntas utilizando el método getTotalPreguntasCount
        int totalPreguntas = dbHelper.getTotalPreguntasCount();

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

        double porcentajeActual = (contadorActual / totalPreguntas) * 100;
        double porcentajePotencial = (contadorPotencial / totalPreguntas) * 100;
        double porcentajeAmbas = (contadorAmbas / totalPreguntas) * 100;

        TextView textActual = view.findViewById(R.id.text_actual);
        TextView textPotencial = view.findViewById(R.id.text_potencial);
        TextView textAmbas = view.findViewById(R.id.text_ambas);

        textActual.setText(String.format("S/ %.2f (%.2f%%)", contadorActual, porcentajeActual));
        textPotencial.setText(String.format("S/ %.2f (%.2f%%)", contadorPotencial, porcentajePotencial));
        textAmbas.setText(String.format("S/ %.2f (%.2f%%)", contadorAmbas, porcentajeAmbas));

        double total = contadorActual + contadorPotencial + contadorAmbas;
        double totalPorcentaje = (total / totalPreguntas) * 100;

        textBalanceTotal.setText(String.format("S/ %.2f (%.2f%%)", total, totalPorcentaje));

        // Actualiza el TextView con la cantidad de preguntas respondidas y el total
        TextView textViewPreguntas = view.findViewById(R.id.text_view_preguntas);
        textViewPreguntas.setText(String.format("%d de %d", respuestasCount, totalPreguntas));

        Log.d("HomeFragment", "setupChart: lineChart = " + lineChart);
        Log.d("HomeFragment", "setupChart: respuestasPorDia = " + dbHelper.obtenerRespuestasPorDia(empresa));
        Log.d("HomeFragment", "setupChart: totalPreguntas = " + totalPreguntas);

        setupChart(lineChart, dbHelper.obtenerRespuestasPorDia(empresa), totalPreguntas);    }

    private void setupChart(LineChart chart, Map<String, Integer> respuestasPorDia, int totalPreguntas) {
        List<Entry> entriesRespuestas = new ArrayList<>();
        List<Entry> entriesObjetivo = new ArrayList<>();
        String[] dias = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes"};
        int index = 0;
        int preguntasPorDia = totalPreguntas / dias.length;

        Log.d("setupChart", "Total Preguntas: " + totalPreguntas);
        Log.d("setupChart", "Preguntas por Día: " + preguntasPorDia);

        for (String dia : dias) {
            int respuestas = respuestasPorDia.containsKey(dia) ? respuestasPorDia.get(dia) : 0;
            Log.d("setupChart", "Día: " + dia + ", Respuestas: " + respuestas);
            entriesRespuestas.add(new Entry(index, respuestas));
            entriesObjetivo.add(new Entry(index, preguntasPorDia));
            index++;
        }

        LineDataSet dataSetRespuestas = new LineDataSet(entriesRespuestas, "Respuestas por día");
        dataSetRespuestas.setColor(Color.BLUE);
        dataSetRespuestas.setLineWidth(2.5f);
        dataSetRespuestas.setCircleColor(ColorTemplate.getHoloBlue());
        dataSetRespuestas.setCircleRadius(5f);
        dataSetRespuestas.setFillColor(ColorTemplate.getHoloBlue());
        dataSetRespuestas.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        dataSetRespuestas.setDrawValues(true);

        LineDataSet dataSetObjetivo = new LineDataSet(entriesObjetivo, "Objetivo diario");
        dataSetObjetivo.setColor(Color.RED);
        dataSetObjetivo.setLineWidth(2.5f);
        dataSetObjetivo.setCircleColor(ColorTemplate.getHoloBlue());
        dataSetObjetivo.setCircleRadius(5f);
        dataSetObjetivo.setFillColor(ColorTemplate.getHoloBlue());
        dataSetObjetivo.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        dataSetObjetivo.setDrawValues(true);

        LineData lineData = new LineData(dataSetRespuestas, dataSetObjetivo);
        chart.setData(lineData);

        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(dias));
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setGranularity(1f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(totalPreguntas);

        chart.getAxisRight().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setPinchZoom(true);
        chart.invalidate(); // Refresca el gráfico
    }

    @SuppressLint("LongLogTag")
    private void iniciarEncuestasActivity() {
        String selectedEmpresa = (String) spinner.getSelectedItem();

        if (selectedEmpresa != null && !selectedEmpresa.isEmpty()) {
            int idEmpresa = spinner.getSelectedItemPosition() + 1; // Sumar uno a la posición del Spinner

            // Mostrar detalles del proceso en Logcat
            Log.d("iniciarEncuestasActivity", "Correo de inicio de encuesta enviado para la empresa: " + selectedEmpresa);
            Log.d("iniciarEncuestasActivity", "ID Empresa (Posición Spinner): " + idEmpresa);
            Log.d("iniciarEncuestasActivity", "ID Operador: " + idOperador);
            Log.d("iniciarEncuestasActivity", "Email: " + email);

            // Iniciar la actividad de encuestas
            Intent intent = new Intent(getActivity(), EncuestasActivity.class);
            intent.putExtra("empresa_nombre", selectedEmpresa);
            intent.putExtra("id_empresa", idEmpresa);
            intent.putExtra("id_operador", idOperador);
            intent.putExtra("email", email);

            // Verificar que los datos están siendo añadidos al Intent
            Log.d("iniciarEncuestasActivity", "Intent preparado con los siguientes datos:");
            Log.d("iniciarEncuestasActivity", "empresa_nombre: " + selectedEmpresa);
            Log.d("iniciarEncuestasActivity", "id_empresa: " + idEmpresa);
            Log.d("iniciarEncuestasActivity", "id_operador: " + idOperador);
            Log.d("iniciarEncuestasActivity", "email: " + email);

            startActivity(intent);
        } else {
            Log.w("iniciarEncuestasActivity", "No se seleccionó ninguna empresa.");
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
                String selectedEmpresa = appPreferences.getNombreEmpresa();
                if (selectedEmpresa != null && !selectedEmpresa.isEmpty()) {
                    File file = dbHelper.guardarRespuestasEnArchivo(selectedEmpresa, requireContext());
                    enviarCorreoConArchivoAdjunto(file);
                } else {
                    Toast.makeText(requireContext(), "No se ha seleccionado ninguna empresa", Toast.LENGTH_SHORT).show();
                }
                return true;
            } else {
                return false;
            }
        });
        popupMenu.show();
    }

    @SuppressLint("LongLogTag")
    private void enviarCorreoConArchivoAdjunto(File file) {
        String selectedEmpresa = appPreferences.getNombreEmpresa();
        String subject = "Exportación de Respuestas : " + selectedEmpresa;
        String messageBody = "Respuestas  " + selectedEmpresa;

        requireActivity().runOnUiThread(this::showProgressDialog);

        new Thread(() -> {
            MailSender mailSender = new MailSender();
            if (mailSender.isConnectedToInternet(requireContext())) {
                try {
                    mailSender.sendMailWithAttachment(email, subject, messageBody, file, requireContext());
                    Log.d("enviarCorreoConArchivoAdjunto", "Correo enviado exitosamente con el archivo adjunto." + file.getName());
                    requireActivity().runOnUiThread(() -> {
                        hideProgressDialog();
                        notificationBadge.setVisibility(View.VISIBLE);
                        actualizarMensajeNotificacion(email, requireContext());
                        Toast.makeText(requireContext(), "Correo enviado exitosamente", Toast.LENGTH_SHORT).show();
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("enviarCorreoConArchivoAdjunto", "Error al enviar el correo: " + e.getMessage());
                    requireActivity().runOnUiThread(() -> {
                        hideProgressDialog();
                        Toast.makeText(requireContext(), "Error al enviar el correo", Toast.LENGTH_SHORT).show();
                    });
                }
            } else {
                Log.e("enviarCorreoConArchivoAdjunto", "No hay conexión a Internet. El correo se enviará más tarde.");
                mailSender.addPendingEmail(new PendingEmail(email, subject, messageBody, file));
                requireActivity().runOnUiThread(() -> {
                    hideProgressDialog();
                    notificationBadge.setVisibility(View.VISIBLE);
                    actualizarMensajeNotificacion(email, requireContext());
                    Toast.makeText(requireContext(), "No hay conexión a Internet, verifique e intente nuevamente", Toast.LENGTH_LONG).show();
                });
            }
        }).start();
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
}

package com.appreman.app.Fragments;

import static com.appreman.app.Utils.Constant.URL;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;


import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appreman.app.Adapter.ElementosAdapter;
import com.appreman.app.Api.ApiAdapter;
import com.appreman.app.Api.ApiServices;
import com.appreman.app.Api.Response.ActualizarAsignarResponse;
import com.appreman.app.Database.DBHelper;
import com.appreman.app.Email.MailSender;
import com.appreman.app.Email.PendingEmail;
import com.appreman.app.Models.Asignar;
import com.appreman.app.Models.Elemento;
import com.appreman.app.Models.Empresa;
import com.appreman.app.Models.Operador;
import com.appreman.app.Repository.AppPreferences;
import com.appreman.appreman.R;
import com.github.mikephil.charting.charts.LineChart;
import com.google.android.material.card.MaterialCardView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

public class AsignarFragment extends Fragment {
    private static final String TAG = "ASIGNAR FRAGMENT";

    private static final int REQUEST_WRITE_STORAGE = 112;

    private Spinner spinner;
    private DBHelper dbHelper;
    private AppPreferences appPreferences;
    private MaterialCardView cardActual;
    private MaterialCardView cardPotencial;
    private MaterialCardView cardAmbas;
    private MaterialCardView cardContinuar;
    private LineChart lineChart;
    private ImageView menuIcon;
    private ImageView notifi;
    private AlertDialog progressDialog;
    private boolean primerIntentoSinConexion = true;
    private View notificationBadge;
    private String notificationMessage;
    private List<String> notificationMessages = new ArrayList<>();
    private String email; // Variable para almacenar el email
    private ApiServices appServices;
    private Executor executor = Executors.newSingleThreadExecutor();

    public static AsignarFragment newInstance() {
        AsignarFragment fragment = new AsignarFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_asignar, container, false);

        dbHelper = new DBHelper(requireActivity());
        appPreferences = new AppPreferences(requireActivity());

        cardActual = view.findViewById(R.id.text_actual);
        cardPotencial = view.findViewById(R.id.tarjeta_potencial);
        cardAmbas = view.findViewById(R.id.tarjeta_ambas);
        cardContinuar = view.findViewById(R.id.tarjeta_continuar);

        lineChart = view.findViewById(R.id.grafico1);
        menuIcon = view.findViewById(R.id.icono_menu);
        notifi = view.findViewById(R.id.icono_notificacion);
        notificationBadge = view.findViewById(R.id.insignia_notificacion);

        // Inicializar el punto rojo como invisible
        notificationBadge.setVisibility(View.GONE);

        ApiAdapter apiAdapter = new ApiAdapter();
        appServices = apiAdapter.getApiService(URL);

        notifi.setOnClickListener(v -> {
            // Verificar si el punto rojo está visible
            if (notificationBadge.getVisibility() == View.VISIBLE) {
                // Ocultar el punto rojo al presionar la campana
                notificationBadge.setVisibility(View.GONE);

                // Inflar el layout del popup
                View popupView = LayoutInflater.from(requireContext()).inflate(R.layout.popup_notifications, null);
                PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

                // Llamar al método para actualizar el mensaje de notificación
                actualizarMensajeNotificacion(email, requireContext());

                // Configurar el contenido del popup con el mensaje actualizado
                TextView notificationText = popupView.findViewById(R.id.notification_text);
                notificationText.setText(notificationMessage);

                // Configurar el PopupWindow para que se cierre al presionar fuera de él
                popupWindow.setOutsideTouchable(true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                // Mostrar el popup
                popupWindow.showAsDropDown(notifi);

                // Borrar la notificación después de mostrarla
                notificationMessage = "";
            }
        });

        Bundle args = getArguments();
        if (args != null) {
            String empresaName = args.getString("empresa_nombre");
            email = args.getString("email"); // Obtener el email del Bundle
            if (empresaName != null) {
                appPreferences.setNombreEmpresa(empresaName);
            }
        }

        cardContinuar.setOnClickListener(v -> mostrarDialogoSeleccionEmpresa());
        menuIcon.setOnClickListener(this::showPopupMenu);

        return view;
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
        Log.d("AsignarFragment", "onDestroy called");
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

    private void mostrarDialogoSeleccionEmpresa() {
        // Obtener la lista de empresas desde la base de datos
        List<Empresa> empresas = dbHelper.getAllEmpresas(); // Suponiendo que tienes un método que devuelve una lista de objetos Empresa
        String[] empresasArray = new String[empresas.size()];
        final int[] empresaIds = new int[empresas.size()];

        for (int i = 0; i < empresas.size(); i++) {
            empresasArray[i] = empresas.get(i).getNombre();
            empresaIds[i] = empresas.get(i).getId();
        }

        final String[] empresaSeleccionada = {null};
        final int[] idEmpresaSeleccionada = {0};

        // Crear un AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Seleccionar Empresa");

        // Crear un LinearLayout para contener las vistas
        LinearLayout layoutEmpresas = new LinearLayout(requireContext());
        layoutEmpresas.setOrientation(LinearLayout.VERTICAL);
        layoutEmpresas.setPadding(16, 16, 16, 16);

        // ListView para seleccionar empresa con diseño de casillas de verificación
        ListView listView = new ListView(requireContext());
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE); // Permitir selección única
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_checked, empresasArray);
        listView.setAdapter(adapter);
        layoutEmpresas.addView(listView);

        // Actualizar el TextView cuando se selecciona una empresa
        listView.setOnItemClickListener((parent, view, position, id) -> {
            // Marcar la empresa seleccionada
            listView.setItemChecked(position, true);

            empresaSeleccionada[0] = empresasArray[position];
            idEmpresaSeleccionada[0] = empresaIds[position];
            mostrarDialogoSeleccionOperadores(empresaSeleccionada[0], idEmpresaSeleccionada[0]);
        });

        // Configurar el botón de cancelar
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

        // Mostrar el AlertDialog
        builder.setView(layoutEmpresas);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void mostrarDialogoSeleccionOperadores(String empresaSeleccionada, int idEmpresaSeleccionada) {
        // Obtener la lista de operadores desde la base de datos
        List<Operador> operadores = dbHelper.getAllOperadores();
        String[] operadoresArray = new String[operadores.size()];
        boolean[] operadoresSeleccionados = new boolean[operadores.size()];
        int[] operadoresIds = new int[operadores.size()];

        for (int i = 0; i < operadores.size(); i++) {
            operadoresArray[i] = operadores.get(i).getNombre() + " " + operadores.get(i).getApellido();
            operadoresIds[i] = operadores.get(i).getId();
        }

        // Crear un AlertDialog para seleccionar operadores
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Seleccionar Operadores");

        // Crear un LinearLayout para contener las vistas
        LinearLayout layoutOperadores = new LinearLayout(requireContext());
        layoutOperadores.setOrientation(LinearLayout.VERTICAL);
        layoutOperadores.setPadding(16, 16, 16, 16);

        // ListView para seleccionar operadores con diseño de casillas de verificación
        ListView listView = new ListView(requireContext());
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE); // Permitir selección múltiple
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_multiple_choice, operadoresArray);
        listView.setAdapter(adapter);
        layoutOperadores.addView(listView);

        // TextView para mostrar las selecciones del usuario
        TextView textViewSelecciones = new TextView(requireContext());
        textViewSelecciones.setPadding(16, 16, 16, 16);
        textViewSelecciones.setTextSize(18);
        textViewSelecciones.setTextColor(Color.DKGRAY);
        textViewSelecciones.setBackgroundColor(Color.LTGRAY);
        textViewSelecciones.setVisibility(View.VISIBLE); // Mostrar inicialmente

        // Mostrar la selección inicial de la empresa
        String seleccionInicial = "Empresa seleccionada: " + empresaSeleccionada;
        SpannableString spannableStringInicial = new SpannableString(seleccionInicial);
        spannableStringInicial.setSpan(new StyleSpan(Typeface.BOLD), 0, seleccionInicial.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringInicial.setSpan(new ForegroundColorSpan(Color.BLUE), seleccionInicial.indexOf(empresaSeleccionada), seleccionInicial.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textViewSelecciones.setText(spannableStringInicial);

        // Añadir el TextView al final del LinearLayout
        layoutOperadores.addView(textViewSelecciones);

        // Actualizar el TextView cuando se seleccionen operadores
        listView.setOnItemClickListener((parent, view, position, id) -> {
            operadoresSeleccionados[position] = !operadoresSeleccionados[position];
            listView.setItemChecked(position, operadoresSeleccionados[position]);

            StringBuilder seleccionados = new StringBuilder("Empresa seleccionada: " + empresaSeleccionada + "\nOperadores seleccionados: ");
            for (int i = 0; i < operadoresSeleccionados.length; i++) {
                if (operadoresSeleccionados[i]) {
                    seleccionados.append(operadoresArray[i]).append(", ");
                }
            }

            if (seleccionados.length() > 0) {
                seleccionados.setLength(seleccionados.length() - 2); // Eliminar la última coma y espacio
            }

            SpannableString spannableString = new SpannableString(seleccionados.toString());
            int startIndex = seleccionados.indexOf("Operadores seleccionados: ") + "Operadores seleccionados: ".length();
            int endIndex = seleccionados.length();
            spannableString.setSpan(new ForegroundColorSpan(Color.BLUE), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            textViewSelecciones.setText(spannableString);
            textViewSelecciones.setVisibility(View.VISIBLE); // Mostrar el texto cuando se selecciona algo
        });

        // Configurar el botón de siguiente
        builder.setPositiveButton("Siguiente", (dialog, which) -> {
            List<Integer> operadoresSeleccionadosList = new ArrayList<>();
            List<String> operadoresNombresSeleccionadosList = new ArrayList<>();
            for (int i = 0; i < operadoresSeleccionados.length; i++) {
                if (operadoresSeleccionados[i]) {
                    operadoresSeleccionadosList.add(operadoresIds[i]);
                    operadoresNombresSeleccionadosList.add(operadoresArray[i]);
                }
            }
            if (!operadoresSeleccionadosList.isEmpty()) {
                // Llamar al método mostrarDialogoSeleccionElementos con los IDs de los operadores seleccionados
                mostrarDialogoSeleccionElementos(idEmpresaSeleccionada, empresaSeleccionada, operadoresSeleccionadosList, operadoresNombresSeleccionadosList);
            } else {
                Toast.makeText(requireContext(), "Por favor, seleccione al menos un operador.", Toast.LENGTH_SHORT).show();
            }
        });

        // Configurar el botón de cancelar
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

        // Mostrar el AlertDialog
        builder.setView(layoutOperadores);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void mostrarDialogoSeleccionElementos(int idEmpresa, String empresaSeleccionada, List<Integer> idsOperadores, List<String> nombresOperadores) {

        List<Elemento> elementosTotales = dbHelper.getElementosDisponibles(); // Obtener todos los elementos
        Map<Integer, List<Elemento>> asignaciones = new HashMap<>();  // Mapa para las asignaciones
        Set<Integer> elementosAsignados = new HashSet<>(); // IDs de elementos ya asignados

        // Llenar el conjunto con IDs de elementos asignados
        for (List<Elemento> lista : asignaciones.values()) {
            for (Elemento elemento : lista) {
                elementosAsignados.add(Integer.valueOf(elemento.getNumero())); // Asume que tienes un método getId()
            }
        }

        // Filtrar los elementos disponibles para el primer operador seleccionado
        int idOperadorInicial = idsOperadores.get(0);
        List<Elemento> elementosDisponibles = new ArrayList<>();
        for (Elemento elemento : elementosTotales) {
            if (!elementosAsignados.contains(elemento.getNumero())) {
                elementosDisponibles.add(elemento);
            }
        }

        // Ordenar los elementos disponibles de forma ascendente
        Collections.sort(elementosDisponibles, new Comparator<Elemento>() {
            @Override
            public int compare(Elemento e1, Elemento e2) {
                return e1.getNumero().compareTo(e2.getNumero());
            }
        });

        ElementosAdapter elementosAdapter = new ElementosAdapter(requireContext(), elementosDisponibles, asignaciones, idOperadorInicial);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Asignar Elementos");

        LinearLayout layoutElementos = new LinearLayout(requireContext());
        layoutElementos.setOrientation(LinearLayout.VERTICAL);
        layoutElementos.setPadding(16, 16, 16, 16);

        Spinner spinnerOperadores = new Spinner(requireContext());
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, nombresOperadores);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOperadores.setAdapter(spinnerAdapter);
        layoutElementos.addView(spinnerOperadores);

        RecyclerView recyclerView = new RecyclerView(requireContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(elementosAdapter);
        layoutElementos.addView(recyclerView);

        TextView textViewEmpresa = new TextView(requireContext());
        textViewEmpresa.setPadding(16, 16, 16, 16);
        textViewEmpresa.setTextSize(18);
        textViewEmpresa.setTextColor(Color.DKGRAY);
        textViewEmpresa.setBackgroundColor(Color.LTGRAY);
        textViewEmpresa.setText("Empresa seleccionada: " + empresaSeleccionada);
        layoutElementos.addView(textViewEmpresa);

        TextView textViewSelecciones = new TextView(requireContext());
        textViewSelecciones.setPadding(16, 16, 16, 16);
        textViewSelecciones.setTextSize(18);
        textViewSelecciones.setTextColor(Color.DKGRAY);
        textViewSelecciones.setBackgroundColor(Color.LTGRAY);
        textViewSelecciones.setVisibility(View.VISIBLE);
        layoutElementos.addView(textViewSelecciones);

        spinnerOperadores.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int idOperadorSeleccionado = idsOperadores.get(position);
                List<Elemento> elementosDisponiblesNuevo = new ArrayList<>();

                for (Elemento elemento : elementosTotales) {
                    if (!elementosAsignados.contains(elemento.getNumero()) || asignaciones.getOrDefault(idOperadorSeleccionado, new ArrayList<>()).contains(elemento)) {
                        elementosDisponiblesNuevo.add(elemento);
                    }
                }

                // Ordenar los elementos disponibles de forma ascendente
                Collections.sort(elementosDisponiblesNuevo, new Comparator<Elemento>() {
                    @Override
                    public int compare(Elemento e1, Elemento e2) {
                        return e1.getNumero().compareTo(e2.getNumero());
                    }
                });

                elementosAdapter.actualizarLista(elementosDisponiblesNuevo, idOperadorSeleccionado);
                actualizarTextViewSelecciones(textViewSelecciones, empresaSeleccionada, elementosDisponiblesNuevo, asignaciones.getOrDefault(idOperadorSeleccionado, new ArrayList<>()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        builder.setPositiveButton("Aceptar", (dialog, which) -> {
            mostrarDialogoConfirmacionMultiple(idEmpresa, empresaSeleccionada, idsOperadores, nombresOperadores, asignaciones);
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> mostrarDialogoSeleccionOperadores(empresaSeleccionada, idEmpresa));

        builder.setView(layoutElementos);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void mostrarDialogoConfirmacionMultiple(int idEmpresa, String empresaSeleccionada, List<Integer> idsOperadores, List<String> nombresOperadores, Map<Integer, List<Elemento>> asignaciones) {
        Log.d("AsignarFragment", "mostrarDialogoConfirmacionMultiple llamado con: idEmpresa=" + idEmpresa + ", empresaSeleccionada=" + empresaSeleccionada + ", idsOperadores=" + idsOperadores.toString() + ", nombresOperadores=" + nombresOperadores.toString() + ", asignaciones=" + asignaciones.toString());

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirmación de Asignación");

        StringBuilder mensaje = new StringBuilder("Empresa: " + empresaSeleccionada + "\n\nAsignaciones:\n");

        for (int i = 0; i < idsOperadores.size(); i++) {
            int idOperador = idsOperadores.get(i);
            String nombreOperador = nombresOperadores.get(i);
            List<Elemento> elementosAsignados = asignaciones.getOrDefault(idOperador, new ArrayList<>());

            mensaje.append("Operador: ").append(nombreOperador).append("\nElementos:\n");

            for (Elemento elemento : elementosAsignados) {
                mensaje.append(elemento.getNumero()).append(". ").append(elemento.getNombre()).append("\n");
            }
            mensaje.append("\n");
        }

        builder.setMessage(mensaje.toString());

        builder.setPositiveButton("Aceptar", (dialog, which) -> {
            for (int i = 0; i < idsOperadores.size(); i++) {
                int idOperador = idsOperadores.get(i);
                String nombreOperador = nombresOperadores.get(i);
                List<Elemento> elementosAsignados = asignaciones.getOrDefault(idOperador, new ArrayList<>());

                if (!elementosAsignados.isEmpty()) {
                    // Obtener los números completos de los elementos asignados (como String)
                    List<String> numerosElementos = new ArrayList<>();
                    for (Elemento elemento : elementosAsignados) {
                        try {
                            // Guardar el número tal cual como se recibe (incluyendo decimales)
                            String numeroElemento = elemento.getNumero();  // El número se guarda tal como es
                            numerosElementos.add(numeroElemento);
                        } catch (Exception e) {
                            Log.e("AsignarFragment", "Error al obtener el número del elemento: " + elemento.getNumero(), e);
                        }
                    }

                    // Guardamos los números de los elementos (como String)
                    dbHelper.guardarAsignar(String.valueOf(idEmpresa), empresaSeleccionada, String.valueOf(idOperador), numerosElementos);

                    // Ejecutamos la tarea en el hilo de fondo
                    executor.execute(() -> {
                        try {
                            // Llamar al método para enviar los datos
                            enviarAsignar(getJsonEncodeAsignar(String.valueOf(idEmpresa), empresaSeleccionada, String.valueOf(idOperador), numerosElementos));
                            Log.d("AsignarFragment", "Mensaje enviado correctamente");
                        } catch (Exception e) {
                            Log.e("AsignarFragment", "Error al enviar mensaje", e);
                        }
                    });


                    Log.d("AsignarFragment", "Se ha guardado correctamente: Empresa: " + idEmpresa + ", Operador: " + idOperador + ", Elementos (números): " + numerosElementos.toString());
                }
            }
            Toast.makeText(requireContext(), "Asignaciones confirmadas.", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void actualizarTextViewSelecciones(TextView textViewSelecciones, String empresaSeleccionada, List<Elemento> elementos, List<Elemento> elementosSeleccionados) {
        StringBuilder seleccionados = new StringBuilder("Empresa seleccionada: " + empresaSeleccionada + "\nElementos seleccionados: ");
        boolean hayElementosSeleccionados = !elementosSeleccionados.isEmpty();

        for (Elemento elemento : elementosSeleccionados) {
            seleccionados.append(elemento.getNumero()).append(". ").append(elemento.getNombre()).append(", ");
        }

        if (seleccionados.length() > 0) {
            seleccionados.setLength(seleccionados.length() - 2); // Eliminar la última coma y espacio
        }

        SpannableString spannableString = new SpannableString(seleccionados.toString());
        if (hayElementosSeleccionados) {
            int startIndex = seleccionados.indexOf("Elementos seleccionados: ") + "Elementos seleccionados: ".length();
            int endIndex = seleccionados.length();
            spannableString.setSpan(new ForegroundColorSpan(Color.BLUE), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        textViewSelecciones.setText(spannableString);
        textViewSelecciones.setVisibility(View.VISIBLE); // Mostrar el texto cuando se selecciona algo
    }

    // TODO: ******************************* ASIGNAR POST *****************************************


    private void enviarAsignar(RequestBody getJsonEncode) {


        Call<ActualizarAsignarResponse> callSync = appServices.postActualizarAsignar(getJsonEncode);

        try {
            Response<ActualizarAsignarResponse> response = callSync.execute();

            if (response.isSuccessful()) {

                ActualizarAsignarResponse actualizarAsignarResponse = response.body();

            } else {

                if (response.code() == 401) {

                    Log.e(TAG, "CODIGO ERROR:" + response.code());


                } else {

                    try {

                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Log.e(TAG, "ERROR:" + jObjError.getString("message"));


                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }
            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    private RequestBody getJsonEncodeAsignar(String idEmpresa, String nombreEmpresa, String idOperador, List<String> numerosElementos) {

        JSONArray jsonAsignar = new JSONArray();

        // Crear un objeto JSON por cada número de elemento
        for (int i = 0; i < numerosElementos.size(); i++) {
            JSONObject obj = new JSONObject();

            try {
                obj.put("id_operador", idOperador);
                obj.put("id_empresa",  idEmpresa);
                obj.put("id_elemento", String.valueOf(numerosElementos.get(i)));
                obj.put("nombre_empresa", nombreEmpresa);

                jsonAsignar.put(obj); // Agregar al arreglo jsonAsignar
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Aquí construimos el objeto JSON con la clave "asignarList"
        Map<String, Object> jsonParams = new HashMap<>();
        jsonParams.put("asinarList", jsonAsignar);

        Log.e(TAG, "asinarList:" + jsonParams.toString());

        // Convertir a JSON y luego a RequestBody
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new JSONObject(jsonParams).toString());
        return body;
    }

}

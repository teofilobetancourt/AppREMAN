package com.appreman.app.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appreman.app.Activity.OpcionSelectionListener;
import com.appreman.app.Database.DBHelper;
import com.appreman.app.Models.Opcion;
import com.appreman.app.Models.Pregunta;
import com.appreman.appreman.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PreguntaAdapter extends RecyclerView.Adapter<PreguntaAdapter.MotivosViewHolder> implements OpcionSelectionListener {

    private static final String TAG = "PreguntaAdapter";

    private final List<Pregunta> items;
    private final Context context;
    private final Map<String, List<Opcion>> opcionesSeleccionadasMap;
    private final DBHelper dbHelper;  // Agregado: una instancia única de DBHelper para evitar abrir y cerrar la base de datos repetidamente

    public PreguntaAdapter(Context context, List<Pregunta> items) {
        this.items = items;
        this.context = context;
        this.opcionesSeleccionadasMap = new HashMap<>();
        this.dbHelper = new DBHelper(context);  // Agregado: inicializar DBHelper
        initializeOpcionesSeleccionadasMap();
    }

    private void initializeOpcionesSeleccionadasMap() {
        for (Pregunta pregunta : items) {
            opcionesSeleccionadasMap.put(pregunta.getNumero(), new ArrayList<>());
        }
    }

    @NonNull
    @Override
    public MotivosViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_pregunta, viewGroup, false);
        return new MotivosViewHolder(v);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull MotivosViewHolder holder, final int i) {
        Pregunta pregunta = items.get(i);

        // Configuración del texto de la pregunta
        holder.txtPregunta.setText(pregunta.getNumero().concat(".- ").concat(pregunta.getDescripcion()));

        // Configuración del RecyclerView para las opciones de la pregunta
        List<Opcion> opciones = dbHelper.getOpcionesPregunta(pregunta.getNumero());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        holder.recycler.setLayoutManager(layoutManager);

        OpcionAdapter opcionAdapter = new OpcionAdapter(opciones, this);
        holder.recycler.setAdapter(opcionAdapter);

        holder.btnPreguntas.setOnClickListener(v -> {
            // Aquí puedes obtener las opciones seleccionadas y guardarlas en la base de datos
            Log.d(TAG, "Botón 'btnPreguntas' presionado para la pregunta: " + pregunta.getNumero());

            // Obtén las opciones seleccionadas directamente desde el adaptador de opciones
            OpcionAdapter preguntaOpcionAdapter = (OpcionAdapter) holder.recycler.getAdapter();
            assert preguntaOpcionAdapter != null;
            List<Opcion> opcionesSeleccionadas = preguntaOpcionAdapter.obtenerOpcionesSeleccionadas();

            // Asegúrate de que haya al menos dos opciones seleccionadas
            if (opcionesSeleccionadas.size() >= 2) {
                Opcion opcionActual = opcionesSeleccionadas.get(0);
                Opcion opcionPotencial = opcionesSeleccionadas.get(1);

                // Llama al método para guardar las opciones seleccionadas
                dbHelper.insertarOpcionesEnRespuestas(pregunta.getNumero(), opcionActual.getNumero(), opcionPotencial.getNumero());

                // Luego, puedes notificar al adaptador que los datos han cambiado si es necesario
                preguntaOpcionAdapter.notifyDataSetChanged();
            } else {
                Log.e(TAG, "No se pudieron obtener al menos dos opciones seleccionadas.");
            }
        });

    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public void onOpcionesSelected(String preguntaNumero, List<Opcion> opcionesSeleccionadas) {
        // Implementación según tus necesidades
    }

    @Override
    public void onOpcionSelected(Opcion opcionActual, Opcion opcionPotencial) {
        // Implementación según tus necesidades
    }

    protected static class MotivosViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtPregunta;
        private final RecyclerView recycler;
        private final Button btnPreguntas;

        public MotivosViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPregunta = itemView.findViewById(R.id.textNombre);
            recycler = itemView.findViewById(R.id.recyclerview);
            btnPreguntas = itemView.findViewById(R.id.btnPregunta);
        }
    }

    @Override
    public void onOpcionSelected(String preguntaNumero, List<Opcion> opcionesSeleccionadas) {
        // Actualiza la lista de opciones seleccionadas para la pregunta actual
        List<Opcion> opcionesSeleccionadasAnteriores = opcionesSeleccionadasMap.get(preguntaNumero);
        assert opcionesSeleccionadasAnteriores != null;
        opcionesSeleccionadasAnteriores.clear();
        opcionesSeleccionadasAnteriores.addAll(opcionesSeleccionadas);

        // Agrega logs para verificar las opciones seleccionadas
        Log.d(TAG, "Opciones seleccionadas para la pregunta " + preguntaNumero + ":");
        for (Opcion opcion : opcionesSeleccionadas) {
            Log.d(TAG, " - " + opcion.getNombre());
        }
    }
}


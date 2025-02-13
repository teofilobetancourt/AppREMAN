package com.appreman.app.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appreman.app.Activity.OpcionSelectionListener;
import com.appreman.app.Database.DBHelper;
import com.appreman.app.Models.Opcion;
import com.appreman.app.Models.Pregunta;
import com.appreman.app.Repository.AppPreferences;
import com.appreman.appreman.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PreguntaAdapter extends RecyclerView.Adapter<PreguntaAdapter.MotivosViewHolder> implements OpcionSelectionListener {

    private static final String TAG = "PreguntaAdapter";

    private final List<Pregunta> items;
    private final Context context;
    private final DBHelper dbHelper;
    private final String nombreEmpresa;
    private final String nombreEncuestado;
    private final String cargoEncuestado;

    public PreguntaAdapter(Context context, List<Pregunta> items, String nombreEmpresa, String nombreEncuestado, String cargoEncuestado) {
        this.context = context;
        this.items = items;
        this.dbHelper = new DBHelper(context);
        this.nombreEmpresa = nombreEmpresa;
        this.nombreEncuestado = nombreEncuestado;
        this.cargoEncuestado = cargoEncuestado;
    }

    @NonNull
    @Override
    public MotivosViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_pregunta, viewGroup, false);
        return new MotivosViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MotivosViewHolder holder, final int i) {
        Pregunta pregunta = items.get(i);

        // Obtener valores de SharedPreferences
        AppPreferences appPreferences = new AppPreferences(context);
        String nombreEmpresa = appPreferences.getNombreEmpresa();
        String nombreEncuestado = appPreferences.getNombreEncuestado();
        String cargoEncuestado = appPreferences.getCargoEncuestado();

        holder.txtPregunta.setText(pregunta.getNumero().concat(".- ").concat(pregunta.getDescripcion()));

        List<Opcion> opciones = dbHelper.getOpcionesPregunta(pregunta.getNumero(), nombreEmpresa);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        holder.recycler.setLayoutManager(layoutManager);

        OpcionAdapter opcionAdapter = new OpcionAdapter(context, opciones, this);
        holder.recycler.setAdapter(opcionAdapter);

        // Verificar si la pregunta ha sido respondida
        if (isQuestionInDatabase(nombreEmpresa, pregunta.getNumero())) {
            // Cambiar el color del botón a su color normal si la pregunta ha sido respondida
            holder.btnPreguntas.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
        } else {
            // Cambiar el color del botón a rojo si la pregunta no ha sido respondida
            holder.btnPreguntas.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
        }

        holder.btnPreguntas.setOnClickListener(v -> {
            OpcionAdapter preguntaOpcionAdapter = (OpcionAdapter) holder.recycler.getAdapter();
            assert preguntaOpcionAdapter != null;
            List<Opcion> opcionesSeleccionadas = preguntaOpcionAdapter.obtenerOpcionesSeleccionadas();

            String preguntaNumero = pregunta.getNumero();
            String fechaRespuesta = obtenerFechaActual(); // Método para obtener la fecha actual

            Opcion opcionComentario = null;
            for (Opcion opcion : opcionesSeleccionadas) {
                if (opcion.getNumero().equals("Comentario")) {
                    opcionComentario = opcion;
                    break;
                }
            }

            if (opcionComentario != null) {
                opcionesSeleccionadas.remove(opcionComentario); // Removemos la opción "Comentario" de la lista
            }

            String opcionActual = "";
            String opcionPotencial = "";
            String comentario = opcionComentario != null ? opcionComentario.getComentario() : "";

            // Si hay solo una opción seleccionada, asignarla como actual y potencial
            if (opcionesSeleccionadas.size() == 1) {
                opcionActual = opcionesSeleccionadas.get(0).getNumero();
                opcionPotencial = opcionActual; // La misma opción se guarda en ambas columnas
            } else if (opcionesSeleccionadas.size() == 2) {
                opcionActual = opcionesSeleccionadas.get(0).getNumero();
                opcionPotencial = opcionesSeleccionadas.get(1).getNumero();
            }

            // Guardamos en la BD asegurándonos de que "Comentario" no sustituya el número de la opción
            if (isQuestionInDatabase(nombreEmpresa, preguntaNumero)) {
                dbHelper.updateAnswerInDatabase(nombreEmpresa, preguntaNumero, opcionActual, opcionPotencial, comentario, nombreEncuestado, cargoEncuestado);
            } else {
                dbHelper.insertarOpcionesEnRespuestas(nombreEmpresa, preguntaNumero, opcionActual, opcionPotencial, fechaRespuesta, comentario, nombreEncuestado, cargoEncuestado);
            }


            preguntaOpcionAdapter.notifyDataSetChanged();

            // Mostrar un log con los datos guardados en una sola fila
            Log.d(TAG, "Opciones guardadas correctamente: " +
                    "Nombre de la empresa: " + nombreEmpresa + ", " +
                    "Número de la pregunta: " + preguntaNumero + ", " +
                    "Opción actual: " + (opcionesSeleccionadas.isEmpty() ? "" : opcionesSeleccionadas.get(0).getNumero()) + ", " +
                    "Opción potencial: " + (opcionesSeleccionadas.size() > 1 ? opcionesSeleccionadas.get(1).getNumero() : "") + ", " +
                    "Fecha de respuesta: " + fechaRespuesta + ", " +
                    "Comentario: " + (opcionComentario != null ? opcionComentario.getComentario() : "") + ", " +
                    "Nombre del encuestado: " + nombreEncuestado + ", " +
                    "Cargo del encuestado: " + cargoEncuestado);

            mostrarToast("Opciones guardadas correctamente");

            // Restaurar el color del botón después de guardar
            holder.btnPreguntas.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
        });
    }

    private String obtenerFechaActual() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onOpcionSelected(String preguntaNumero, List<Opcion> opcionesSeleccionadas) {
    }

    @Override
    public void onOpcionesSelected(String preguntaNumero, List<Opcion> opcionesSeleccionadas) {
    }

    @Override
    public void onOpcionSelected(Opcion opcionActual, Opcion opcionPotencial) {
    }

    private boolean isQuestionInDatabase(String nombreEmpresa, String preguntaNumero) {
        return dbHelper.isQuestionInDatabase(nombreEmpresa, preguntaNumero);
    }

    private String obtenerNombreEmpresaDesdePreferencias() {
        AppPreferences appPreferences = new AppPreferences(context);
        return appPreferences.getNombreEmpresa();
    }

    private void mostrarToast(String mensaje) {
        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
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
}

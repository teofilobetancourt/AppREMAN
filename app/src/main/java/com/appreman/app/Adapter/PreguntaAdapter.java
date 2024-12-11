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

    public PreguntaAdapter(Context context, List<Pregunta> items, String nombreEmpresa) {
        this.context = context;
        this.items = items;
        this.dbHelper = new DBHelper(context);
        this.nombreEmpresa = nombreEmpresa;
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

        holder.txtPregunta.setText(pregunta.getNumero().concat(".- ").concat(pregunta.getDescripcion()));

        List<Opcion> opciones = dbHelper.getOpcionesPregunta(pregunta.getNumero(), nombreEmpresa);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        holder.recycler.setLayoutManager(layoutManager);

        OpcionAdapter opcionAdapter = new OpcionAdapter(opciones, this);
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
            String nombreEmpresa = obtenerNombreEmpresaDesdePreferencias();
            String fechaRespuesta = obtenerFechaActual(); // Método para obtener la fecha actual

            if (!opcionesSeleccionadas.isEmpty()) {
                Opcion opcionActual = opcionesSeleccionadas.get(0);
                Opcion opcionPotencial = opcionesSeleccionadas.size() > 1 ? opcionesSeleccionadas.get(1) : opcionActual;

                if (isQuestionInDatabase(nombreEmpresa, preguntaNumero)) {
                    dbHelper.updateAnswerInDatabase(nombreEmpresa, preguntaNumero, opcionActual.getNumero(), opcionPotencial.getNumero());
                } else {
                    dbHelper.insertarOpcionesEnRespuestas(nombreEmpresa, preguntaNumero, opcionActual.getNumero(), opcionPotencial.getNumero(), fechaRespuesta);
                }

                preguntaOpcionAdapter.notifyDataSetChanged();

                mostrarToast("Opciones guardadas correctamente");

                Log.d(TAG, "Pregunta seleccionada: " + preguntaNumero);
                Log.d(TAG, "Nombre de la empresa seleccionada: " + nombreEmpresa);

                // Restaurar el color del botón después de guardar
                holder.btnPreguntas.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
            } else {
                Log.e(TAG, "No se pudieron obtener opciones seleccionadas.");
            }
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

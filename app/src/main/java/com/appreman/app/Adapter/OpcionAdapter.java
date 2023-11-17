package com.appreman.app.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appreman.app.Models.Opcion;
import com.appreman.app.Models.Seleccion;
import com.appreman.appreman.R;

import java.util.ArrayList;
import java.util.List;

public class OpcionAdapter extends RecyclerView.Adapter<OpcionAdapter.MotivosViewHolder> {

    private List<Opcion> items;
    private List<Seleccion> selecciones;

    public OpcionAdapter(List<Opcion> items) {
        this.items = items;
        this.selecciones = selecciones != null ? selecciones : new ArrayList<>();
    }

    @NonNull
    @Override
    public MotivosViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_opcion, viewGroup, false);
        return new MotivosViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull MotivosViewHolder holder, int position) {
        Opcion opcion = items.get(position);
        holder.txtOpcion.setText(opcion.getNumero().concat(".- ").concat(opcion.getNombre()));

        // Obtener las selecciones asociadas a esta opción
        List<Seleccion> seleccionesDeOpcion = getSeleccionesOpcion(opcion.getPregunta());

        // Configurar las selecciones debajo de la opción
        StringBuilder seleccionText = new StringBuilder("Selecciones:\n");
        for (Seleccion seleccion : seleccionesDeOpcion) {
            seleccionText.append(seleccion.getSeleccionUsuario()).append("\n");
        }
        holder.txtSeleccion.setText(seleccionText.toString());
    }


    // Método para obtener selecciones según el número de pregunta (o número de opción, si aplica)
    private List<Seleccion> getSeleccionesOpcion(String numeroPregunta) {
        List<Seleccion> seleccionesByPregunta = new ArrayList<>();

        // Llamada al método getSeleccionesPregunta para obtener las selecciones basadas en el número de pregunta
        seleccionesByPregunta = getSeleccionesByOpcion(numeroPregunta);

        return seleccionesByPregunta;
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    private List<Seleccion> getSeleccionesByOpcion(String opcionId) {
        List<Seleccion> seleccionesByOpcion = new ArrayList<>();
        for (Seleccion seleccion : selecciones) {
            if (seleccion.getId().equals(opcionId)) {
                seleccionesByOpcion.add(seleccion);
            }
        }
        return seleccionesByOpcion;
    }

    protected static class MotivosViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtOpcion;
        private final TextView txtSeleccion;

        public MotivosViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOpcion = itemView.findViewById(R.id.textNombre);
            txtSeleccion = itemView.findViewById(R.id.txtNombreSeleccion);
        }
    }
}

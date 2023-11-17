package com.appreman.app.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.appreman.app.Models.Seleccion;
import com.appreman.appreman.R;
import java.util.List;

public class SeleccionAdapter extends RecyclerView.Adapter<SeleccionAdapter.SeleccionViewHolder> {

    private List<Seleccion> selecciones;

    public SeleccionAdapter(List<Seleccion> selecciones) {
        this.selecciones = selecciones;
    }

    @NonNull
    @Override
    public SeleccionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_seleccion, parent, false);
        return new SeleccionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeleccionViewHolder holder, int position) {
        Seleccion seleccion = selecciones.get(position);

        // Aquí configura las vistas de tu diseño de elemento de selección
        holder.txtSeleccion.setText(seleccion.getNumeroPregunta()); // Cambia "getNombre()" por el método que accede al nombre de la selección
        // Agrega más configuraciones según tu diseño y datos de la selección
    }

    @Override
    public int getItemCount() {
        return selecciones == null ? 0 : selecciones.size();
    }

    static class SeleccionViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtSeleccion;

        SeleccionViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSeleccion = itemView.findViewById(R.id.txtNombreSeleccion);
        }
    }
}

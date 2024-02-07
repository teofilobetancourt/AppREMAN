package com.appreman.app.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appreman.app.Models.Opcion;
import com.appreman.appreman.R;

import java.util.ArrayList;
import java.util.List;

public class OpcionAdapter extends RecyclerView.Adapter<OpcionAdapter.MotivosViewHolder> {

    private final List<Opcion> items;

    public OpcionAdapter(List<Opcion> items) {
        this.items = items != null ? items : new ArrayList<>();
    }

    @NonNull
    @Override
    public MotivosViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_opcion, viewGroup, false);
        return new MotivosViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MotivosViewHolder holder, int position) {
        final Opcion opcion = items.get(position);

        // Modificar el texto para indicar la opción seleccionada
        if (opcion.isSeleccionada()) {
            holder.txtOpcion.setText(opcion.getNombreOpcion() + " : " + opcion.getNumero().concat(".- ").concat(opcion.getNombre()));
        } else {
            holder.txtOpcion.setText(opcion.getNumero().concat(".- ").concat(opcion.getNombre()));
        }

        holder.txtOpcion.setOnClickListener(v -> handleOptionSelection(opcion));

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(opcion.isSeleccionada());

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> handleOptionSelection(opcion));
    }

    @SuppressLint("NotifyDataSetChanged")
    private void handleOptionSelection(Opcion selectedOption) {
        int selectedCount = getSelectedCount();

        if (selectedCount == 2) {
            uncheckOldestSelectedOptions(selectedOption.getPregunta());
        }

        selectedOption.setSeleccionada(!selectedOption.isSeleccionada());

        if (selectedOption.isSeleccionada()) {
            if (selectedCount == 0) {
                selectedOption.setNombreOpcion("Actual");
            } else if (selectedCount == 1) {
                selectedOption.setNombreOpcion("Potencial");
            }
        } else {
            selectedOption.setNombreOpcion("");
        }

        notifyDataSetChanged();
    }

    private void uncheckOldestSelectedOptions(String pregunta) {
        for (Opcion option : items) {
            if (option.isSeleccionada() && option.getPregunta().equals(pregunta) && !option.getNombreOpcion().equals("Actual")) {
                option.setSeleccionada(false);
                option.setNombreOpcion("");
                notifyItemChanged(items.indexOf(option));
                return;
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    protected static class MotivosViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtOpcion;
        private final CheckBox checkBox;

        public MotivosViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOpcion = itemView.findViewById(R.id.textNombre);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }

    private int getSelectedCount() {
        int count = 0;
        for (Opcion opcion : items) {
            if (opcion.isSeleccionada()) {
                count++;
            }
        }
        return count;
    }

    public List<Opcion> obtenerOpcionesSeleccionadas() {
        List<Opcion> opcionesSeleccionadas = new ArrayList<>();

        for (Opcion opcion : items) {
            if (opcion.isSeleccionada()) {
                opcionesSeleccionadas.add(opcion);
            }
        }

        return opcionesSeleccionadas;
    }
}

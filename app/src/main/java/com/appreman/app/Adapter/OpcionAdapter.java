package com.appreman.app.Adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appreman.app.Activity.OpcionSelectionListener;
import com.appreman.app.Models.Opcion;
import com.appreman.appreman.R;

import java.util.ArrayList;
import java.util.List;

public class OpcionAdapter extends RecyclerView.Adapter<OpcionAdapter.MotivosViewHolder> {

    private final List<Opcion> items;
    private OpcionSelectionListener opcionSelectionListener;

    public OpcionAdapter(List<Opcion> items, OpcionSelectionListener opcionSelectionListener) {
        this.items = items != null ? items : new ArrayList<>();
        this.opcionSelectionListener = opcionSelectionListener;
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

        if (opcion.isSeleccionada()) {
            holder.txtOpcion.setText(opcion.getNombreOpcion() + " : " + opcion.getNumero().concat(".- ").concat(opcion.getNombre()));
        } else {
            holder.txtOpcion.setText(opcion.getNumero().concat(".- ").concat(opcion.getNombre()));
        }

        if (opcion.isRespondida()) {
            holder.itemView.setBackgroundColor(Color.LTGRAY);
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }

        holder.txtOpcion.setOnClickListener(v -> handleOptionSelection(opcion, holder));

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(opcion.isSeleccionada());

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> handleOptionSelection(opcion, holder));
    }

    @SuppressLint("NotifyDataSetChanged")
    private void handleOptionSelection(Opcion selectedOption, MotivosViewHolder holder) {
        int selectedCount = getSelectedCount();

        if (selectedCount == 2) {
            uncheckOldestSelectedOptions(selectedOption.getPregunta());
        }

        if (!selectedOption.isSeleccionada() || selectedOption.getNombreOpcion().isEmpty()) {
            selectedOption.setSeleccionada(!selectedOption.isSeleccionada());

            if (selectedOption.isSeleccionada()) {
                if (selectedCount == 0) {
                    selectedOption.setNombreOpcion("Actual");
                } else if (selectedCount == 1) {
                    selectedOption.setNombreOpcion("Potencial");
                } else {
                    // Si hay tres opciones seleccionadas, cambiar entre actual y potencial
                    Opcion actualAnterior = obtenerOpcionActualSeleccionada();
                    Opcion potencialAnterior = obtenerOpcionPotencialSeleccionada();

                    if (actualAnterior != null && actualAnterior.isSeleccionada()) {
                        actualAnterior.setSeleccionada(false);
                        actualAnterior.setNombreOpcion("");

                        selectedOption.setNombreOpcion("Actual");
                    } else if (potencialAnterior != null && potencialAnterior.isSeleccionada()) {
                        potencialAnterior.setSeleccionada(false);
                        potencialAnterior.setNombreOpcion("");

                        selectedOption.setNombreOpcion("Potencial");
                    }
                }
            } else {
                selectedOption.setNombreOpcion("");
            }

            notifyDataSetChanged();

            if (opcionSelectionListener != null) {
                opcionSelectionListener.onOpcionSelected(
                        obtenerOpcionActualSeleccionada(),
                        obtenerOpcionPotencialSeleccionada());
            }
        }
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

    public Opcion obtenerOpcionActualSeleccionada() {
        for (Opcion opcion : items) {
            if (opcion.isSeleccionada() && opcion.getNombreOpcion().equals("Actual")) {
                return opcion;
            }
        }
        return null;
    }

    public Opcion obtenerOpcionPotencialSeleccionada() {
        for (Opcion opcion : items) {
            if (opcion.isSeleccionada() && opcion.getNombreOpcion().equals("Potencial")) {
                return opcion;
            }
        }
        return null;
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

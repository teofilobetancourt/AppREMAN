package com.appreman.app.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appreman.app.Models.Opcion;
import com.appreman.appreman.R;

import java.util.ArrayList;
import java.util.List;

public class OpcionAdapter extends RecyclerView.Adapter<OpcionAdapter.MotivosViewHolder> {

    private List<Opcion> items;

    public OpcionAdapter(List<Opcion> items) {
        this.items = items != null ? items : new ArrayList<>();
    }

    @NonNull
    @Override
    public MotivosViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_opcion, viewGroup, false);
        return new MotivosViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MotivosViewHolder holder, int position) {
        final Opcion opcion = items.get(position);

        // Modificar el texto para indicar la opción seleccionada
        if (opcion.getTipo() == 1) {
            holder.txtOpcion.setText("Opción 1: " + opcion.getNumero().concat(".- ").concat(opcion.getNombre()));
        } else if (opcion.getTipo() == 2) {
            holder.txtOpcion.setText("Opción 2: " + opcion.getNumero().concat(".- ").concat(opcion.getNombre()));
        } else {
            holder.txtOpcion.setText(opcion.getNumero().concat(".- ").concat(opcion.getNombre()));
        }

        holder.txtOpcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOptionSelection(opcion);
            }
        });

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(opcion.isSeleccionada());

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                handleOptionSelection(opcion);
            }
        });
    }

    private void handleOptionSelection(Opcion selectedOption) {
        int selectedCount = getSelectedCount();

        if (selectedCount == 2) {
            uncheckOldestSelectedOptions();
        }

        if (!selectedOption.isSeleccionada() && selectedCount < 2) {
            selectedOption.setTipo(selectedCount + 1);
        }

        selectedOption.setSeleccionada(!selectedOption.isSeleccionada());

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
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

    private void uncheckOldestSelectedOptions() {
        int oldestType = 0;

        for (Opcion option : items) {
            if (option.isSeleccionada() && option.getTipo() == 1) {
                oldestType = 1;
                break;
            }
        }

        for (Opcion option : items) {
            if (option.isSeleccionada() && option.getTipo() == oldestType) {
                option.setSeleccionada(false);
                option.setTipo(0);
                notifyItemChanged(items.indexOf(option));
                return;
            }
        }
    }
}

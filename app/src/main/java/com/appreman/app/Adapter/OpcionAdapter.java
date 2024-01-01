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
import java.util.HashMap;
import java.util.List;

public class OpcionAdapter extends RecyclerView.Adapter<OpcionAdapter.MotivosViewHolder> {

    private List<Opcion> items;
    private HashMap<String, Integer> selectedCountMap;

    public OpcionAdapter(List<Opcion> items) {
        this.items = items != null ? items : new ArrayList<>();
        this.selectedCountMap = new HashMap<>();
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
        holder.txtOpcion.setText(opcion.getNumero().concat(".- ").concat(opcion.getNombre()));

        holder.txtOpcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.checkBox.setChecked(!holder.checkBox.isChecked());
            }
        });

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(opcion.isSeleccionada());

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int count = selectedCountMap.containsKey(opcion.getPregunta()) ? selectedCountMap.get(opcion.getPregunta()) : 0;

                if (isChecked && count >= 2) {
                    uncheckOldestSelectedOption();
                    opcion.setSeleccionada(true);
                } else {
                    opcion.setSeleccionada(isChecked);
                }

                if (isChecked) {
                    selectedCountMap.put(opcion.getPregunta(), count + 1);
                } else {
                    selectedCountMap.put(opcion.getPregunta(), count - 1);
                }
            }
        });
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
            checkBox = itemView.findViewById(R.id.checkBox); // Reemplaza R.id.checkBox con tu ID real de CheckBox
        }
    }

    private void uncheckOldestSelectedOption() {
        for (int i = 0; i < items.size(); i++) {
            Opcion opc = items.get(i);
            if (opc.isSeleccionada()) {
                opc.setSeleccionada(false);
                notifyItemChanged(i);
                return;
            }
        }
    }
}

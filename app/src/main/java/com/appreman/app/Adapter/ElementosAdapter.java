package com.appreman.app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appreman.app.Models.Elemento;
import com.appreman.appreman.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ElementosAdapter extends RecyclerView.Adapter<ElementosAdapter.ViewHolder> {
    private Context context;
    private List<Elemento> elementos;
    private Map<Integer, List<Elemento>> asignaciones;
    private int idOperadorSeleccionado;

    public ElementosAdapter(Context context, List<Elemento> elementos, Map<Integer, List<Elemento>> asignaciones, int idOperadorSeleccionado) {
        this.context = context;
        this.elementos = new ArrayList<>(elementos);
        this.asignaciones = asignaciones;
        this.idOperadorSeleccionado = idOperadorSeleccionado;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_elementos, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Elemento elemento = elementos.get(position);
        holder.textViewElementos.setText(elemento.toString());

        List<Elemento> elementosAsignados = asignaciones.getOrDefault(idOperadorSeleccionado, new ArrayList<>());

        // Desactivar temporalmente el listener para evitar bucles
        holder.checkBoxElementos.setOnCheckedChangeListener(null);

        // Establecer el estado correcto del CheckBox
        boolean estaAsignadoAOtro = false;

        for (Map.Entry<Integer, List<Elemento>> entry : asignaciones.entrySet()) {
            if (entry.getKey() != idOperadorSeleccionado && entry.getValue().contains(elemento)) {
                estaAsignadoAOtro = true;
                break;
            }
        }

        holder.checkBoxElementos.setChecked(elementosAsignados.contains(elemento));
        holder.checkBoxElementos.setEnabled(!estaAsignadoAOtro); // Deshabilitar si ya está asignado

        // Reactivar el listener
        holder.checkBoxElementos.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                desasignarElementoDeOtrosOperadores(elemento);
                elementosAsignados.add(elemento);
            } else {
                elementosAsignados.remove(elemento);
            }
            asignaciones.put(idOperadorSeleccionado, elementosAsignados);
            buttonView.post(() -> notifyDataSetChanged());
        });

        // Agregar OnClickListener al itemView para marcar/desmarcar el CheckBox
        holder.itemView.setOnClickListener(v -> {
            if (holder.checkBoxElementos.isEnabled()) {
                boolean isChecked = !holder.checkBoxElementos.isChecked();
                holder.checkBoxElementos.setChecked(isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return elementos.size();
    }

    public void actualizarLista(List<Elemento> nuevaLista, int idOperadorSeleccionado) {
        this.elementos.clear();
        this.elementos.addAll(nuevaLista);
        this.idOperadorSeleccionado = idOperadorSeleccionado;
        notifyDataSetChanged();
    }

    private void desasignarElementoDeOtrosOperadores(Elemento elemento) {
        for (Map.Entry<Integer, List<Elemento>> entry : asignaciones.entrySet()) {
            if (entry.getKey() != idOperadorSeleccionado) {
                entry.getValue().remove(elemento);
            }
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewElementos;
        CheckBox checkBoxElementos;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewElementos = itemView.findViewById(R.id.textViewElementos);
            checkBoxElementos = itemView.findViewById(R.id.checkBoxElementos);
        }
    }
}
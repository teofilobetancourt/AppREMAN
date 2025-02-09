package com.appreman.app.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appreman.app.Activity.OpcionSelectionListener;
import com.appreman.app.Models.Opcion;
import com.appreman.appreman.R;

import java.util.ArrayList;
import java.util.List;

public class OpcionAdapter extends RecyclerView.Adapter<OpcionAdapter.MotivosViewHolder> {

    private final List<Opcion> items;
    private final OpcionSelectionListener opcionSelectionListener;
    private final Context context;

    public OpcionAdapter(Context context, List<Opcion> items, OpcionSelectionListener opcionSelectionListener) {
        this.context = context;
        this.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
        this.opcionSelectionListener = opcionSelectionListener;
        agregarOpcionComentario();
    }

    private void agregarOpcionComentario() {
        Opcion opcionComentario = new Opcion();
        opcionComentario.setNumero("Comentario");
        opcionComentario.setNombre("Agregar comentario");
        opcionComentario.setPregunta("");
        opcionComentario.setSeleccionada(false);
        opcionComentario.setNombreOpcion("");
        opcionComentario.setRespondida(false);
        opcionComentario.setComentario("");
        items.add(opcionComentario);
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

        // Configurar el texto de la opción dependiendo de si es "Actual" o "Potencial"
        if (opcion.getNumero().equals("Comentario")) {
            holder.txtOpcion.setText(opcion.getNombre());
        } else if (opcion.isSeleccionada()) {
            holder.txtOpcion.setText(opcion.getNombreOpcion() + " : " + opcion.getNumero().concat(".- ").concat(opcion.getNombre()));
        } else {
            holder.txtOpcion.setText(opcion.getNumero().concat(".- ").concat(opcion.getNombre()));
        }

        // Configurar el fondo de la opción
        if (opcion.isRespondida()) {
            holder.itemView.setBackgroundColor(Color.LTGRAY);  // Fondo gris para opciones respondidas
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);  // Fondo transparente para las no respondidas
        }

        // Restablecer el OnCheckedChangeListener para evitar problemas al reciclar vistas
        holder.checkBox.setOnCheckedChangeListener(null);

        // Asegurarse de que el CheckBox refleje el estado correcto
        holder.checkBox.setChecked(opcion.isSeleccionada());

        // Manejar la selección al cambiar el estado del CheckBox
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> handleOptionSelection(opcion));

        // Manejar la selección al hacer clic en el texto de la opción
        holder.txtOpcion.setOnClickListener(v -> handleOptionSelection(opcion));
    }

    @SuppressLint("NotifyDataSetChanged")
    private void handleOptionSelection(Opcion selectedOption) {
        if (selectedOption.getNumero().equals("Comentario")) {
            mostrarDialogoComentario(selectedOption);
        } else {
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
                        Opcion actualAnterior = obtenerOpcionActualSeleccionada();
                        Opcion potencialAnterior = obtenerOpcionPotencialSeleccionada();

                        if (actualAnterior != null && actualAnterior.isSeleccionada()) {
                            actualAnterior.setSeleccionada(false);
                            actualAnterior.setNombreOpcion("");
                        }

                        if (potencialAnterior != null && potencialAnterior.isSeleccionada()) {
                            potencialAnterior.setSeleccionada(false);
                            potencialAnterior.setNombreOpcion("");
                        }

                        selectedOption.setNombreOpcion("Actual");
                    }
                } else {
                    selectedOption.setNombreOpcion("");
                }

                // Evitar que la palabra "Comentario" se guarde en las opciones "Actual" y "Potencial"
                if (selectedOption.getNumero().equals("Comentario")) {
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
    }

    private void mostrarDialogoComentario(Opcion opcion) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Agregar comentario");

        final EditText input = new EditText(context);
        input.setText(opcion.getComentario());
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String comentario = input.getText().toString();
            opcion.setComentario(comentario);
            opcion.setNombre("Comentario: " + comentario);
            opcion.setSeleccionada(true);
            notifyDataSetChanged();  // Notificar al adaptador que los datos han cambiado
            Toast.makeText(context, "Comentario guardado", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> {
            opcion.setComentario("");
            opcion.setNombre("Agregar comentario");
            opcion.setSeleccionada(false);
            notifyDataSetChanged();  // Notificar al adaptador que los datos han cambiado
            dialog.cancel();
        });

        builder.show();
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
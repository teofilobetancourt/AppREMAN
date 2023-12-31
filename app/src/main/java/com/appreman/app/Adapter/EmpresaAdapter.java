package com.appreman.app.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appreman.app.Models.Empresa;
import com.appreman.appreman.R;

import java.util.List;

public class EmpresaAdapter extends RecyclerView.Adapter<EmpresaAdapter.EmpresaViewHolder> {

    private List<Empresa> empresas;
    private EncuestaClickListener encuestaClickListener;

    public EmpresaAdapter(List<Empresa> empresas) {
        this.empresas = empresas;
    }

    @NonNull
    @Override
    public EmpresaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.empresa_item_layout, parent, false);
        return new EmpresaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmpresaViewHolder holder, int position) {
        Empresa empresa = empresas.get(position);
        holder.bindData(empresa);
    }

    @Override
    public int getItemCount() {
        return empresas.size();
    }

    public class EmpresaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewNombre, textViewPais, textViewRegion, textViewSitio, textViewSector, textViewPlanta, textViewRepresentante, textViewTelefono, textViewEmail, textViewClienteAct, textViewNumeroDePlant, textViewNumeroDePlantIm;
        TextView textViewFechaRegistro, textViewHoraRegistro;
        View buttonEncuesta;

        public EmpresaViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNombre = itemView.findViewById(R.id.textViewNombre);
            textViewPais = itemView.findViewById(R.id.textViewPais);
            textViewRegion = itemView.findViewById(R.id.textViewRegion);
            textViewSitio = itemView.findViewById(R.id.textViewSitio);
            textViewSector = itemView.findViewById(R.id.textViewSector);
            textViewPlanta = itemView.findViewById(R.id.textViewPlanta);
            textViewRepresentante = itemView.findViewById(R.id.textViewRepresentante);
            textViewTelefono = itemView.findViewById(R.id.textViewTelefono);
            textViewEmail = itemView.findViewById(R.id.textViewEmail);
            textViewClienteAct = itemView.findViewById(R.id.textViewClienteActual);
            textViewNumeroDePlant = itemView.findViewById(R.id.textViewNumeroDePlantas);
            textViewNumeroDePlantIm = itemView.findViewById(R.id.textViewPlantasImplementar);
            textViewFechaRegistro = itemView.findViewById(R.id.textViewFechaRegistro);
            textViewHoraRegistro = itemView.findViewById(R.id.textViewHoraRegistro);

            buttonEncuesta = itemView.findViewById(R.id.buttonEncuesta);
            buttonEncuesta.setOnClickListener(this);
        }

        public void bindData(Empresa empresa) {
            // Asignar valores a los TextView
            textViewNombre.setText(empresa.getNombre());
            textViewPais.setText(empresa.getPais());
            textViewRegion.setText(empresa.getRegion());
            textViewSitio.setText(empresa.getSitio());
            textViewSector.setText(empresa.getSector());
            textViewPlanta.setText(empresa.getPlanta());
            textViewRepresentante.setText(empresa.getRepresentante());
            textViewTelefono.setText(empresa.getTelefono());
            textViewEmail.setText(empresa.getEmail());
            textViewClienteAct.setText(empresa.getClienteAct());
            textViewNumeroDePlant.setText(empresa.getNumeroDePlant());
            textViewNumeroDePlantIm.setText(empresa.getNumeroDePlantIm());
            textViewFechaRegistro.setText("Fecha: " + empresa.getFechaRegistro());
            textViewHoraRegistro.setText("Hora: " + empresa.getHoraRegistro());
        }

        @Override
        public void onClick(View v) {
            // Maneja el clic del botón "Encuestar"
            if (v.getId() == R.id.buttonEncuesta && encuestaClickListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    // Notifica al listener sobre el clic del botón "Encuestar"
                    encuestaClickListener.onEncuestaClick(position);
                }
            }
        }
    }

    // Interfaz para manejar el clic del botón "Encuestar"
    public interface EncuestaClickListener {
        void onEncuestaClick(int position);
    }

    // Método para configurar el listener desde fuera del adaptador
    public void setEncuestaClickListener(EncuestaClickListener listener) {
        this.encuestaClickListener = listener;
    }
}

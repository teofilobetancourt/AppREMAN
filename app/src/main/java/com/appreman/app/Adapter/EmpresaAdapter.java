package com.appreman.app.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appreman.app.Models.Empresa;
import com.appreman.appreman.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class EmpresaAdapter extends RecyclerView.Adapter<EmpresaAdapter.EmpresaViewHolder> {

    private final List<Empresa> empresas;
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

    public class EmpresaViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNombre, textViewPais, textViewRegion, textViewSitio, textViewSector, textViewPlanta, textViewRepresentante, textViewTelefono, textViewEmail, textViewClienteAct, textViewNumeroDePlant, textViewNumeroDePlantIm;
        TextView textViewFechaRegistro, textViewHoraRegistro;
        TextView textViewPlantaLabel, textViewRepresentanteLabel, textViewTelefonoLabel, textViewEmailLabel,
                textViewClienteActLabel, textViewNumeroDePlantLabel, textViewNumeroDePlantImLabel,
                textViewFechaRegistroLabel, textViewHoraRegistroLabel;
        View buttonEncuesta;
        FloatingActionButton fabMostrarMas;

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

            textViewPlantaLabel = itemView.findViewById(R.id.textViewPlantaLabel);
            textViewRepresentanteLabel = itemView.findViewById(R.id.textViewRepresentanteLabel);
            textViewTelefonoLabel = itemView.findViewById(R.id.textViewTelefonoLabel);
            textViewEmailLabel = itemView.findViewById(R.id.textViewEmailLabel);
            textViewClienteActLabel = itemView.findViewById(R.id.textViewClienteActualLabel);
            textViewNumeroDePlantLabel = itemView.findViewById(R.id.textViewNumeroDePlantasLabel);
            textViewNumeroDePlantImLabel = itemView.findViewById(R.id.textViewPlantasImplementarLabel);

            buttonEncuesta = itemView.findViewById(R.id.buttonEncuesta);
            buttonEncuesta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (encuestaClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            encuestaClickListener.onEncuestaClick(position);
                        }
                    }
                }
            });

            fabMostrarMas = itemView.findViewById(R.id.fabMostrarMas);
            fabMostrarMas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Empresa empresa = empresas.get(getAdapterPosition());
                    empresa.toggleVisibilidadCamposAdicionales();
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }

        @SuppressLint("SetTextI18n")
        public void bindData(Empresa empresa) {
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

            updateVisibility(empresa);
        }

        private void updateVisibility(Empresa empresa) {
            int visibility = empresa.areCamposAdicionalesVisible() ? View.VISIBLE : View.GONE;
            textViewPlanta.setVisibility(visibility);
            textViewPlantaLabel.setVisibility(visibility);

            textViewRepresentante.setVisibility(visibility);
            textViewRepresentanteLabel.setVisibility(visibility);

            textViewTelefono.setVisibility(visibility);
            textViewTelefonoLabel.setVisibility(visibility);

            textViewEmail.setVisibility(visibility);
            textViewEmailLabel.setVisibility(visibility);

            textViewClienteAct.setVisibility(visibility);
            textViewClienteActLabel.setVisibility(visibility);

            textViewNumeroDePlant.setVisibility(visibility);
            textViewNumeroDePlantLabel.setVisibility(visibility);

            textViewNumeroDePlantIm.setVisibility(visibility);
            textViewNumeroDePlantImLabel.setVisibility(visibility);

        }
    }

    public interface EncuestaClickListener {
        void onEncuestaClick(int position);
    }

    public void setEncuestaClickListener(EncuestaClickListener listener) {
        this.encuestaClickListener = listener;
    }
}

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

public class EmpresaEncuestadaAdapter extends RecyclerView.Adapter<EmpresaEncuestadaAdapter.EmpresaViewHolder> {

    private final List<Empresa> empresasEncuestadas;
    private EncuestaClickListener encuestaClickListener;

    public EmpresaEncuestadaAdapter(List<Empresa> empresasEncuestadas) {
        this.empresasEncuestadas = empresasEncuestadas;
    }

    @NonNull
    @Override
    public EmpresaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.empresa_encuestada_item_layout, parent, false);
        return new EmpresaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmpresaViewHolder holder, int position) {
        Empresa empresa = empresasEncuestadas.get(position);
        holder.bindData(empresa);

    }

    @Override
    public int getItemCount() {
        return empresasEncuestadas.size();
    }

    public class EmpresaViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNombre, textViewPais, textViewRegion, textViewSitio;

        public EmpresaViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNombre = itemView.findViewById(R.id.textViewNombre);
            textViewPais = itemView.findViewById(R.id.textViewPais);
            textViewRegion = itemView.findViewById(R.id.textViewRegion);
            textViewSitio = itemView.findViewById(R.id.textViewSitio);
        }

        public void bindData(Empresa empresa) {
            textViewNombre.setText(empresa.getNombre());
            textViewPais.setText("País: " + empresa.getPais());
            textViewRegion.setText("Región: " + empresa.getRegion());
            textViewSitio.setText("Sitio: " + empresa.getSitio());
        }
    }

    public interface EncuestaClickListener {
        void onEncuestaClick(int position);
    }

    public void setEncuestaClickListener(EncuestaClickListener listener) {
        this.encuestaClickListener = listener;
    }
}

package com.appreman.app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appreman.app.Activity.MainActivity;
import com.appreman.app.Models.Empresa;
import com.appreman.appreman.R;

import java.util.List;

public class EmpresaEncuestadaAdapter extends RecyclerView.Adapter<EmpresaEncuestadaAdapter.EmpresaViewHolder> {

    private final List<Empresa> empresasEncuestadas;
    private final Context context;

    public EmpresaEncuestadaAdapter(Context context, List<Empresa> empresasEncuestadas) {
        this.context = context;
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

    public class EmpresaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewNombre, textViewPais, textViewRegion, textViewSitio;

        public EmpresaViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNombre = itemView.findViewById(R.id.textViewNombre);
            textViewPais = itemView.findViewById(R.id.textViewPais);
            textViewRegion = itemView.findViewById(R.id.textViewRegion);
            textViewSitio = itemView.findViewById(R.id.textViewSitio);
            itemView.setOnClickListener(this);
        }

        public void bindData(Empresa empresa) {
            textViewNombre.setText(empresa.getNombre());
            textViewPais.setText(empresa.getPais());
            textViewRegion.setText(empresa.getRegion());
            textViewSitio.setText(empresa.getSitio());
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Empresa empresa = empresasEncuestadas.get(position);
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("empresa_nombre", empresa.getNombre());
                intent.putExtra("pais", empresa.getPais());
                intent.putExtra("region", empresa.getRegion());
                intent.putExtra("sitio", empresa.getSitio());
                context.startActivity(intent);
            }
        }
    }
}
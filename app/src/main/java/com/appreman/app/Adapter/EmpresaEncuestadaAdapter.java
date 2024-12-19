package com.appreman.app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appreman.app.Database.DBHelper;
import com.appreman.app.Models.Empresa;
import com.appreman.appreman.R;

import java.util.List;
import java.util.Map;

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

    public class EmpresaViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNombre, textViewPais, textViewRegion, textViewSitio;
        TextView textViewRepresentante, textViewTipoPlanta, textViewEncuestadoPor;

        public EmpresaViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNombre = itemView.findViewById(R.id.textViewNombre);
            textViewPais = itemView.findViewById(R.id.textViewPais);
            textViewRegion = itemView.findViewById(R.id.textViewRegion);
            textViewSitio = itemView.findViewById(R.id.textViewSitio);
            textViewRepresentante = itemView.findViewById(R.id.textViewRepresentante);
            textViewTipoPlanta = itemView.findViewById(R.id.textViewTipoPlanta);
            textViewEncuestadoPor = itemView.findViewById(R.id.textViewEncuestadoPor);
        }

        public void bindData(Empresa empresa) {
            textViewNombre.setText(empresa.getNombre());
            textViewPais.setText(empresa.getPais());
            textViewRegion.setText(empresa.getRegion());
            textViewSitio.setText(empresa.getSitio());
            textViewRepresentante.setText(empresa.getRepresentante());
            textViewTipoPlanta.setText(empresa.getPlanta());

            // Obtener el nombre y apellido del operador
            DBHelper dbHelper = new DBHelper(itemView.getContext());
            Map<String, String> nombreApellido = dbHelper.getNombreApellidoPorIdOperador(empresa.getIdOperador());
            if (nombreApellido != null) {
                String nombreCompleto = nombreApellido.get("nombre") + " " + nombreApellido.get("apellido");
                textViewEncuestadoPor.setText(nombreCompleto);
            } else {
                textViewEncuestadoPor.setText("Operador desconocido");
            }
        }
    }
}
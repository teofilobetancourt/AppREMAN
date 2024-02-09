package com.appreman.app.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appreman.app.Database.DBHelper;
import com.appreman.app.Models.Elemento;
import com.appreman.app.Models.Pregunta;
import com.appreman.app.Repository.AppPreferences;
import com.appreman.appreman.R;

import java.util.List;

public class ElementoAdapter extends RecyclerView.Adapter<ElementoAdapter.MotivosViewHolder> {

    private final List<Elemento> items;
    private final Context context;
    private final SharedPreferences sharedPreferences;

    public ElementoAdapter(List<Elemento> items, Context context) {
        this.items = items;
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(AppPreferences.APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public MotivosViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_elemento, viewGroup, false);
        return new MotivosViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MotivosViewHolder holder, final int i) {
        holder.txtElemento.setText(items.get(i).getNumero().concat(".- ").concat(items.get(i).getNombre()));

        // Obtén el nombre de la empresa desde SharedPreferences
        String nombreEmpresa = obtenerNombreEmpresaDesdePreferencias();

        DBHelper v_db_helper = new DBHelper(context);
        List<Pregunta> mPreguntas = v_db_helper.getPreguntasElemento(items.get(i).getNumero());

        RecyclerView.LayoutManager lManager = new LinearLayoutManager(context);
        holder.recycler.setLayoutManager(lManager);

        // Utiliza el adaptador de PreguntaAdapter corregido
        PreguntaAdapter adapter = new PreguntaAdapter(context, mPreguntas, nombreEmpresa);
        holder.recycler.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private String obtenerNombreEmpresaDesdePreferencias() {
        return sharedPreferences.getString(AppPreferences.KEY_NOMBRE_EMPRESA, "");
    }

    protected static class MotivosViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtElemento;
        private final RecyclerView recycler;

        public MotivosViewHolder(@NonNull View itemView) {
            super(itemView);
            txtElemento = itemView.findViewById(R.id.textNombre);
            recycler = itemView.findViewById(R.id.recyclerview);
        }
    }
}

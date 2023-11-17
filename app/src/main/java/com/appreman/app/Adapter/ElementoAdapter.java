package com.appreman.app.Adapter;



import android.annotation.SuppressLint;
import android.content.Context;
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
import com.appreman.appreman.R;


import java.util.ArrayList;
import java.util.List;


public class ElementoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {


    private static final String TAG = "RMAN";

    private List<Elemento> items;


    @SuppressLint("StaticFieldLeak")
    static Context context;

    private int mPosition;

    public ElementoAdapter(List<Elemento> items) {
        this.items = items;
    }

    public List<Elemento>  getElementos() {
        return items;
    }

    public void setElementos(ArrayList<Elemento> elemento) {
        this.items = elemento;
    }


    public int getmPosition() {
        return mPosition;
    }


    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public Elemento getItem(int position) {
        return items.get(position);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        context = viewGroup.getContext();

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_elemento, viewGroup, false);


        return new MotivosViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {

        final MotivosViewHolder holder = (MotivosViewHolder) viewHolder;

        holder.txtElemento.setText(items.get(i).getNumero().concat(".- ").concat(items.get(i).getNombre()));

        DBHelper v_db_helper = new DBHelper(context);

        List<Pregunta> mPreguntas = v_db_helper.getPreguntasElemento(items.get(i).getNumero());

        holder.recycler.setHasFixedSize(true);
        RecyclerView.LayoutManager lManager = new LinearLayoutManager(context);
        holder.recycler.setLayoutManager(lManager);

        PreguntaAdapter adapter = new PreguntaAdapter(mPreguntas);

        holder.recycler.setAdapter(adapter);


        }



    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }


    protected static class MotivosViewHolder extends RecyclerView.ViewHolder{

        private final TextView txtElemento;
        private final RecyclerView recycler;

        public MotivosViewHolder(@NonNull View itemView) {
            super(itemView);

            txtElemento = itemView.findViewById(R.id.textNombre);
            recycler = itemView.findViewById(R.id.recyclerview);


        }
    }

}

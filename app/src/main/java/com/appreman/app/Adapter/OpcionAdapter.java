package com.appreman.app.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appreman.app.Models.Opcion;
import com.appreman.app.Models.Pregunta;
import com.appreman.appreman.R;

import java.util.ArrayList;
import java.util.List;


public class OpcionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {


    private static final String TAG = "RMAN";

    private List<Opcion> items;

    private View.OnClickListener mClickListener;



    private int lastSelectedPosition = 0;


    @SuppressLint("StaticFieldLeak")
    static Context context;

    private int mPosition;

    public OpcionAdapter(List<Opcion> items) {
        this.items = items;
    }

    public List<Opcion>  getOpciones() {
        return items;
    }

    public void setOpciones(ArrayList<Opcion> opcion) {
        this.items = opcion;
    }


    public int getmPosition() {
        return mPosition;
    }


    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public Opcion getItem(int position) {
        return items.get(position);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_opcion, viewGroup, false);

        return new MotivosViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {

        final MotivosViewHolder holder = (MotivosViewHolder) viewHolder;

        holder.txtOpcion.setText(items.get(i).getNumero().concat(".- ").concat(items.get(i).getNombre()));
        holder.radioButton.setChecked(lastSelectedPosition == i);

        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPosition = holder.getLayoutPosition();
                //mClickListener.onClick(v);
                lastSelectedPosition = mPosition;
                notifyDataSetChanged();
            }
        });


        }



    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }


    protected static class MotivosViewHolder extends RecyclerView.ViewHolder{

        private final TextView txtOpcion;
        private final RadioButton radioButton;

        public MotivosViewHolder(@NonNull View itemView) {
            super(itemView);

            txtOpcion = itemView.findViewById(R.id.textNombre);
            radioButton = itemView.findViewById(R.id.radioButton);

        }


    }

    public void setClickListener(View.OnClickListener callback) {
        mClickListener = callback;
    }

}

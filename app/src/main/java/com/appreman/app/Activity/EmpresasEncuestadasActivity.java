package com.appreman.app.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.appreman.app.Adapter.EmpresaEncuestadaAdapter;
import com.appreman.app.Database.DBHelper;
import com.appreman.app.Models.Empresa;
import com.appreman.appreman.R;

import java.util.List;

public class EmpresasEncuestadasActivity extends AppCompatActivity {

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresas_encuestadas);

        dbHelper = new DBHelper(this);

        List<Empresa> empresasEncuestadas = dbHelper.getEmpresasEnRespuestas();

        RecyclerView recyclerView = findViewById(R.id.recyclerViewEmpresasEncuestadasAdapter);
        EmpresaEncuestadaAdapter adapter = new EmpresaEncuestadaAdapter(empresasEncuestadas);
        recyclerView.setAdapter(adapter);
    }
}

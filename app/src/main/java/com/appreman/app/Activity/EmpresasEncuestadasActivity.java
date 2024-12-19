package com.appreman.app.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

        // Configurar el Toolbar
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Toolbar toolbar = findViewById(R.id.toolbarEmpresasEncuestadas);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            SpannableString spannableTitle = getSpannableTitle("Empresas encuestadas");
            getSupportActionBar().setTitle(spannableTitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationIcon(R.drawable.arrow_back);
        }

        dbHelper = new DBHelper(this);

        List<Empresa> empresasEncuestadas = dbHelper.getEmpresasEnRespuestas();

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) RecyclerView recyclerView = findViewById(R.id.recyclerViewEmpresasEncuestadasAdapter);
        EmpresaEncuestadaAdapter adapter = new EmpresaEncuestadaAdapter(this, empresasEncuestadas);
        recyclerView.setAdapter(adapter);
    }

    private SpannableString getSpannableTitle(String title) {
        SpannableString spannable = new SpannableString(title);
        spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), 0, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Cierra la actividad actual y vuelve a la anterior
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

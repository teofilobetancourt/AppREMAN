package com.appreman.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.appreman.app.Adapter.ViewPagerAdapter;
import com.appreman.app.Database.DBHelper;
import com.appreman.app.Fragments.ElementosFragment;
import com.appreman.app.Models.Grupo;
import com.appreman.app.Models.Pregunta;
import com.appreman.app.Repository.AppPreferences;
import com.appreman.appreman.databinding.ActivityEncuestasBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class EncuestasActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private DBHelper dbHelper;

    public static void start(Context context) {
        Intent intent = new Intent(context, EncuestasActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityEncuestasBinding binding = ActivityEncuestasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitle(getTitle());

        viewPager = binding.viewPager;
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        dbHelper = new DBHelper(getApplicationContext());

        List<Grupo> grupos = dbHelper.getAllGrupos();

        for (Grupo grupo : grupos) {
            ElementosFragment fragment = ElementosFragment.newInstance(grupo.getNumero());
            viewPagerAdapter.addFragment(fragment, grupo.getNombre());
        }

        viewPager.setAdapter(viewPagerAdapter);
        binding.tabLayoutGrupos.setupWithViewPager(viewPager);

        viewPager.setCurrentItem(0);

        binding.tabLayoutGrupos.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        String nombreEmpresa = obtenerNombreEmpresaDesdePreferencias();

        FloatingActionButton fabEncuestar = binding.fabEncuestar;
        fabEncuestar.setOnClickListener(view -> {
            insertarNombreEmpresaEnBD(nombreEmpresa);

            guardarPreguntasParaEmpresa(nombreEmpresa);

            mostrarToast();
        });
    }

    private String obtenerNombreEmpresaDesdePreferencias() {
        AppPreferences appPreferences = new AppPreferences(this);
        return appPreferences.getNombreEmpresa();
    }

    private void insertarNombreEmpresaEnBD(String nombreEmpresa) {
        if (nombreEmpresa != null) {
            dbHelper.insertarNombreEmpresaEnRespuestas(nombreEmpresa);
            Log.d("EncuestasActivity", "Nombre de Empresa almacenado en respuestas: " + nombreEmpresa);
        } else {
            Log.d("EncuestasActivity", "No se pudo almacenar el nombre de empresa en respuestas. Nombre no válido.");
        }
    }

    private void guardarPreguntasParaEmpresa(String nombreEmpresa) {
        List<Pregunta> preguntas = dbHelper.getAllPreguntas();
        for (Pregunta pregunta : preguntas) {
            dbHelper.insertarPreguntaEnRespuestas(nombreEmpresa, pregunta.getNumero());
        }
    }

    private void mostrarToast() {
        Toast.makeText(this, "Nombre de empresa y preguntas guardados correctamente", Toast.LENGTH_SHORT).show();
    }
}

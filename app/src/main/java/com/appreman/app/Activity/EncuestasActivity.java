package com.appreman.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.appreman.app.Adapter.ViewPagerAdapter;
import com.appreman.app.Database.DBHelper;
import com.appreman.app.Fragments.ElementosFragment;
import com.appreman.app.Models.Grupo;
import com.appreman.appreman.databinding.ActivityEncuestasBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class EncuestasActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private DBHelper dbHelper;
    private String nombreEmpresa;

    public static void start(Context context, String nombreEmpresa) {
        Intent intent = new Intent(context, EncuestasActivity.class);
        intent.putExtra("nombre_empresa", nombreEmpresa);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Utiliza View Binding para inflar el diseño de la actividad
        ActivityEncuestasBinding binding = ActivityEncuestasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        TabLayout tabLayout = binding.tabLayoutGrupos;
        viewPager = binding.viewPager;

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        dbHelper = new DBHelper(getApplicationContext());

        // Agregado: Log para verificar que la base de datos se abrió correctamente
        Log.d("EncuestasActivity", "Base de datos abierta correctamente");

        List<Grupo> grupos = dbHelper.getAllGrupos();

        for (Grupo grupo : grupos) {
            ElementosFragment fragment = ElementosFragment.newInstance(grupo.getNumero());
            viewPagerAdapter.addFragment(fragment, grupo.getNombre());
        }

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setCurrentItem(0);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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

        // Cambiado: Obtener el nombre de la empresa desde el Intent
        obtenerNombreEmpresaDesdeIntent();

        // Configurar el OnClickListener del botón flotante
        FloatingActionButton fabEncuestar = binding.fabEncuestar;
        fabEncuestar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Cambiado: Insertar el nombre de la empresa en la base de datos
                insertarNombreEmpresaEnBD();

                // Mostrar un Toast indicando que los datos se han guardado correctamente
                mostrarToast("Nombre de empresa guardado correctamente");
            }
        });
    }

    private void obtenerNombreEmpresaDesdeIntent() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("nombre_empresa")) {
            nombreEmpresa = intent.getStringExtra("nombre_empresa");
            Log.d("EncuestasActivity", "Nombre de empresa obtenido: " + nombreEmpresa);
        } else {
            nombreEmpresa = null;
            Log.d("EncuestasActivity", "No se pudo obtener el nombre de empresa desde el Intent");
        }
    }

    private void insertarNombreEmpresaEnBD() {
        // Agregado: Log para verificar el valor de nombreEmpresa antes de la inserción
        Log.d("EncuestasActivity", "Nombre de empresa antes de la inserción: " + nombreEmpresa);

        // Cambiado: Verificar si el nombre de la empresa es válido antes de la inserción
        if (nombreEmpresa != null) {
            // Insertar el nombre de la empresa en la base de datos en la tabla "respuestas" y columna "empresa"
            dbHelper.insertarNombreEmpresaEnRespuestas(nombreEmpresa);

            // Agregado: Log después de la inserción
            Log.d("EncuestasActivity", "Nombre de Empresa almacenado en respuestas: " + nombreEmpresa);
        } else {
            // Manejar el caso de nombre de empresa no válido
            Log.d("EncuestasActivity", "No se pudo almacenar el nombre de empresa en respuestas. Nombre no válido.");
        }
    }

    private void mostrarToast(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }
}

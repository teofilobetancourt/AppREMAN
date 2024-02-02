package com.appreman.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.appreman.app.Adapter.OpcionAdapter;
import com.appreman.app.Adapter.ViewPagerAdapter;
import com.appreman.app.Database.DBHelper;
import com.appreman.app.Fragments.ElementosFragment;
import com.appreman.app.Models.Grupo;
import com.appreman.app.Models.Opcion;
import com.appreman.app.Models.Pregunta;
import com.appreman.appreman.R;
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

        // Configurar la barra de herramientas
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitle(getTitle());

        // Configurar el ViewPager y el TabLayout
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

        // Obtener el nombre de la empresa desde el Intent
        obtenerNombreEmpresaDesdeIntent();

        // Configurar el OnClickListener del botón flotante
        FloatingActionButton fabEncuestar = binding.fabEncuestar;
        fabEncuestar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Insertar el nombre de la empresa en la base de datos
                insertarNombreEmpresaEnBD();

                // Obtener la lista de preguntas de la base de datos y guardarlas para la empresa actual
                guardarPreguntasParaEmpresa();

                // Guardar las opciones seleccionadas
                guardarOpcionesSeleccionadas();

                // Mostrar un Toast indicando que los datos se han guardado correctamente
                mostrarToast("Nombre de empresa, preguntas y opciones guardados correctamente");
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
        if (nombreEmpresa != null) {
            dbHelper.insertarNombreEmpresaEnRespuestas(nombreEmpresa);
            Log.d("EncuestasActivity", "Nombre de Empresa almacenado en respuestas: " + nombreEmpresa);
        } else {
            Log.d("EncuestasActivity", "No se pudo almacenar el nombre de empresa en respuestas. Nombre no válido.");
        }
    }

    private void guardarPreguntasParaEmpresa() {
        List<Pregunta> preguntas = dbHelper.getAllPreguntas();
        for (Pregunta pregunta : preguntas) {
            dbHelper.insertarPreguntaEnRespuestas(nombreEmpresa, pregunta.getNumero());
        }
    }

    private void guardarOpcionesSeleccionadas() {
        OpcionAdapter opcionAdapter = obtenerOpcionAdapter();

        if (opcionAdapter != null) {
            List<Opcion> opcionesSeleccionadas = opcionAdapter.obtenerOpcionesSeleccionadas();

            for (Opcion opcion : opcionesSeleccionadas) {
                // Imprime logs para verificar los valores antes de la inserción
                Log.d("EncuestasActivity", "Opcion seleccionada: " + opcion.getNombreOpcion());

                dbHelper.insertarOpcionEnRespuestas(
                        nombreEmpresa,
                        opcion.getPregunta(),
                        opcion.getNumero(),      // Número de la opción
                        opcion.getNombreOpcion(), // Nombre de la opción
                        opcion.getNumero(),      // Número de la opción
                        opcion.getNombreOpcion()  // Nombre de la opción
                );
            }
        }
    }

    private OpcionAdapter obtenerOpcionAdapter() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.view_pager + ":" + viewPager.getCurrentItem());

        if (fragment instanceof ElementosFragment) {
            ElementosFragment elementosFragment = (ElementosFragment) fragment;
            return elementosFragment.obtenerOpcionAdapter();
        }

        return null;
    }

    private void mostrarToast(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }
}

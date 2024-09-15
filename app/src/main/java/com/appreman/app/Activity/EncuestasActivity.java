package com.appreman.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
<<<<<<< HEAD
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
=======

>>>>>>> a21008206cf1f372d46ed21e6732f650f9060c30
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.appreman.app.Adapter.ViewPagerAdapter;
import com.appreman.app.Database.DBHelper;
import com.appreman.app.Fragments.ElementosFragment;
import com.appreman.app.Models.Grupo;
<<<<<<< HEAD
import com.appreman.app.Repository.AppPreferences;
import com.appreman.appreman.R;
import com.appreman.appreman.databinding.ActivityEncuestasBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
=======
import com.appreman.appreman.databinding.ActivityEncuestasBinding;
>>>>>>> a21008206cf1f372d46ed21e6732f650f9060c30
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class EncuestasActivity extends AppCompatActivity {

    private ViewPager viewPager;
<<<<<<< HEAD
    private DBHelper dbHelper;
=======
>>>>>>> a21008206cf1f372d46ed21e6732f650f9060c30

    public static void start(Context context) {
        Intent intent = new Intent(context, EncuestasActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityEncuestasBinding binding = ActivityEncuestasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

<<<<<<< HEAD
        // Configura la barra de herramientas
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitle(getTitle());

        // Inicializa los componentes de la vista
        viewPager = binding.viewPager;
        FloatingActionButton fabNotification = binding.fabNotification; // Suponiendo que fabNotification está en el binding

        // Configura el adaptador para el ViewPager
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        dbHelper = new DBHelper(getApplicationContext());

        // Obtiene la lista de grupos desde la base de datos
        List<Grupo> grupos = dbHelper.getAllGrupos();

        // Añade los fragmentos al adaptador
=======
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitle(getTitle());

        viewPager = binding.viewPager;
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        DBHelper dbHelper = new DBHelper(getApplicationContext());

        List<Grupo> grupos = dbHelper.getAllGrupos();

>>>>>>> a21008206cf1f372d46ed21e6732f650f9060c30
        for (Grupo grupo : grupos) {
            ElementosFragment fragment = ElementosFragment.newInstance(grupo.getNumero());
            viewPagerAdapter.addFragment(fragment, grupo.getNombre());
        }

<<<<<<< HEAD
        // Configura el ViewPager y TabLayout
        viewPager.setAdapter(viewPagerAdapter);
        binding.tabLayoutGrupos.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);

        // Configura el listener para cambios en las pestañas
=======
        viewPager.setAdapter(viewPagerAdapter);
        binding.tabLayoutGrupos.setupWithViewPager(viewPager);

        viewPager.setCurrentItem(0);

>>>>>>> a21008206cf1f372d46ed21e6732f650f9060c30
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

<<<<<<< HEAD
        // Configura el listener para el FloatingActionButton
        fabNotification.setOnClickListener(v -> showGroupInfoDialog());
    }

    private void showGroupInfoDialog() {
        int currentItem = viewPager.getCurrentItem(); // Obtiene el índice del grupo actual
        Grupo currentGrupo = dbHelper.getGrupoByPosition(currentItem); // Obtiene el grupo basado en el índice

        if (currentGrupo == null) {
            Log.e("EncuestasActivity", "Grupo no encontrado para la posición: " + currentItem);
            return;
        }

        // Obtén el nombre de la empresa desde AppPreferences
        AppPreferences appPreferences = new AppPreferences(this);
        String nombreEmpresa = appPreferences.getNombreEmpresa();

        // Aquí obtén los datos reales basados en currentItem
        int totalQuestions = dbHelper.getTotalQuestionsForGrupo(currentGrupo.getNumero(), nombreEmpresa);
        int questionsPerElement = dbHelper.getTotalElementsForGrupo(currentGrupo.getNumero());
        int answeredQuestions = dbHelper.getAnsweredQuestionsForGrupo(currentGrupo.getNumero(), nombreEmpresa);
        int unansweredQuestions = totalQuestions - answeredQuestions;

        // Infla el diseño del diálogo
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_group_info, null);

        // Configura los valores en los TextViews
        TextView totalQuestionsView = dialogView.findViewById(R.id.text_total_questions);
        TextView elementQuestionsView = dialogView.findViewById(R.id.text_element_questions);
        TextView answeredQuestionsView = dialogView.findViewById(R.id.text_answered_questions);
        TextView unansweredQuestionsView = dialogView.findViewById(R.id.text_unanswered_questions);

        totalQuestionsView.setText("Total de Preguntas: " + totalQuestions);
        elementQuestionsView.setText("Numero de Elementos: " + questionsPerElement);
        answeredQuestionsView.setText("Preguntas Respondidas: " + answeredQuestions);
        unansweredQuestionsView.setText("Preguntas por Responder: " + unansweredQuestions);

        // Crea y muestra el diálogo
        new AlertDialog.Builder(this)
                .setTitle("Información del Grupo")
                .setView(dialogView)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }


=======
    }
>>>>>>> a21008206cf1f372d46ed21e6732f650f9060c30
}

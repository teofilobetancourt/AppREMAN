package com.appreman.app.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.appreman.app.Adapter.ViewPagerAdapter;
import com.appreman.app.Database.DBHelper;
import com.appreman.app.Email.Credentials;
import com.appreman.app.Email.MailSender;
import com.appreman.app.Fragments.ElementosFragment;
import com.appreman.app.Models.Grupo;
import com.appreman.app.Repository.AppPreferences;
import com.appreman.appreman.R;
import com.appreman.appreman.databinding.ActivityEncuestasBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EncuestasActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private DBHelper dbHelper;
    private String nombreEmpresa;
    private Spinner spinner;
    private String email;


    public static void start(Context context) {
        Intent intent = new Intent(context, EncuestasActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityEncuestasBinding binding = ActivityEncuestasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configura la barra de herramientas
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitle(getTitle());

        // Obtener el email desde el Intent
        email = getIntent().getStringExtra("email");
        Log.d("EncuestasActivity", "Email obtenido en EncuestasActivity es: " + email);

        // Inicializa los componentes de la vista
        viewPager = binding.viewPager;
        FloatingActionButton fabNotification = binding.fabNotification; // Suponiendo que fabNotification está en el binding

        // Configura el adaptador para el ViewPager
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        dbHelper = new DBHelper(getApplicationContext());

        // Obtiene la lista de grupos desde la base de datos
        List<Grupo> grupos = dbHelper.getAllGrupos();

        // Añade los fragmentos al adaptador
        for (Grupo grupo : grupos) {
            ElementosFragment fragment = ElementosFragment.newInstance(grupo.getNumero());
            viewPagerAdapter.addFragment(fragment, grupo.getNombre());
        }

        // Configura el ViewPager y TabLayout
        viewPager.setAdapter(viewPagerAdapter);
        binding.tabLayoutGrupos.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);

        // Configura el listener para cambios en las pestañas
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

        // Configura el listener para el FloatingActionButton
        fabNotification.setOnClickListener(v -> showGroupInfoDialog());

        // Obtén el nombre de la empresa desde AppPreferences
        AppPreferences appPreferences = new AppPreferences(this);
        nombreEmpresa = appPreferences.getNombreEmpresa();

        // Registrar la fecha y hora de inicio
        dbHelper.insertarTiempo(nombreEmpresa, true);

        // Enviar correo electrónico al iniciar la encuesta
        String subject = "Inicio de Encuesta";
        String messageBody = "Se ha iniciado una nueva encuesta para: " + nombreEmpresa +
                "\nFecha y hora de inicio: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        String emailOperador = email; // Obtén el email del operador
        sendEmail(subject, messageBody, emailOperador);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Registrar la fecha y hora de fin
        String fechaFin = dbHelper.insertarTiempo(nombreEmpresa, false);
        int respuestasCount = dbHelper.getRespuestasCount(nombreEmpresa);
        // Enviar correo electrónico al finalizar la encuesta
        String subject = "Fin de Encuesta";
        String messageBody = "Se ha finalizado la encuesta para la empresa: " + nombreEmpresa +
                "\nFecha y hora de fin: " + fechaFin +
                "\nNúmero de preguntas respondidas: " + respuestasCount;
        String emailOperador = email; // Obtén el email del operador
        sendEmail(subject, messageBody, emailOperador);
    }

    private void showGroupInfoDialog() {
        int currentItem = viewPager.getCurrentItem(); // Obtiene el índice del grupo actual
        Grupo currentGrupo = dbHelper.getGrupoByPosition(currentItem); // Obtiene el grupo basado en el índice

        if (currentGrupo == null) {
            Log.e("EncuestasActivity", "Grupo no encontrado para la posición: " + currentItem);
            return;
        }

        // Aquí obtén los datos reales basados en currentItem
        int totalQuestions = dbHelper.getTotalQuestionsForGrupo(currentGrupo.getNumero(), nombreEmpresa);
        int questionsPerElement = dbHelper.getTotalElementsForGrupo(currentGrupo.getNumero());
        int answeredQuestions = dbHelper.getAnsweredQuestionsForGrupo(currentGrupo.getNumero(), nombreEmpresa);
        int unansweredQuestions = totalQuestions - answeredQuestions;

        // Infla el diseño del diálogo
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_group_info, null);

        // Configura los valores en los TextViews
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView empresaNameView = dialogView.findViewById(R.id.text_empresa_name);
        TextView totalQuestionsView = dialogView.findViewById(R.id.text_total_questions);
        TextView elementQuestionsView = dialogView.findViewById(R.id.text_element_questions);
        TextView answeredQuestionsView = dialogView.findViewById(R.id.text_answered_questions);
        TextView unansweredQuestionsView = dialogView.findViewById(R.id.text_unanswered_questions);

        empresaNameView.setText("Nombre de la Empresa: " + nombreEmpresa);
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

    private void sendEmail(String subject, String messageBody, String recipient) {
        new Thread(() -> {
            try {
                MailSender mailSender = new MailSender();
                mailSender.sendMail(recipient, subject, messageBody, this); // Asegúrate de que sendMail acepte estos parámetros
                Log.d("sendEmail", "Correo enviado exitosamente a: " + recipient);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("sendEmail", "Error al enviar el correo: " + e.getMessage());
            }
        }).start();
    }
}

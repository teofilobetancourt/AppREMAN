package com.appreman.app.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.appreman.app.Adapter.ViewPagerAdapter;
import com.appreman.app.Database.DBHelper;
import com.appreman.app.Email.Credentials;
import com.appreman.app.Email.MailSender;
import com.appreman.app.Fragments.ElementosFragment;
import com.appreman.app.Models.Elemento;
import com.appreman.app.Models.Grupo;
import com.appreman.app.Repository.AppPreferences;
import com.appreman.appreman.R;
import com.appreman.appreman.databinding.ActivityEncuestasBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;


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

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitle(getTitle());

        // Obtener los datos del Intent
        String nombreEmpresa = getIntent().getStringExtra("empresa_nombre"); // Obtener el nombre de la empresa
        int idEmpresa = getIntent().getIntExtra("id_empresa", -1); // Obtener el ID de la empresa (por defecto -1 si no existe)
        int idOperador = getIntent().getIntExtra("id_operador", -1);  // Usamos -1 como valor predeterminado
        String email = getIntent().getStringExtra("email"); // Obtener el email

        // Mostrar los datos recibidos por log para verificar
        Log.d("EncuestasActivity", "Nombre de la empresa: " + nombreEmpresa);
        Log.d("EncuestasActivity", "ID de la empresa: " + idEmpresa);
        Log.d("EncuestasActivity", "ID del operador: " + idOperador);
        Log.d("EncuestasActivity", "Email: " + email);

        // Continuar con el resto de la configuración de la actividad

        viewPager = binding.viewPager;
        FloatingActionButton fabNotification = binding.fabNotification;

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        dbHelper = new DBHelper(getApplicationContext());

        // Obtener la lista de elementos asignados a este operador y empresa
        List<Elemento> elementos = dbHelper.getElementosAsignados(idOperador, idEmpresa);

        // Verificar si la lista de elementos está vacía
        if (elementos.isEmpty()) {
            Log.d("EncuestasActivity", "No se encontraron elementos asignados.");
        } else {
            Log.d("EncuestasActivity", "Se encontraron " + elementos.size() + " elementos.");
        }

        // Usar un TreeMap para agrupar los elementos por su número completo (por ejemplo, "1.1", "1.2", "2.1")
        Map<String, List<Elemento>> elementosAgrupados = new TreeMap<>();

        for (Elemento elemento : elementos) {
            String claveGrupo = elemento.getNumero();  // El número completo como String (por ejemplo, "1.1", "1.2")
            elementosAgrupados.putIfAbsent(claveGrupo, new ArrayList<>());
            elementosAgrupados.get(claveGrupo).add(elemento);

            // Log para cada elemento agregado
            Log.d("EncuestasActivity", "Elemento agregado: " + elemento.getNombre() + ", Grupo: " + claveGrupo);
        }

        // Verificar cuántos grupos se han creado
        Log.d("EncuestasActivity", "Número de grupos creados: " + elementosAgrupados.size());

        // Agregar los fragmentos por cada grupo de elementos
        for (Map.Entry<String, List<Elemento>> entry : elementosAgrupados.entrySet()) {
            String grupo = entry.getKey();  // La clave es el número completo como String (por ejemplo, "1.1")
            List<Elemento> elementosGrupo = entry.getValue();

            // Obtener el nombre del primer elemento del grupo (si existe)
            String nombreElemento = elementosGrupo.isEmpty() ? "Sin Nombre" : elementosGrupo.get(0).getNombre();

            // Log para cada fragmento que se agregará
            Log.d("EncuestasActivity", "Agregando fragmento: Grupo = " + grupo + ", Nombre Elemento = " + nombreElemento);

            // Crear el fragmento pasando el nombre del primer elemento
            ElementosFragment fragment = ElementosFragment.newInstance(grupo, nombreElemento);

            // Agregar el fragmento al ViewPager con el nombre del primer elemento
            viewPagerAdapter.addFragment(fragment, nombreElemento);
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

        fabNotification.setOnClickListener(v -> showGroupInfoDialog());

        AppPreferences appPreferences = new AppPreferences(this);
        nombreEmpresa = appPreferences.getNombreEmpresa();

        // Obtener el representante de la empresa desde la base de datos
        String representanteEmpresa = dbHelper.getRepresentanteEmpresa(nombreEmpresa);

        // Si el representante es null o vacío, se asigna un valor predeterminado
        if (representanteEmpresa == null || representanteEmpresa.isEmpty()) {
            representanteEmpresa = "Desconocido"; // O cualquier valor que consideres adecuado
        }

        dbHelper.insertarTiempo(nombreEmpresa, true);

        String subject = "Inicio de Encuesta";
        String messageBody = "Se ha iniciado una nueva encuesta para: " + nombreEmpresa +
                "\nRepresentante de la Empresa: " + representanteEmpresa +
                "\nFecha y hora de inicio: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        sendEmail(subject, messageBody, email);

        showEncuestadoDialog();
    }

    private void showEncuestadoDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_encuestado, null);

        EditText inputNombre = dialogView.findViewById(R.id.input_nombre_encuestado);
        EditText inputCargo = dialogView.findViewById(R.id.input_cargo_encuestado);

        // Recuperar los valores almacenados en SharedPreferences
        AppPreferences appPreferences = new AppPreferences(this);
        final String nombreEncuestado = appPreferences.getNombreEncuestado();
        final String cargoEncuestado = appPreferences.getCargoEncuestado();

        // Establecer los valores recuperados en los EditText
        inputNombre.setText(nombreEncuestado);
        inputCargo.setText(cargoEncuestado);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Información del Encuestado")
                .setView(dialogView)
                .setPositiveButton("OK", null) // Pasar null aquí para manejar el clic manualmente
                .setNegativeButton("Cancelar", (dialogInterface, which) -> {
                    dialogInterface.dismiss();
                    finish(); // Cerrar la actividad si se cancela
                })
                .setCancelable(false)
                .create();

        dialog.setOnShowListener(dialogInterface -> {
            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(view -> {
                String nuevoNombreEncuestado = inputNombre.getText().toString();
                String nuevoCargoEncuestado = inputCargo.getText().toString();

                if (nuevoNombreEncuestado.isEmpty() || nuevoCargoEncuestado.isEmpty()) {
                    Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    // Almacenar los valores en SharedPreferences
                    appPreferences.setNombreEncuestado(nuevoNombreEncuestado);
                    appPreferences.setCargoEncuestado(nuevoCargoEncuestado);
                    dialog.dismiss();
                }
            });
        });

        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Registrar la fecha y hora de fin
        String fechaFin = dbHelper.insertarTiempo(nombreEmpresa, false);
        int respuestasCount = dbHelper.getRespuestasCount(nombreEmpresa);
        // Obtén el nombre del representante de la empresa
        String representanteEmpresa = dbHelper.getRepresentanteEmpresa(nombreEmpresa);
        // Enviar correo electrónico al finalizar la encuesta
        String subject = "Fin de Encuesta";
        String messageBody = "Se ha finalizado la encuesta para la empresa: " + nombreEmpresa +
                "\nRepresentante de la Empresa: " + representanteEmpresa +
                "\nFecha y hora de fin: " + fechaFin +
                "\nNúmero de preguntas respondidas: " + respuestasCount;
        String emailOperador = email; // Obtén el email del operador
        sendEmail(subject, messageBody, emailOperador);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmación")
                .setMessage("¿Deseas pausar o finalizar la encuesta?")
                .setPositiveButton("Finalizar", (dialog, which) -> {
                    // Limpiar los campos del encuestado
                    AppPreferences appPreferences = new AppPreferences(this);
                    appPreferences.setNombreEncuestado("");
                    appPreferences.setCargoEncuestado("");
                    finish();
                })
                .setNegativeButton("Pausar", (dialog, which) -> {
                    // No limpiar los campos del encuestado
                    finish();
                })
                .setNeutralButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .setCancelable(false)
                .show();
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
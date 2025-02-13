package com.appreman.app.Activity;

import static com.appreman.app.Repository.AppPreferences.KEY_ASIGNACIONES;
import static com.appreman.app.Utils.Constant.URL;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.appreman.app.Api.ApiAdapter;
import com.appreman.app.Api.ApiServices;
import com.appreman.app.Api.Response.AsignarResponse;
import com.appreman.app.Database.DBHelper;
import com.appreman.app.Models.Asignar;
import com.appreman.app.Utils.Constant;
import com.appreman.appreman.R;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "APPREMAM";
    private EditText etUser;
    private EditText etPass;
    private Button buttonIngresar;
    private DBHelper dbHelper;
    private ApiServices appServices;
    private SharedPreferences sharedPreferences;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar vistas
        etUser = findViewById(R.id.etUser);
        etPass = findViewById(R.id.etPass);
        buttonIngresar = findViewById(R.id.buttonIngresar);
        dbHelper = new DBHelper(this);
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        gson = new Gson();

        ApiAdapter apiAdapter = new ApiAdapter();
        appServices = apiAdapter.getApiService(URL);

        // Llamar a agetAsignar al iniciar la actividad
        new Thread(this::agetAsignar).start();

        // Configurar el botón de ingreso
        buttonIngresar.setOnClickListener(v -> {
            // Obtener el texto de los campos de usuario y contraseña
            String email = etUser.getText().toString().trim();
            String password = etPass.getText().toString().trim();

            // Validar campos
            if (email.isEmpty() || password.isEmpty()) {
                if (email.isEmpty()) {
                    etUser.setError("Campo requerido");
                    Toast.makeText(LoginActivity.this, "Campo email vacío", Toast.LENGTH_SHORT).show();
                }
                if (password.isEmpty()) {
                    etPass.setError("Campo requerido");
                    Toast.makeText(LoginActivity.this, "Campo password vacío", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Verificar credenciales
                boolean credencialesCorrectas = dbHelper.verificarOperador(email, password);
                if (credencialesCorrectas) {
                    // Obtener el ID del operador
                    int idOperador = dbHelper.getOperadorIdPorEmail(email);

                    // Guardar el ID del operador en SharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("id_operador", idOperador);
                    editor.apply();

                    // Logcat para verificar valores antes de pasar a MainActivity
                    Log.d("LoginActivity", "Email: " + email);
                    Log.d("LoginActivity", "ID Operador: " + idOperador);

                    // Iniciar la siguiente actividad sin pasar el id_operador
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    finish();
                } else {
                    // Mostrar error si las credenciales no son correctas
                    etUser.setError("Usuario o contraseña incorrectos");
                    etPass.setError("Usuario o contraseña incorrectos");
                    Toast.makeText(LoginActivity.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void agetAsignar() {
        Call<AsignarResponse> callSync = appServices.getAsignaciones();

        try {
            Response<AsignarResponse> response = callSync.execute();

            if (response.isSuccessful()) {
                List<Asignar> asignarList = fetchResultsAsignar(response);

                if (asignarList != null) {
                    // Guardar las asignaciones en SharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    String asignacionesJson = gson.toJson(asignarList);
                    editor.putString(KEY_ASIGNACIONES, asignacionesJson);
                    editor.apply();  // Guarda de manera asíncrona

                    // Verificar si los datos se guardaron correctamente
                    String savedData = sharedPreferences.getString(KEY_ASIGNACIONES, null);
                    Log.d(TAG, "✅ Datos guardados en SharedPreferences: " + savedData);

                    // Imprimir detalles de las asignaciones
                    StringBuilder asignaciones = new StringBuilder();
                    for (Asignar asignar : asignarList) {
                        asignaciones.append("Operador: ").append(asignar.getIdOperador())
                                .append(", Empresa: ").append(asignar.getIdEmpresa())
                                .append(", Elemento: ").append(asignar.getIdElemento())
                                .append(", Nombre Empresa: ").append(asignar.getNombreEmpresa()).append("\n");
                    }

                    Log.d(TAG, "✅ Asignaciones anexadas al JSON y guardadas en SharedPreferences");
                    Log.d(TAG, "📌 Asignaciones: \n" + asignaciones);
                } else {
                    Log.e(TAG, "⚠️ Error: La lista de asignaciones es null.");
                }
            } else {
                Log.e(TAG, "❌ Error en la respuesta: " + response.code());
                if (response.code() == 401) {
                    Log.e(TAG, "❌ Error 401: No autorizado.");
                } else {
                    try {
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Log.e(TAG, "❌ Error Body: " + jObjError.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "❌ IOException al ejecutar la llamada a la API", e);
        }
    }

    private List<Asignar> fetchResultsAsignar(Response<AsignarResponse> response) {
        AsignarResponse asignarResponse = response.body();
        return asignarResponse != null ? asignarResponse.getAsignaciones() : new ArrayList<>();
    }


}

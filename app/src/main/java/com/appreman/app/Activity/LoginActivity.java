package com.appreman.app.Activity;

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

import org.json.JSONObject;

import java.io.IOException;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar vistas
        etUser = findViewById(R.id.etUser);
        etPass = findViewById(R.id.etPass);
        buttonIngresar = findViewById(R.id.buttonIngresar);
        dbHelper = new DBHelper(this);

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
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
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

    // TODO: ******************************* ASIGNAR *****************************************


    private void  agetAsignar(){
        Call<AsignarResponse> callSync = appServices.getAsignaciones();

        try
        {
            Response<AsignarResponse> response = callSync.execute();


            if (response.isSuccessful()) {



                List<Asignar> asignarList = fetchResultsAsignar(response);


                //assert asignar != null;

                for (Asignar asignar : asignarList) {
                     Log.e(TAG, "Operador:" + asignar.getIdOperador() + "\n" + "Elemento:" + asignar.getIdElemento());

                }



            }else {

                if (response.code() == 401) {



                } else {

                    try {

                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());


                    } catch (Exception e) {

                        e.printStackTrace();


                    }
                }
            }

        }
        catch (IOException e)
        {

            e.printStackTrace();
        }
    }


    private List<Asignar> fetchResultsAsignar(Response<AsignarResponse> response) {
        AsignarResponse asignarResponse = response.body();
        return asignarResponse != null ? asignarResponse.getAsignaciones() : null;
    }
}

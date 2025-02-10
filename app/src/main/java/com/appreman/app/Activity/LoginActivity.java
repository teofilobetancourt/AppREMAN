package com.appreman.app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.appreman.app.Database.DBHelper;
import com.appreman.appreman.R;

public class LoginActivity extends AppCompatActivity {

    private EditText etUser;
    private EditText etPass;
    private Button buttonIngresar;
    private DBHelper dbHelper;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar vistas
        etUser = findViewById(R.id.etUser);
        etPass = findViewById(R.id.etPass);
        buttonIngresar = findViewById(R.id.buttonIngresar);
        dbHelper = new DBHelper(this);

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
}
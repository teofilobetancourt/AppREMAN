package com.appreman.app.Activity;

import android.content.Intent;
import android.os.Bundle;
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
                    // Si las credenciales son correctas, iniciar la siguiente actividad y pasar el email
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
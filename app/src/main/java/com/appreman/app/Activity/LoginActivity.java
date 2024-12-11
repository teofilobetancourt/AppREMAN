package com.appreman.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import com.appreman.appreman.R;

public class LoginActivity extends AppCompatActivity {

    private EditText etUser;
    private EditText etPass;
    private Button buttonIngresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Asegúrate de que tu layout se llama activity_login.xml

        // Inicializar vistas
        etUser = findViewById(R.id.etUser);
        etPass = findViewById(R.id.etPass);
        buttonIngresar = findViewById(R.id.buttonIngresar);

        // Configurar el botón de ingreso
        buttonIngresar.setOnClickListener(v -> {
            // Obtener el texto de los campos de usuario y contraseña
            String username = etUser.getText().toString().trim();
            String password = etPass.getText().toString().trim();

            // Validar campos (puedes añadir más validaciones si es necesario)
            if (username.isEmpty() || password.isEmpty()) {
                // Mostrar errores en los campos (puedes usar un Toast o Snackbar si prefieres)
                if (username.isEmpty()) {
                    etUser.setError("Campo requerido");
                }
                if (password.isEmpty()) {
                    etPass.setError("Campo requerido");
                }
            } else if (username.equals("admin") && password.equals("admin")) {
                // Si los campos no están vacíos y son "admin", iniciar la siguiente actividad
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Cerrar la actividad de inicio de sesión para que no se pueda volver atrás
            } else {
                // Mostrar error si las credenciales no son correctas
                etUser.setError("Usuario o contraseña incorrectos");
                etPass.setError("Usuario o contraseña incorrectos");
            }
        });
    }
}
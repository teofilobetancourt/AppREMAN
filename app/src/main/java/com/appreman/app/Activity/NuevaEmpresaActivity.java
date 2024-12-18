package com.appreman.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.appreman.app.Database.DBHelper;
import com.appreman.appreman.R;

public class NuevaEmpresaActivity extends AppCompatActivity {

    EditText txtNombre, txtPais, txtRegion, txtSitio, txtSector, txtPlanta, txtRepresentante, txtTelefono, txtEmail, txtClienteAct, txtNumeroDePlant, txtNumeroDePlantIm;
    Button btnGuarda;
    DBHelper dbHelper;
    String operadorEmail; // Email del operador

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_empresa);

        // Configurar el Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            SpannableString spannableTitle = getSpannableTitle("Nueva Empresa");
            getSupportActionBar().setTitle(spannableTitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationIcon(R.drawable.arrow_back);
        }

        // Obtener el email del Intent
        operadorEmail = getIntent().getStringExtra("email");

        dbHelper = new DBHelper(this);

        txtNombre = findViewById(R.id.txtNombre);
        txtPais = findViewById(R.id.txtPais);
        txtRegion = findViewById(R.id.txtRegion);
        txtSitio = findViewById(R.id.txtSitio);
        txtSector = findViewById(R.id.txtSector);
        txtPlanta = findViewById(R.id.txtPlanta);
        txtRepresentante = findViewById(R.id.txtRepresentanteEmpresa);
        txtTelefono = findViewById(R.id.txtTelefono);
        txtEmail = findViewById(R.id.txtEmail);
        txtClienteAct = findViewById(R.id.txtClienteActual);
        txtNumeroDePlant = findViewById(R.id.txtNumeroPlantas);
        txtNumeroDePlantIm = findViewById(R.id.txtPlantasImplementar);
        btnGuarda = findViewById(R.id.btnGuarda);

        btnGuarda.setOnClickListener(view -> guardarEmpresa());
    }

    private SpannableString getSpannableTitle(String title) {
        SpannableString spannable = new SpannableString(title);
        spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), 0, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    private void guardarEmpresa() {
        String nombre = txtNombre.getText().toString();
        String pais = txtPais.getText().toString();
        String emailEncuestado = txtEmail.getText().toString(); // Email de la persona encuestada

        if (operadorEmail != null && !operadorEmail.isEmpty()) {
            int id_operador = dbHelper.getOperadorIdPorEmail(operadorEmail);

            if (!nombre.isEmpty() && !pais.isEmpty() && !emailEncuestado.isEmpty()) {
                String fechaActual = dbHelper.insertarTiempo(String.valueOf(txtNombre), true);

                dbHelper.insertEmpresa(
                        nombre,
                        pais,
                        txtRegion.getText().toString(),
                        txtSitio.getText().toString(),
                        txtSector.getText().toString(),
                        txtPlanta.getText().toString(),
                        txtRepresentante.getText().toString(),
                        txtTelefono.getText().toString(),
                        emailEncuestado,
                        txtClienteAct.getText().toString(),
                        txtNumeroDePlant.getText().toString(),
                        txtNumeroDePlantIm.getText().toString(),
                        fechaActual,
                        String.valueOf(id_operador) // Pasar el ID del operador
                );

                Toast.makeText(this, "REGISTRO GUARDADO", Toast.LENGTH_LONG).show();
                limpiar();

                // Pasar el resultado a la actividad que inició esta actividad
                Intent resultIntent = new Intent();
                resultIntent.putExtra("nombre", nombre);
                setResult(RESULT_OK, resultIntent);
                finish(); // Finalizar la actividad
            } else {
                Toast.makeText(this, "DEBE LLENAR LOS CAMPOS OBLIGATORIOS", Toast.LENGTH_LONG).show();
            }
        } else {
            Log.e("NuevaEmpresaActivity", "El valor del email del operador no puede ser nulo o vacío");
            // Muestra un mensaje al usuario o toma alguna acción apropiada
        }
    }

    private void limpiar() {
        txtNombre.setText("");
        txtPais.setText("");
        txtRegion.setText("");
        txtSitio.setText("");
        txtSector.setText("");
        txtPlanta.setText("");
        txtRepresentante.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");
        txtClienteAct.setText("");
        txtNumeroDePlant.setText("");
        txtNumeroDePlantIm.setText("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

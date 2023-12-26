package com.appreman.app.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.appreman.app.Database.DBHelper;
import com.appreman.appreman.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class NuevaEmpresaActivity extends AppCompatActivity {

    EditText txtNombre, txtPais, txtRegion, txtSitio, txtSector, txtPlanta, txtRepresentante, txtTelefono, txtEmail, txtClienteAct, txtNumeroDePlant, txtNumeroDePlantIm;
    Button btnGuarda;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_empresa);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView imageView = findViewById(R.id.imageView);
        imageView.setOnClickListener(v -> onBackPressed());

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

    private void guardarEmpresa() {
        String nombre = txtNombre.getText().toString();
        String pais = txtPais.getText().toString();

        if (!nombre.isEmpty() && !pais.isEmpty()) {
            // Obtener la fecha actual del sistema
            String fechaActual = obtenerFechaActual();

            // Obtener la hora actual del sistema
            String horaActual = obtenerHoraActual();

            // Insertar la empresa en la base de datos con la fecha y hora actuales
            dbHelper.insertEmpresa(
                    txtNombre.getText().toString(),
                    txtPais.getText().toString(),
                    txtRegion.getText().toString(),
                    txtSitio.getText().toString(),
                    txtSector.getText().toString(),
                    txtPlanta.getText().toString(),
                    txtRepresentante.getText().toString(),
                    txtTelefono.getText().toString(),
                    txtEmail.getText().toString(),
                    txtClienteAct.getText().toString(),
                    txtNumeroDePlant.getText().toString(),
                    txtNumeroDePlantIm.getText().toString(),
                    fechaActual,
                    horaActual
            );

            Toast.makeText(this, "REGISTRO GUARDADO", Toast.LENGTH_LONG).show();
            limpiar();
        } else {
            Toast.makeText(this, "DEBE LLENAR LOS CAMPOS OBLIGATORIOS", Toast.LENGTH_LONG).show();
        }
    }

    private void limpiar() {
        // Limpiar los EditText después de guardar
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

    private String obtenerFechaActual() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    private String obtenerHoraActual() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, -4); // Restar 4 horas a la hora actual

        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        timeFormat.setTimeZone(TimeZone.getDefault()); // Establecer la zona horaria del dispositivo
        return timeFormat.format(calendar.getTime());
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

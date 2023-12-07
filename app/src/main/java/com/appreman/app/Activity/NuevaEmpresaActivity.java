package com.appreman.app.Activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.appreman.app.Database.DBHelper;
import com.appreman.appreman.R;

import java.util.Objects;

public class NuevaEmpresaActivity extends AppCompatActivity {

    EditText txtNombre, txtPais, txtRegion, txtSitio, txtSector, txtPlanta, txtRepresentante, txtTelefono, txtEmail, txtClienteAct, txtNumeroDePlant, txtNumeroDePlantIm;
    Button btnGuarda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_empresa);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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

        btnGuarda.setOnClickListener(view -> {
            String nombre = txtNombre.getText().toString();
            String pais = txtPais.getText().toString();

            if (!nombre.isEmpty() && !pais.isEmpty()) {
                DBHelper dbHelper = new DBHelper(this);

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
                        txtNumeroDePlantIm.getText().toString()
                );

                Toast.makeText(this, "REGISTRO GUARDADO", Toast.LENGTH_LONG).show();
                limpiar();
            } else {
                Toast.makeText(this, "DEBE LLENAR LOS CAMPOS OBLIGATORIOS", Toast.LENGTH_LONG).show();
            }
        });

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

package com.appreman.app.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.appreman.app.Database.DBHelper;
import com.appreman.app.ViewModel.PieChartView;
import com.appreman.appreman.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class HomeFragment extends Fragment {

    private PieChartView pieChartView;
    private Spinner spinner;
    private DBHelper dbHelper;

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ImageView imageView = view.findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.eman2);

        pieChartView = view.findViewById(R.id.pie_chart_view);
        dbHelper = new DBHelper(getActivity());

        // Busca el Spinner en el layout
        spinner = view.findViewById(R.id.spinner_empresas);

        // Configura el adaptador del Spinner y el listener
        List<String> empresaNames = dbHelper.getAllEmpresaNames();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, empresaNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        // Configura el listener del Spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Maneja el evento de selección del Spinner
                String selectedEmpresa = (String) parentView.getItemAtPosition(position);
                // Actualiza la descripción en PieChartView
                pieChartView.setSelectedEmpresa(selectedEmpresa);
                // Invalida la vista para forzar el redibujado
                pieChartView.invalidate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Maneja el caso donde no hay nada seleccionado
            }
        });

        // Busca el FloatingActionButton (botón flotante) en el layout
        FloatingActionButton fabExportar = view.findViewById(R.id.fabExportar);

        // Configura el listener del FloatingActionButton
        fabExportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Llama a tu método para exportar la base de datos
                exportarBaseDeDatos();
            }
        });

        return view;
    }

    private void exportarBaseDeDatos() {
        Log.d("DBHelper", "Iniciando exportación de la base de datos...");

        // Obtén el nombre de la empresa seleccionada desde el Spinner
        String selectedEmpresa = (String) spinner.getSelectedItem();

        // Verifica si se han concedido los permisos de escritura
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // No se tienen permisos, solicitarlos
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        } else {
            // Se tienen permisos, proceder con la exportación

            // Verifica si se ha seleccionado una empresa
            if (selectedEmpresa != null) {
                // Genera un nombre de archivo basado en el nombre de la empresa
                String nombreArchivo = "datos_" + selectedEmpresa + ".xls";

                // Llama al método de DBHelper con el nombre de la empresa seleccionada y el nombre del archivo
                boolean exportacionExitosa = dbHelper.exportRespuestasToExcel(nombreArchivo, selectedEmpresa);

                if (exportacionExitosa) {
                    // La exportación fue exitosa, muestra un Toast y registra un mensaje de log
                    Toast.makeText(requireContext(), "Exportación exitosa a Excel", Toast.LENGTH_SHORT).show();
                    Log.d("DBHelper", "Exportación exitosa a Excel: " + nombreArchivo);
                } else {
                    // Maneja el caso donde hubo un problema con la exportación
                    Toast.makeText(requireContext(), "Error en la exportación a Excel", Toast.LENGTH_SHORT).show();
                    Log.e("DBHelper", "Error en la exportación a Excel");
                }
            } else {
                // Maneja el caso donde no se ha seleccionado una empresa
                Toast.makeText(requireContext(), "Selecciona una empresa antes de exportar", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // En tu método onRequestPermissionsResult()
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE) {
            // Verificar si el permiso fue concedido
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, proceder con la exportación
                exportarBaseDeDatos();
            } else {
                // Permiso denegado, mostrar un mensaje o realizar alguna acción necesaria
                Toast.makeText(requireContext(), "Permiso de escritura denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

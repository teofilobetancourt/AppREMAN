package com.appreman.app.Activity;

import static com.appreman.app.Repository.AppPreferences.KEY_ASIGNACIONES;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.appreman.app.Database.DBHelper;
import com.appreman.app.Email.NetworkChangeReceiver;
import com.appreman.app.Fragments.AsignarFragment;
import com.appreman.app.Fragments.EmpresasFragment;
import com.appreman.app.Fragments.HomeFragment;
import com.appreman.app.Fragments.SurveyFragment;
import com.appreman.app.Models.Asignar;
import com.appreman.appreman.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private BottomNavigationView navigation;
    private GestureDetector gestureDetector;
    private String nombre_empresa;
    private NetworkChangeReceiver networkChangeReceiver;
    private android.app.AlertDialog alertDialog;
    private String email;
    private int idOperador; // Variable para almacenar el ID del operador
    private Fragment previousFragment; // Variable para almacenar el fragmento anterior
    private List<Asignar> asignarList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigation = findViewById(R.id.navigation);

        email = getIntent().getStringExtra("email");

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        idOperador = sharedPreferences.getInt("id_operador", -1);

        if (idOperador == -1) {
            Log.e("MainActivity", "⚠️ Error: idOperador no encontrado en SharedPreferences.");
        } else {
            Log.d("MainActivity", "ID del operador recibido: " + idOperador);
        }

        DBHelper dbHelper = new DBHelper(this);
        Map<String, String> nombreApellido = dbHelper.getNombreApellidoPorEmail(email);

        TextView userNameTextView = findViewById(R.id.user_name);
        if (nombreApellido != null) {
            String nombreCompleto = nombreApellido.get("nombre") + " " + nombreApellido.get("apellido");
            userNameTextView.setText(nombreCompleto);
        } else {
            userNameTextView.setText("Usuario");
        }

        Log.d("MainActivity", "ID del operador que se está pasando: " + idOperador);

        String asignacionesJson = sharedPreferences.getString(KEY_ASIGNACIONES, null);
        List<Asignar> asignarList = new ArrayList<>();
        if (asignacionesJson != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Asignar>>() {}.getType();
            asignarList = gson.fromJson(asignacionesJson, type);

            if (asignarList != null) {
                StringBuilder asignaciones = new StringBuilder();
                for (Asignar asignar : asignarList) {
                    asignaciones.append("Operador: ").append(asignar.getIdOperador())
                            .append(", Empresa: ").append(asignar.getIdEmpresa())
                            .append(", Elemento: ").append(asignar.getIdElemento())
                            .append(", Nombre Empresa: ").append(asignar.getNombreEmpresa()).append("\n");
                }
                Log.d("MainActivity", "📌 Asignaciones recuperadas: \n" + asignaciones);
            } else {
                Log.e("MainActivity", "⚠️ Error: La lista de asignaciones es null.");
            }
        } else {
            Log.e("MainActivity", "⚠️ Error: No se encontraron asignaciones en SharedPreferences.");
        }

        displayFragment(HomeFragment.newInstance(email, idOperador, asignarList), false, null);

        List<Asignar> finalAsignarList = asignarList;
        navigation.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            int id = item.getItemId();
            Bundle bundle = new Bundle();
            bundle.putString("email", email);
            bundle.putString("id_operador", String.valueOf(idOperador));

            if (id == R.id.navigation_home) {
                fragment = HomeFragment.newInstance(email, idOperador, finalAsignarList);
            } else if (id == R.id.navigation_empresa) {
                fragment = EmpresasFragment.newInstance(email, String.valueOf(idOperador));
            } else if (id == R.id.navigation_survey) {
                fragment = SurveyFragment.newInstance(email, String.valueOf(idOperador));
            }

            if (fragment != null) {
                displayFragment(fragment, false, null);
            }

            return true;
        });

        // Configurar el detector de gestos
        gestureDetector = new GestureDetector(this, new GestureListener());
        findViewById(R.id.fragment_content).setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);
            return true;
        });

        // Registrar el receptor de cambios de red
        networkChangeReceiver = new NetworkChangeReceiver();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, filter);

        // Configurar el botón flotante
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) FloatingActionButton fab = findViewById(R.id.asignar);
        if (!"admin".equals(email)) {
            fab.setVisibility(View.GONE);
        } else {
            fab.setOnClickListener(v -> {
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_content);
                if (currentFragment instanceof AsignarFragment) {
                    // Regresar al fragmento anterior
                    displayFragment(previousFragment, false, null);
                } else {
                    // Guardar el fragmento actual como el anterior
                    previousFragment = currentFragment;
                    // Cargar el fragmento AsignarFragment
                    displayFragment(AsignarFragment.newInstance(), true, null);
                }
            });
        }

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_content);
            if (currentFragment != null) {
                updateToolbarTitle(currentFragment);
                updateNavigationSelection(currentFragment);
            }
        });
    }

    public void displayFragment(Fragment fragment, boolean addToBackStack, Bundle bundle) {
        if (fragment == null) return;
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_content);
        if (currentFragment != null && currentFragment.getClass().equals(fragment.getClass())) {
            return; // Evitar reemplazar el mismo fragmento
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        transaction.replace(R.id.fragment_content, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(null); // Añadir la transacción a la pila de retroceso
        }
        transaction.commit();
        updateToolbarTitle(fragment); // Asegurar que el título se actualice al cambiar de fragmento
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver);
    }

    private void updateToolbarTitle(Fragment fragment) {
        if (fragment instanceof HomeFragment) {
            toolbar.setTitle("Inicio");
        } else if (fragment instanceof EmpresasFragment) {
            toolbar.setTitle("Empresas");
        } else if (fragment instanceof SurveyFragment) {
            toolbar.setTitle("Survey");
        } else if (fragment instanceof AsignarFragment) {
            toolbar.setTitle("Asignación de encuesta");
        }
    }

    private void updateNavigationSelection(Fragment fragment) {
        int selectedItemId = navigation.getSelectedItemId();
        if (fragment instanceof HomeFragment && selectedItemId != R.id.navigation_home) {
            navigation.setSelectedItemId(R.id.navigation_home);
        } else if (fragment instanceof EmpresasFragment && selectedItemId != R.id.navigation_empresa) {
            navigation.setSelectedItemId(R.id.navigation_empresa);
        } else if (fragment instanceof SurveyFragment && selectedItemId != R.id.navigation_survey) {
            navigation.setSelectedItemId(R.id.navigation_survey);
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Salir")
                .setMessage("¿Deseas cerrar sesión?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("No", (dialog, which) -> {
                    dialog.dismiss();
                })
                .setCancelable(false)
                .show();
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_THRESHOLD) {
                return false;
            }
            if (Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD && Math.abs(e1.getX() - e2.getX()) > SWIPE_THRESHOLD) {
                if (e1.getX() > e2.getX()) {
                    navigateToNextFragment();
                } else {
                    navigateToPreviousFragment();
                }
                return true;
            }
            return false;
        }

        private void navigateToNextFragment() {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_content);
            if (currentFragment instanceof HomeFragment) {
                displayFragment(new EmpresasFragment(), true, null);
            } else if (currentFragment instanceof EmpresasFragment) {
                displayFragment(SurveyFragment.newInstance(email, String.valueOf(idOperador)), true, null);
            } else if (currentFragment instanceof SurveyFragment) {
                displayFragment(HomeFragment.newInstance(email, idOperador, asignarList), true, null);
            }
        }

        private void navigateToPreviousFragment() {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_content);
            if (currentFragment instanceof SurveyFragment) {
                displayFragment(new EmpresasFragment(), true, null);
            } else if (currentFragment instanceof EmpresasFragment) {
                displayFragment(HomeFragment.newInstance(email, idOperador, asignarList), true, null);
            } else if (currentFragment instanceof HomeFragment) {
                displayFragment(SurveyFragment.newInstance(email, String.valueOf(idOperador)), true, null);
            }
        }
    }
}
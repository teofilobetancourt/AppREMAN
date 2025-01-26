package com.appreman.app.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
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
import com.appreman.appreman.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private BottomNavigationView navigation;
    private GestureDetector gestureDetector;
    private String nombre_empresa;
    private NetworkChangeReceiver networkChangeReceiver;
    private android.app.AlertDialog alertDialog;
    private String email;
    private Fragment previousFragment; // Variable para almacenar el fragmento anterior

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigation = findViewById(R.id.navigation);

        nombre_empresa = getIntent().getStringExtra("empresa_nombre");
        if (nombre_empresa == null) {
            nombre_empresa = ""; // Valor por defecto
        }

        email = getIntent().getStringExtra("email");

        DBHelper dbHelper = new DBHelper(this);
        Map<String, String> nombreApellido = dbHelper.getNombreApellidoPorEmail(email);

        TextView userNameTextView = findViewById(R.id.user_name);
        if (nombreApellido != null) {
            String nombreCompleto = nombreApellido.get("nombre") + " " + nombreApellido.get("apellido");
            userNameTextView.setText(nombreCompleto);
        } else {
            userNameTextView.setText("Usuario");
        }

        displayFragment(HomeFragment.newInstance(nombre_empresa, email), false, null);

        navigation.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            int id = item.getItemId();
            Bundle bundle = new Bundle();
            bundle.putString("email", email);

            if (id == R.id.navigation_home) {
                fragment = HomeFragment.newInstance(nombre_empresa, email);
            } else if (id == R.id.navigation_empresa) {
                fragment = new EmpresasFragment();
            } else if (id == R.id.navigation_survey) {
                fragment = SurveyFragment.newInstance(email);
            }

            displayFragment(fragment, true, bundle);
            return true;
        });

        gestureDetector = new GestureDetector(this, new GestureListener());
        findViewById(R.id.fragment_content).setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);
            return true;
        });

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_content);
            if (currentFragment != null) {
                updateToolbarTitle(currentFragment);
                updateNavigationSelection(currentFragment);
            }
        });

        networkChangeReceiver = new NetworkChangeReceiver();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, filter);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) FloatingActionButton fab = findViewById(R.id.asignar);
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
            toolbar.setTitle("Asignación de encuestas");
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
                displayFragment(SurveyFragment.newInstance(email), true, null);
            } else if (currentFragment instanceof SurveyFragment) {
                displayFragment(HomeFragment.newInstance(nombre_empresa, email), true, null);
            }
        }

        private void navigateToPreviousFragment() {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_content);
            if (currentFragment instanceof SurveyFragment) {
                displayFragment(new EmpresasFragment(), true, null);
            } else if (currentFragment instanceof EmpresasFragment) {
                displayFragment(HomeFragment.newInstance(nombre_empresa, email), true, null);
            } else if (currentFragment instanceof HomeFragment) {
                displayFragment(SurveyFragment.newInstance(email), true, null);
            }
        }
    }
}
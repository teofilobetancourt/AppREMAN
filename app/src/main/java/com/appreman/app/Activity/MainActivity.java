package com.appreman.app.Activity;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.appreman.app.Fragments.EmpresasFragment;
import com.appreman.app.Fragments.HomeFragment;
import com.appreman.app.Fragments.SurveyFragment;
import com.appreman.appreman.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private BottomNavigationView navigation;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigation = findViewById(R.id.navigation);

        // Configurar el fragmento inicial
        displayFragment(new HomeFragment(), false);

        navigation.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            int id = item.getItemId();

            if (id == R.id.navigation_home) {
                fragment = new HomeFragment();
            } else if (id == R.id.navigation_empresa) {
                fragment = new EmpresasFragment();
            } else if (id == R.id.navigation_survey) {
                fragment = new SurveyFragment();
            }

            displayFragment(fragment, true);
            return true;
        });

        // Configurar el GestureDetector
        gestureDetector = new GestureDetector(this, new GestureListener());
        findViewById(R.id.fragment_content).setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);
            return true;
        });

        // Añadir un listener para manejar los cambios en la pila de retroceso
        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_content);
            if (currentFragment != null) {
                updateToolbarTitle(currentFragment);
                updateNavigationSelection(currentFragment);
            }
        });
    }

    public void displayFragment(Fragment fragment, boolean addToBackStack) {
        if (fragment == null) return;
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_content);
        if (currentFragment != null && currentFragment.getClass().equals(fragment.getClass())) {
            return; // Evitar reemplazar el mismo fragmento
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_content, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(null); // Añadir la transacción a la pila de retroceso
        }
        transaction.commit();
        updateToolbarTitle(fragment); // Asegurar que el título se actualice al cambiar de fragmento
    }

    private void updateToolbarTitle(Fragment fragment) {
        if (fragment instanceof HomeFragment) {
            toolbar.setTitle("Inicio");
        } else if (fragment instanceof EmpresasFragment) {
            toolbar.setTitle("Empresas");
        } else if (fragment instanceof SurveyFragment) {
            toolbar.setTitle("Survey");
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

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
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
                    // Deslizar a la izquierda (ir hacia adelante)
                    navigateToNextFragment();
                } else {
                    // Deslizar a la derecha (ir hacia atrás)
                    navigateToPreviousFragment();
                }
                return true;
            }
            return false;
        }

        private void navigateToNextFragment() {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_content);
            if (currentFragment instanceof HomeFragment) {
                displayFragment(new EmpresasFragment(), true);
            } else if (currentFragment instanceof EmpresasFragment) {
                displayFragment(new SurveyFragment(), true);
            } else if (currentFragment instanceof SurveyFragment) {
                displayFragment(new HomeFragment(), true); // Volver al primer fragmento
            }
        }

        private void navigateToPreviousFragment() {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_content);
            if (currentFragment instanceof SurveyFragment) {
                displayFragment(new EmpresasFragment(), true);
            } else if (currentFragment instanceof EmpresasFragment) {
                displayFragment(new HomeFragment(), true);
            } else if (currentFragment instanceof HomeFragment) {
                displayFragment(new SurveyFragment(), true); // Volver al último fragmento
            }
        }
    }
}

package com.appreman.app.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.appreman.app.Activity.EncuestasActivity;
import com.appreman.app.Activity.MainActivity;
import com.appreman.app.Adapter.EmpresaAdapter;
import com.appreman.app.Database.DBHelper;
import com.appreman.app.Models.Empresa;
import com.appreman.app.Repository.AppPreferences;
import com.appreman.appreman.R;
import java.util.Collections;
import java.util.List;

public class SurveyFragment extends Fragment {

    private RecyclerView recyclerViewEmpresas;
    private DBHelper dbHelper;
    private AppPreferences appPreferences;
    private GestureDetector gestureDetector;
    private String email;

    public SurveyFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("SurveyFragment", "onCreateView called");

        View rootView = inflater.inflate(R.layout.fragment_survey, container, false);

        recyclerViewEmpresas = rootView.findViewById(R.id.recyclerViewEmpresas);
        dbHelper = new DBHelper(requireActivity());
        appPreferences = new AppPreferences(requireActivity());

        // Obtener el email del Bundle
        if (getArguments() != null) {
            email = getArguments().getString("email");
            Log.d("SurveyFragment", "Email obtenido del Bundle: " + email);
        }

        setupRecyclerView();

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gestureDetector = new GestureDetector(requireActivity(), new GestureListener());

        recyclerViewEmpresas.setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);
            return false; // Return false to let the RecyclerView handle the event if not consumed
        });

        // Configurar el título del Toolbar para este Fragmento
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            activity.getSupportActionBar().setTitle("Survey");
        }
    }

    private void setupRecyclerView() {
        Log.d("SurveyFragment", "setupRecyclerView called");

        List<Empresa> empresas = dbHelper.getAllEmpresas();
        Log.d("SurveyFragment", "Empresas size: " + empresas.size());

        Collections.reverse(empresas);

        EmpresaAdapter adapter = new EmpresaAdapter(empresas);
        recyclerViewEmpresas.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerViewEmpresas.setAdapter(adapter);

        adapter.setEncuestaClickListener(position -> {
            if (position != RecyclerView.NO_POSITION) {
                Empresa empresa = empresas.get(position);
                String nombreEmpresaSeleccionada = empresa.getNombre();

                appPreferences.setNombreEmpresa(nombreEmpresaSeleccionada);

                Log.d("SurveyFragment", "Empresa selected: " + nombreEmpresaSeleccionada);

                // Iniciar EncuestasActivity y pasar el email
                Log.d("SurveyFragment", "Iniciando EncuestasActivity con email: " + email);
                Intent intent = new Intent(requireActivity(), EncuestasActivity.class);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.d("GestureListener", "onFling: e1 = (" + e1.getX() + ", " + e1.getY() + "), e2 = (" + e2.getX() + ", " + e2.getY() + "), velocityX = " + velocityX + ", velocityY = " + velocityY);

            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_THRESHOLD) {
                return false; // Movimiento vertical, no interferir
            }
            if (Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD && Math.abs(e1.getX() - e2.getX()) > SWIPE_THRESHOLD) {
                if (e1.getX() > e2.getX()) {
                    Log.d("GestureListener", "Swiped left");
                    navigateToNextFragment();
                } else {
                    Log.d("GestureListener", "Swiped right");
                    navigateToPreviousFragment();
                }
                return true;
            }
            return false;
        }

        private void navigateToNextFragment() {
            Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_content);
            Log.d("SurveyFragment", "navigateToNextFragment: Current Fragment = " + currentFragment);

            Bundle bundle = new Bundle(); // Crear un Bundle vacío

            if (currentFragment instanceof HomeFragment) {
                Log.d("SurveyFragment", "Navigating to EmpresasFragment");
                ((MainActivity) getActivity()).displayFragment(new EmpresasFragment(), true, bundle);
            } else if (currentFragment instanceof EmpresasFragment) {
                Log.d("SurveyFragment", "Navigating to SurveyFragment");
                ((MainActivity) getActivity()).displayFragment(new SurveyFragment(), true, bundle);
            } else if (currentFragment instanceof SurveyFragment) {
                Log.d("SurveyFragment", "Navigating back to HomeFragment");
                ((MainActivity) getActivity()).displayFragment(new HomeFragment(), true, bundle); // Volver al primer fragmento
            }
        }

        private void navigateToPreviousFragment() {
            Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_content);
            Log.d("SurveyFragment", "navigateToPreviousFragment: Current Fragment = " + currentFragment);

            Bundle bundle = new Bundle(); // Crear un Bundle vacío

            if (currentFragment instanceof SurveyFragment) {
                Log.d("SurveyFragment", "Navigating to EmpresasFragment");
                ((MainActivity) getActivity()).displayFragment(new EmpresasFragment(), true, bundle);
            } else if (currentFragment instanceof EmpresasFragment) {
                Log.d("SurveyFragment", "Navigating to HomeFragment");
                ((MainActivity) getActivity()).displayFragment(new HomeFragment(), true, bundle);
            } else if (currentFragment instanceof HomeFragment) {
                Log.d("SurveyFragment", "No action for HomeFragment on back");
                // Aquí puedes agregar lógica si necesitas manejar el desplazamiento desde HomeFragment
            }
        }
    }
}

package com.appreman.app.Fragments;

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

public class EmpresasFragment extends Fragment {

    private RecyclerView recyclerViewEmpresas;
    private DBHelper dbHelper;
    private AppPreferences appPreferences;
    private GestureDetector gestureDetector;

    public EmpresasFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("EmpresasFragment", "onCreateView called");

        View rootView = inflater.inflate(R.layout.fragment_empresas, container, false);

        recyclerViewEmpresas = rootView.findViewById(R.id.recyclerViewEmpresas);
        dbHelper = new DBHelper(requireActivity());
        appPreferences = new AppPreferences(requireActivity());

        setupRecyclerView();

        gestureDetector = new GestureDetector(requireActivity(), new GestureListener());

        recyclerViewEmpresas.setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);
            return false; // Return false to let the RecyclerView handle the event if not consumed
        });

        // Configurar el título del Toolbar para este Fragmento
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            activity.getSupportActionBar().setTitle("Empresas");
        }

        return rootView;
    }

    private void setupRecyclerView() {
        Log.d("EmpresasFragment", "setupRecyclerView called");

        List<Empresa> empresas = dbHelper.getAllEmpresas();
        Log.d("EmpresasFragment", "Empresas size: " + empresas.size());

        Collections.reverse(empresas);

        EmpresaAdapter adapter = new EmpresaAdapter(empresas);
        recyclerViewEmpresas.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerViewEmpresas.setAdapter(adapter);

        adapter.setEncuestaClickListener(position -> {
            if (position != RecyclerView.NO_POSITION) {
                Empresa empresa = empresas.get(position);
                String nombreEmpresaSeleccionada = empresa.getNombre();

                appPreferences.setNombreEmpresa(nombreEmpresaSeleccionada);

                Log.d("EmpresasFragment", "Empresa selected: " + nombreEmpresaSeleccionada);
                EncuestasActivity.start(requireActivity());
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
            Log.d("EmpresasFragment", "navigateToNextFragment: Current Fragment = " + currentFragment);

            if (currentFragment instanceof HomeFragment) {
                Log.d("EmpresasFragment", "Navigating to EmpresasFragment");
                ((MainActivity) getActivity()).displayFragment(new EmpresasFragment(), true);
            } else if (currentFragment instanceof EmpresasFragment) {
                Log.d("EmpresasFragment", "Navigating to SurveyFragment");
                ((MainActivity) getActivity()).displayFragment(new SurveyFragment(), true);
            } else if (currentFragment instanceof SurveyFragment) {
                Log.d("EmpresasFragment", "Navigating back to HomeFragment");
                ((MainActivity) getActivity()).displayFragment(new HomeFragment(), true); // Volver al primer fragmento
            }
        }

        private void navigateToPreviousFragment() {
            Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_content);
            Log.d("EmpresasFragment", "navigateToPreviousFragment: Current Fragment = " + currentFragment);

            if (currentFragment instanceof SurveyFragment) {
                Log.d("EmpresasFragment", "Navigating to EmpresasFragment");
                ((MainActivity) getActivity()).displayFragment(new EmpresasFragment(), true);
            } else if (currentFragment instanceof EmpresasFragment) {
                Log.d("EmpresasFragment", "Navigating to HomeFragment");
                ((MainActivity) getActivity()).displayFragment(new HomeFragment(), true);
            } else if (currentFragment instanceof HomeFragment) {
                Log.d("EmpresasFragment", "No action for HomeFragment on back");
                // Aquí puedes agregar lógica si necesitas manejar el desplazamiento desde HomeFragment
            }
        }
    }
}

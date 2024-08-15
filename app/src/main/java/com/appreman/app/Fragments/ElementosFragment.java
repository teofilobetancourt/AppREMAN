package com.appreman.app.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appreman.app.Adapter.ElementoAdapter;
import com.appreman.app.Database.DBHelper;
import com.appreman.app.Models.Elemento;
import com.appreman.appreman.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ElementosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ElementosFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private int mParam1;

    public ElementosFragment() {
    }

    public static ElementosFragment newInstance(int param1) {
        ElementosFragment fragment = new ElementosFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_elementos, container, false);

        RecyclerView recycler = fragment.findViewById(R.id.recyclerview);

        DBHelper v_db_helper = new DBHelper(requireActivity().getApplicationContext());
        List<Elemento> mElementos = v_db_helper.getElementosGrupo(mParam1);

        RecyclerView.LayoutManager lManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(lManager);

        ElementoAdapter adapter = new ElementoAdapter(mElementos, getContext());
        recycler.setAdapter(adapter);

        return fragment;
    }

}

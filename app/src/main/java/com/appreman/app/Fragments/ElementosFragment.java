package com.appreman.app.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";


    // TODO: Rename and change types of parameters
    private int mParam1;


    public ElementosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.

     * @return A new instance of fragment ElementosFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        // Inflate the layout for this fragment
        View fragment = inflater.inflate(R.layout.fragment_elementos, container, false);

        Context context = getContext();

        RecyclerView recycler  = fragment.findViewById(R.id.recyclerview);


        DBHelper v_db_helper = new DBHelper(getActivity().getApplicationContext());

        List<Elemento> mElementos = v_db_helper.getElementosGrupo(mParam1);

        recycler.setHasFixedSize(true);
        RecyclerView.LayoutManager lManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(lManager);

        ElementoAdapter adapter = new ElementoAdapter(mElementos);

        recycler.setAdapter(adapter);


        /*for(Elemento e:mElementos){
            Log.e("REMAN",e.getNombre() );

        }*/

        return fragment;
    }
}
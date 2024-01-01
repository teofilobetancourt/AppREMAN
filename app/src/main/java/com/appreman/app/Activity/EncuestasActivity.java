package com.appreman.app.Activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.appreman.app.Adapter.ElementoListAdapter;
import com.appreman.app.Adapter.ViewPagerAdapter;
import com.appreman.app.Database.DBHelper;
import com.appreman.app.Fragments.ElementosFragment;
import com.appreman.app.Models.Grupo;
import com.appreman.appreman.databinding.ActivityEncuestasBinding;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class EncuestasActivity extends AppCompatActivity {

    private Context context;
    private ElementoListAdapter Adapter;

    private  ViewPager viewPager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.appreman.appreman.databinding.ActivityEncuestasBinding binding = ActivityEncuestasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        TabLayout tabLayout = binding.tabLayoutGrupos;

        viewPager = binding.viewPager;


        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),   0);

        DBHelper v_db_helper = new DBHelper(getApplicationContext());

        List<Grupo> mGrupos = v_db_helper.getAllGrupos();

        for(Grupo g:mGrupos){
            //Log.e("REMAN",g.getNombre() );
            ElementosFragment fragment = ElementosFragment.newInstance(g.getNumero());
            viewPagerAdapter.addFragment(fragment, g.getNombre());
        }

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setCurrentItem(0);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                Log.e("REMAN", tab.getPosition()    + " " + tab.getText());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });










       /* RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final GrupoListAdapter adapter = new GrupoListAdapter(new GrupoListAdapter.GrupoDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));





*/

    }
}

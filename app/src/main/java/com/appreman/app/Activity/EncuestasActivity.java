package com.appreman.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.appreman.app.Adapter.ViewPagerAdapter;
import com.appreman.app.Database.DBHelper;
import com.appreman.app.Fragments.ElementosFragment;
import com.appreman.app.Models.Grupo;
import com.appreman.appreman.databinding.ActivityEncuestasBinding;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class EncuestasActivity extends AppCompatActivity {

    private ViewPager viewPager;

    public static void start(Context context) {
        Intent intent = new Intent(context, EncuestasActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityEncuestasBinding binding = ActivityEncuestasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitle(getTitle());

        viewPager = binding.viewPager;
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        DBHelper dbHelper = new DBHelper(getApplicationContext());

        List<Grupo> grupos = dbHelper.getAllGrupos();

        for (Grupo grupo : grupos) {
            ElementosFragment fragment = ElementosFragment.newInstance(grupo.getNumero());
            viewPagerAdapter.addFragment(fragment, grupo.getNombre());
        }

        viewPager.setAdapter(viewPagerAdapter);
        binding.tabLayoutGrupos.setupWithViewPager(viewPager);

        viewPager.setCurrentItem(0);

        binding.tabLayoutGrupos.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }
}

package com.appreman.app.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.appreman.app.Fragments.EmpresasFragment;
import com.appreman.app.Fragments.HomeFragment;
import com.appreman.app.Fragments.SurveyFragment;
import com.appreman.appreman.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        BottomNavigationView navigation = findViewById(R.id.navigation);


        displayfragment(new HomeFragment());


        navigation.setOnItemSelectedListener(item -> {

            Fragment fragment = null;
            int id = item.getItemId();

            if (id==R.id.navigation_home){

                fragment = new HomeFragment();


            }

            if (id==R.id.navigation_empresa){

                fragment = new EmpresasFragment();

            }

            if (id==R.id.navigation_survey){

                fragment = new SurveyFragment();

            }


            displayfragment(fragment);

            return true;

        });


    }

    private void displayfragment(Fragment fragment) {

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content, fragment).commit();
    }
}

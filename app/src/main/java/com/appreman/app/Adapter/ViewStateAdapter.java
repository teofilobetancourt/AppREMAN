package com.appreman.app.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


import com.appreman.app.Fragments.ElementosFragment;

import java.util.ArrayList;
import java.util.List;

public class ViewStateAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> fragments = new ArrayList<>();
    private final List<String> fragmentTitles = new ArrayList<>();

    public ViewStateAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }


    public void addFragment(Fragment fragment, String title){
        fragments.add(fragment);
        fragmentTitles.add(title);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return  fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}

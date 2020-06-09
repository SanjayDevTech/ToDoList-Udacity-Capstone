package com.sanjay.udacity.todolist.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> fragments;
    int count;
    ArrayList<String> titles;

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior, ArrayList<Fragment> fragments, int count, ArrayList<String> titles) {
        super(fm, behavior);
        this.fragments = fragments;
        this.count = count;
        this.titles = titles;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return count;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}

package com.iti.mansoura.tot.easytripplanner.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MyHomePagerAdapter extends FragmentPagerAdapter {
    static Fragment[] fragments;
    private String[] tabTitles =new String[]{"UpComming","History"};
    public MyHomePagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }
    public static void setFragments(Fragment[] Fragments){
        fragments=Fragments;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new UpCommingFragment();
            case 1:
                return new HistoryFragment();

            default:
                return null; // shouldn't happen
        }
    }

    @Override
    public int getCount() {
        return fragments!=null?fragments.length:0;
    }
}

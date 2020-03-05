package com.example.technote.TN_Network.Adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.technote.TN_Network.Fragment_Board_Home;
import com.example.technote.TN_Network.Fragment_Board_List;

public class TabLayoutAdapter extends FragmentStatePagerAdapter {

    // Count number of tabs
    private int tabCount;
    public TabLayoutAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {

        // Returning the current tabs
        switch (position) {
            case 0:
                return new Fragment_Board_Home();
            case 1:
                return new Fragment_Board_List();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
package com.yunjeapark.technote.tn_dialog_activity_fragment.my_tab_viewpager;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.yunjeapark.technote.tn_dialog_activity_fragment.Fragment_A;
import com.yunjeapark.technote.tn_dialog_activity_fragment.Fragment_B;
import com.yunjeapark.technote.tn_dialog_activity_fragment.Fragment_C;

public class MyTabLayoutAdapter extends FragmentStatePagerAdapter {

    // Count number of tabs
    private int tabCount;
    public MyTabLayoutAdapter(FragmentManager fm, int tabCount) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {

        // Returning the current tabs
        switch (position) {
            case 0:
                return new Fragment_A();
            case 1:
                return new Fragment_B();
            case 2:
                return new Fragment_C();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
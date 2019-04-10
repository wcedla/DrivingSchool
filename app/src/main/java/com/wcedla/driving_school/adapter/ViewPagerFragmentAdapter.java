package com.wcedla.driving_school.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * @author wcedla
 */
public class ViewPagerFragmentAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragmentList;
    private FragmentManager fragmentManager;

    public ViewPagerFragmentAdapter(FragmentManager fragmentManager, List<Fragment> fragmentList) {
        super(fragmentManager);
        this.fragmentManager=fragmentManager;
        this.fragmentList=fragmentList;
    }

    @Override
    public Fragment getItem(int i) {
        return fragmentList.get(i);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}

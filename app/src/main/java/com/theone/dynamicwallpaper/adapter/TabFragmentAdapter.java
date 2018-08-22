package com.theone.dynamicwallpaper.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.theone.dynamicwallpaper.ui.VideoFragment;

import java.util.List;


public class TabFragmentAdapter extends FragmentPagerAdapter {
    private String[] tabs ;
    private List<VideoFragment> fragments;

    public TabFragmentAdapter(FragmentManager fm, List<VideoFragment> fragments,String[]  tabs) {
        super(fm);
        this.tabs = tabs;
        this.fragments = fragments;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
    }
}
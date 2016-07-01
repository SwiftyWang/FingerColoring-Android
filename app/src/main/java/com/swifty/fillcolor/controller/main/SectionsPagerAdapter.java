package com.swifty.fillcolor.controller.main;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Swifty.Wang on 2015/8/14.
 */
public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
    List<String> tabs = new ArrayList<String>();

    public SectionsPagerAdapter(FragmentManager fragmentManager, List<String> tabs) {
        super(fragmentManager);
        this.tabs = tabs;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return ThemeListFragment.getInstance();
//        } else if (position == 1) {
//            return ImageWallFragment.getInstance();
//        } else {
        } else {
            return UserFragment.getInstance();
        }
    }

    @Override
    public int getCount() {
        return tabs.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position);
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    public void destroyAllFragment() {
        ThemeListFragment.getInstance().finish();
        UserFragment.getInstance().finish();
    }
}

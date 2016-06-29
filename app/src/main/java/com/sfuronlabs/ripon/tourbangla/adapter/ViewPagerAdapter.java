package com.sfuronlabs.ripon.tourbangla.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.sfuronlabs.ripon.tourbangla.fragment.DhakaFragment;

/**
 * Created by Ripon on 6/13/15.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    CharSequence Titles[];
    int NumbOfTabs;

    public ViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            return DhakaFragment.newInstanceOfDhakaFragment(Titles[0].toString().toUpperCase());
        } else if (position == 1) {
            return DhakaFragment.newInstanceOfDhakaFragment(Titles[1].toString().toUpperCase());
        } else if (position == 2) {
            return DhakaFragment.newInstanceOfDhakaFragment(Titles[2].toString().toUpperCase());
        } else if (position == 3) {
            return DhakaFragment.newInstanceOfDhakaFragment(Titles[3].toString().toUpperCase());
        } else if (position == 4) {
            return DhakaFragment.newInstanceOfDhakaFragment(Titles[4].toString().toUpperCase());
        } else if (position == 5) {
            return DhakaFragment.newInstanceOfDhakaFragment(Titles[5].toString().toUpperCase());
        } else {
            return DhakaFragment.newInstanceOfDhakaFragment(Titles[6].toString().toUpperCase());
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}

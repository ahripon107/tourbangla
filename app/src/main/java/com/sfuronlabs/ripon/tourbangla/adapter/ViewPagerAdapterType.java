package com.sfuronlabs.ripon.tourbangla.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.sfuronlabs.ripon.tourbangla.fragment.ArcheologicalFragment;

/**
 * Created by Ripon on 6/15/15.
 */
public class ViewPagerAdapterType extends FragmentStatePagerAdapter {
    CharSequence Titles[];
    int NumbOfTabs;

    public ViewPagerAdapterType(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            return ArcheologicalFragment.newInstanceArcheologicalFragment(Titles[0].toString().toUpperCase());
        } else if (position == 1) {
            return ArcheologicalFragment.newInstanceArcheologicalFragment(Titles[1].toString().toUpperCase());
        } else if (position == 2) {
            return ArcheologicalFragment.newInstanceArcheologicalFragment(Titles[2].toString().toUpperCase());
        } else if (position == 3) {
            return ArcheologicalFragment.newInstanceArcheologicalFragment(Titles[3].toString().toUpperCase());
        } else if (position == 4) {
            return ArcheologicalFragment.newInstanceArcheologicalFragment(Titles[4].toString().toUpperCase());
        } else if (position == 5) {
            return ArcheologicalFragment.newInstanceArcheologicalFragment(Titles[5].toString().toUpperCase());
        } else if (position == 6) {
            return ArcheologicalFragment.newInstanceArcheologicalFragment(Titles[6].toString().toUpperCase());
        } else {
            return ArcheologicalFragment.newInstanceArcheologicalFragment(Titles[7].toString().toUpperCase());
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

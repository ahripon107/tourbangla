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

    public ViewPagerAdapterType(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
    }

    @Override
    public Fragment getItem(int position) {

        if(position == 0)
        {
            ArcheologicalFragment tab1 = ArcheologicalFragment.newInstanceArcheologicalFragment(Titles[0].toString().toUpperCase());
            return tab1;
        }
        else if(position==1)
        {
            ArcheologicalFragment tab2 = ArcheologicalFragment.newInstanceArcheologicalFragment(Titles[1].toString().toUpperCase());
            return tab2;
        }
        else if (position==2)
        {
            ArcheologicalFragment tab3 = ArcheologicalFragment.newInstanceArcheologicalFragment(Titles[2].toString().toUpperCase());
            return tab3;
        }
        else if (position==3)
        {
            ArcheologicalFragment tab4 = ArcheologicalFragment.newInstanceArcheologicalFragment(Titles[3].toString().toUpperCase());
            return tab4;
        }
        else if (position == 4)
        {
            ArcheologicalFragment tab5 = ArcheologicalFragment.newInstanceArcheologicalFragment(Titles[4].toString().toUpperCase());
            return tab5;
        }
        else if (position == 5)
        {
            ArcheologicalFragment tab6 = ArcheologicalFragment.newInstanceArcheologicalFragment(Titles[5].toString().toUpperCase());
            return tab6;
        }
        else if (position == 6)
        {
            ArcheologicalFragment tab7 = ArcheologicalFragment.newInstanceArcheologicalFragment(Titles[6].toString().toUpperCase());
            return tab7;
        }
        else
        {
            ArcheologicalFragment tab8 = ArcheologicalFragment.newInstanceArcheologicalFragment(Titles[7].toString().toUpperCase());
            return tab8;
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

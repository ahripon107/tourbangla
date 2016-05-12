package com.sfuronlabs.ripon.tourbangla;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by Ripon on 6/15/15.
 */
public class ViewPagerAdapterType extends FragmentStatePagerAdapter {
    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created


    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapterType(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;


    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        if(position == 0) // if the position is 0 we are returning the First tab
        {
            //ArcheologicalFragment tab1 = new ArcheologicalFragment();
            ArcheologicalFragment tab1 = ArcheologicalFragment.newInstanceArcheologicalFragment(Titles[0].toString().toUpperCase());
            return tab1;
        }
        else if(position==1)            // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
        {
            //BeachFragment tab2 = new BeachFragment();
            ArcheologicalFragment tab2 = ArcheologicalFragment.newInstanceArcheologicalFragment(Titles[1].toString().toUpperCase());
            return tab2;
        }
        else if (position==2)
        {
            ArcheologicalFragment tab3 = ArcheologicalFragment.newInstanceArcheologicalFragment(Titles[2].toString().toUpperCase());
            //ForestFragment tab3 = new ForestFragment();
            return tab3;
        }
        else if (position==3)
        {
            ArcheologicalFragment tab4 = ArcheologicalFragment.newInstanceArcheologicalFragment(Titles[3].toString().toUpperCase());
            //HillsFragment tab4 = new HillsFragment();
            return tab4;
        }
        else if (position == 4)
        {
            ArcheologicalFragment tab5 = ArcheologicalFragment.newInstanceArcheologicalFragment(Titles[4].toString().toUpperCase());
            //IslandsFragment tab5 = new IslandsFragment();
            return tab5;
        }
        else if (position == 5)
        {
            ArcheologicalFragment tab6 = ArcheologicalFragment.newInstanceArcheologicalFragment(Titles[5].toString().toUpperCase());
            //HistoricalFragment tab6 = new HistoricalFragment();
            return tab6;
        }
        else if (position == 6)
        {
            ArcheologicalFragment tab7 = ArcheologicalFragment.newInstanceArcheologicalFragment(Titles[6].toString().toUpperCase());
            //FragmentReligious tab7 = new FragmentReligious();
            return tab7;
        }
        else
        {
            ArcheologicalFragment tab8 = ArcheologicalFragment.newInstanceArcheologicalFragment(Titles[7].toString().toUpperCase());
            //OthersFragment tab8 = new OthersFragment();
            return tab8;
        }


    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}

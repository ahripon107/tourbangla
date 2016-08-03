package com.androidfragmant.tourxyz.banglatourism.activities;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;


import com.androidfragmant.tourxyz.banglatourism.FileProcessor;
import com.androidfragmant.tourxyz.banglatourism.fragment.FareFragment;
import com.androidfragmant.tourxyz.banglatourism.fragment.SuggestNewPlaceFragment;
import com.androidfragmant.tourxyz.banglatourism.fragment.UpdateDatabaseFragment;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;
import com.batch.android.Batch;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.RoboAppCompatActivity;
import com.androidfragmant.tourxyz.banglatourism.fragment.VisitedPlacesFragment;
import com.androidfragmant.tourxyz.banglatourism.fragment.AboutAppFragment;
import com.androidfragmant.tourxyz.banglatourism.fragment.FeedbackFragment;
import com.androidfragmant.tourxyz.banglatourism.fragment.FragmentDrawer;
import com.androidfragmant.tourxyz.banglatourism.fragment.HomeFragment;
import com.androidfragmant.tourxyz.banglatourism.fragment.WishListFragment;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_main)
public class MainActivity extends RoboAppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    @InjectView(R.id.adView)
    AdView adView;

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(mToolbar);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);

        boolean exists = false;
        String[] files = MainActivity.this.fileList();
        for (String file : files) {
            if (file.equals("data.txt")) {
                exists = true;
                break;
            } else {
                exists = false;
            }
        }
        if (exists) {
            FileProcessor fileProcessor = new FileProcessor(MainActivity.this);
            fileProcessor.readFileAndProcess();
        }
        FragmentDrawer drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
        displayView(0);

        AdRequest adRequest = new AdRequest.Builder().addTestDevice(Constants.ONE_PLUS_TEST_DEVICE).build();
        adView.loadAd(adRequest);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        Batch.onStart(this);
    }

    @Override
    protected void onStop()
    {
        Batch.onStop(this);

        super.onStop();
    }

    @Override
    protected void onDestroy()
    {
        Batch.onDestroy(this);

        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        Batch.onNewIntent(this, intent);

        super.onNewIntent(intent);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        String subtitle = "";
        switch (position) {
            case 0:

                fragment = new HomeFragment();
                subtitle = getString(R.string.title_home);
                break;
            case 1:
                fragment = new WishListFragment();
                subtitle = getString(R.string.title_messages);
                break;
            case 2:
                fragment = new VisitedPlacesFragment();
                subtitle = getString(R.string.title_visitedplaces);
                break;
            case 3:
                fragment = new AboutAppFragment();
                subtitle = getString(R.string.aboutapp);
                break;
            case 4:
                fragment = new FeedbackFragment();
                subtitle = getString(R.string.givefeedback);
                break;
            case 5:
                fragment = new SuggestNewPlaceFragment();
                subtitle = getString(R.string.suggestplace);
                break;
            case 6:
                fragment = new UpdateDatabaseFragment();
                subtitle = getString(R.string.updatedatabase);
                break;
            case 7:
                fragment = new FareFragment();
                subtitle = getString(R.string.fare);
                break;
            case 8:
                final String appPackageName = getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                break;
            case 9:
                final String packageName = getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
                }
                break;
            default:
                break;
        }

        getSupportActionBar().setTitle(title);
        getSupportActionBar().setSubtitle(subtitle);
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public void onBackPressed() {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.container_body);
        if (f instanceof HomeFragment) {
            super.onBackPressed();
        } else {
            displayView(0);
        }
    }
}

package com.androidfragmant.tourxyz.banglatourism.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidfragmant.tourxyz.banglatourism.FileProcessor;
import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.RoboAppCompatActivity;
import com.androidfragmant.tourxyz.banglatourism.fragment.FareFragment;
import com.androidfragmant.tourxyz.banglatourism.fragment.ForumPostListFragment;
import com.androidfragmant.tourxyz.banglatourism.fragment.TopPlacesFragment;
import com.androidfragmant.tourxyz.banglatourism.fragment.TourBlogListFragment;
import com.androidfragmant.tourxyz.banglatourism.fragment.TourOperatorOffersListFragment;
import com.androidfragmant.tourxyz.banglatourism.model.BlogPost;
import com.androidfragmant.tourxyz.banglatourism.model.LoginEvent;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;
import com.androidfragmant.tourxyz.banglatourism.view.RoundedImageView;
import com.batch.android.Batch;
import com.facebook.Profile;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_main)
public class MainActivity extends RoboAppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @InjectView(R.id.toolbar)
    private Toolbar toolbar;

    @InjectView(R.id.drawer_layout)
    private DrawerLayout drawer;

    @InjectView(R.id.nav_view)
    private NavigationView navigationView;

    @InjectView(R.id.tab_layout_home)
    private TabLayout tabLayout;

    @InjectView(R.id.home_view_pager)
    private ViewPager viewPager;

    private RoundedImageView imageView;
    private TextView name;
    private TextView email;

    private SectionPagerAdapter homePagerAdapter;
    private Profile profile;

    String[] titles = {"Top Places","Blog","Forum","Tour Offers","Fare"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);

        View relativeLayout =  navigationView.getHeaderView(0);
        imageView = (RoundedImageView) relativeLayout.findViewById(R.id.circleView);
        name = (TextView) relativeLayout.findViewById(R.id.name);
        email = (TextView) relativeLayout.findViewById(R.id.email);

        setProfileData();

        homePagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(homePagerAdapter);

        tabLayout.setupWithViewPager(viewPager);

        Typeface tf = Constants.iosFont(this);
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(tf);
                }
            }
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        //navigationView.getChildAt(0).setPadding(0, -10, 0, 0);

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

        EventBus.getDefault().register(this);
    }

    private void setProfileData() {
        profile = Profile.getCurrentProfile();
        if (profile != null) {
            Picasso.with(this).load(profile.getProfilePictureUri(70,70)).into(imageView);
            name.setText(profile.getName());
            email.setText("");
        } else {
            name.setText("Login");
            email.setText("");
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            });
            Picasso.with(this).load(R.drawable.default_image).into(imageView);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LoginEvent loginEvent) {
        setProfileData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Batch.onStart(this);
    }

    @Override
    protected void onStop() {
        Batch.onStop(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Batch.onDestroy(this);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Batch.onNewIntent(this, intent);
        super.onNewIntent(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_wishlist) {
            Intent intent = new Intent(this, WishListActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
        } else if (id == R.id.nav_visitedplaces) {
            Intent intent = new Intent(this, VisitedPlacesActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
        } else if (id == R.id.nav_aboutapp) {
            Intent intent = new Intent(this, AboutAppActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
        } else if (id == R.id.nav_feedback) {
            Intent intent = new Intent(this, FeedbackActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
        } else if (id == R.id.nav_suggestplace) {
            Intent intent = new Intent(this, SuggestNewPlaceActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
        } else if (id == R.id.nav_updatedatabase) {
            Intent intent = new Intent(this, UpdateDatabaseActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
        } else if (id == R.id.nav_costcalculator) {
            Intent intent = new Intent(this, TourCostCalculatorActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
        } else if (id == R.id.nav_login) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
        } else if (id == R.id.nav_rateus) {
            String appPackageName = getPackageName();
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        } else if (id == R.id.nav_update) {
            String appPackageName = getPackageName();
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class SectionPagerAdapter extends FragmentPagerAdapter {

        public SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new TopPlacesFragment();
            } else if (position == 1) {
                return new TourBlogListFragment();
            } else if (position == 2){
                return  new ForumPostListFragment();
            } else if (position == 3){
                return new TourOperatorOffersListFragment();
            } else
                return new FareFragment();
            }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}

package com.androidfragmant.tourxyz.banglatourism.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidfragmant.tourxyz.banglatourism.fragment.YoutubeVideoFragment;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;
import com.androidfragmant.tourxyz.banglatourism.util.DefaultMessageHandler;
import com.androidfragmant.tourxyz.banglatourism.util.FalseProgress;
import com.androidfragmant.tourxyz.banglatourism.util.NetworkService;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import com.google.gson.Gson;
import com.androidfragmant.tourxyz.banglatourism.PlaceAccessHelper;
import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.RoboAppCompatActivity;
import com.androidfragmant.tourxyz.banglatourism.fragment.CommentAddComment;
import com.androidfragmant.tourxyz.banglatourism.fragment.DescriptionFragment;
import com.androidfragmant.tourxyz.banglatourism.model.Place;
import com.androidfragmant.tourxyz.banglatourism.view.cpb.CircularProgressButton;
import com.google.inject.Inject;
import com.squareup.picasso.Picasso;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONException;
import org.json.JSONObject;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * @author Ripon
 */
@ContentView(R.layout.newplacedetails)
public class NewPlaceDetailsActivity extends RoboAppCompatActivity {

    @InjectView(R.id.root_coordinator)
    private CoordinatorLayout mCoordinator;

    @InjectView(R.id.collapsing_toolbar_layout)
    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    @InjectView(R.id.app_bar)
    private Toolbar mToolbar;

    @InjectView(R.id.view_pager)
    private ViewPager mPager;

    @InjectView(R.id.tab_layout)
    private TabLayout mTabLayout;

    @InjectView(R.id.placeimage)
    private ImageView imageView;

    @InjectView(R.id.btnAddToFavourite)
    private CircularProgressButton addToFavourite;

    @InjectView(R.id.btnBeenThere)
    private CircularProgressButton beenThere;

    private Typeface typeface;

    @Inject
    private NetworkService networkService;

    private CharSequence Titles[] = {"বর্ণনা", "কিভাবে যাবেন", "হোটেল", "অন্যান্য", "কমেন্ট", "ভিডিও"};
    private Place selectedPlace;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(mToolbar);

        typeface = Constants.solaimanLipiFont(this);

        if (Build.VERSION.SDK_INT >= 21) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*SupportMapFragment mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map123));

                if (mapFragment != null) {
                    FragmentManager fM = getSupportFragmentManager();
                    fM.beginTransaction().remove(mapFragment).commit();
                }*/
                finish();
                overridePendingTransition(R.anim.right_in, R.anim.right_out);
            }
        });

        id = getIntent().getExtras().getInt("id");

        if (!isFileExist()) {
            networkService.fetchSpecificPlace(id,new DefaultMessageHandler(this,true) {
                @Override
                public void onSuccess(Message msg) {
                    String string = (String) msg.obj;

                    try {
                        JSONObject response = new JSONObject(string);
                        response = response.getJSONArray("content").getJSONObject(0);
                        Gson gson = new Gson();
                        selectedPlace = gson.fromJson(String.valueOf(response),Place.class);
                        populateData();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            selectedPlace = PlaceAccessHelper.getPlace(id);
            populateData();
        }
    }

    private boolean isFileExist() {
        boolean exists = false;
        String[] files = fileList();
        for (String file : files) {
            if (file.equals("data.txt")) {
                exists = true;
                break;
            } else {
                exists = false;
            }
        }
        return exists;
    }

    private void populateData() {
        String picture = selectedPlace.getPicture();
        Log.d(Constants.TAG, picture);


        networkService.insertVisitedPlace(selectedPlace.getName(),System.currentTimeMillis()+"",
                new DefaultMessageHandler(this));

        Picasso.with(getApplicationContext()).load(picture).into(imageView);
        YourPagerAdapter mAdapter = new YourPagerAdapter(getSupportFragmentManager(), Titles, selectedPlace);
        mPager.setAdapter(mAdapter);

        mTabLayout.setupWithViewPager(mPager);
        changeTabsFont();
        mCollapsingToolbarLayout.setTitle(selectedPlace.getName());
        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBarPlus1);
        mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBarPlus1);

        addToFavourite.setText(R.string.addtowishlist);
        SharedPreferences sharedPreferenc = this.getSharedPreferences("wishlist", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editr = sharedPreferenc.edit();
        if (sharedPreferenc.contains(selectedPlace.getName())) {
            Log.d(Constants.TAG, "exist");
            addToFavourite.setText(R.string.removefromwishlist);
        }

        addToFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addToFavourite.getText().toString().equals("ADD TO WISHLIST")) {
                    Gson gson = new Gson();
                    String string = gson.toJson(selectedPlace);
                    editr.putString(selectedPlace.getName(), string).apply();
                    new FalseProgress(addToFavourite).execute(100);
                    addToFavourite.setText(R.string.removefromwishlist);
                } else {
                    editr.remove(selectedPlace.getName()).apply();
                    new FalseProgress(addToFavourite).execute(100);
                    addToFavourite.setText(R.string.addtowishlist);
                }
            }
        });
        beenThere.setText("I'VE VISITED THERE");
        final SharedPreferences sharedPreferences = this.getSharedPreferences("rating", Context.MODE_PRIVATE);
        if (sharedPreferences.contains(selectedPlace.getName())) {

            String string = sharedPreferences.getString(selectedPlace.getName(), null);
            Gson gson = new Gson();
            Place place = gson.fromJson(string, Place.class);
            int rating = place.getRating();
            if (rating == 5)
                beenThere.setText(R.string.iloveit);
            else if (rating == 4)
                beenThere.setText(R.string.ilikeit);
            else if (rating == 3)
                beenThere.setText(R.string.itsok);
            else if (rating == 2)
                beenThere.setText(R.string.dontlike);
            else if (rating == 1)
                beenThere.setText(R.string.hateit);


        }
        beenThere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final SharedPreferences.Editor editor;
                editor = sharedPreferences.edit();

                AlertDialog.Builder builderSingle = new AlertDialog.Builder(NewPlaceDetailsActivity.this);
                builderSingle.setIcon(R.drawable.ic_profile);
                builderSingle.setTitle("Rate the place:-");
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        NewPlaceDetailsActivity.this,
                        android.R.layout.select_dialog_singlechoice);
                arrayAdapter.add("I love it");
                arrayAdapter.add("I like it");
                arrayAdapter.add("Its ok");
                arrayAdapter.add("I dont like it");
                arrayAdapter.add("I hate it");
                arrayAdapter.add("I've not visited there");
                builderSingle.setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                builderSingle.setAdapter(arrayAdapter,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (sharedPreferences.contains(selectedPlace.getName())) {
                                    editor.remove(selectedPlace.getName()).apply();
                                }
                                Place product = selectedPlace;
                                Place place = new Place(product.getId(), product.getName(), product.getDescription(), product.getHowtogo(), product.getLattitude(), product.getLongitude(), product.getHotel(), product.getOthers(), product.getPicture(), product.getDivision(), product.getDistrict(), 5 - which);
                                Gson gson = new Gson();
                                String string = gson.toJson(place);
                                if (which != 5) {
                                    editor.putString(selectedPlace.getName(), string).apply();
                                }
                                new FalseProgress(beenThere).execute(100);

                                if (which == 0)
                                    beenThere.setText(R.string.iloveit);
                                else if (which == 1)
                                    beenThere.setText(R.string.ilikeit);
                                else if (which == 2)
                                    beenThere.setText(R.string.itsok);
                                else if (which == 3)
                                    beenThere.setText(R.string.dontlike);
                                else if (which == 4)
                                    beenThere.setText(R.string.hateit);
                                else
                                    beenThere.setText("I'VE VISITED THERE");
                            }
                        });
                builderSingle.show();
            }


        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
        /*SupportMapFragment mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map123));

        if (mapFragment != null) {
            FragmentManager fM = getSupportFragmentManager();
            fM.beginTransaction().remove(mapFragment).commit();
        }*/
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.facebook_share_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_load:
                ShareDialog shareDialog = new ShareDialog(NewPlaceDetailsActivity.this);
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    String appPackageName = getPackageName();
                    ShareLinkContent linkContent;
                    String aURL = "https://play.google.com/store/apps/details?id=" + appPackageName;
                    linkContent = new ShareLinkContent.Builder()
                            .setContentUrl(Uri.parse(aURL))
                            .build();
                    shareDialog.show(linkContent);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void changeTabsFont() {

        ViewGroup vg = (ViewGroup) mTabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(typeface);
                }
            }
        }
    }

}

class YourPagerAdapter extends FragmentStatePagerAdapter {
    CharSequence[] Titles;
    private Place SelectedPlace;

    public YourPagerAdapter(FragmentManager fm, CharSequence[] Titles, Place selectedPlace) {
        super(fm);
        this.Titles = Titles;
        this.SelectedPlace = selectedPlace;
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            return DescriptionFragment.newInstanceOfDescriptionFragment(SelectedPlace.getDescription());
        } else if (position == 1) {
            return DescriptionFragment.newInstanceOfDescriptionFragment(SelectedPlace.getHowtogo());
        } else if (position == 2) {
            return DescriptionFragment.newInstanceOfDescriptionFragment(SelectedPlace.getHotel());
        } else if (position == 3) {
            return DescriptionFragment.newInstanceOfDescriptionFragment(SelectedPlace.getOthers());
        } else if (position == 4) {
            CommentAddComment commentAddComment = new CommentAddComment();
            Bundle arguments = new Bundle();
            arguments.putInt("number", 1);
            arguments.putInt("id", SelectedPlace.getId());
            commentAddComment.setArguments(arguments);
            return commentAddComment;
        } else {
            return YoutubeVideoFragment.newInstanceOfYoutubeVideoFragment(SelectedPlace.getId());
        }
    }

    @Override
    public int getCount() {
        return Titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }


}


package com.androidfragmant.tourxyz.banglatourism.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.RoboAppCompatActivity;
import com.androidfragmant.tourxyz.banglatourism.fragment.BlogDetails;
import com.androidfragmant.tourxyz.banglatourism.fragment.CommentAddComment;
import com.squareup.picasso.Picasso;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * Created by Ripon on 8/27/15.
 */
@ContentView(R.layout.blogpostdetails)
public class TourBlogDetailsActivity extends RoboAppCompatActivity {

    @InjectView(R.id.blog_coordinator)
    private CoordinatorLayout mCoordinator;

    @InjectView(R.id.blogcollapsing_toolbar_layout)
    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    @InjectView(R.id.blogapp_bar)
    private Toolbar mToolbar;

    @InjectView(R.id.blogview_pager)
    private ViewPager mPager;

    @InjectView(R.id.blogtab_layout)
    private TabLayout mTabLayout;

    @InjectView(R.id.blogplaceimage)
    ImageView imageView;

    private CharSequence Titles[] = {"Post Details", "Comments"};
    private int NoOfTabs = 2;
    private MyPagerAdapter mAdapter;

    String blogtitle;
    String blogwriter;
    String blogdetails;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Intent i = getIntent();
        blogwriter = i.getStringExtra("name");
        blogtitle = i.getStringExtra("title");
        blogdetails = i.getStringExtra("details");
        String image = i.getStringExtra("image");
        id = i.getExtras().getInt("id");
        Picasso.with(TourBlogDetailsActivity.this).load(image).into(imageView);

        setTitle(blogtitle);
        mAdapter = new MyPagerAdapter(getSupportFragmentManager(), Titles, NoOfTabs, blogtitle, blogwriter, blogdetails, id);
        mPager.setAdapter(mAdapter);
        mTabLayout.setTabsFromPagerAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mPager);

        mCollapsingToolbarLayout.setTitle("Tour Blog");
        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBarPlus1);
        mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBarPlus1);


    }

}

class MyPagerAdapter extends FragmentStatePagerAdapter {
    CharSequence[] Titles;
    int NoOfTabs;
    String blogtitle;
    String blogwriter;
    String blogdetails;
    int id;

    public MyPagerAdapter(FragmentManager fm, CharSequence[] Titles, int NoOfTabs, String blogtitle, String blogwriter, String blogdetails, int id) {
        super(fm);
        this.Titles = Titles;
        this.NoOfTabs = NoOfTabs;
        this.blogtitle = blogtitle;
        this.blogwriter = blogwriter;
        this.blogdetails = blogdetails;
        this.id = id;

    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return BlogDetails.newInstanceofBlogDetails(blogtitle, blogwriter, blogdetails);
        } else {
            return CommentAddComment.NewInstanceofCommentAddComment(id, 2);
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }
}
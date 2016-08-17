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
import android.widget.ImageView;

import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.RoboAppCompatActivity;
import com.androidfragmant.tourxyz.banglatourism.fragment.BlogDetails;
import com.androidfragmant.tourxyz.banglatourism.fragment.CommentAddComment;
import com.androidfragmant.tourxyz.banglatourism.model.BlogPost;
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
        BlogPost blogPost = (BlogPost) i.getSerializableExtra("post");
        Picasso.with(TourBlogDetailsActivity.this).load(blogPost.getImage()).into(imageView);

        setTitle(blogPost.getTitle());

        MyPagerAdapter mAdapter = new MyPagerAdapter(getSupportFragmentManager(), Titles, blogPost);
        mPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mPager);

        mCollapsingToolbarLayout.setTitle(blogPost.getTitle());
        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBarPlus1);
        mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBarPlus1);
    }

    private class MyPagerAdapter extends FragmentStatePagerAdapter {
        CharSequence[] Titles;
        BlogPost blogPost;

        public MyPagerAdapter(FragmentManager fm, CharSequence[] Titles, BlogPost blogPost) {
            super(fm);
            this.Titles = Titles;
            this.blogPost = blogPost;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return BlogDetails.newInstanceofBlogDetails(blogPost.getTitle(), blogPost.getName(), blogPost.getDetails());
            } else {
                CommentAddComment commentAddComment = new CommentAddComment();
                Bundle arguments = new Bundle();
                arguments.putInt("number", 2);
                arguments.putInt("id", blogPost.getId());
                commentAddComment.setArguments(arguments);
                return commentAddComment;
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
}


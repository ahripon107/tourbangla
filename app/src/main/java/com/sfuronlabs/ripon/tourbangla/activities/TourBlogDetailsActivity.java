package com.sfuronlabs.ripon.tourbangla.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.sfuronlabs.ripon.tourbangla.R;
import com.sfuronlabs.ripon.tourbangla.RoboAppCompatActivity;
import com.sfuronlabs.ripon.tourbangla.fragment.BlogDetails;
import com.sfuronlabs.ripon.tourbangla.fragment.CommentAddComment;

import java.util.ArrayList;
import java.util.List;

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
    ParseObject parseObject;

    String blogtitle;
    String blogwriter;
    String blogdetails;

    ArrayList<CharSequence> names;
    ArrayList<CharSequence> comments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        names = new ArrayList<>();
        comments = new ArrayList<>();

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
        int index = i.getExtras().getInt("index");

        parseObject = TourBlogActivity.retrievedObjects.get(index);
        blogtitle = (String) parseObject.get("blogtitle");
        blogwriter = (String) parseObject.get("name");
        blogdetails = (String) parseObject.get("blogdetails");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("CommentOfBlogPost");
        query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.whereEqualTo("post", parseObject);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    for (int x = 0; x < list.size(); x++) {
                        names.add((String) list.get(x).get("commenter"));
                        comments.add((String) list.get(x).get("comment"));
                    }
                    ParseFile parseFile = (ParseFile) parseObject.get("picture");
                    parseFile.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] bytes, ParseException e) {
                            if (e == null) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                imageView.setImageBitmap(bitmap);
                            }
                        }
                    });

                    mAdapter = new MyPagerAdapter(getSupportFragmentManager(), Titles, NoOfTabs, blogtitle, blogwriter, blogdetails, names, comments);
                    mPager.setAdapter(mAdapter);
                    mTabLayout.setTabsFromPagerAdapter(mAdapter);
                    mTabLayout.setupWithViewPager(mPager);

                    mCollapsingToolbarLayout.setTitle("Tour Blog");
                    mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
                    mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
                    mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBarPlus1);
                    mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBarPlus1);

                } else {
                    Toast.makeText(TourBlogDetailsActivity.this, "Error occured", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

}

class MyPagerAdapter extends FragmentStatePagerAdapter {
    CharSequence[] Titles;
    int NoOfTabs;
    String blogtitle;
    String blogwriter;
    String blogdetails;
    ArrayList<CharSequence> names;
    ArrayList<CharSequence> comments;

    public MyPagerAdapter(FragmentManager fm, CharSequence[] Titles, int NoOfTabs, String blogtitle, String blogwriter, String blogdetails, ArrayList<CharSequence> names, ArrayList<CharSequence> comments) {
        super(fm);
        this.Titles = Titles;
        this.NoOfTabs = NoOfTabs;
        this.blogtitle = blogtitle;
        this.blogwriter = blogwriter;
        this.blogdetails = blogdetails;
        this.names = names;
        this.comments = comments;

    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            return BlogDetails.newInstanceofBlogDetails(blogtitle, blogwriter, blogdetails);
        } else {
            return CommentAddComment.NewInstanceofCommentAddComment(names, comments, blogdetails, 2);
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
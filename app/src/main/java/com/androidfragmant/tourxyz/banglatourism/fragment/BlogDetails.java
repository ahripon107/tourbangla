package com.androidfragmant.tourxyz.banglatourism.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

/**
 * Created by Ripon on 8/27/15.
 */
public class BlogDetails extends RoboFragment {

    public static final String BLOG_TITLE = "title";
    public static final String BLOG_WRITER = "writer";
    public static final String BLOG_DETAILS = "details";

    @InjectView(R.id.tvTitle)
    TextView title;

    @InjectView(R.id.tvWriter)
    TextView writer;

    @InjectView(R.id.tvDetailsofPost)
    TextView details;

    public BlogDetails() {

    }

    public static BlogDetails newInstanceofBlogDetails(String title, String writer, String details) {
        BlogDetails blogDetails = new BlogDetails();
        Bundle arguments = new Bundle();
        arguments.putString(BLOG_TITLE, title);
        arguments.putString(BLOG_WRITER, writer);
        arguments.putString(BLOG_DETAILS, details);
        blogDetails.setArguments(arguments);
        return blogDetails;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.blogdetails, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), Constants.SOLAIMAN_LIPI_FONT);
        title.setTypeface(tf);
        writer.setTypeface(tf);
        details.setTypeface(tf);
        title.setText(getArguments().getString(BLOG_TITLE));
        writer.setText("লিখেছেন: " + getArguments().getString(BLOG_WRITER));
        details.setText(getArguments().getString(BLOG_DETAILS));
    }
}

package com.sfuronlabs.ripon.tourbangla.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sfuronlabs.ripon.tourbangla.R;

/**
 * Created by Ripon on 8/27/15.
 */
public class BlogDetails extends Fragment {

    public BlogDetails()
    {

    }

    public static BlogDetails newInstanceofBlogDetails (String title,String writer,String details)
    {
        BlogDetails blogDetails = new BlogDetails();
        Bundle arguments = new Bundle();
        arguments.putString("title",title);
        arguments.putString("writer",writer);
        arguments.putString("details",details);
        blogDetails.setArguments(arguments);
        return blogDetails;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflater = (LayoutInflater)getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.blogdetails,null,false);
        TextView title = (TextView) v.findViewById(R.id.tvTitle);
        TextView writer = (TextView) v.findViewById(R.id.tvWriter);
        TextView details = (TextView) v.findViewById(R.id.tvDetailsofPost);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),"font/solaimanlipi.ttf");
        title.setTypeface(tf);
        writer.setTypeface(tf);
        details.setTypeface(tf);
        title.setText(getArguments().getString("title"));
        writer.setText("লিখেছেন: "+getArguments().getString("writer"));
        details.setText(getArguments().getString("details"));
        return v;
    }
}

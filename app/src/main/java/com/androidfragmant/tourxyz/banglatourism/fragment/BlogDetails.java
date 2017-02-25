package com.androidfragmant.tourxyz.banglatourism.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.model.BlogPost;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;
import com.androidfragmant.tourxyz.banglatourism.util.DefaultMessageHandler;
import com.androidfragmant.tourxyz.banglatourism.util.NetworkService;
import com.google.gson.Gson;
import com.google.inject.Inject;

import org.json.JSONException;
import org.json.JSONObject;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

/**
 * @author Ripon
 */
public class BlogDetails extends RoboFragment {

    @InjectView(R.id.tvTitle)
    private TextView title;

    @InjectView(R.id.tvWriter)
    private TextView writer;

    @InjectView(R.id.tvDetailsofPost)
    private TextView details;

    @Inject
    private NetworkService networkService;

    public static BlogDetails newInstanceofBlogDetails(int id) {
        BlogDetails blogDetails = new BlogDetails();
        Bundle arguments = new Bundle();
        arguments.putString("id", id+"");
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

        Typeface tf = Constants.solaimanLipiFont(getContext());
        title.setTypeface(tf);
        writer.setTypeface(tf);
        details.setTypeface(tf);

        networkService.fetchBlogDetails(getArguments().getString("id"),new DefaultMessageHandler(getContext(),true){
            @Override
            public void onSuccess(Message msg) {
                String string = (String) msg.obj;
                try {
                    JSONObject response = new JSONObject(string);
                    Gson gson = new Gson();
                    response = response.getJSONArray("details").getJSONObject(0);
                    BlogPost blogPost1 = gson.fromJson(String.valueOf(response), BlogPost.class);
                    title.setText(blogPost1.getTitle());
                    writer.setText("লিখেছেন: " + blogPost1.getName());
                    details.setText(blogPost1.getDetails());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}

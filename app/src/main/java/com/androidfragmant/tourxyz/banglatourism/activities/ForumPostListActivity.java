package com.androidfragmant.tourxyz.banglatourism.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.androidfragmant.tourxyz.banglatourism.FetchFromWeb;
import com.androidfragmant.tourxyz.banglatourism.model.ForumPost;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;
import com.androidfragmant.tourxyz.banglatourism.util.Validator;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.RoboAppCompatActivity;
import com.androidfragmant.tourxyz.banglatourism.adapter.ForumPostListAdapter;
import com.androidfragmant.tourxyz.banglatourism.view.ProgressWheel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * @author Ripon
 */
@ContentView(R.layout.forumpostlist)
public class ForumPostListActivity extends RoboAppCompatActivity {

    @InjectView(R.id.pwTourOperator)
    private ProgressWheel progressWheel;

    @InjectView(R.id.recyclerViewForumPostList)
    private RecyclerView recyclerView;

    @InjectView(R.id.toolbar2)
    private Toolbar toolbar;

    @InjectView(R.id.adViewTourOperator)
    private AdView adView;

    @InjectView(R.id.fabAddNewForumPost)
    private FloatingActionButton fab;

    @Inject
    private ArrayList<ForumPost> forumPosts;

    private ForumPostListAdapter forumPostListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressWheel.spin();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        forumPostListAdapter = new ForumPostListAdapter(this, forumPosts);
        recyclerView.setAdapter(forumPostListAdapter);

        fetchContents();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(ForumPostListActivity.this);
                View promptsView = li.inflate(R.layout.addnewforumpost, null,false);
                final EditText writeComment = (EditText) promptsView.findViewById(R.id.etYourQuestion);
                final EditText yourName = (EditText) promptsView.findViewById(R.id.etYourForumPostName);
                AlertDialog.Builder builder = new AlertDialog.Builder(ForumPostListActivity.this);
                builder.setView(promptsView);

                builder.setTitle("নতুন ফোরাম পোস্ট").setPositiveButton("SUBMIT", null).setNegativeButton("CANCEL", null);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                Button okButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Validator.validateNotEmpty(yourName,"Required") && Validator.validateNotEmpty(writeComment,"Required")) {
                            RequestParams params = new RequestParams();
                            params.put(Constants.KEY, Constants.KEY_VALUE);
                            params.put("name", yourName.getText().toString());
                            params.put("question", writeComment.getText().toString());
                            params.put("timestamp",System.currentTimeMillis()+"");


                            final ProgressDialog progressDialog = new ProgressDialog(ForumPostListActivity.this);
                            progressDialog.setMessage("Posting..Please wait...");
                            progressDialog.show();

                            Handler handler = new Handler(Looper.getMainLooper()) {
                                @Override
                                public void handleMessage(Message msg) {
                                    progressDialog.dismiss();
                                    if (msg.what == Constants.SUCCESS) {
                                        JSONObject response = (JSONObject) msg.obj;
                                        if (response!= null) {
                                            Toast.makeText(ForumPostListActivity.this, "Successfully Posted", Toast.LENGTH_LONG).show();
                                            fetchContents();
                                            Log.d(Constants.TAG, response.toString());
                                        } else {
                                            Toast.makeText(ForumPostListActivity.this, "Failed..Please try again..", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(ForumPostListActivity.this, "Failed..Please try again..", Toast.LENGTH_LONG).show();
                                    }
                                }
                            };

                            FetchFromWeb fetchFromWeb = new FetchFromWeb(handler);
                            fetchFromWeb.postData(Constants.INSERT_FORUM_POST_URL,params);

                            alertDialog.dismiss();
                        }
                    }
                });
            }
        });

        AdRequest adRequest = new AdRequest.Builder().addTestDevice(Constants.ONE_PLUS_TEST_DEVICE).build();
        adView.loadAd(adRequest);
    }

    public void fetchContents() {
        progressWheel.setVisibility(View.VISIBLE);
        progressWheel.spin();

        RequestParams requestParams = new RequestParams();
        requestParams.add(Constants.KEY,Constants.KEY_VALUE);

        Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (progressWheel.isSpinning()) {
                    progressWheel.stopSpinning();
                    progressWheel.setVisibility(View.INVISIBLE);
                }
                if (msg.what == Constants.SUCCESS) {
                    JSONObject response = (JSONObject) msg.obj;
                    if (response!= null) {
                        forumPosts.clear();
                        try {
                            JSONArray jsonArray = response.getJSONArray("content");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Gson gson = new Gson();
                                ForumPost forumPost = gson.fromJson(String.valueOf(jsonObject),ForumPost.class);
                                forumPosts.add(forumPost);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        forumPostListAdapter.notifyDataSetChanged();
                        Log.d(Constants.TAG, response.toString());
                    } else {
                        Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
                }
            }
        };

        FetchFromWeb fetchFromWeb = new FetchFromWeb(handler);
        fetchFromWeb.retreiveData(Constants.FETCH_FORUM_POSTS_URL,requestParams);

    }
}

package com.androidfragmant.tourxyz.banglatourism.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.androidfragmant.tourxyz.banglatourism.FetchFromWeb;
import com.androidfragmant.tourxyz.banglatourism.model.ForumPost;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;
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
 * Created by Ripon on 6/18/15.
 */
@ContentView(R.layout.forumpostlist)
public class ForumPostListActivity extends RoboAppCompatActivity {

    @InjectView(R.id.pwTourOperator)
    ProgressWheel progressWheel;

    @InjectView(R.id.recyclerViewForumPostList)
    RecyclerView recyclerView;

    @InjectView(R.id.toolbar2)
    Toolbar toolbar;

    @InjectView(R.id.adViewTourOperator)
    AdView adView;

    @InjectView(R.id.fabAddNewForumPost)
    private FloatingActionButton fab;

    ArrayList<ForumPost> forumPosts;

    ForumPostListAdapter forumPostListAdapter;

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
        setTitle("Forum");

        forumPosts = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ForumPostListActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        forumPostListAdapter = new ForumPostListAdapter(ForumPostListActivity.this, forumPosts);
        recyclerView.setAdapter(forumPostListAdapter);

        String url = Constants.FETCH_FORUM_POSTS_URL;
        Log.d(Constants.TAG, url);

        FetchFromWeb.get(url, null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (progressWheel.isSpinning()) {
                    progressWheel.stopSpinning();
                    progressWheel.setVisibility(View.INVISIBLE);
                }

                try {
                    JSONArray jsonArray = response.getJSONArray("content");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int id = jsonObject.getInt("id");
                        String name = jsonObject.getString("name");
                        String question = jsonObject.getString("question");
                        ForumPost forumPost = new ForumPost(id, name, question);
                        forumPosts.add(forumPost);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                forumPostListAdapter.notifyDataSetChanged();
                Log.d(Constants.TAG, response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (progressWheel.isSpinning()) {
                    progressWheel.stopSpinning();
                    progressWheel.setVisibility(View.INVISIBLE);
                }
                Toast.makeText(ForumPostListActivity.this, statusCode + "failure", Toast.LENGTH_SHORT).show();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(ForumPostListActivity.this);
                View promptsView = li.inflate(R.layout.addnewforumpost, null);
                final EditText writeComment = (EditText) promptsView.findViewById(R.id.etYourQuestion);
                final EditText yourName = (EditText) promptsView.findViewById(R.id.etYourForumPostName);
                AlertDialog.Builder builder = new AlertDialog.Builder(ForumPostListActivity.this);
                builder.setView(promptsView);
                builder.setTitle("New Forum Post");
                builder.setCancelable(false)
                        .setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final String comment = writeComment.getText().toString().trim();
                                final String name = yourName.getText().toString().trim();
                                if (comment.length() == 0 || name.length() == 0) {
                                    Toast.makeText(ForumPostListActivity.this, "Please give input correctly", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                writeComment.getText().clear();
                                yourName.getText().clear();

                                RequestParams params = new RequestParams();
                                params.put("key", "bl905577");
                                params.put("name", name);
                                params.put("question", comment);

                                String url1 = Constants.INSERT_FORUM_POST_URL;


                                final ProgressDialog progressDialog = new ProgressDialog(ForumPostListActivity.this);
                                progressDialog.setMessage("Posting..Please wait...");
                                progressDialog.show();
                                FetchFromWeb.post(url1, params, new JsonHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        progressDialog.dismiss();
                                        Toast.makeText(ForumPostListActivity.this, "Successfully Posted", Toast.LENGTH_LONG).show();

                                        Log.d(Constants.TAG, response.toString());
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                        progressDialog.dismiss();
                                        Toast.makeText(ForumPostListActivity.this, "Failed..Please try again..", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }


                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        AdRequest adRequest = new AdRequest.Builder().addTestDevice("7D3F3DF2A7214E839DBE70BE2132D5B9").build();
        adView.loadAd(adRequest);
    }
}

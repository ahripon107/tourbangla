package com.androidfragmant.tourxyz.banglatourism.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidfragmant.tourxyz.banglatourism.model.ForumPost;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;
import com.androidfragmant.tourxyz.banglatourism.util.DefaultMessageHandler;
import com.androidfragmant.tourxyz.banglatourism.util.NetworkService;
import com.androidfragmant.tourxyz.banglatourism.util.Validator;
import com.google.gson.Gson;
import com.google.inject.Inject;

import java.util.ArrayList;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.RoboAppCompatActivity;
import com.androidfragmant.tourxyz.banglatourism.adapter.ForumPostListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * @author Ripon
 */
@ContentView(R.layout.forumpostlist)
public class ForumPostListActivity extends RoboAppCompatActivity {

    @InjectView(R.id.recyclerViewForumPostList)
    private RecyclerView recyclerView;

    @InjectView(R.id.adViewTourOperator)
    private AdView adView;

    @InjectView(R.id.fabAddNewForumPost)
    private FloatingActionButton fab;

    @Inject
    private ArrayList<ForumPost> forumPosts;

    @Inject
    private NetworkService networkService;

    private ForumPostListAdapter forumPostListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        forumPostListAdapter = new ForumPostListAdapter(this, forumPosts);
        recyclerView.setAdapter(forumPostListAdapter);

        fetchContents();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(ForumPostListActivity.this);
                View promptsView = li.inflate(R.layout.addnewforumpost, null, false);
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
                        if (Validator.validateNotEmpty(yourName, "Required") && Validator.validateNotEmpty(writeComment, "Required")) {

                            networkService.insertForumPost(yourName.getText().toString(), writeComment.getText().toString(), System.currentTimeMillis() + "",
                                    new DefaultMessageHandler(ForumPostListActivity.this, true) {
                                        @Override
                                        public void onSuccess(Message msg) {
                                            Toast.makeText(ForumPostListActivity.this, "Successfully Posted", Toast.LENGTH_LONG).show();
                                            fetchContents();
                                        }
                                    });

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

        networkService.fetchForumPostList(new DefaultMessageHandler(this, true) {
            @Override
            public void onSuccess(Message msg) {
                String string = (String) msg.obj;

                try {
                    JSONObject response = new JSONObject(string);
                    forumPosts.clear();

                    JSONArray jsonArray = response.getJSONArray("content");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Gson gson = new Gson();
                        ForumPost forumPost = gson.fromJson(String.valueOf(jsonObject), ForumPost.class);
                        forumPosts.add(forumPost);

                        forumPostListAdapter.notifyDataSetChanged();
                        Log.d(Constants.TAG, response.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.right_in, R.anim.right_out);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }
}

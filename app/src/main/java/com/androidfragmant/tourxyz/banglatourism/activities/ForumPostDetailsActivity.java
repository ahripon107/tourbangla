package com.androidfragmant.tourxyz.banglatourism.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidfragmant.tourxyz.banglatourism.FetchFromWeb;
import com.androidfragmant.tourxyz.banglatourism.adapter.CommentAdapter;
import com.androidfragmant.tourxyz.banglatourism.model.Comment;
import com.androidfragmant.tourxyz.banglatourism.model.ForumPost;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;
import com.androidfragmant.tourxyz.banglatourism.util.Validator;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.RoboAppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * Created by Ripon on 8/28/15.
 */
@ContentView(R.layout.forumpostdetails)
public class ForumPostDetailsActivity extends RoboAppCompatActivity {

    @InjectView(R.id.touroperatorapp_bar)
    private Toolbar mToolbar;

    @InjectView(R.id.tvQuestion)
    private TextView question;

    @InjectView(R.id.tvAskedBy)
    private TextView askedBy;

    @InjectView(R.id.forumPostCommentList)
    private RecyclerView recyclerView;

    @InjectView(R.id.adViewForumPostDetails)
    AdView adView;

    @InjectView(R.id.btnAnswer)
    private Button btnAns;

    String nameString, questionString;

    ArrayList<Comment> comments;

    CommentAdapter commentAdapter;

    Typeface tf;


    @Override
    public void onCreate(Bundle savedInstanceState) {
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

        comments = new ArrayList<>();
        tf = Typeface.createFromAsset(getAssets(), Constants.SOLAIMAN_LIPI_FONT);

        commentAdapter = new CommentAdapter(ForumPostDetailsActivity.this, comments);
        recyclerView.setAdapter(commentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ForumPostDetailsActivity.this));


        Intent i = getIntent();
        ForumPost forumPost = (ForumPost) i.getSerializableExtra("forumpost");

        nameString = forumPost.getName();
        questionString = forumPost.getQuestion();
        final int id = forumPost.getId();
        setTitle(questionString);

        askedBy.setTypeface(tf);
        question.setTypeface(tf);
        askedBy.setText("পোস্ট করেছেনঃ " + nameString);
        question.setText(questionString);

        RequestParams requestParams = new RequestParams();
        requestParams.add(Constants.KEY,Constants.KEY_VALUE);
        requestParams.add("postid",id+"");

        String url = Constants.FETCH_FORUM_POST_COMMENTS;
        Log.d(Constants.TAG, url);

        final ProgressDialog progressDialog1 = new ProgressDialog(ForumPostDetailsActivity.this);
        progressDialog1.setMessage("Please Wait...");
        progressDialog1.show();

        FetchFromWeb.get(url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progressDialog1.dismiss();
                try {
                    JSONArray jsonArray = response.getJSONArray("content");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        comments.add(new Comment(jsonObject.getString("name"),jsonObject.getString("comment"),jsonObject.getString("timestamp")));


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                commentAdapter.notifyDataSetChanged();
                if (comments.size() != 0) {
                    recyclerView.smoothScrollToPosition(comments.size()-1);
                }

                Log.d(Constants.TAG, response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progressDialog1.dismiss();
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });

        btnAns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(ForumPostDetailsActivity.this);
                View promptsView = li.inflate(R.layout.addnewcomment, null,false);
                final EditText writeComment = (EditText) promptsView.findViewById(R.id.etYourComment);
                final EditText yourName = (EditText) promptsView.findViewById(R.id.etYourName);
                AlertDialog.Builder builder = new AlertDialog.Builder(ForumPostDetailsActivity.this);
                builder.setView(promptsView);
                builder.setTitle("মন্তব্য").setPositiveButton("SUBMIT", null).setNegativeButton("CANCEL", null);

                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                Button okButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Validator.validateNotEmpty(yourName,"Required") && Validator.validateNotEmpty(writeComment,"Required")) {
                            final String comment = writeComment.getText().toString().trim();
                            final String name = yourName.getText().toString().trim();

                            RequestParams params = new RequestParams();

                            params.put(Constants.KEY, Constants.KEY_VALUE);
                            params.put("name", name);
                            params.put("postid", id);
                            params.put("comment", comment);
                            params.put("timestamp",System.currentTimeMillis()+"");
                            String url1 = Constants.INSERT_FORUM_POST_COMMENT_URL;
                            final ProgressDialog progressDialog = new ProgressDialog(ForumPostDetailsActivity.this);
                            progressDialog.setMessage("Posting comment..Please wait...");
                            progressDialog.show();
                            FetchFromWeb.post(url1, params, new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    progressDialog.dismiss();
                                    Toast.makeText(ForumPostDetailsActivity.this, "Comment successfully posted", Toast.LENGTH_LONG).show();
                                    comments.add(new Comment(name,comment,System.currentTimeMillis()+""));
                                    commentAdapter.notifyDataSetChanged();
                                    if (comments.size() != 0) {
                                        recyclerView.smoothScrollToPosition(comments.size()-1);
                                    }
                                    Log.d(Constants.TAG, response.toString());
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                    progressDialog.dismiss();
                                    Toast.makeText(ForumPostDetailsActivity.this, "Failed", Toast.LENGTH_LONG).show();
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
}
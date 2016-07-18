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
import com.androidfragmant.tourxyz.banglatourism.util.Constants;
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

    @InjectView(R.id.adViewTourOperatorDetails)
    AdView adView;

    @InjectView(R.id.btnAnswer)
    private Button btnAns;

    String nameString, questionString;

    ArrayList<String> names,comments;

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

        tf = Typeface.createFromAsset(getAssets(),"font/solaimanlipi.ttf");

        names = new ArrayList<>();
        comments = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ForumPostDetailsActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        commentAdapter = new CommentAdapter(ForumPostDetailsActivity.this,names,comments);
        recyclerView.setAdapter(commentAdapter);

        Intent i = getIntent();
        nameString = i.getExtras().getString("name");
        questionString = i.getExtras().getString("question");
        final int id = i.getExtras().getInt("id");
        setTitle(questionString);

        askedBy.setTypeface(tf);
        question.setTypeface(tf);
        askedBy.setText("পোস্ট করেছেনঃ "+nameString);
        question.setText(questionString);

        String url = Constants.FETCH_FORUM_POST_COMMENTS+id;
        Log.d(Constants.TAG, url);

        final ProgressDialog progressDialog1 = new ProgressDialog(ForumPostDetailsActivity.this);
        progressDialog1.setMessage("Please Wait...");
        progressDialog1.show();

        FetchFromWeb.get(url,null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progressDialog1.dismiss();
                try {
                    JSONArray jsonArray = response.getJSONArray("content");
                    for (int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int id = jsonObject.getInt("id");
                        String name = jsonObject.getString("name");
                        String comment = jsonObject.getString("comment");
                        names.add(name);
                        comments.add(comment);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                commentAdapter.notifyDataSetChanged();
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
                View promptsView = li.inflate(R.layout.addnewcomment, null);
                final EditText writeComment = (EditText) promptsView.findViewById(R.id.etYourComment);
                final EditText yourName = (EditText) promptsView.findViewById(R.id.etYourName);
                AlertDialog.Builder builder = new AlertDialog.Builder(ForumPostDetailsActivity.this);
                builder.setView(promptsView);
                builder.setTitle("Comment");
                builder.setCancelable(false)
                        .setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final String comment = writeComment.getText().toString().trim();
                                final String name = yourName.getText().toString().trim();
                                if (comment.length() == 0 || name.length() == 0) {
                                    Toast.makeText(ForumPostDetailsActivity.this, "Please give input correctly", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                writeComment.getText().clear();
                                yourName.getText().clear();

                                RequestParams params = new RequestParams();

                                params.put("key","bl905577");
                                params.put("name",name);
                                params.put("postid",id);
                                params.put("comment",comment);
                                 String url1 = Constants.INSERT_FORUM_POST_COMMENT_URL;
                                final ProgressDialog progressDialog = new ProgressDialog(ForumPostDetailsActivity.this);
                                progressDialog.setMessage("Posting comment..Please wait...");
                                progressDialog.show();
                                FetchFromWeb.post(url1,params,new JsonHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        progressDialog.dismiss();
                                        Toast.makeText(ForumPostDetailsActivity.this, "Comment successfully posted", Toast.LENGTH_LONG).show();
                                        names.add(0,name);
                                        comments.add(0,comment);
                                        commentAdapter.notifyDataSetChanged();
                                        Log.d(Constants.TAG, response.toString());
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                        progressDialog.dismiss();
                                        Toast.makeText(ForumPostDetailsActivity.this, "failed", Toast.LENGTH_LONG).show();
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
package com.androidfragmant.tourxyz.banglatourism.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidfragmant.tourxyz.banglatourism.FetchFromWeb;
import com.androidfragmant.tourxyz.banglatourism.model.Comment;
import com.androidfragmant.tourxyz.banglatourism.model.ForumPost;
import com.androidfragmant.tourxyz.banglatourism.util.AbstractListAdapter;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;
import com.androidfragmant.tourxyz.banglatourism.util.Validator;
import com.androidfragmant.tourxyz.banglatourism.util.ViewHolder;
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

    Typeface tf;

    ProgressDialog progressDialog;


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

        progressDialog = new ProgressDialog(ForumPostDetailsActivity.this);
        progressDialog.setMessage("Please Wait...");

        comments = new ArrayList<>();
        tf = Typeface.createFromAsset(getAssets(), Constants.SOLAIMAN_LIPI_FONT);

        recyclerView.setAdapter(new AbstractListAdapter<Comment,ForumCommentViewHolder>(comments) {
            @Override
            public ForumCommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlecomment, parent, false);
                return new ForumCommentViewHolder(v);
            }

            @Override
            public void onBindViewHolder(ForumCommentViewHolder holder, int position) {
                holder.comment.setTypeface(tf);
                holder.commenter.setTypeface(tf);
                holder.commenter.setText("মন্তব্য করেছেন:  " + comments.get(position).getName());
                holder.comment.setText(comments.get(position).getComment());
                holder.timestamp.setText(Constants.getTimeAgo(Long.parseLong(comments.get(position).getTimestamp())));
            }

        });
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


        progressDialog.show();

        Handler handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                progressDialog.dismiss();
                if (msg.what == Constants.SUCCESS) {
                    JSONObject response = (JSONObject) msg.obj;

                    if (response != null) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("content");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                comments.add(new Comment(jsonObject.getString("name"),jsonObject.getString("comment"),jsonObject.getString("timestamp")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        recyclerView.getAdapter().notifyDataSetChanged();
                        if (comments.size() != 0) {
                            recyclerView.smoothScrollToPosition(comments.size()-1);
                        }

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
        fetchFromWeb.retreiveData(Constants.FETCH_FORUM_POST_COMMENTS,requestParams);


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

                            progressDialog.show();
                            Handler handler1 = new Handler(Looper.getMainLooper()){
                                @Override
                                public void handleMessage(Message msg) {
                                    progressDialog.dismiss();
                                    if (msg.what == Constants.SUCCESS) {
                                        JSONObject response = (JSONObject) msg.obj;
                                        if (response != null) {
                                            Toast.makeText(ForumPostDetailsActivity.this, "Comment successfully posted", Toast.LENGTH_LONG).show();
                                            comments.add(new Comment(name,comment,System.currentTimeMillis()+""));
                                            recyclerView.getAdapter().notifyDataSetChanged();
                                            if (comments.size() != 0) {
                                                recyclerView.smoothScrollToPosition(comments.size()-1);
                                            }
                                            Log.d(Constants.TAG, response.toString());
                                        } else {
                                            Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            };

                            FetchFromWeb send = new FetchFromWeb(handler1);
                            send.postData(Constants.INSERT_FORUM_POST_COMMENT_URL,params);

                            alertDialog.dismiss();
                        }
                    }
                });
            }
        });

        AdRequest adRequest = new AdRequest.Builder().addTestDevice(Constants.ONE_PLUS_TEST_DEVICE).build();
        adView.loadAd(adRequest);

    }

    private static class ForumCommentViewHolder extends RecyclerView.ViewHolder {
        protected TextView commenter;
        protected TextView comment;
        protected TextView timestamp;

        public ForumCommentViewHolder(View v) {
            super(v);
            commenter = ViewHolder.get(v, R.id.tvName);
            comment = ViewHolder.get(v, R.id.tvComment);
            timestamp = ViewHolder.get(itemView, R.id.tv_time_stamp);
        }
    }
}
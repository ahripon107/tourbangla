package com.androidfragmant.tourxyz.banglatourism.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidfragmant.tourxyz.banglatourism.BuildConfig;
import com.androidfragmant.tourxyz.banglatourism.model.Comment;
import com.androidfragmant.tourxyz.banglatourism.model.ForumPost;
import com.androidfragmant.tourxyz.banglatourism.util.AbstractListAdapter;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;
import com.androidfragmant.tourxyz.banglatourism.util.DefaultMessageHandler;
import com.androidfragmant.tourxyz.banglatourism.util.NetworkService;
import com.androidfragmant.tourxyz.banglatourism.util.Validator;
import com.androidfragmant.tourxyz.banglatourism.util.ViewHolder;
import com.google.inject.Inject;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.RoboAppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * @author Ripon
 */
@ContentView(R.layout.forumpostdetails)
public class ForumPostDetailsActivity extends RoboAppCompatActivity {

    @InjectView(R.id.tvQuestion)
    private TextView question;

    @InjectView(R.id.tvAskedBy)
    private TextView askedBy;

    @InjectView(R.id.forumPostCommentList)
    private RecyclerView recyclerView;

    @InjectView(R.id.adViewForumPostDetails)
    private AdView adView;

    @InjectView(R.id.btnAnswer)
    private Button btnAns;

    @Inject
    private ArrayList<Comment> comments;

    @Inject
    private NetworkService networkService;

    private Typeface tf;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tf = Constants.solaimanLipiFont(this);

        recyclerView.setAdapter(new AbstractListAdapter<Comment, ForumCommentViewHolder>(comments) {
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

        ForumPost forumPost = (ForumPost) getIntent().getSerializableExtra("forumpost");

        String nameString = forumPost.getName();
        String questionString = forumPost.getQuestion();
        final int id = forumPost.getId();
        setTitle(questionString);

        askedBy.setTypeface(tf);
        question.setTypeface(tf);
        askedBy.setText("পোস্ট করেছেনঃ " + nameString);
        question.setText(questionString);


        networkService.fetchForumPostComments(forumPost.getId(), new DefaultMessageHandler(this, true) {
            @Override
            public void onSuccess(Message msg) {
                String string = (String) msg.obj;
                try {
                    JSONObject response = new JSONObject(string);
                    JSONArray jsonArray = response.getJSONArray("content");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        comments.add(new Comment(jsonObject.getString("name"), jsonObject.getString("comment"), jsonObject.getString("timestamp")));
                    }
                    recyclerView.getAdapter().notifyDataSetChanged();
                    if (comments.size() != 0) {
                        recyclerView.smoothScrollToPosition(comments.size() - 1);
                    }

                    if (BuildConfig.DEBUG)
                        Log.d(Constants.TAG, response.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        btnAns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(ForumPostDetailsActivity.this);
                View promptsView = li.inflate(R.layout.addnewcomment, null, false);

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
                        if (Validator.validateNotEmpty(yourName, "Required") && Validator.validateNotEmpty(writeComment, "Required")) {
                            final String comment = writeComment.getText().toString().trim();
                            final String name = yourName.getText().toString().trim();

                            networkService.insertForumPostComment(name, String.valueOf(id), comment, System.currentTimeMillis() + "",
                                    new DefaultMessageHandler(ForumPostDetailsActivity.this, true) {
                                        @Override
                                        public void onSuccess(Message msg) {
                                            Toast.makeText(ForumPostDetailsActivity.this, "Comment successfully posted", Toast.LENGTH_LONG).show();
                                            comments.add(new Comment(name, comment, System.currentTimeMillis() + ""));
                                            recyclerView.getAdapter().notifyDataSetChanged();
                                            if (comments.size() != 0) {
                                                recyclerView.smoothScrollToPosition(comments.size() - 1);
                                            }
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
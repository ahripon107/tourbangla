package com.androidfragmant.tourxyz.banglatourism.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.facebook.Profile;
import com.google.inject.Inject;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.RoboAppCompatActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * @author Ripon
 */
@ContentView(R.layout.activity_forum_post_details)
public class ForumPostDetailsActivity extends RoboAppCompatActivity {

    @InjectView(R.id.tvQuestion)
    private TextView question;

    @InjectView(R.id.tvAskedBy)
    private TextView askedBy;

    @InjectView(R.id.forumPostCommentList)
    private RecyclerView recyclerView;

    @InjectView(R.id.btnSubmitComment)
    private ImageButton btnAns;

    @InjectView(R.id.et_comment)
    private EditText commentEditText;

    @Inject
    private ArrayList<Comment> comments;

    @Inject
    private NetworkService networkService;

    private Typeface tf;
    private Profile profile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tf = Constants.solaimanLipiFont(this);

        recyclerView.setAdapter(new AbstractListAdapter<Comment, ForumCommentViewHolder>(comments) {
            @Override
            public ForumCommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_comment, parent, false);
                return new ForumCommentViewHolder(v);
            }

            @Override
            public void onBindViewHolder(ForumCommentViewHolder holder, int position) {
                holder.comment.setTypeface(tf);
                holder.commenter.setTypeface(tf);
                holder.commenter.setText("মন্তব্য করেছেন:  " + comments.get(position).getName());
                holder.comment.setText(comments.get(position).getComment());
                if (!comments.get(position).getProfileimage().equals("")) {
                    Picasso.with(ForumPostDetailsActivity.this).load(comments.get(position).getProfileimage()).into(holder.profileImageView);
                }
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
                        comments.add(new Comment(jsonObject.getString("name"), jsonObject.getString("comment"),jsonObject.getString("profileimage"), jsonObject.getString("timestamp")));
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
                profile = Profile.getCurrentProfile();
                if (profile != null) {
                    if (Validator.validateNotEmpty(commentEditText, "Required")) {
                        final String comment = commentEditText.getText().toString().trim();

                        networkService.insertForumPostComment(profile.getName(), String.valueOf(id), comment, profile.getProfilePictureUri(50,50).toString(), System.currentTimeMillis() + "",
                                new DefaultMessageHandler(ForumPostDetailsActivity.this, true) {
                                    @Override
                                    public void onSuccess(Message msg) {
                                        Toast.makeText(ForumPostDetailsActivity.this, "Comment successfully posted", Toast.LENGTH_LONG).show();
                                        comments.add(new Comment(profile.getName(), comment,profile.getProfilePictureUri(50,50).toString(), System.currentTimeMillis() + ""));
                                        commentEditText.getText().clear();
                                        recyclerView.getAdapter().notifyDataSetChanged();
                                        if (comments.size() != 0) {
                                            recyclerView.smoothScrollToPosition(comments.size() - 1);
                                        }
                                    }
                                });
                    }
                } else {
                    Intent intent = new Intent(ForumPostDetailsActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private static class ForumCommentViewHolder extends RecyclerView.ViewHolder {
        protected TextView commenter;
        protected TextView comment;
        protected TextView timestamp;
        private ImageView profileImageView;

        public ForumCommentViewHolder(View v) {
            super(v);
            commenter = ViewHolder.get(v, R.id.tvName);
            comment = ViewHolder.get(v, R.id.tvComment);
            timestamp = ViewHolder.get(v, R.id.tv_time_stamp);
            profileImageView = ViewHolder.get(v, R.id.profile_image);
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
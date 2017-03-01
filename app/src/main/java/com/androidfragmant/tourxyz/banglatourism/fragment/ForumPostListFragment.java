package com.androidfragmant.tourxyz.banglatourism.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidfragmant.tourxyz.banglatourism.activities.ForumPostDetailsActivity;
import com.androidfragmant.tourxyz.banglatourism.model.ForumPost;
import com.androidfragmant.tourxyz.banglatourism.util.AbstractListAdapter;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;
import com.androidfragmant.tourxyz.banglatourism.util.DefaultMessageHandler;
import com.androidfragmant.tourxyz.banglatourism.util.NetworkService;
import com.androidfragmant.tourxyz.banglatourism.util.Validator;
import com.androidfragmant.tourxyz.banglatourism.util.ViewHolder;
import com.google.gson.Gson;
import com.google.inject.Inject;

import java.util.ArrayList;

import com.androidfragmant.tourxyz.banglatourism.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

/**
 * @author Ripon
 */
public class ForumPostListFragment extends RoboFragment {

    @InjectView(R.id.list)
    private RecyclerView recyclerView;

    @InjectView(R.id.fab)
    private FloatingActionButton fab;

    @Inject
    private NetworkService networkService;

    private AbstractListAdapter<ForumPost, ForumPostViewHolder> forumPostListAdapter;
    private ArrayList<ForumPost> forumPosts;
    private Typeface tf;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        forumPosts = new ArrayList<>();
        tf = Constants.solaimanLipiFont(getContext());

        forumPostListAdapter = new AbstractListAdapter<ForumPost, ForumPostViewHolder>(forumPosts) {
            @Override
            public ForumPostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_forum_post,parent,false);
                return new ForumPostViewHolder(view);
            }

            @Override
            public void onBindViewHolder(ForumPostViewHolder holder, final int position) {
                holder.question.setTypeface(tf);
                holder.askedby.setTypeface(tf);
                holder.question.setText(forumPosts.get(position).getQuestion());
                holder.askedby.setText("পোস্ট করেছেনঃ "+forumPosts.get(position).getName());
                holder.timeStamp.setText(Constants.getTimeAgo(Long.parseLong(forumPosts.get(position).getTimestamp())));
                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getContext(),ForumPostDetailsActivity.class);
                        i.putExtra("forumpost",forumPosts.get(position));
                        getActivity().startActivity(i);
                        getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    }
                });
                Constants.setLeftInAnimation(holder.cardView,getContext());
                Constants.setRightInAnimation(holder.question,getContext());
            }
        };

        fetchContents();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_with_fab,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(forumPostListAdapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(getContext());
                View promptsView = li.inflate(R.layout.dialog_add_new_forum_post, null, false);
                final EditText writeComment = (EditText) promptsView.findViewById(R.id.etYourQuestion);
                final EditText yourName = (EditText) promptsView.findViewById(R.id.etYourForumPostName);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
                                    new DefaultMessageHandler(getContext(), true) {
                                        @Override
                                        public void onSuccess(Message msg) {
                                            Toast.makeText(getContext(), "Successfully Posted", Toast.LENGTH_LONG).show();
                                            fetchContents();
                                        }
                                    });

                            alertDialog.dismiss();
                        }
                    }
                });
            }
        });
    }

    public void fetchContents() {

        networkService.fetchForumPostList(new DefaultMessageHandler(getContext(), true) {
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

                    }
                    Log.d(Constants.TAG, response.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static class ForumPostViewHolder extends RecyclerView.ViewHolder {
        protected TextView question;
        protected TextView askedby;
        protected TextView timeStamp;
        protected LinearLayout linearLayout;
        protected CardView cardView;

        public ForumPostViewHolder(View itemView) {
            super(itemView);
            question = ViewHolder.get(itemView,R.id.tvQuestionOfForumPost);
            askedby = ViewHolder.get(itemView,R.id.tvAskedByWhom);
            timeStamp = ViewHolder.get(itemView,R.id.forum_post_time_stamp);
            linearLayout = ViewHolder.get(itemView,R.id.forumpostlinearlayout);
            cardView = ViewHolder.get(itemView,R.id.card);
        }
    }
}

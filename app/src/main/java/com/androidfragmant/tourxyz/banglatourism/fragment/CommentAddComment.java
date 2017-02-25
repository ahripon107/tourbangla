package com.androidfragmant.tourxyz.banglatourism.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidfragmant.tourxyz.banglatourism.model.Comment;
import com.androidfragmant.tourxyz.banglatourism.util.AbstractListAdapter;
import com.androidfragmant.tourxyz.banglatourism.util.DefaultMessageHandler;
import com.androidfragmant.tourxyz.banglatourism.util.NetworkService;
import com.androidfragmant.tourxyz.banglatourism.util.Validator;
import com.androidfragmant.tourxyz.banglatourism.util.ViewHolder;
import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;
import com.google.inject.Inject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;


/**
 * @author Ripon
 */
public class CommentAddComment extends RoboFragment {
    @Inject
    private ArrayList<Comment> comments;

    @InjectView(R.id.rvComments)
    private RecyclerView recyclerView;

    @InjectView(R.id.btnSubmitComment)
    private Button sendComment;

    @Inject
    protected NetworkService networkService;

    private String url;
    private Typeface tf;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.commentandaddcomment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tf = Constants.solaimanLipiFont(getContext());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(new AbstractListAdapter<Comment, CommentViewHolder>(comments) {
            @Override
            public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlecomment, parent, false);
                return new CommentViewHolder(v);
            }

            @Override
            public void onBindViewHolder(CommentViewHolder holder, int position) {
                holder.comment.setTypeface(tf);
                holder.commenter.setTypeface(tf);
                holder.commenter.setText("মন্তব্য করেছেন:  " + comments.get(position).getName());
                holder.comment.setText(comments.get(position).getComment());
                holder.timestamp.setText(Constants.getTimeAgo(Long.parseLong(comments.get(position).getTimestamp())));
            }

        });

        if (getArguments().getInt("number") == 2) {
            url = Constants.FETCH_BLOG_POST_COMMENTS_URL;
        } else {
            url = Constants.FETCH_PLACE_COMMENTS_URL;
        }

        Log.d(Constants.TAG, url);

        networkService.fetchComments(url, getArguments().getInt("id"), new DefaultMessageHandler(getContext()) {
            @Override
            public void onSuccess(Message msg) {
                String string = (String) msg.obj;
                try {
                    JSONObject response = new JSONObject(string);
                    try {
                        JSONArray jsonArray = response.getJSONArray("content");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            comments.add(new Comment(jsonObject.getString("name"), jsonObject.getString("comment"), jsonObject.getString("timestamp")));
                            Collections.reverse(comments);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    recyclerView.getAdapter().notifyDataSetChanged();
                    if (comments.size() != 0) {
                        recyclerView.smoothScrollToPosition(comments.size() - 1);
                    }
                    Log.d(Constants.TAG, response.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View promptsView = LayoutInflater.from(getContext()).inflate(R.layout.addnewcomment, null, false);
                final EditText writeComment = (EditText) promptsView.findViewById(R.id.etYourComment);
                final EditText yourName = (EditText) promptsView.findViewById(R.id.etYourName);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

                            if (getArguments().getInt("number") == 2) {
                                url = Constants.INSERT_BLOG_POST_COMMENT_URL;
                            } else {
                                url = Constants.INSERT_PLACE_COMMENT_URL;
                            }

                            networkService.insertComment(comment, name, url, getArguments().getInt("id"), System.currentTimeMillis() + "", new DefaultMessageHandler(getContext(), true, "Posting comment..Please wait...") {
                                @Override
                                public void onSuccess(Message msg) {

                                    Toast.makeText(getContext(), "Comment successfully posted", Toast.LENGTH_LONG).show();
                                    comments.add(0, new Comment(name, comment, System.currentTimeMillis() + ""));
                                    recyclerView.getAdapter().notifyDataSetChanged();
                                    if (comments.size() != 0) {
                                        recyclerView.smoothScrollToPosition(comments.size() - 1);
                                    }
                                    Log.d(Constants.TAG, msg.obj.toString());
                                }
                            });
                            alertDialog.dismiss();
                        }
                    }
                });
            }
        });
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        protected TextView commenter;
        protected TextView comment;
        protected TextView timestamp;

        public CommentViewHolder(View v) {
            super(v);
            commenter = ViewHolder.get(v, R.id.tvName);
            comment = ViewHolder.get(v, R.id.tvComment);
            timestamp = ViewHolder.get(itemView, R.id.tv_time_stamp);
        }
    }
}

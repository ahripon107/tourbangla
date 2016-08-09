package com.androidfragmant.tourxyz.banglatourism.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.androidfragmant.tourxyz.banglatourism.FetchFromWeb;
import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.adapter.CommentAdapter;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Ripon on 8/27/15.
 */
public class CommentAddComment extends Fragment {
    ArrayList<String> names;
    ArrayList<String> comments;
    ArrayList<String> timestamps;
    CommentAdapter commentAdapter;
    String url;
    RecyclerView recyclerView;

    public CommentAddComment() {

    }

    public static CommentAddComment NewInstanceofCommentAddComment(int id, int number) {
        CommentAddComment commentAddComment = new CommentAddComment();
        Bundle arguments = new Bundle();
        arguments.putInt("number", number);
        arguments.putInt("id", id);
        commentAddComment.setArguments(arguments);
        return commentAddComment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.commentandaddcomment, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.rvComments);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        names = new ArrayList<>();
        comments = new ArrayList<>();
        timestamps = new ArrayList<>();

        commentAdapter = new CommentAdapter(getContext(), names, comments, timestamps);
        recyclerView.setAdapter(commentAdapter);

        RequestParams requestParams = new RequestParams();
        if (getArguments().get("number") == 2) {
            requestParams.add(Constants.KEY, Constants.KEY_VALUE);
            requestParams.add("id", getArguments().getInt("id") + "");
            url = Constants.FETCH_BLOG_POST_COMMENTS_URL;
        } else {
            requestParams.add(Constants.KEY, Constants.KEY_VALUE);
            requestParams.add("id", getArguments().getInt("id") + "");
            url = Constants.FETCH_PLACE_COMMENTS_URL;
        }
        Log.d(Constants.TAG, url);

        FetchFromWeb.get(url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("content");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        names.add(jsonObject.getString("name"));
                        comments.add(jsonObject.getString("comment"));
                        timestamps.add(jsonObject.getString("timestamp"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                commentAdapter.notifyDataSetChanged();
                if (names.size() != 0) {
                    recyclerView.smoothScrollToPosition(names.size()-1);
                }
                Log.d(Constants.TAG, response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getContext(), statusCode + "Failed", Toast.LENGTH_LONG).show();
            }
        });

        Button sendComment = (Button) v.findViewById(R.id.btnSubmitComment);

        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater li = LayoutInflater.from(getContext());
                View promptsView = li.inflate(R.layout.addnewcomment, null, false);
                final EditText writeComment = (EditText) promptsView.findViewById(R.id.etYourComment);
                final EditText yourName = (EditText) promptsView.findViewById(R.id.etYourName);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(promptsView);
                builder.setTitle("মন্তব্য");
                builder.setCancelable(false)
                        .setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final String comment = writeComment.getText().toString().trim();
                                final String name = yourName.getText().toString().trim();
                                if (comment.length() == 0 || name.length() == 0) {
                                    Toast.makeText(getActivity(), "Please give input correctly", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                writeComment.getText().clear();
                                yourName.getText().clear();

                                RequestParams params = new RequestParams();
                                if (getArguments().get("number") == 2) {
                                    params.put(Constants.KEY, Constants.KEY_VALUE);
                                    params.put("id", getArguments().getInt("id"));
                                    params.put("name", name);
                                    params.put("comment", comment);
                                    params.put("timestamp", System.currentTimeMillis() + "");
                                    url = Constants.INSERT_BLOG_POST_COMMENT_URL;
                                } else {
                                    params.put(Constants.KEY, Constants.KEY_VALUE);
                                    params.put("id", getArguments().getInt("id"));
                                    params.put("name", name);
                                    params.put("comment", comment);
                                    params.put("timestamp", System.currentTimeMillis() + "");
                                    url = Constants.INSERT_PLACE_COMMENT_URL;
                                }
                                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                                progressDialog.setMessage("Posting comment..Please wait...");
                                progressDialog.show();
                                FetchFromWeb.post(url, params, new JsonHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getContext(), "Comment successfully posted", Toast.LENGTH_LONG).show();
                                        names.add(name);
                                        comments.add(comment);
                                        timestamps.add(System.currentTimeMillis() + "");
                                        commentAdapter.notifyDataSetChanged();
                                        if (names.size() != 0) {
                                            recyclerView.smoothScrollToPosition(names.size()-1);
                                        }
                                        Log.d(Constants.TAG, response.toString());
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getContext(), "Failed", Toast.LENGTH_LONG).show();
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
        return v;
    }
}

package com.sfuronlabs.ripon.tourbangla.fragment;

import android.app.AlertDialog;
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
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.sfuronlabs.ripon.tourbangla.FetchFromWeb;
import com.sfuronlabs.ripon.tourbangla.R;
import com.sfuronlabs.ripon.tourbangla.adapter.CommentAdapter;
import com.sfuronlabs.ripon.tourbangla.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Ripon on 8/27/15.
 */
public class CommentAddComment extends Fragment {
    ArrayList<CharSequence> names;
    ArrayList<CharSequence> comments;
    CommentAdapter commentAdapter;
    String url;

    public CommentAddComment() {

    }

    public static CommentAddComment NewInstanceofCommentAddComment(int id, int number) {
        CommentAddComment commentAddComment = new CommentAddComment();

        Bundle arguments = new Bundle();

        arguments.putInt("number", number);
        arguments.putInt("id",id);
        commentAddComment.setArguments(arguments);

        return commentAddComment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.commentandaddcomment, container, false);
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.rvComments);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        names = new ArrayList<>();
        comments = new ArrayList<>();
        commentAdapter = new CommentAdapter(getContext(),names, comments);
        recyclerView.setAdapter(commentAdapter);

        if (getArguments().get("number") == 2) {
            url = "http://apisea.xyz/TourBangla/BlogPostComments.php?key=bl905577&id="+getArguments().getInt("id");
        } else {
            url = "http://apisea.xyz/TourBangla/PlaceComments.php?key=bl905577&id="+getArguments().getInt("id");
        }
        Log.d(Constants.TAG, url);

        FetchFromWeb.get(url,null,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("content");
                    for (int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        names.add(jsonObject.getString("name"));
                        comments.add(jsonObject.getString("comment"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                commentAdapter.notifyDataSetChanged();
                Log.d(Constants.TAG, response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getContext(), statusCode+"failed", Toast.LENGTH_LONG).show();
            }
        });

        FloatingActionButton sendComment = (FloatingActionButton) v.findViewById(R.id.btnSubmitComment);

        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater li = LayoutInflater.from(getContext());
                View promptsView = li.inflate(R.layout.addnewcomment, null);
                final EditText writeComment = (EditText) promptsView.findViewById(R.id.etYourComment);
                final EditText yourName = (EditText) promptsView.findViewById(R.id.etYourName);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(promptsView);
                builder.setTitle("Comment");
                builder.setCancelable(false)
                        .setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String comment = writeComment.getText().toString().trim();
                                String name = yourName.getText().toString().trim();
                                if (comment.length() == 0 || name.length() == 0) {
                                    Toast.makeText(getActivity(), "Please give input correctly", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                writeComment.getText().clear();
                                yourName.getText().clear();
                                names.add(name);

                                comments.add(comment);
                                commentAdapter.notifyDataSetChanged();

                                if (getArguments().get("number") == 2) {
                                    url = "http://apisea.xyz/TourBangla/InsertBlogPostComment.php?key=bl905577&id="+getArguments().getInt("id")+"&name="+name+"&comment="+comment;
                                } else {
                                    url = "http://apisea.xyz/TourBangla/InsertPlaceComment.php?key=bl905577&id="+getArguments().getInt("id")+"&name="+name+"&comment="+comment;
                                }
                                FetchFromWeb.get(url,null,new JsonHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        Toast.makeText(getContext(), statusCode+"success", Toast.LENGTH_LONG).show();

                                        Log.d(Constants.TAG, response.toString());
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                        Toast.makeText(getContext(), statusCode+"failed", Toast.LENGTH_LONG).show();
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

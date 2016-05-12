package com.sfuronlabs.ripon.tourbangla;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ripon on 8/27/15.
 */
public class CommentAddComment extends Fragment {
    ParseObject selectedObject;
    ArrayList<CharSequence> names;
    ArrayList<CharSequence> comments1;
    RecyclerView.Adapter mAdapter;

    public CommentAddComment() {

    }

    public static CommentAddComment NewInstanceofCommentAddComment(ArrayList<CharSequence> names, ArrayList<CharSequence> comments, String blogDetails, int number) {
        CommentAddComment commentAddComment = new CommentAddComment();

        Bundle arguments = new Bundle();


        arguments.putCharSequenceArrayList("names", names);
        arguments.putCharSequenceArrayList("comments", comments);
        arguments.putCharSequence("details", blogDetails);
        arguments.putInt("number", number);
        commentAddComment.setArguments(arguments);

        return commentAddComment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.commentandaddcomment, container, false);
        RecyclerView comments = (RecyclerView) v.findViewById(R.id.rvComments);
        comments.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        comments.setLayoutManager(mLayoutManager);
        names = getArguments().getCharSequenceArrayList("names");
        comments1 = getArguments().getCharSequenceArrayList("comments");
        mAdapter = new CommentAdapter(names, comments1);
        comments.setAdapter(mAdapter);

        Button sendComment = (Button) v.findViewById(R.id.btnSubmitComment);
        ParseQuery<ParseObject> query;
        if (getArguments().get("number") == 2) {
            query = ParseQuery.getQuery("BlogPosts");
            query.whereEqualTo("blogdetails", getArguments().get("details"));

        } else {
            query = ParseQuery.getQuery("PlaceTable");
            query.whereEqualTo("objectId", getArguments().get("details"));
        }
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    selectedObject = list.get(0);
                }

            }
        });
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
                                //names.add((String) ParseUser.getCurrentUser().get("FullName"));
                                names.add(name);

                                comments1.add(comment);
                                mAdapter.notifyDataSetChanged();
                                ParseObject commnt;
                                if (getArguments().get("number") == 2) {
                                    commnt = new ParseObject("CommentOfBlogPost");
                                } else {
                                    commnt = new ParseObject("CommentOnPlace");
                                }
                                commnt.put("comment", comment);
                                commnt.put("commenter", name);
                                commnt.put("post", selectedObject);
                                commnt.saveInBackground();
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

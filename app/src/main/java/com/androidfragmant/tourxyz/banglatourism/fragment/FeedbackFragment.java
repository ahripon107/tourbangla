package com.androidfragmant.tourxyz.banglatourism.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidfragmant.tourxyz.banglatourism.util.FalseProgress;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.androidfragmant.tourxyz.banglatourism.FetchFromWeb;
import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;
import com.androidfragmant.tourxyz.banglatourism.view.cpb.CircularProgressButton;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Ripon on 9/27/15.
 */
public class FeedbackFragment extends Fragment {

    public FeedbackFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragmentfeedback, container, false);
        final EditText title = (EditText) rootView.findViewById(R.id.etFeedbackTitle);
        final EditText details = (EditText) rootView.findViewById(R.id.etFeedbackDetails);
        final Button send = (Button) rootView.findViewById(R.id.btnSend);
        send.setText("SEND");
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t = title.getText().toString().trim();
                String d = details.getText().toString().trim();
                if (t.length() == 0 || d.length() == 0) {
                    Toast.makeText(getActivity(), "Please give input correctly", Toast.LENGTH_LONG).show();
                    return;
                }


                RequestParams params = new RequestParams();
                params.put("key","bl905577");
                params.put("title",t);
                params.put("details",d);
                String url = Constants.SEND_FEEDBACK_URL;
                Log.d(Constants.TAG, url);
                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Sending Feedback...Please Wait...");
                progressDialog.show();
                FetchFromWeb.post(url,params,new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        progressDialog.dismiss();
                        title.getText().clear();
                        details.getText().clear();
                        Toast.makeText(getContext(), "Thank you for your feedback.", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Failed "+statusCode, Toast.LENGTH_LONG).show();
                    }
                });
            }


        });
        return rootView;
    }

}

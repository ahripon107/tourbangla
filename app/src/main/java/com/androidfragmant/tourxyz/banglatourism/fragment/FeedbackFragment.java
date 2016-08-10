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
import com.androidfragmant.tourxyz.banglatourism.util.Validator;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.androidfragmant.tourxyz.banglatourism.FetchFromWeb;
import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;
import com.androidfragmant.tourxyz.banglatourism.view.cpb.CircularProgressButton;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

/**
 * Created by Ripon on 9/27/15.
 */
public class FeedbackFragment extends RoboFragment {

    @InjectView((R.id.etFeedbackTitle))
    EditText title;

    @InjectView((R.id.etFeedbackDetails))
    EditText details;

    @InjectView(R.id.btnSend)
    Button send;

    public FeedbackFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmentfeedback, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        send.setText("SEND");
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validator.validateNotEmpty(title,"Required") && Validator.validateNotEmpty(details,"Required")) {
                    RequestParams params = new RequestParams();
                    params.put(Constants.KEY,Constants.KEY_VALUE);
                    params.put("title",title.getText().toString());
                    params.put("details",details.getText().toString());
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
            }
        });
    }
}

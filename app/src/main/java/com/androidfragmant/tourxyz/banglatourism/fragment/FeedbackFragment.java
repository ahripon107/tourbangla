package com.androidfragmant.tourxyz.banglatourism.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
                if (Validator.validateNotEmpty(title, "Required") && Validator.validateNotEmpty(details, "Required")) {

                    RequestParams params = new RequestParams();
                    params.put(Constants.KEY, Constants.KEY_VALUE);
                    params.put("title", title.getText().toString());
                    params.put("details", details.getText().toString());

                    final ProgressDialog progressDialog = new ProgressDialog(getContext());
                    progressDialog.setMessage("Sending Feedback...Please Wait...");
                    progressDialog.show();

                    Handler handler = new Handler(Looper.getMainLooper()) {
                        @Override
                        public void handleMessage(Message msg) {
                            progressDialog.dismiss();
                            if (msg.what == Constants.SUCCESS) {
                                JSONObject response = (JSONObject) msg.obj;
                                if (response != null) {
                                    title.getText().clear();
                                    details.getText().clear();
                                    Toast.makeText(getContext(), "Thank you for your feedback.", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(getContext(), "Failed", Toast.LENGTH_LONG).show();
                            }
                        }
                    };

                    FetchFromWeb fetchFromWeb = new FetchFromWeb(handler);
                    fetchFromWeb.postData(Constants.SEND_FEEDBACK_URL, params);

                }
            }
        });
    }
}

package com.androidfragmant.tourxyz.banglatourism.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidfragmant.tourxyz.banglatourism.FetchFromWeb;
import com.androidfragmant.tourxyz.banglatourism.FileProcessor;
import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Ripon on 7/15/16.
 */
public class UpdateDatabaseFragment extends Fragment {

    public UpdateDatabaseFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_update_database,container,false);
        TextView textView = (TextView) rootView.findViewById(R.id.tvUpdateDatabase);
        textView.setText("Click button to update place database");
        Button button = (Button) rootView.findViewById(R.id.btnUpdateDatabase);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkAvailable()) {
                    Toast.makeText(getActivity(),"Please check your internet connection",Toast.LENGTH_LONG).show();
                }
                else {
                    RequestParams requestParams = new RequestParams();
                    requestParams.add(Constants.KEY,Constants.KEY_VALUE);

                    String url = Constants.FETCH_PLACES_URL;
                    Log.d(Constants.TAG, url);
                    final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("Please wait...this may take a while...");
                    progressDialog.setTitle("Loading data");
                    progressDialog.show();

                    FetchFromWeb.get(url, requestParams, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            progressDialog.dismiss();
                            FileProcessor fileProcessor = new FileProcessor(getActivity());
                            fileProcessor.writeToFile(response.toString());
                            Toast.makeText(getActivity(),"Successfully Updated", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), statusCode + "failed", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
        return rootView;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

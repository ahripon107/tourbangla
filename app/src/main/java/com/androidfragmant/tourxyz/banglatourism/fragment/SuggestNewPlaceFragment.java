package com.androidfragmant.tourxyz.banglatourism.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidfragmant.tourxyz.banglatourism.util.Validator;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.androidfragmant.tourxyz.banglatourism.FetchFromWeb;
import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

/**
 * Created by Ripon on 9/27/15.
 */

public class SuggestNewPlaceFragment extends RoboFragment {
    @InjectView(R.id.etSuggestedPlaceName)
    EditText name;

    @InjectView(R.id.etSuggestedPlaceAddress)
    EditText address;

    @InjectView(R.id.etSuggestedPlaceDivision)
    EditText division;

    @InjectView(R.id.etSuggestedPlaceDescription)
    EditText description;

    @InjectView(R.id.etSuggestedPlaceHowtogo)
    EditText howtogo;

    @InjectView(R.id.etSuggestedPlaceHotels)
    EditText hotels;

    @InjectView(R.id.btnDoneSuggest)
    Button suggestDone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.suggestnewplace, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        suggestDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validator.validateNotEmpty(name,"Required") && Validator.validateNotEmpty(address,"Required")
                        && Validator.validateNotEmpty(division,"Required") && Validator.validateNotEmpty(description,"Required")
                        && Validator.validateNotEmpty(howtogo,"Required") && Validator.validateNotEmpty(hotels,"Required")) {
                    RequestParams params = new RequestParams();
                    params.put(Constants.KEY, Constants.KEY_VALUE);
                    params.put("hotels", hotels.getText().toString());
                    params.put("howtogo", howtogo.getText().toString());
                    params.put("description", description.getText().toString());
                    params.put("division", division.getText().toString());
                    params.put("address", address.getText().toString());
                    params.put("name", name.getText().toString());

                    String url = Constants.SUGGEST_NEW_PLACE_URL;
                    Log.d(Constants.TAG, url);

                    final ProgressDialog progressDialog = new ProgressDialog(getContext());
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();

                    FetchFromWeb.post(url, params, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            progressDialog.dismiss();
                            name.getText().clear();
                            address.getText().clear();
                            division.getText().clear();
                            description.getText().clear();
                            howtogo.getText().clear();
                            hotels.getText().clear();
                            Toast.makeText(getContext(), "Thank you for your suggestion.", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Failed..Please try again.." + statusCode, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }
}

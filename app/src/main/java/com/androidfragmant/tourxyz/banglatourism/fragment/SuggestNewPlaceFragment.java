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
                String pname = name.getText().toString().trim();
                String paddress = address.getText().toString().trim();
                String pdivision = division.getText().toString().trim();
                String pdescription = description.getText().toString().trim();
                String phowtogo = howtogo.getText().toString().trim();
                String photels = hotels.getText().toString().trim();
                if (pname.length() == 0 || paddress.length() == 0 || pdivision.length() == 0 || pdescription.length() == 0 || phowtogo.length() == 0 || photels.length() == 0) {
                    Toast.makeText(getActivity(), "Please give input correctly", Toast.LENGTH_LONG).show();
                    return;
                }

                RequestParams params = new RequestParams();
                params.put(Constants.KEY,Constants.KEY_VALUE);
                params.put("hotels",photels);
                params.put("howtogo",phowtogo);
                params.put("description",pdescription);
                params.put("division",pdivision);
                params.put("address",paddress);
                params.put("name",pname);
                String url = Constants.SUGGEST_NEW_PLACE_URL;
                Log.d(Constants.TAG, url);
                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                FetchFromWeb.post(url,params,new JsonHttpResponseHandler() {
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
                        Toast.makeText(getContext(), "Failed..Please try again.."+statusCode, Toast.LENGTH_LONG).show();
                    }
                });
            }

        });
    }
}

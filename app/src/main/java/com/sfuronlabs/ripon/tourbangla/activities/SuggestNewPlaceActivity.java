package com.sfuronlabs.ripon.tourbangla.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sfuronlabs.ripon.tourbangla.FetchFromWeb;
import com.sfuronlabs.ripon.tourbangla.R;
import com.sfuronlabs.ripon.tourbangla.RoboAppCompatActivity;
import com.sfuronlabs.ripon.tourbangla.util.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * Created by Ripon on 9/27/15.
 */
@ContentView(R.layout.suggestnewplace)
public class SuggestNewPlaceActivity extends RoboAppCompatActivity {
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

    @InjectView(R.id.tool_barsuggestplace)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);
        setTitle("Suggest New Place");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        suggestDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String pname = name.getText().toString().trim();
                final String paddress = address.getText().toString().trim();
                final String pdivision = division.getText().toString().trim();
                final String pdescription = description.getText().toString().trim();
                final String phowtogo = howtogo.getText().toString().trim();
                final String photels = hotels.getText().toString().trim();
                if (pname.length() == 0 || paddress.length() == 0 || pdivision.length() == 0 || pdescription.length() == 0 || phowtogo.length() == 0 || photels.length() == 0) {
                    Toast.makeText(SuggestNewPlaceActivity.this, "Please give input correctly", Toast.LENGTH_LONG).show();
                    return;
                }

                String url = "http://apisea.xyz/TourBangla/SuggestNewPlace.php?key=bl905577&hotels="+photels+"&howtogo="+phowtogo;
                url = url + "&description="+pdescription+"&division="+pdivision+"&address="+paddress+"&name="+pname;
                Log.d(Constants.TAG, url);
                final ProgressDialog progressDialog = new ProgressDialog(SuggestNewPlaceActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                FetchFromWeb.get(url,null,new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        progressDialog.dismiss();
                        name.getText().clear();
                        address.getText().clear();
                        division.getText().clear();
                        description.getText().clear();
                        howtogo.getText().clear();
                        hotels.getText().clear();
                        Toast.makeText(getApplicationContext(), "Thank you for your suggestion.", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        progressDialog.dismiss();
                        Toast.makeText(SuggestNewPlaceActivity.this, "Failed "+statusCode, Toast.LENGTH_LONG).show();
                    }
                });
            }

        });
    }
}

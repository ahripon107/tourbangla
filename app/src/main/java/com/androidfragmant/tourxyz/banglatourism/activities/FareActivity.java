package com.androidfragmant.tourxyz.banglatourism.activities;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidfragmant.tourxyz.banglatourism.FetchFromWeb;
import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.RoboAppCompatActivity;
import com.androidfragmant.tourxyz.banglatourism.adapter.FareListAdapter;
import com.androidfragmant.tourxyz.banglatourism.model.Fare;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import cz.msebera.android.httpclient.Header;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * Created by Ripon on 8/2/16.
 */

@ContentView(R.layout.activity_fare)
public class FareActivity extends RoboAppCompatActivity {

    @InjectView(R.id.spnFrom)
    Spinner fromSpinner;

    @InjectView(R.id.spnTo)
    Spinner toSpinner;

    @InjectView(R.id.spnVehicle)
    Spinner vehicleSpinner;

    @InjectView(R.id.btnSearch)
    Button searchButton;

    @InjectView(R.id.farelist)
    RecyclerView fareList;

    @InjectView(R.id.numberOfResultsFound)
    TextView resultsFound;

    @InjectView(R.id.tvStartPlace)
    TextView startPlace;

    @InjectView(R.id.tvEndPlace)
    TextView EndPlace;

    @InjectView(R.id.tvJanbahon)
    TextView janbahon;

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    ArrayList<String> elements;
    ArrayList<Fare> allFares, selectedFares;
    FareListAdapter fareListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);
        setTitle("Fare");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        elements = populate();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(FareActivity.this,
                android.R.layout.simple_spinner_item, elements);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(dataAdapter);
        toSpinner.setAdapter(dataAdapter);

        Typeface tf = Typeface.createFromAsset(FareActivity.this.getAssets(), Constants.SOLAIMAN_LIPI_FONT);
        resultsFound.setTypeface(tf);
        startPlace.setTypeface(tf);
        EndPlace.setTypeface(tf);
        janbahon.setTypeface(tf);

        ArrayList<String> vehicles = new ArrayList<>();
        vehicles.add("বাস");
        vehicles.add("ট্রেন");
        vehicles.add("লঞ্চ");

        ArrayAdapter<String> vehicleAdapter = new ArrayAdapter<String>(FareActivity.this,
                android.R.layout.simple_spinner_item, vehicles);
        vehicleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        vehicleSpinner.setAdapter(vehicleAdapter);

        allFares = new ArrayList<>();
        selectedFares = new ArrayList<>();
        fareListAdapter = new FareListAdapter(FareActivity.this, selectedFares);
        fareList.setAdapter(fareListAdapter);
        fareList.setLayoutManager(new LinearLayoutManager(FareActivity.this));

        String url = Constants.FETCH_FARES_URL;
        Log.d(Constants.TAG, url);
        RequestParams requestParams = new RequestParams();
        requestParams.add(Constants.KEY, Constants.KEY_VALUE);

        final ProgressDialog progressDialog = new ProgressDialog(FareActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        FetchFromWeb.get(url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progressDialog.dismiss();
                try {
                    JSONArray jsonArray = response.getJSONArray("content");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Gson gson = new Gson();
                        Fare f = gson.fromJson(String.valueOf(jsonObject), Fare.class);
                        allFares.add(f);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                fareListAdapter.notifyDataSetChanged();
                Log.d(Constants.TAG, response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progressDialog.dismiss();
                Toast.makeText(FareActivity.this, "Failed", Toast.LENGTH_LONG).show();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedFares.clear();
                String from = fromSpinner.getSelectedItem().toString();
                String to = toSpinner.getSelectedItem().toString();
                String vehicle = vehicleSpinner.getSelectedItem().toString();

                for (Fare fa : allFares) {
                    if (fa.getFrom().equals(from) && fa.getTo().equals(to) && fa.getVehicle().equals(vehicle)) {
                        selectedFares.add(fa);
                    }
                }
                fareListAdapter.notifyDataSetChanged();
                resultsFound.setText(selectedFares.size() + " টি ফলাফল পাওয়া গেছে");
            }
        });
    }


    public ArrayList<String> populate() {
        ArrayList<String> e = new ArrayList<>();
        String[] districtsBangla = getResources().getStringArray(R.array.dhaka_districts_bangla);
        e.addAll(Arrays.asList(districtsBangla));
        districtsBangla = getResources().getStringArray(R.array.chittagong_districts_bangla);
        e.addAll(Arrays.asList(districtsBangla));
        districtsBangla = getResources().getStringArray(R.array.rajshahi_districts_bangla);
        e.addAll(Arrays.asList(districtsBangla));
        districtsBangla = getResources().getStringArray(R.array.rangpur_districts_bangla);
        e.addAll(Arrays.asList(districtsBangla));
        districtsBangla = getResources().getStringArray(R.array.mymensingh_districts_bangla);
        e.addAll(Arrays.asList(districtsBangla));
        districtsBangla = getResources().getStringArray(R.array.sylhet_districts_bangla);
        e.addAll(Arrays.asList(districtsBangla));
        districtsBangla = getResources().getStringArray(R.array.khulna_districts_bangla);
        e.addAll(Arrays.asList(districtsBangla));
        districtsBangla = getResources().getStringArray(R.array.barisal_districts_bangla);
        e.addAll(Arrays.asList(districtsBangla));
        return e;
    }
}

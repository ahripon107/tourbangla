package com.androidfragmant.tourxyz.banglatourism.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.RoboAppCompatActivity;
import com.androidfragmant.tourxyz.banglatourism.model.Fare;
import com.androidfragmant.tourxyz.banglatourism.util.AbstractListAdapter;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;
import com.androidfragmant.tourxyz.banglatourism.util.DefaultMessageHandler;
import com.androidfragmant.tourxyz.banglatourism.util.NetworkService;
import com.androidfragmant.tourxyz.banglatourism.util.ViewHolder;
import com.google.gson.Gson;
import com.google.inject.Inject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * @author Ripon
 */

@ContentView(R.layout.activity_fare)
public class FareActivity extends RoboAppCompatActivity {

    @InjectView(R.id.spnFrom)
    private Spinner fromSpinner;

    @InjectView(R.id.spnTo)
    private Spinner toSpinner;

    @InjectView(R.id.spnVehicle)
    private Spinner vehicleSpinner;

    @InjectView(R.id.btnSearch)
    private Button searchButton;

    @InjectView(R.id.farelist)
    private RecyclerView fareList;

    @InjectView(R.id.numberOfResultsFound)
    private TextView resultsFound;

    @InjectView(R.id.tvStartPlace)
    private TextView startPlace;

    @InjectView(R.id.tvEndPlace)
    private TextView EndPlace;

    @InjectView(R.id.tvJanbahon)
    private TextView janbahon;

    @Inject
    private ArrayList<String> elements;

    @Inject
    private ArrayList<Fare> allFares;

    @Inject
    private ArrayList<Fare> selectedFares;

    @Inject
    private NetworkService networkService;

    private Typeface tf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        elements = populate();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(FareActivity.this,
                android.R.layout.simple_spinner_item, elements);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        fromSpinner.setAdapter(dataAdapter);
        toSpinner.setAdapter(dataAdapter);

        tf = Constants.solaimanLipiFont(this);

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

        fareList.setAdapter(new AbstractListAdapter<Fare,FareViewHolder>(selectedFares) {
            @Override
            public FareViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fare_list_item, parent, false);
                return new FareViewHolder(view);
            }

            @Override
            public void onBindViewHolder(FareViewHolder holder, int position) {
                Fare fare = selectedFares.get(position);

                holder.companyName.setTypeface(tf);
                holder.fare.setTypeface(tf);
                holder.timeToLeave.setTypeface(tf);
                holder.estimatedTime.setTypeface(tf);
                holder.leavingPlace.setTypeface(tf);

                holder.companyName.setText(fare.getCompanyName());
                holder.fare.setText(fare.getFare());
                holder.timeToLeave.setText(fare.getStartTime());
                holder.estimatedTime.setText(fare.getEstimatedTime());
                holder.leavingPlace.setText(fare.getLeavePlace());
            }
        });
        fareList.setLayoutManager(new LinearLayoutManager(FareActivity.this));

        networkService.fetchFares(new DefaultMessageHandler(this,true){
            @Override
            public void onSuccess(Message msg) {
                String string = (String) msg.obj;
                try {
                    JSONObject response = new JSONObject(string);
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
                    Log.d(Constants.TAG, fa.toString());
                    if (fa.getFromPlace().equals(from) && fa.getToPlace().equals(to) && fa.getVehicle().equals(vehicle)) {
                        selectedFares.add(fa);
                    }
                }
                fareList.getAdapter().notifyDataSetChanged();
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

    private static class FareViewHolder extends RecyclerView.ViewHolder {
        protected TextView companyName;
        protected TextView fare;
        protected TextView timeToLeave;
        protected TextView estimatedTime;
        protected TextView leavingPlace;

        public FareViewHolder(View itemView) {
            super(itemView);
            companyName = ViewHolder.get(itemView, R.id.tvCompanyName);
            fare = ViewHolder.get(itemView, R.id.tvFare);
            timeToLeave = ViewHolder.get(itemView, R.id.tvTimeToLeave);
            estimatedTime = ViewHolder.get(itemView, R.id.tvEstimatedTime);
            leavingPlace = ViewHolder.get(itemView, R.id.tvLeavingPlace);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.right_in, R.anim.right_out);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }
}

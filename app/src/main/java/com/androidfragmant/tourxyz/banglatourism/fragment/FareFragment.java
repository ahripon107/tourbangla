package com.androidfragmant.tourxyz.banglatourism.fragment;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidfragmant.tourxyz.banglatourism.FetchFromWeb;
import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.adapter.FareListAdapter;
import com.androidfragmant.tourxyz.banglatourism.model.Fare;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Ripon on 8/2/16.
 */

public class FareFragment extends Fragment {


    Spinner fromSpinner;
    Spinner toSpinner;
    Spinner vehicleSpinner;
    Button searchButton;
    RecyclerView fareList;
    TextView resultsFound;
    TextView startPlace;
    TextView EndPlace;
    TextView janbahon;

    ArrayList<String> elements;
    ArrayList<Fare> allFares,selectedFares;
    FareListAdapter fareListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_fare,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fromSpinner = (Spinner) view.findViewById(R.id.spnFrom);
        toSpinner = (Spinner) view.findViewById(R.id.spnTo);
        vehicleSpinner = (Spinner) view.findViewById(R.id.spnVehicle);
        searchButton = (Button) view.findViewById(R.id.btnSearch);
        fareList = (RecyclerView) view.findViewById(R.id.farelist);
        resultsFound = (TextView) view.findViewById(R.id.numberOfResultsFound);
        startPlace = (TextView) view.findViewById(R.id.tvStartPlace);
        EndPlace = (TextView) view.findViewById(R.id.tvEndPlace);
        janbahon = (TextView) view.findViewById(R.id.tvJanbahon);

        elements = populate();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, elements);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(dataAdapter);
        toSpinner.setAdapter(dataAdapter);

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),Constants.SOLAIMAN_LIPI_FONT);
        resultsFound.setTypeface(tf);
        startPlace.setTypeface(tf);
        EndPlace.setTypeface(tf);
        janbahon.setTypeface(tf);

        ArrayList<String> vehicles = new ArrayList<>();
        vehicles.add("বাস");
        vehicles.add("ট্রেন");
        vehicles.add("লঞ্চ");

        ArrayAdapter<String> vehicleAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, vehicles);
        vehicleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        vehicleSpinner.setAdapter(vehicleAdapter);

        allFares = new ArrayList<>();
        selectedFares = new ArrayList<>();
        fareListAdapter = new FareListAdapter(getActivity(),selectedFares);
        fareList.setAdapter(fareListAdapter);
        fareList.setLayoutManager(new LinearLayoutManager(getActivity()));

        String url = Constants.FETCH_FARES_URL;
        Log.d(Constants.TAG, url);

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        FetchFromWeb.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progressDialog.dismiss();
                try {
                    JSONArray jsonArray = response.getJSONArray("content");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int id = jsonObject.getInt("id");
                        String from = jsonObject.getString("fromPlace");
                        String to = jsonObject.getString("toPlace");
                        String fare = jsonObject.getString("fare");
                        String startTime = jsonObject.getString("startTime");
                        String companyName = jsonObject.getString("companyName");
                        String estimatedTime = jsonObject.getString("estimatedTime");
                        String leavePlace = jsonObject.getString("leavePlace");
                        String vehicle = jsonObject.getString("vehicle");
                        Fare f = new Fare(id,from,to,fare,startTime,companyName,estimatedTime,leavePlace,vehicle);
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
                Toast.makeText(getContext(), "Failed", Toast.LENGTH_LONG).show();
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
                resultsFound.setText(selectedFares.size()+" টি ফলাফল পাওয়া গেছে");
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

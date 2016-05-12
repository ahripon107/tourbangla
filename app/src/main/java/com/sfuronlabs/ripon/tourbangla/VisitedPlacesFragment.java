package com.sfuronlabs.ripon.tourbangla;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Ripon on 9/26/15.
 */
public class VisitedPlacesFragment extends Fragment {
    ListView listView;

    String[] web = new String[0];
    String[] picname = new String[0];
    ArrayList<Place> allPlaces;
    GridAdapter gridAdapter;

    public VisitedPlacesFragment()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        allPlaces = new ArrayList<>();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("rating", Context.MODE_PRIVATE);
        Map<String,?> elements = sharedPreferences.getAll();
        for(Map.Entry<String,?> entry : elements.entrySet()){
            String string =  entry.getValue().toString();
            Gson gson = new Gson();
            Place place = gson.fromJson(string,Place.class);
            allPlaces.add(place);
        }
        if (allPlaces != null)
        {
            BrowseByDivision.finalplaces = allPlaces;
            web = new String[allPlaces.size()];
            picname = new String[allPlaces.size()];

            for (int j = 0; j < allPlaces.size(); j++) {
                web[j] = allPlaces.get(j).getName();
                picname[j] = allPlaces.get(j).getPicture();
            }
        }

        gridAdapter = new GridAdapter(getActivity(),
                web, picname, "font/Amaranth-Bold.ttf");
        listView.setAdapter(gridAdapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragmentvisitedplaces, container, false);
        listView = (ListView) rootView.findViewById(R.id.gridviewvisitedplaces);

        allPlaces = new ArrayList<>();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("rating", Context.MODE_PRIVATE);
        Map<String,?> elements = sharedPreferences.getAll();
        for(Map.Entry<String,?> entry : elements.entrySet()){
            String string =  entry.getValue().toString();
            Gson gson = new Gson();
            Place place = gson.fromJson(string,Place.class);
            allPlaces.add(place);
        }
        if (allPlaces != null)
        {
            BrowseByDivision.finalplaces = allPlaces;
            web = new String[allPlaces.size()];
            picname = new String[allPlaces.size()];

            for (int j = 0; j < allPlaces.size(); j++) {
                web[j] = allPlaces.get(j).getName();
                picname[j] = allPlaces.get(j).getPicture();
            }
        }

        gridAdapter = new GridAdapter(getActivity(),
                web, picname, "font/Amaranth-Bold.ttf");
        listView.setAdapter(gridAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                //Intent i = new Intent("android.intent.action.PLACEDETAILS");
                final Intent i = new Intent("android.intent.action.NEWPLACEDETAILSACTIVITY");
                i.putExtra("index", position);
                final Place product = allPlaces.get(position);
                final String objectId = allPlaces.get(position).getObjectId();
                ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("PlaceTable");
                parseQuery.whereEqualTo("name", allPlaces.get(position).getName());
                final ProgressDialog dialog = ProgressDialog.show(getActivity(), "Loading", "Please wait...", true);
                dialog.setCancelable(true);
                parseQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, ParseException e) {
                        dialog.dismiss();
                        if (e == null)
                        {
                            Place place = new Place(product.getId(),product.getName(),product.getDescription(),product.getHowtogo(),product.getLattitude(),product.getLongitude(),product.getHotel(),product.getOthers(),product.getPicture(),product.getAddress(),product.getType(),product.getDistrict(),list.get(0));
                            BrowseByDivision.finalplaces.set(position,place);
                            startActivity(i);
                        }
                        else
                        {
                            Toast.makeText(getActivity(),"Error occured",Toast.LENGTH_LONG).show();
                        }

                    }
                });

            }
        });
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}

package com.sfuronlabs.ripon.tourbangla;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ripon on 6/11/15.
 */
public class MessagesFragment extends Fragment{
    ListView listView;
    SharedPreference sharedPreference;
    String[] web = new String[0];
    String[] picname = new String[0];
    GridAdapter gridAdapter;
    ArrayList<Place> allPlaces;
    public static ArrayList<Place> wishlist;
    public MessagesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        int done=0,p;

        allPlaces = wishlist;
        if (allPlaces.size()==wishlist.size())
        {

            web = new String[allPlaces.size()];
            picname = new String[allPlaces.size()];
            for (int j = 0; j < allPlaces.size(); j++) {
                web[j] = allPlaces.get(j).getName();
                picname[j] = allPlaces.get(j).getPicture();
            }
            gridAdapter = new GridAdapter(getActivity(),
                    web, picname, "font/Amaranth-Bold.ttf");
            listView.setAdapter(gridAdapter);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_messages, container, false);
        listView = (ListView) rootView.findViewById(R.id.gridviewfavourite);
        sharedPreference = new SharedPreference();
        allPlaces = new ArrayList<>();
        wishlist = new ArrayList<>();
        allPlaces = sharedPreference.getFavorites(getActivity());
        if (allPlaces != null)
        {
            BrowseByDivision.finalplaces = allPlaces;
            wishlist = allPlaces;
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
                parseQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, ParseException e) {
                        dialog.dismiss();
                        if (e == null)
                        {
                            Place place = new Place(product.getId(),product.getName(),product.getDescription(),product.getHowtogo(),product.getLattitude(),product.getLongitude(),product.getHotel(),product.getOthers(),product.getPicture(),product.getAddress(),product.getType(),product.getDistrict(),list.get(0));
                            BrowseByDivision.finalplaces.set(position,place);
                            startActivity(i);
                            return;
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

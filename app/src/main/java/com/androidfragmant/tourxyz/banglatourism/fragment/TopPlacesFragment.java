package com.androidfragmant.tourxyz.banglatourism.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidfragmant.tourxyz.banglatourism.FileProcessor;
import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.activities.DivisionListActivity;
import com.androidfragmant.tourxyz.banglatourism.activities.NewPlaceDetailsActivity;
import com.androidfragmant.tourxyz.banglatourism.model.Place;
import com.androidfragmant.tourxyz.banglatourism.util.AbstractListAdapter;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;
import com.androidfragmant.tourxyz.banglatourism.util.DefaultMessageHandler;
import com.androidfragmant.tourxyz.banglatourism.util.NetworkService;
import com.androidfragmant.tourxyz.banglatourism.util.ViewHolder;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.squareup.picasso.Picasso;
import com.yayandroid.parallaxrecyclerview.ParallaxImageView;
import com.yayandroid.parallaxrecyclerview.ParallaxRecyclerView;
import com.yayandroid.parallaxrecyclerview.ParallaxViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

import static com.androidfragmant.tourxyz.banglatourism.util.Constants.setLeftInAnimation;
import static com.androidfragmant.tourxyz.banglatourism.util.Constants.setRightInAnimation;

/**
 * @author Ripon
 */

public class TopPlacesFragment extends RoboFragment {

    @InjectView(R.id.gridview)
    private ParallaxRecyclerView recyclerView;

    @Inject
    private NetworkService networkService;

    private ArrayList<Place> places;
    private Typeface tf;

    private TopPlaceAdapter topPlacesListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        places = new ArrayList<>();
        tf = Constants.solaimanLipiFont(getContext());

        topPlacesListAdapter = new TopPlaceAdapter(getContext(), places);


        loadPlaces();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_top_places, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView.setAdapter(topPlacesListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //recyclerView.setNestedScrollingEnabled(false);

    }

    private void loadPlaces() {
        networkService.fetchTopPlaces(new DefaultMessageHandler(getContext(), true) {
            @Override
            public void onSuccess(Message msg) {
                String string = (String) msg.obj;
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    JSONArray jsonArray = jsonObject.getJSONArray("content");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        Gson gson = new Gson();
                        Place place = gson.fromJson(String.valueOf(object), Place.class);
//                        Place place = new Place(object.getInt("id"), object.getString("name")
//                                , object.getString("description"), object.getString("howtogo"),
//                                object.getString("lattitude"), object.getString("longitude"),
//                                object.getString("hotel"), object.getString("others"),
//                                object.getString("picture"), object.getString("district"),
//                                object.getString("division"));
                        places.add(place);
                    }
                    topPlacesListAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private static class PlaceViewHolder extends ParallaxViewHolder {
        protected TextView title;
        protected RelativeLayout linearLayout;
        protected TextView summary;
        protected Button details;

        @Override
        public int getParallaxImageId() {
            return R.id.list_item_google_cards_travel_image;
        }

        public PlaceViewHolder(View itemView) {
            super(itemView);
            title = ViewHolder.get(itemView, R.id.list_item_google_cards_travel_title);
            linearLayout = ViewHolder.get(itemView, R.id.linear);
            summary = ViewHolder.get(itemView,R.id.summary);
            details = ViewHolder.get(itemView,R.id.details);
        }
    }

    private class TopPlaceAdapter extends RecyclerView.Adapter<PlaceViewHolder> {

        protected Context context;
        protected ArrayList<Place> places;

        public TopPlaceAdapter(Context context, ArrayList<Place> places) {
            this.context = context;
            this.places = places;
        }

        @Override
        public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_place, parent, false);
            return new PlaceViewHolder(view);
        }

        @Override
        public void onBindViewHolder(PlaceViewHolder holder, int position) {
            final Place place = places.get(position);
            holder.title.setTypeface(tf);
            holder.summary.setTypeface(tf);
            holder.details.setTypeface(tf);
            Picasso.with(getContext()).load(place.getPicture()).into(holder.getBackgroundImage());
            holder.title.setText(place.getName());
            holder.summary.setText(place.getSummary());

            holder.getBackgroundImage().reuse();

            if (position == places.size()-1) {
                exploreMorePlace();
            }

            holder.details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getContext(), NewPlaceDetailsActivity.class);
                    i.putExtra("id", place.getId());
                    startActivity(i);
                    getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
                }
            });
        }

        @Override
        public int getItemCount() {
            return places.size();
        }
    }

    private void exploreMorePlace() {
        Snackbar snackbar = Snackbar
                .make(recyclerView, "Explore More Places!", Snackbar.LENGTH_LONG)
                .setAction("EXPLORE", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean exists = false;
                        String[] files = getActivity().fileList();
                        for (String file : files) {
                            if (file.equals("data.txt")) {
                                exists = true;
                                break;
                            } else {
                                exists = false;
                            }
                        }
                        if (!exists && !isNetworkAvailable()) {
                            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
                        } else if (!exists && isNetworkAvailable()) {

                            networkService.fetchPlaces(new DefaultMessageHandler(getContext(), true) {
                                @Override
                                public void onSuccess(Message msg) {
                                    String response = (String) msg.obj;
                                    FileProcessor fileProcessor = new FileProcessor(getContext());
                                    fileProcessor.writeToFile(response);
                                    Intent i = new Intent(getActivity(), DivisionListActivity.class);
                                    getActivity().startActivity(i);
                                    getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
                                }
                            });
                        } else {
                            FileProcessor fileProcessor = new FileProcessor(getContext());
                            fileProcessor.readFileAndProcess();
                            Intent i = new Intent(getActivity(), DivisionListActivity.class);
                            getActivity().startActivity(i);
                            getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        }
                    }
                });


        snackbar.setActionTextColor(Color.RED);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }
}

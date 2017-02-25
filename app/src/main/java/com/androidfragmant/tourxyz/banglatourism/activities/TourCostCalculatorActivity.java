package com.androidfragmant.tourxyz.banglatourism.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.RoboAppCompatActivity;
import com.androidfragmant.tourxyz.banglatourism.activities.TourCostItemActivity;
import com.androidfragmant.tourxyz.banglatourism.model.CostItem;
import com.androidfragmant.tourxyz.banglatourism.model.CostPlace;
import com.androidfragmant.tourxyz.banglatourism.util.AbstractListAdapter;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;
import com.androidfragmant.tourxyz.banglatourism.util.Validator;
import com.androidfragmant.tourxyz.banglatourism.util.ViewHolder;
import com.google.gson.Gson;
import com.google.inject.Inject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import roboguice.fragment.RoboFragment;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * @author Ripon
 */
@ContentView(R.layout.tour_cost)
public class TourCostCalculatorActivity extends RoboAppCompatActivity {

    @InjectView(R.id.cost_recycler_view)
    private RecyclerView recyclerView;

    @InjectView(R.id.fab_add_new_tour_place)
    private FloatingActionButton addNewPlace;

    @Inject
    private ArrayList<CostPlace> costPlaces;

    @Inject
    private Gson gson;

    private SharedPreferences sharedPreferences, idpreference;
    private Typeface tf;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tf = Constants.solaimanLipiFont(this);

        sharedPreferences = getSharedPreferences(Constants.TOUR_COST_PLACE_PREFERENCE_FILE, Context.MODE_PRIVATE);
        idpreference = getSharedPreferences(Constants.COST_PLACE_ID_PREFERENCE_FILE,Context.MODE_PRIVATE);

        if (!idpreference.contains("id")) {
            idpreference.edit().putInt("id", 1).apply();
        }

        Map<String, ?> elements = sharedPreferences.getAll();

        for (Map.Entry<String, ?> entry : elements.entrySet()) {
            String string = entry.getValue().toString();
            CostPlace costPlace = gson.fromJson(string, CostPlace.class);
            costPlaces.add(costPlace);
        }


        Collections.sort(costPlaces);
        recyclerView.setAdapter(new AbstractListAdapter<CostPlace,CostPlaceViewHolder>(costPlaces) {
            @Override
            public CostPlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_tour_cost_place,parent,false);
                return new CostPlaceViewHolder(view);
            }

            @Override
            public void onBindViewHolder(CostPlaceViewHolder holder, final int position) {
                holder.costPlace.setTypeface(tf);
                holder.totallCost.setTypeface(tf);
                holder.costPlace.setText("ট্যুর : "+costPlaces.get(position).getCostPlace());
                holder.totallCost.setText("সর্বমোট খরচ "+costPlaces.get(position).getCost()+" টাকা");
                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(TourCostCalculatorActivity.this, TourCostItemActivity.class);
                        intent.putExtra("placeid",costPlaces.get(position).getId());
                        TourCostCalculatorActivity.this.startActivity(intent);
                    }
                });
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.getAdapter().notifyDataSetChanged();

        addNewPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View promptsView = LayoutInflater.from(TourCostCalculatorActivity.this).inflate(R.layout.insert_cost_place, null,false);
                final EditText placeName = (EditText) promptsView.findViewById(R.id.et_tour_cost_place);

                AlertDialog.Builder builder = new AlertDialog.Builder(TourCostCalculatorActivity.this);
                builder.setView(promptsView);
                builder.setTitle("New Tour Cost Calculator").setPositiveButton("SUBMIT", null).setNegativeButton("CANCEL", null);

                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                Button okButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Validator.validateNotEmpty(placeName,"Required")) {
                            id = idpreference.getInt("id", 0);
                            CostPlace costPlace = new CostPlace(id, 0, placeName.getText().toString());
                            costPlaces.add(costPlace);
                            Collections.sort(costPlaces);

                            String json = gson.toJson(costPlace);
                            sharedPreferences.edit().putString(costPlace.getId()+"", json).apply();
                            idpreference.edit().putInt("id", id + 1).apply();
                            recyclerView.getAdapter().notifyDataSetChanged();
                            alertDialog.dismiss();
                        }
                    }
                });
            }
        });

        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CostItem costItem) {
        String string = sharedPreferences.getString(costItem.getTourId()+"","");
        CostPlace costPlace = gson.fromJson(string,CostPlace.class);
        costPlace.setCost(costPlace.getCost()+costItem.getCostAmount());
        string = gson.toJson(costPlace);
        sharedPreferences.edit().putString(costPlace.getId()+"",string).apply();

        costPlaces.clear();
        Map<String, ?> elements = sharedPreferences.getAll();

        for (Map.Entry<String, ?> entry : elements.entrySet()) {
            string = entry.getValue().toString();
            costPlace = gson.fromJson(string, CostPlace.class);
            costPlaces.add(costPlace);
        }
        Collections.sort(costPlaces);

        recyclerView.getAdapter().notifyDataSetChanged();

    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    private static class CostPlaceViewHolder extends RecyclerView.ViewHolder {
        protected TextView costPlace;
        protected TextView totallCost;
        protected LinearLayout linearLayout;
        public CostPlaceViewHolder(View itemView) {
            super(itemView);
            costPlace = ViewHolder.get(itemView,R.id.tv_tour_place);
            totallCost = ViewHolder.get(itemView,R.id.tv_total_cost);
            linearLayout = ViewHolder.get(itemView,R.id.cost_place_layout);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

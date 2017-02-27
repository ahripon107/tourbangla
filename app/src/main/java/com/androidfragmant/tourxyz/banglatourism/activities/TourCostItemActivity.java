package com.androidfragmant.tourxyz.banglatourism.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.RoboAppCompatActivity;
import com.androidfragmant.tourxyz.banglatourism.model.CostItem;
import com.androidfragmant.tourxyz.banglatourism.util.AbstractListAdapter;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;
import com.androidfragmant.tourxyz.banglatourism.util.Validator;
import com.androidfragmant.tourxyz.banglatourism.util.ViewHolder;
import com.google.gson.Gson;
import com.google.inject.Inject;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * @author Ripon
 */
@ContentView(R.layout.activity_tour_cost_item)
public class TourCostItemActivity extends RoboAppCompatActivity {

    public static final String EXTRA_PLACE_ID = "placeid";

    @InjectView(R.id.cost_item_recycler_view)
    private RecyclerView recyclerView;

    @InjectView(R.id.new_cost_item_button)
    private Button addNewCostItem;

    @Inject
    private Gson gson;

    @Inject
    private ArrayList<CostItem> costItems;

    private SharedPreferences sharedPreferences;
    private SharedPreferences sharedPreferencesid;

    int id, placeid;

    private Typeface tf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tf = Constants.solaimanLipiFont(this);

        placeid = getIntent().getIntExtra(EXTRA_PLACE_ID, 0);

        sharedPreferences = getSharedPreferences(Constants.COST_ITEM_PREFERENCE_FILE, Context.MODE_PRIVATE);
        sharedPreferencesid = getSharedPreferences(Constants.COST_ITEM_ID_PREFERENCE_FILE, Context.MODE_PRIVATE);

        if (!sharedPreferencesid.contains("id")) {
            sharedPreferencesid.edit().putInt("id", 1).apply();
        }

        Map<String, ?> elements = sharedPreferences.getAll();

        for (Map.Entry<String, ?> entry : elements.entrySet()) {
            String string = entry.getValue().toString();
            CostItem costItem = gson.fromJson(string, CostItem.class);
            if (costItem.getTourId() == placeid) {
                costItems.add(costItem);
            }
        }

        Collections.sort(costItems);

        recyclerView.setAdapter(new AbstractListAdapter<CostItem,CostItemViewHolder>(costItems) {
            @Override
            public CostItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sigle_tour_cost_item,parent,false);
                return new CostItemViewHolder(view);
            }

            @Override
            public void onBindViewHolder(CostItemViewHolder holder, int position) {
                holder.costAmount.setTypeface(tf);
                holder.costPurpose.setTypeface(tf);
                holder.costAmount.setText("খরচের পরিমাণ : "+costItems.get(position).getCostAmount()+" টাকা");
                holder.costPurpose.setText("খরচের খাত : "+costItems.get(position).getCostPurpose());
            }

        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.getAdapter().notifyDataSetChanged();

        addNewCostItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View promptsView = LayoutInflater.from(TourCostItemActivity.this).inflate(R.layout.insert_cost_item, null, false);
                final EditText costAmount = (EditText) promptsView.findViewById(R.id.et_tour_cost_amount);
                final EditText costPurpose = (EditText) promptsView.findViewById(R.id.et_tour_cost_purpose);

                AlertDialog.Builder builder = new AlertDialog.Builder(TourCostItemActivity.this);

                builder.setView(promptsView);
                builder.setTitle("New Cost Item").setPositiveButton("SUBMIT", null).setNegativeButton("CANCEL", null);

                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                Button okButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Validator.validateNotEmpty(costAmount, "Required") && Validator.validateNotEmpty(costPurpose, "Required")) {

                            id = sharedPreferencesid.getInt("id", 0);

                            CostItem costItem = new CostItem(id, placeid, Integer.parseInt(costAmount.getText().toString()), costPurpose.getText().toString());
                            costItems.add(costItem);

                            Collections.sort(costItems);
                            recyclerView.getAdapter().notifyDataSetChanged();

                            String json = gson.toJson(costItem);
                            sharedPreferences.edit().putString(id + "", json).apply();
                            sharedPreferencesid.edit().putInt("id", id + 1).apply();
                            alertDialog.dismiss();
                            EventBus.getDefault().post(costItem);
                        }
                    }
                });
            }
        });
    }

    private static class CostItemViewHolder extends RecyclerView.ViewHolder {
        protected TextView costAmount;
        protected TextView costPurpose;

        public CostItemViewHolder(View itemView) {
            super(itemView);
            costAmount = ViewHolder.get(itemView,R.id.tv_cost_amount);
            costPurpose = ViewHolder.get(itemView,R.id.tv_cost_purpose);
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

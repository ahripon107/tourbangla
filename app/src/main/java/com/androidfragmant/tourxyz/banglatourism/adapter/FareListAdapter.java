package com.androidfragmant.tourxyz.banglatourism.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.model.Fare;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;
import com.androidfragmant.tourxyz.banglatourism.util.ViewHolder;

import java.util.ArrayList;

/**
 * Created by Ripon on 8/2/16.
 */
public class FareListAdapter extends RecyclerView.Adapter<FareListAdapter.FareViewHolder> {

    Context context;
    ArrayList<Fare> fareArrayList;
    Typeface tf;

    public FareListAdapter(Context context,ArrayList<Fare> fareArrayList) {
        this.context = context;
        this.fareArrayList = fareArrayList;
        tf = Typeface.createFromAsset(context.getAssets(), Constants.SOLAIMAN_LIPI_FONT);
    }

    @Override
    public FareViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fare_list_item,parent,false);
        return new FareViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FareViewHolder holder, int position) {
        Fare fare = fareArrayList.get(position);

        holder.companyName.setTypeface(tf);
        holder.fare.setTypeface(tf);
        holder.timeToLeave.setTypeface(tf);
        holder.estimatedTime.setTypeface(tf);
        holder.leavingPlace.setTypeface(tf);

        holder.companyName.setText(fare.getCompanyName());
        holder.fare.setText("ভাড়াঃ "+fare.getFare());
        holder.timeToLeave.setText("ছাড়ার সময়ঃ "+fare.getStartTime());
        holder.estimatedTime.setText("অনুমিত সময়ঃ "+fare.getEstimatedTime());
        holder.leavingPlace.setText("ছাড়ার স্থানঃ "+fare.getLeavePlace());
    }

    @Override
    public int getItemCount() {
        return fareArrayList.size();
    }

    static class FareViewHolder extends RecyclerView.ViewHolder {
        protected TextView companyName;
        protected TextView fare;
        protected TextView timeToLeave;
        protected TextView estimatedTime;
        protected TextView leavingPlace;

        public FareViewHolder(View itemView) {
            super(itemView);
            companyName = ViewHolder.get(itemView,R.id.tvCompanyName);
            fare = ViewHolder.get(itemView,R.id.tvFare);
            timeToLeave = ViewHolder.get(itemView,R.id.tvTimeToLeave);
            estimatedTime = ViewHolder.get(itemView,R.id.tvEstimatedTime);
            leavingPlace = ViewHolder.get(itemView,R.id.tvLeavingPlace);
        }
    }
}

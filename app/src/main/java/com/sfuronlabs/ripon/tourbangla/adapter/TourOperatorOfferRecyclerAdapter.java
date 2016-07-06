package com.sfuronlabs.ripon.tourbangla.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sfuronlabs.ripon.tourbangla.R;
import com.sfuronlabs.ripon.tourbangla.model.TourOperatorOffer;
import com.sfuronlabs.ripon.tourbangla.util.ViewHolder;

import java.util.ArrayList;

/**
 * Created by Ripon on 7/6/16.
 */
public class TourOperatorOfferRecyclerAdapter extends RecyclerView.Adapter<TourOperatorOfferRecyclerAdapter.TourOperatorOfferViewHolder> {

    Context context;
    ArrayList<TourOperatorOffer> offers;
    public TourOperatorOfferRecyclerAdapter(Context context, ArrayList<TourOperatorOffer> offers) {
        this.context = context;
        this.offers = offers;
    }
    @Override
    public TourOperatorOfferViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return null;
    }

    @Override
    public void onBindViewHolder(TourOperatorOfferViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class TourOperatorOfferViewHolder extends RecyclerView.ViewHolder {

        TextView offerTitle;
        TextView offerSummary;
        Button button;
        ImageView offerImage;

        public TourOperatorOfferViewHolder(View itemView) {
            super(itemView);
            offerTitle = ViewHolder.get(itemView, R.id.tourOfferTitle);
            offerSummary = ViewHolder.get(itemView,R.id.tourOfferSummary);
            button = ViewHolder.get(itemView,R.id.tourOfferDetails);
            offerImage = ViewHolder.get(itemView,R.id.tourOfferImage);
        }
    }
}

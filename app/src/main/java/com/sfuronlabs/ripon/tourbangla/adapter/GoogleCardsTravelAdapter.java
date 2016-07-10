package com.sfuronlabs.ripon.tourbangla.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sfuronlabs.ripon.tourbangla.R;
import com.sfuronlabs.ripon.tourbangla.activities.NewPlaceDetailsActivity;
import com.sfuronlabs.ripon.tourbangla.model.Place;
import com.sfuronlabs.ripon.tourbangla.util.ImageUtil;
import com.sfuronlabs.ripon.tourbangla.util.ViewHolder;

import java.util.List;

/**
 * Created by Ripon on 10/1/15.
 */
public class GoogleCardsTravelAdapter extends RecyclerView.Adapter<GoogleCardsTravelAdapter.PlaceCardViewHolder> {
    private LayoutInflater mInflater;
    Context con;
    List<Place> items;

    public GoogleCardsTravelAdapter(Context context, List<Place> items) {
        con = context;
        this.items = items;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public PlaceCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_google_cards_travel,parent,false);
        return new PlaceCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlaceCardViewHolder holder, int position) {
        final Place place = items.get(holder.getAdapterPosition());
        ImageUtil.displayImage(holder.imageView, "http://apisea.xyz/TourBangla/images/" + place.getPicture() + ".jpg", null);
        holder.title.setText(place.getName());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(con, NewPlaceDetailsActivity.class);
                i.putExtra("id", place.getId());
                con.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class PlaceCardViewHolder extends RecyclerView.ViewHolder {

        protected ImageView imageView;
        protected TextView title;
        protected LinearLayout linearLayout;

        public PlaceCardViewHolder(View itemView) {
            super(itemView);

            imageView = ViewHolder.get(itemView,R.id.list_item_google_cards_travel_image);
            title = ViewHolder.get(itemView,R.id.list_item_google_cards_travel_title);
            linearLayout = ViewHolder.get(itemView,R.id.linear);
        }
    }

}

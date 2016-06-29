package com.sfuronlabs.ripon.tourbangla.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sfuronlabs.ripon.tourbangla.R;
import com.sfuronlabs.ripon.tourbangla.model.DummyModel;
import com.sfuronlabs.ripon.tourbangla.util.ImageUtil;
import com.sfuronlabs.ripon.tourbangla.util.ViewHolder;

import java.util.List;

/**
 * Created by Ripon on 10/1/15.
 */
public class GoogleCardsTravelAdapter extends RecyclerView.Adapter<GoogleCardsTravelAdapter.PlaceCardViewHolder> {
    private LayoutInflater mInflater;
    Context con;
    List<DummyModel> items;

    public GoogleCardsTravelAdapter(Context context, List<DummyModel> items) {
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
    public void onBindViewHolder(PlaceCardViewHolder holder, final int position) {
        DummyModel dummyModel = items.get(position);
        ImageUtil.displayImage(holder.imageView, dummyModel.getImageURL(), null);
        holder.title.setText(dummyModel.getText());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent("android.intent.action.NEWPLACEDETAILSACTIVITY");
                i.putExtra("index", position);
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

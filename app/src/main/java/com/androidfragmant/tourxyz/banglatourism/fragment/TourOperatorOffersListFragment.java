package com.androidfragmant.tourxyz.banglatourism.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidfragmant.tourxyz.banglatourism.activities.TourOfferDetailsActivity;
import com.androidfragmant.tourxyz.banglatourism.util.AbstractListAdapter;
import com.androidfragmant.tourxyz.banglatourism.util.DefaultMessageHandler;
import com.androidfragmant.tourxyz.banglatourism.util.NetworkService;
import com.androidfragmant.tourxyz.banglatourism.util.ViewHolder;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.model.TourOperatorOffer;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

/**
 * @author Ripon
 */
public class TourOperatorOffersListFragment extends RoboFragment {

    @InjectView(R.id.gridview)
    private RecyclerView recyclerView;

    @Inject
    private ArrayList<TourOperatorOffer> offers;

    @Inject
    private NetworkService networkService;

    private Typeface tf;

    private AbstractListAdapter<TourOperatorOffer, TourOperatorOfferViewHolder> tourOperatorOfferListAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tour_offer,container,false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        tf = Constants.solaimanLipiFont(getContext());

        tourOperatorOfferListAdapter = new AbstractListAdapter<TourOperatorOffer, TourOperatorOfferViewHolder>(offers) {
            @Override
            public TourOperatorOfferViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singletouroperatoroffer, parent, false);
                return new TourOperatorOfferViewHolder(view);
            }

            @Override
            public void onBindViewHolder(TourOperatorOfferViewHolder holder, int position) {
                final TourOperatorOffer offer = offers.get(position);
                holder.offerTitle.setText(offer.getTitle());
                holder.offerTitle.setTypeface(tf);
                holder.offerSummary.setText(offer.getSummary());
                Picasso.with(getContext()).load(offers.get(position).getImageName()).into(holder.offerImage);
                holder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), TourOfferDetailsActivity.class);
                        intent.putExtra("id", offer.getId());
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    }
                });
            }
        };

        networkService.fetchTourOperatorOffers(new DefaultMessageHandler(getContext(), true) {
            @Override
            public void onSuccess(Message msg) {
                String string = (String) msg.obj;

                try {
                    JSONObject response = new JSONObject(string);
                    JSONArray jsonArray = response.getJSONArray("content");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        Gson gson = new Gson();
                        TourOperatorOffer operatorOffer = gson.fromJson(String.valueOf(jsonObject), TourOperatorOffer.class);
                        offers.add(operatorOffer);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                tourOperatorOfferListAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView.setAdapter(tourOperatorOfferListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private static class TourOperatorOfferViewHolder extends RecyclerView.ViewHolder {
        protected TextView offerTitle;
        protected TextView offerSummary;
        protected Button button;
        protected ImageView offerImage;

        public TourOperatorOfferViewHolder(View itemView) {
            super(itemView);
            offerTitle = ViewHolder.get(itemView, R.id.tourOfferTitle);
            offerSummary = ViewHolder.get(itemView, R.id.tourOfferSummary);
            button = ViewHolder.get(itemView, R.id.tourOfferDetails);
            offerImage = ViewHolder.get(itemView, R.id.tourOfferImage);
        }
    }
}

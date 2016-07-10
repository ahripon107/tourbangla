package com.sfuronlabs.ripon.tourbangla.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sfuronlabs.ripon.tourbangla.R;
import com.sfuronlabs.ripon.tourbangla.activities.DivisionListActivity;
import com.sfuronlabs.ripon.tourbangla.activities.TourOperatorOffersListActivity;
import com.sfuronlabs.ripon.tourbangla.model.HomeFragmentElement;
import com.sfuronlabs.ripon.tourbangla.util.ViewHolder;

import java.util.ArrayList;

/**
 * Created by amin on 6/27/16.
 */
public class HomeFragmentRecyclerAdapter extends RecyclerView.Adapter<HomeFragmentRecyclerAdapter.HomeFragmentViewHolder> {

    ArrayList<HomeFragmentElement> elements;
    Context context;
    public HomeFragmentRecyclerAdapter(Context context, ArrayList<HomeFragmentElement> elements) {
        this.elements = elements;
        this.context = context;
    }

    @Override
    public HomeFragmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlehomefragmentelement,parent,false);
        return new HomeFragmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HomeFragmentViewHolder holder, final int position) {

        HomeFragmentElement element = elements.get(position);
        holder.title.setText(element.getTitle());
        holder.button.setText(element.getButtonText());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 0) {
                    Intent i = new Intent(context, DivisionListActivity.class);
                    context.startActivity(i);
                }  else if (position == 1) {
                    AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
                    builderSingle.setIcon(R.drawable.ic_profile);
                    builderSingle.setTitle("Select One Name:-");
                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                            context,
                            android.R.layout.select_dialog_singlechoice);
                    arrayAdapter.add("DHAKA");
                    arrayAdapter.add("CHITTAGONG");
                    arrayAdapter.add("RAJSHAHI");
                    arrayAdapter.add("KHULNA");
                    arrayAdapter.add("SYLHET");
                    arrayAdapter.add("BARISAL");
                    arrayAdapter.add("RANGPUR");
                    builderSingle.setNegativeButton("cancel",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                    builderSingle.setAdapter(arrayAdapter,
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String strName = arrayAdapter.getItem(which);
                                    Intent i = new Intent("android.intent.action.HOTELS");
                                    i.putExtra("place", strName);
                                    context.startActivity(i);

                                }
                            });
                    builderSingle.show();

                } else if (position == 2) {
                    Intent i = new Intent("android.intent.action.SELECTTOUROPERATOR");
                    context.startActivity(i);
                } else if (position == 3) {
                    Intent i = new Intent("android.intent.action.TOURBLOG");
                    context.startActivity(i);
                } else if (position == 4) {
                    Intent i = new Intent("android.intent.action.SUGGESTNEWPLACE");
                    context.startActivity(i);
                } else if (position == 5) {
                    Intent intent = new Intent(context, TourOperatorOffersListActivity.class);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return elements.size();
    }

    public static class HomeFragmentViewHolder extends RecyclerView.ViewHolder {

        protected TextView title;
        protected Button button;
        protected ImageView imageView;
        protected LinearLayout linearLayout;

        public HomeFragmentViewHolder(View itemView) {
            super(itemView);

            title = ViewHolder.get(itemView, R.id.tourTitle);
            button = ViewHolder.get(itemView, R.id.button2);
            imageView = ViewHolder.get(itemView,R.id.imageView);
            linearLayout = ViewHolder.get(itemView,R.id.cardcontainer);
        }
    }
}

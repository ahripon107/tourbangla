package com.androidfragmant.tourxyz.banglatourism.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.model.NavDrawerItem;

import java.util.Collections;
import java.util.List;

/**
 * Created by Ripon on 6/11/15.
 */
public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.NavigationDrawerViewHolder>{
    List<NavDrawerItem> data = Collections.emptyList();
    private Context context;
    private Typeface tf;

    public NavigationDrawerAdapter(Context context, List<NavDrawerItem> data) {
        this.context = context;
        this.data = data;
        tf = Typeface.createFromAsset(context.getAssets(),"font/timeroman.ttf");
    }


    @Override
    public NavigationDrawerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_drawer_row, parent, false);
        view.getBackground().setAlpha(128);
        return new NavigationDrawerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NavigationDrawerViewHolder holder, int position) {
        NavDrawerItem current = data.get(position);
        holder.title.setTypeface(tf,Typeface.BOLD);
        holder.title.setText(current.getTitle());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class NavigationDrawerViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public NavigationDrawerViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }
}

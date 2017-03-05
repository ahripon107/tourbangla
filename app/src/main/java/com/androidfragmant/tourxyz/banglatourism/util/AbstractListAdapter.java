package com.androidfragmant.tourxyz.banglatourism.util;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * @author Ripon
 */
public abstract class AbstractListAdapter<X,T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T>{

    ArrayList<X> elements;

    public AbstractListAdapter(ArrayList<X> elements){
        this.elements = elements;
    }
    @Override
    public abstract T onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public abstract void onBindViewHolder(T holder, int position);

    @Override
    public int getItemCount() {
        return elements.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}


package com.androidfragmant.tourxyz.banglatourism.util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by amin on 8/21/16.
 */
public abstract class FilterableRecyclerAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> implements android.widget.Filterable {

    private ArrayList<Filterable> mOriginalValues;
    private List<Filterable> mObjects;// = new ArrayList<>();
    private final Object mLock = new Object();
    private CharSequence lastUsedFilterText;
    private ArrayFilter mFilter;
    private boolean mNotifyOnChange = true;
    private Context mContext;
    private LayoutInflater mInflater;
    private int mResource;
    private int mFieldId = 0;
    private int mDropDownResource;
    @Override
    public abstract T onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public abstract void onBindViewHolder(T holder, int position);

    @Override
    public int getItemCount() {
        return mObjects.size();
    }

    public FilterableRecyclerAdapter(Context context, int textViewResourceId, List<Filterable> objects) {
        init(context, textViewResourceId, 0, objects);
    }

    public void add(Filterable object) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.add(object);
            } else {
                mObjects.add(object);
            }
        }

        rePerformFilter();

        if (mNotifyOnChange) notifyDataSetChanged();
    }


    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }

    public void clear() {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.clear();
            } else {
                mObjects.clear();
            }
        }

        rePerformFilter();

        if (mNotifyOnChange) notifyDataSetChanged();
    }

    private void rePerformFilter() {
        if (lastUsedFilterText != null) {
            getFilter().filter(lastUsedFilterText);
        }
    }

    public void addAll(Collection<? extends Filterable> collection) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.addAll(collection);
            } else {
                mObjects.addAll(collection);
            }
        }

        rePerformFilter();

        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void addAll(Filterable... items) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                Collections.addAll(mOriginalValues, items);
            } else {
                Collections.addAll(mObjects, items);
            }
        }

        rePerformFilter();

        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void insert(Filterable object, int index) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.add(index, object);
            } else {
                mObjects.add(index, object);
            }
        }

        rePerformFilter();

        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void remove(Filterable object) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.remove(object);
            } else {
                mObjects.remove(object);
            }
        }

        rePerformFilter();

        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void sort(Comparator<? super Filterable> comparator) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                Collections.sort(mOriginalValues, comparator);
            } else {
                Collections.sort(mObjects, comparator);
            }
        }

        rePerformFilter();

        if (mNotifyOnChange) notifyDataSetChanged();
    }

    private void init(Context context, int resource, int textViewResourceId, List<Filterable> objects) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mResource = mDropDownResource = resource;
        mObjects = objects;
        mFieldId = textViewResourceId;
    }

    public Context getContext() {
        return mContext;
    }

    /**
     * {@inheritDoc}
     */
    public int getCount() {
        return mObjects.size();
    }

    /**
     * {@inheritDoc}
     */
    public Filterable getItem(int position) {
        return mObjects.get(position);
    }

    /**
     * Returns the position of the specified item in the array.
     *
     * @param item The item to retrieve the position of.
     * @return The position of the specified item.
     */
    public int getPosition(Filterable item) {
        return mObjects.indexOf(item);
    }

    /**
     * {@inheritDoc}
     */
    public long getItemId(int position) {
        return position;
    }

    /**
     * Always set value for each view inside convertView. Because when scrolling, convertView might
     * get reused and contain dirty data. A common error pattern is setting values in 'if' condition
     * but not in 'else'. In that case, dirty data will have shown.
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent, mResource);
    }

    private View createViewFromResource(int position, View convertView, ViewGroup parent,
                                        int resource) {
        View view;
        TextView text;

        if (convertView == null) {
            view = mInflater.inflate(resource, parent, false);
        } else {
            view = convertView;
        }

        try {
            if (mFieldId == 0) {
                //  If no custom field is assigned, assume the whole resource is a TextView
                text = (TextView) view;
            } else {
                //  Otherwise, find the TextView field within the layout
                text = (TextView) view.findViewById(mFieldId);
            }
        } catch (ClassCastException e) {
            Log.e("FilterableArrayAdapter", "You must supply a resource ID for a TextView");
            throw new IllegalStateException(
                    "FilterableArrayAdapter requires the resource ID to be a TextView", e);
        }

        Filterable item = getItem(position);
        if (item instanceof CharSequence) {
            text.setText((CharSequence) item);
        } else {
            text.setText(item.toString());
        }

        return view;
    }


    private class ArrayFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence searchText) {
            FilterResults results = new FilterResults();

            if (mOriginalValues == null) {
                synchronized (mLock) {
                    mOriginalValues = new ArrayList<Filterable>(mObjects);
                }
            }

            if (searchText == null || searchText.length() == 0) {
                ArrayList<Filterable> list;
                synchronized (mLock) {
                    list = new ArrayList<Filterable>(mOriginalValues);
                }
                results.values = list;
                results.count = list.size();
            } else {
                String searchTextLowercased = searchText.toString().toLowerCase();

                ArrayList<Filterable> values;
                synchronized (mLock) {
                    values = new ArrayList<Filterable>(mOriginalValues);
                }

                final ArrayList<Filterable> newValues = new ArrayList<Filterable>();

                for (final Filterable value : values) {
                    if (value.getFilterableText().toLowerCase().contains(searchTextLowercased)) {
                        newValues.add(value);
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }

            lastUsedFilterText = searchText;

            return results;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mObjects = (List<Filterable>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                //notifyDataSetInvalidated();
                notifyDataSetChanged();
            }
        }
    }
}

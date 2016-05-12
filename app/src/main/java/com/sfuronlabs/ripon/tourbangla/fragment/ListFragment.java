package com.sfuronlabs.ripon.tourbangla.fragment;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import com.sfuronlabs.ripon.tourbangla.R;

/**
 * Created by Ripon on 6/13/15.
 */
public class ListFragment extends Fragment implements OnItemClickListener{
    private View v;
    private static GridView gridView;
    private List<String> listOfDistricts;
    private ArrayAdapter<String> adapter;
    ListItemSelectedListener itemListerner;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        inflater = (LayoutInflater)getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        v = inflater.inflate(R.layout.listofattributes, null, false);

        addListItemsToListView();

        initializeListeners();

        return v;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);

        itemListerner = (ListItemSelectedListener)activity;

    }
    private void addListItemsToListView()
    {
        gridView = (GridView)v.findViewById(R.id.gridviewlists);

        listOfDistricts = new ArrayList<String>();

        listOfDistricts.add("Description");

        listOfDistricts.add("How to go");

        listOfDistricts.add("Hotels");

        listOfDistricts.add("Other Info");

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listOfDistricts);

        gridView.setAdapter(adapter);
    }

    private void initializeListeners()
    {
        gridView.setOnItemClickListener(this);
    }

    public interface ListItemSelectedListener
    {
        public void listItemSelectedListener(String name);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        itemListerner.listItemSelectedListener(listOfDistricts.get(position));
    }
}

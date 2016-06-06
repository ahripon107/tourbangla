package com.sfuronlabs.ripon.tourbangla.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.sfuronlabs.ripon.tourbangla.R;
import com.sfuronlabs.ripon.tourbangla.adapter.GridAdapter;

/**
 * Created by Ripon on 6/11/15.
 */
public class HomeFragment extends Fragment {

    String web[];
    String picname[];
    ListView view;

    public HomeFragment() {
        web = new String[6];
        picname = new String[6];
        web[0] = "Browse by division";
        web[1] = "Browse by type";
        web[2] = "Hotels";
        web[3] = "Tour operators";
        web[4] = "Tour Blog";
        web[5] = "Suggest New Place";
        picname[0] = "division";
        picname[1] = "type";
        picname[2] = "hotels";
        picname[3] = "touroperator";
        picname[4] = "tourblog";
        picname[5] = "suggestplace";

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        GridAdapter gridAdapter = new GridAdapter(getActivity(), this.web, this.picname,
                "font/Amaranth-Bold.ttf");
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        view = (ListView) rootView.findViewById(R.id.gridviewselect);
        view.setAdapter(gridAdapter);

        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent i = new Intent("android.intent.action.BROWSEBYDIVISION");
                    startActivity(i);
                } else if (position == 1) {
                    Intent i = new Intent("android.intent.action.BROWSEBYTYPE");
                    startActivity(i);
                } else if (position == 2) {
                    AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
                    builderSingle.setIcon(R.drawable.ic_profile);
                    builderSingle.setTitle("Select One Name:-");
                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                            getActivity(),
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
                                    startActivity(i);

                                }
                            });
                    builderSingle.show();

                } else if (position == 3) {
                    Intent i = new Intent("android.intent.action.SELECTTOUROPERATOR");
                    startActivity(i);
                } else if (position == 4) {
                    Intent i = new Intent("android.intent.action.TOURBLOG");
                    startActivity(i);
                } else if (position == 5) {
                    Intent i = new Intent("android.intent.action.SUGGESTNEWPLACE");
                    startActivity(i);
                }

            }
        });
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}

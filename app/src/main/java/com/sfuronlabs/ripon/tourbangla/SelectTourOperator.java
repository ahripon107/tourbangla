package com.sfuronlabs.ripon.tourbangla;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.sfuronlabs.ripon.tourbangla.view.ProgressWheel;

/**
 * Created by Ripon on 6/18/15.
 */
public class SelectTourOperator extends AppCompatActivity {
    ProgressWheel progressWheel;

    ListView  gridView;
    String[] names;
    public static ArrayList<ParseObject> allOperators;
    Toolbar toolbar;
    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selecttouroperator);
        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        progressWheel = (ProgressWheel) findViewById(R.id.pwTourOperator);
        progressWheel.spin();

        adView = (AdView) findViewById(R.id.adViewTourOperator);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        gridView = (ListView) findViewById(R.id.gridViewtouroperator);
        setTitle("Tour Operators");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("TourOperator");
        query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
        //final ProgressDialog dialog = ProgressDialog.show(SelectTourOperator.this, "Loading", "Please wait...", true);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (progressWheel.isSpinning())
                {
                    progressWheel.stopSpinning();
                    progressWheel.setVisibility(View.INVISIBLE);
                }
                //dialog.dismiss();
                if (e==null)
                {
                    names = new String[list.size()];
                    allOperators = (ArrayList)list;
                    for (int i = 0; i < list.size(); i++) {
                        names[i] = ((String) list.get(i).get("name"));
                    }
                    SingleTourOperator adapter = new SingleTourOperator(SelectTourOperator.this, names);
                    gridView.setAdapter(adapter);
                }
                else
                {
                    Toast.makeText(SelectTourOperator.this,"Error occured",Toast.LENGTH_LONG).show();
                }

            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent("android.intent.action.TOUROPERATORDETAILS");
                i.putExtra("namet", names[position]);
                i.putExtra("index", position);
                startActivity(i);
                return;
            }
        });

        //AdRequest adRequest = new AdRequest.Builder().addTestDevice("D3FA0144AD5EA91460638306E4CB0FB2").build();
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }
}

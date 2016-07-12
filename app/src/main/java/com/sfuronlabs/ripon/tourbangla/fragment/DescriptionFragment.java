package com.sfuronlabs.ripon.tourbangla.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sfuronlabs.ripon.tourbangla.R;
import com.sfuronlabs.ripon.tourbangla.util.Constants;

/**
 * Created by Ripon on 8/26/15.
 */
public class DescriptionFragment extends Fragment {

    Typeface tf;

    public DescriptionFragment() {
    }

    public static DescriptionFragment newInstanceOfDescriptionFragment(String text) {
        DescriptionFragment myFragment = new DescriptionFragment();
        Bundle arguments = new Bundle();
        arguments.putString(Constants.DESCRIPTION, text);
        myFragment.setArguments(arguments);
        return myFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.placedescription, container, false);
        TextView textView = (TextView) v.findViewById(R.id.placedescription);
        tf = Typeface.createFromAsset(getActivity().getAssets(), "font/solaimanlipi.ttf");
        textView.setTypeface(tf);
        textView.setText(getArguments().getString("description"));
        return v;
    }
}
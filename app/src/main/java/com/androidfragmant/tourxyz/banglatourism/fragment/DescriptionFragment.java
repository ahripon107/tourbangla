package com.androidfragmant.tourxyz.banglatourism.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

/**
 * @author Ripon
 */
public class DescriptionFragment extends RoboFragment {
    public static final String DESCRIPTION = "description";

    @InjectView(R.id.placedescription)
    TextView description;

    public static DescriptionFragment newInstanceOfDescriptionFragment(String text) {
        DescriptionFragment myFragment = new DescriptionFragment();
        Bundle arguments = new Bundle();
        arguments.putString(DESCRIPTION, text);
        myFragment.setArguments(arguments);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.placedescription, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        description.setTypeface(Constants.solaimanLipiFont(getContext()));
        description.setText(getArguments().getString("description"));

    }
}
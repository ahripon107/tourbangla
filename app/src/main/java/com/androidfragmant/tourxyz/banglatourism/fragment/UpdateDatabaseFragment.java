package com.androidfragmant.tourxyz.banglatourism.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidfragmant.tourxyz.banglatourism.FileProcessor;
import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.activities.DivisionListActivity;
import com.androidfragmant.tourxyz.banglatourism.util.DefaultMessageHandler;
import com.androidfragmant.tourxyz.banglatourism.util.NetworkService;
import com.google.inject.Inject;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

/**
 * @author Ripon
 */
public class UpdateDatabaseFragment extends RoboFragment {

    @InjectView(R.id.tvUpdateDatabase)
    private TextView textView;

    @InjectView(R.id.btnUpdateDatabase)
    private Button button;

    @Inject
    private NetworkService networkService;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_update_database,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textView.setText("Click button to update place database");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkAvailable()) {
                    Toast.makeText(getActivity(),"Please check your internet connection",Toast.LENGTH_LONG).show();
                }
                else {
                    networkService.fetchPlaces(new DefaultMessageHandler(getContext(),true) {
                        @Override
                        public void onSuccess(Message msg) {
                            String response = (String) msg.obj;
                            FileProcessor fileProcessor = new FileProcessor(getContext());
                            fileProcessor.writeToFile(response);
                            Intent i = new Intent(getActivity(), DivisionListActivity.class);
                            getActivity().startActivity(i);
                        }
                    });

                }
            }
        });

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

package com.androidfragmant.tourxyz.banglatourism.fragment;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidfragmant.tourxyz.banglatourism.util.DefaultMessageHandler;
import com.androidfragmant.tourxyz.banglatourism.util.NetworkService;
import com.androidfragmant.tourxyz.banglatourism.util.Validator;
import com.google.inject.Inject;
import com.androidfragmant.tourxyz.banglatourism.R;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

/**
 * @author Ripon
 */
public class FeedbackFragment extends RoboFragment {

    @InjectView((R.id.etFeedbackTitle))
    private EditText title;

    @InjectView((R.id.etFeedbackDetails))
    private EditText details;

    @InjectView(R.id.btnSend)
    private Button send;

    @Inject
    NetworkService networkService;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmentfeedback, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validator.validateNotEmpty(title, "Required") && Validator.validateNotEmpty(details, "Required")) {

                    networkService.sendFeedback(title.getText().toString(), details.getText().toString(), new DefaultMessageHandler(getContext(),true) {
                        @Override
                        public void onSuccess(Message msg) {
                            title.getText().clear();
                            details.getText().clear();
                            Toast.makeText(getContext(), "Thank you for your feedback.", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }
}

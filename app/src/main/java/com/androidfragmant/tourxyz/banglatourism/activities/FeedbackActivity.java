package com.androidfragmant.tourxyz.banglatourism.activities;

import android.os.Bundle;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidfragmant.tourxyz.banglatourism.RoboAppCompatActivity;
import com.androidfragmant.tourxyz.banglatourism.util.DefaultMessageHandler;
import com.androidfragmant.tourxyz.banglatourism.util.NetworkService;
import com.androidfragmant.tourxyz.banglatourism.util.Validator;
import com.google.inject.Inject;
import com.androidfragmant.tourxyz.banglatourism.R;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * @author Ripon
 */
@ContentView(R.layout.fragmentfeedback)
public class FeedbackActivity extends RoboAppCompatActivity {

    @InjectView((R.id.etFeedbackTitle))
    private EditText title;

    @InjectView((R.id.etFeedbackDetails))
    private EditText details;

    @InjectView(R.id.btnSend)
    private Button send;

    @Inject
    private NetworkService networkService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validator.validateNotEmpty(title, "Required") && Validator.validateNotEmpty(details, "Required")) {

                    networkService.sendFeedback(title.getText().toString(), details.getText().toString(), new DefaultMessageHandler(FeedbackActivity.this,true) {
                        @Override
                        public void onSuccess(Message msg) {
                            title.getText().clear();
                            details.getText().clear();
                            Toast.makeText(FeedbackActivity.this, "Thank you for your feedback.", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

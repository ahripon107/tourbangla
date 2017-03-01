package com.androidfragmant.tourxyz.banglatourism.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidfragmant.tourxyz.banglatourism.FileProcessor;
import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.RoboAppCompatActivity;
import com.androidfragmant.tourxyz.banglatourism.util.DefaultMessageHandler;
import com.androidfragmant.tourxyz.banglatourism.util.NetworkService;
import com.google.inject.Inject;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * @author Ripon
 */
@ContentView(R.layout.fragment_update_database)
public class UpdateDatabaseActivity extends RoboAppCompatActivity {

    @InjectView(R.id.tvUpdateDatabase)
    private TextView textView;

    @InjectView(R.id.btnUpdateDatabase)
    private Button button;

    @Inject
    private NetworkService networkService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textView.setText("Click button to update place database");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkAvailable()) {
                    Toast.makeText(UpdateDatabaseActivity.this,"Please check your internet connection",Toast.LENGTH_LONG).show();
                }
                else {
                    networkService.fetchPlaces(new DefaultMessageHandler(UpdateDatabaseActivity.this,true) {
                        @Override
                        public void onSuccess(Message msg) {
                            String response = (String) msg.obj;
                            FileProcessor fileProcessor = new FileProcessor(UpdateDatabaseActivity.this);
                            fileProcessor.writeToFile(response);
                            Intent i = new Intent(UpdateDatabaseActivity.this, DivisionListActivity.class);
                            UpdateDatabaseActivity.this.startActivity(i);
                        }
                    });

                }
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) UpdateDatabaseActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.right_in, R.anim.right_out);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }
}

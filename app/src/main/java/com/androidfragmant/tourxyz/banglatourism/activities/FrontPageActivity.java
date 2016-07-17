package com.androidfragmant.tourxyz.banglatourism.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.androidfragmant.tourxyz.banglatourism.R;

/**
 * Created by Ripon on 9/27/15.
 */
public class FrontPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frontpage);
        final Thread t;
        t = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(500);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent intent1 = new Intent(FrontPageActivity.this, MainActivity.class);
                    startActivity(intent1);

                }
            }

        });
        t.start();

    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}

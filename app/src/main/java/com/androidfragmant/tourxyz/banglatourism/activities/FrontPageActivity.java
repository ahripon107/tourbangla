package com.androidfragmant.tourxyz.banglatourism.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.androidfragmant.tourxyz.banglatourism.R;

/**
 * @author Ripon
 */
public class FrontPageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_frontpage);
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

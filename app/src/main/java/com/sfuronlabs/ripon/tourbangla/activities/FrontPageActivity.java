package com.sfuronlabs.ripon.tourbangla.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sfuronlabs.ripon.tourbangla.R;

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
                // TODO Auto-generated method stub
                try {
                    Thread.sleep(2000);

                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    Intent intent1 = new Intent(FrontPageActivity.this,MainActivity.class);

                    //Intent intent = new Intent("android.intent.action.CHOOSESUBJECT");
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

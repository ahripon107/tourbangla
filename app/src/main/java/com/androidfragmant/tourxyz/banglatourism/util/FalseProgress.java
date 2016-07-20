package com.androidfragmant.tourxyz.banglatourism.util;

import android.os.AsyncTask;

import com.androidfragmant.tourxyz.banglatourism.view.cpb.CircularProgressButton;

/**
 * Created by amin on 7/20/16.
 */
public class FalseProgress extends AsyncTask<Integer, Integer, Integer> {

    private CircularProgressButton cpb;

    public FalseProgress(CircularProgressButton cpb) {
        this.cpb = cpb;
    }

    @Override
    protected Integer doInBackground(Integer... params) {
        for (int progress = 0; progress < 100; progress += 5) {
            publishProgress(progress);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return params[0];
    }

    @Override
    protected void onPostExecute(Integer result) {
        cpb.setProgress(result);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = values[0];
        cpb.setProgress(progress);
    }
}
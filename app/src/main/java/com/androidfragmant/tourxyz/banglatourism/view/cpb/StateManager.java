package com.androidfragmant.tourxyz.banglatourism.view.cpb;

/**
 * Created by Ripon on 10/2/15.
 */
public class StateManager {
    private boolean mIsEnabled;
    private int mProgress;

    public StateManager(CircularProgressButton progressButton) {
        mIsEnabled = progressButton.isEnabled();
        mProgress = progressButton.getProgress();
    }

    public void saveProgress(CircularProgressButton progressButton) {
        mProgress = progressButton.getProgress();
    }

    public boolean isEnabled() {
        return mIsEnabled;
    }

    public int getProgress() {
        return mProgress;
    }

    public void checkState(CircularProgressButton progressButton) {
        if (progressButton.getProgress() != getProgress()) {
            progressButton.setProgress(progressButton.getProgress());
        } else if(progressButton.isEnabled() != isEnabled()) {
            progressButton.setEnabled(progressButton.isEnabled());
        }
    }
}

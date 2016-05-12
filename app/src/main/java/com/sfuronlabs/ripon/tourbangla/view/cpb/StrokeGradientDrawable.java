package com.sfuronlabs.ripon.tourbangla.view.cpb;

import android.graphics.drawable.GradientDrawable;

/**
 * Created by Ripon on 10/2/15.
 */
public class StrokeGradientDrawable {
    private int mStrokeWidth;
    private int mStrokeColor;

    private GradientDrawable mGradientDrawable;

    public StrokeGradientDrawable(GradientDrawable drawable) {
        mGradientDrawable = drawable;
    }

    public int getStrokeWidth() {
        return mStrokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        mStrokeWidth = strokeWidth;
        mGradientDrawable.setStroke(strokeWidth, getStrokeColor());
    }

    public int getStrokeColor() {
        return mStrokeColor;
    }

    public void setStrokeColor(int strokeColor) {
        mStrokeColor = strokeColor;
        mGradientDrawable.setStroke(getStrokeWidth(), strokeColor);
    }

    public GradientDrawable getGradientDrawable() {
        return mGradientDrawable;
    }
}

package com.sfuronlabs.ripon.tourbangla;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Ripon on 9/23/15.
 */
public class LinedEditText extends EditText {
    Rect mRect;
    Paint mPaint;
    public LinedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        mRect = new Rect();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(Color.GRAY); //SET YOUR OWN COLOR HERE
    }

    private void init() {
        this.mPaint = new Paint();
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setColor(Color.GRAY);
        this.mPaint.setStrokeWidth(getLineHeight() / 10);
        this.mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    protected void onDraw(Canvas canvas) {
        float startX = getPaddingLeft();
        float stopX = getWidth() - getPaddingRight();
        float offsetY = getPaddingTop()
                - getPaint().getFontMetrics().top
                + mPaint.getStrokeWidth() / 2;
        for (int i = 0; i < getLineCount(); ++i) {
            float y = offsetY + getLineHeight() * i;
            canvas.drawLine(startX, y, stopX, y, mPaint);
        }

        super.onDraw(canvas);
    }
}

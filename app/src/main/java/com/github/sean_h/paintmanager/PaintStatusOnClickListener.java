package com.github.sean_h.paintmanager;

import android.view.View;
import android.view.ViewGroup;

public class PaintStatusOnClickListener implements View.OnClickListener {
    private Paint mPaint;
    private PaintStatus mStatus;

    public PaintStatusOnClickListener(Paint paint, PaintStatus status) {
        mPaint = paint;
        mStatus = status;
    }

    @Override
    public void onClick(View v) {
        // reset all of the status buttons
        ViewGroup parent = (ViewGroup)v.getParent();
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (child.getId() == R.id.have_button
                    || child.getId() == R.id.dont_have_button
                    || child.getId() == R.id.need_button) {
                child.setEnabled(true);
            }
        }

        mPaint.status = mStatus;
        mPaint.save();
        v.setEnabled(false);
    }
}

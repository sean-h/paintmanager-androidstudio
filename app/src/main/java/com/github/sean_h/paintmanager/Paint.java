package com.github.sean_h.paintmanager;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.Date;

class Paint extends SugarRecord<Paint> {
    long guid;
    String name;
    Range range;
    String colorCode;
    Date updatedAt;

    enum PaintStatus {
        have(0), dontHave(1), need(2), outOf(3);

        private int value;
        PaintStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }
    PaintStatus status;

    public Paint() {

    }

    public Paint(String name) {
        this.name = name;
    }

    public Paint(long guid, String name, Range range, PaintStatus status, String colorCode) {
        this.guid = guid;
        this.name = name;
        this.range = range;
        this.status = status;
        this.colorCode = colorCode;
        this.updatedAt = new Date();
    }

    public Bitmap getColorBitmap(int width, int height) {
        int color = Color.parseColor(colorCode);
        int[] colors = new int[width * height];
        for (int i = 0; i < width * height; i++) {
            colors[i] = color;
        }
        Bitmap bm = Bitmap.createBitmap(colors, width, height, Bitmap.Config.ARGB_8888);
        return bm;
    }

    public void setRange(int rangeId) {
        Range range = Select.from(Range.class).where(Condition.prop("guid").eq(rangeId)).first();
        this.range = range;
        this.updatedAt = new Date();
        this.save();
    }

    public void setStatus(PaintStatus status) {
        this.status = status;
        this.updatedAt = new Date();
        this.save();
    }

    public void setStatus(int status) {
        this.status = PaintStatus.values()[status];
        this.updatedAt = new Date();
        this.save();
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
        this.updatedAt = new Date();
        this.save();
    }

    public static PaintStatus getStatusIdFromName(int statusName)
            throws IllegalArgumentException {
        switch (statusName) {
            case R.string.have:
                return PaintStatus.have;
            case R.string.dont_have:
                return PaintStatus.dontHave;
            case R.string.need:
                return PaintStatus.need;
            default:
                throw new IllegalArgumentException("Status Name is not a valid status");
        }
    }

    public static PaintStatus getStatusIdFromName(String statusName, Activity activity)
            throws IllegalArgumentException {
        if (statusName == activity.getString(R.string.have)) {
            return PaintStatus.have;
        } else if (statusName == activity.getString(R.string.dont_have)) {
            return PaintStatus.dontHave;
        } else if (statusName == activity.getString(R.string.need)) {
            return PaintStatus.need;
        }

        throw new IllegalArgumentException("Status Name is not a valid status");
    }
}

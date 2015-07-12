package com.github.sean_h.paintmanager;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.orm.SugarRecord;

import java.util.Date;

class Paint extends SugarRecord<Paint> {
    long guid;
    String name;
    Range range;
    PaintStatus status;
    String colorCode;
    Date updatedAt;

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

    public void setStatus(PaintStatus status) {
        this.status = status;
        this.updatedAt = new Date();
        this.save();
    }
}

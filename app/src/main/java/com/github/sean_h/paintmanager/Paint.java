package com.github.sean_h.paintmanager;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.orm.SugarRecord;

class Paint extends SugarRecord<Paint> {
    String name;
    Range range;
    PaintStatus status;
    String colorCode;

    public Paint() {

    }

    public Paint(String name) {
        this.name = name;
    }

    public Paint(String name, Range range, PaintStatus status, String colorCode) {
        this.name = name;
        this.range = range;
        this.status = status;
        this.colorCode = colorCode;
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
}

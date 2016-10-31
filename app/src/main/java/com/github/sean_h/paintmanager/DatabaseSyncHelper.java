package com.github.sean_h.paintmanager;

import android.util.Log;

import com.orm.query.Condition;
import com.orm.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class DatabaseSyncHelper {
    public static final String TAG = "DatabaseSyncHelper";

    public void updateFromJSON(JSONObject jsonData) {
        try {
            JSONArray brandsArray = jsonData.getJSONArray("brand");
            loadBrands(brandsArray);
        } catch (JSONException ex) {
            Log.e(TAG, "Error parsing brand data from JSON");
        }

        try {
            JSONArray rangesArray = jsonData.getJSONArray("paint_range");
            loadRanges(rangesArray);
        } catch (JSONException ex) {
            Log.e(TAG, "Error parsing range data from JSON");
        }

        try {
            JSONArray paintsArray = jsonData.getJSONArray("paint");
            loadPaints(paintsArray);
        } catch (JSONException ex) {
            Log.e(TAG, "Error parsing paint data from JSON");
        }

        try {
            JSONArray barcodesArray = jsonData.getJSONArray("barcode");
            loadBarcodes(barcodesArray);
        } catch (JSONException ex) {
            Log.e(TAG, "Error parsing barcode data from JSON");
        }
    }

    private void loadBrands(JSONArray brandsArray) throws  JSONException {
        if (brandsArray == null) {
            return;
        }

        for (int i = 0; i < brandsArray.length(); i++) {
            JSONObject brand = brandsArray.getJSONObject(i);
            long brandId = brand.getInt("id");

            Brand b = Select.from(Brand.class)
                    .where(Condition.prop("guid").eq(brandId))
                    .first();
            String name = brand.getString("name");
            if (b == null) {
                new Brand(brandId, name).save();
            } else {
                b.setName(name);
            }
        }
    }

    private void loadRanges(JSONArray rangesArray) throws JSONException {
        if (rangesArray == null) {
            return;
        }

        for (int i = 0; i < rangesArray.length(); i++) {
            JSONObject range = rangesArray.getJSONObject(i);
            long rangeId = range.getInt("id");

            Range r = Select.from(Range.class)
                    .where(Condition.prop("guid").eq(rangeId))
                    .first();
            String name = range.getString("name");
            long brandId = range.getInt("brand_id");

            if (r == null) {
                new Range(rangeId, name, brandId).save();
            } else {
                r.setAll(name, brandId);
            }
        }
    }

    private void loadPaints(JSONArray paintsArray) throws JSONException {
        if (paintsArray == null) {
            return;
        }

        for (int i = 0; i < paintsArray.length(); i++) {
            JSONObject paint = paintsArray.getJSONObject(i);
            long paintId = paint.getInt("id");

            Paint p = Select.from(Paint.class)
                    .where(Condition.prop("guid").eq(paintId))
                    .first();
            String name = paint.getString("name");
            long rangeId = paint.getInt("range_id");
            int status = paint.getInt("status");
            String colorCode = paint.getString("color");

            if (p == null) {
                new Paint(paintId, name, rangeId, status, colorCode).save();
            } else {
                p.setAll(name, rangeId, status, colorCode);
            }
        }
    }

    private void loadBarcodes(JSONArray barcodesArray) throws  JSONException {
        if (barcodesArray == null) {
            return;
        }

        for (int i = 0; i < barcodesArray.length(); i++) {
            JSONObject barcode = barcodesArray.getJSONObject(i);
            long barcodeId = barcode.getInt("id");

            Barcode b = Select.from(Barcode.class)
                    .where(Condition.prop("guid").eq(barcodeId))
                    .first();
            int paintId = barcode.getInt("paint_id");
            String code = barcode.getString("barcode");

            if (b == null) {
                new Barcode(barcodeId, paintId, code).save();
            } else {
                b.setAll(barcodeId, paintId, code);
            }
        }
    }

    public static List<Paint> getUpdatedPaints() {
        SyncLog lastSync = Select.from(SyncLog.class)
                .orderBy("sync_time DESC")
                .first();

        long lastSuccessfulSyncTime;
        if (lastSync == null) {
            lastSuccessfulSyncTime = 0;
        } else {
            lastSuccessfulSyncTime = lastSync.getSyncTime();
        }

        return Select.from(Paint.class)
                .where("updated_at > " + Long.toString(lastSuccessfulSyncTime))
                .list();
    }
}

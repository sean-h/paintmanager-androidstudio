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
            if (b == null) {
                b = new Brand();
            }

            b.setName(brand.getString("name"));
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
            if (r == null) {
                r = new Range();
            }

            r.setName(range.getString("name"));
            r.setBrand(range.getInt("brand_id"));
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
            if (p == null) {
                p = new Paint();
            }
            p.name = paint.getString("name");
            p.setRange(paint.getInt("range_id"));
            p.setStatus(paint.getInt("status"));
            p.setColorCode(paint.getString("color"));
        }
    }

    public static List<Paint> getUpdatedPaints() {
        SyncLog lastSync = Select.from(SyncLog.class)
                .where(Condition.prop("was_successful").eq(true))
                .orderBy("sync_time DESC")
                .first();

        long lastSuccessfulSyncTime;
        if (lastSync == null) {
            lastSuccessfulSyncTime = 0;
        } else {
            lastSuccessfulSyncTime = lastSync.getSyncTime();
        }

        return Select.from(Paint.class)
                .where(Condition.prop("updated_at").gt(lastSuccessfulSyncTime))
                .list();
    }
}

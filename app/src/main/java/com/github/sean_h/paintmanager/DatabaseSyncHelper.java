package com.github.sean_h.paintmanager;

import com.orm.query.Condition;
import com.orm.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DatabaseSyncHelper {

    public void updateFromJSON(JSONObject jsonData) throws JSONException {
        JSONArray paintsArray = jsonData.getJSONArray("paint");
        if (paintsArray != null) {
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
    }
}

package com.github.sean_h.paintmanager;

import android.os.AsyncTask;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

class DatabaseSyncTask extends AsyncTask<Void, Void, Void> {
    @Override
    protected Void doInBackground(Void... params) {
        LoadDatabaseFixtures();
        return null;
    }

    private void SyncFromRemoteDatabase() {
        Paint.deleteAll(Paint.class);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://192.168.1.166:4567/paints.json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                List<Paint> paints = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        String paintName = response.getJSONObject(i).getString("name");
                        paints.add(new Paint(paintName));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // Only take the first 10 elements
                    if (i == 10) {
                        break;
                    }
                }

                Paint.saveInTx(paints);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void LoadDatabaseFixtures() {
        Paint.PaintStatus dontHave = Paint.PaintStatus.dontHave;
        Paint.PaintStatus outOf = Paint.PaintStatus.outOf;
        Paint.PaintStatus need = Paint.PaintStatus.need;
        Paint.PaintStatus have = Paint.PaintStatus.have;

        Brand.deleteAll(Brand.class);
        Brand basicColors = new Brand(1, "Basic Colors");
        basicColors.save();

        Range.deleteAll(Range.class);
        Range primaryColors = new Range(1, "Primary Colors", basicColors);
        primaryColors.save();
        Range secondaryColors = new Range(2, "Secondary Colors", basicColors);
        secondaryColors.save();

        Paint.deleteAll(Paint.class);
        new Paint(1, "Red", primaryColors, have, "#ff0000").save();
        new Paint(2, "Blue", primaryColors, dontHave, "#0000ff").save();
        new Paint(3, "Yellow", primaryColors, dontHave, "#ffff00").save();
        new Paint(4, "Green", secondaryColors, need, "#00ff00").save();
        new Paint(5, "White", secondaryColors, dontHave, "#ffffff").save();
        new Paint(6, "Black", secondaryColors, dontHave, "#000000").save();
    }
}

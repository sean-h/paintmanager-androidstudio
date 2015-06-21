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
        PaintStatus.deleteAll(PaintStatus.class);
        PaintStatus dontHave = new PaintStatus("Don't Have");
        dontHave.save();
        PaintStatus outOf = new PaintStatus("Out Of");
        outOf.save();
        PaintStatus need = new PaintStatus("Need");
        need.save();
        PaintStatus have = new PaintStatus("Have");
        have.save();

        Brand.deleteAll(Brand.class);
        Brand basicColors = new Brand("Basic Colors");
        basicColors.save();

        Range.deleteAll(Range.class);
        Range primaryColors = new Range("Primary Colors", basicColors);
        primaryColors.save();
        Range secondaryColors = new Range("Secondary Colors", basicColors);
        secondaryColors.save();

        Paint.deleteAll(Paint.class);
        new Paint("Red", primaryColors, have, "#ff0000").save();
        new Paint("Blue", primaryColors, dontHave, "#0000ff").save();
        new Paint("Yellow", primaryColors, dontHave, "#ffff00").save();
        new Paint("Green", secondaryColors, need, "#00ff00").save();
        new Paint("White", secondaryColors, dontHave, "#ffffff").save();
        new Paint("Black", secondaryColors, dontHave, "#000000").save();
    }
}

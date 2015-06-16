package com.github.sean_h.paintmanager;

import android.os.AsyncTask;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class DatabaseSyncTask extends AsyncTask<Void, Void, Void> {
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
        Paint.deleteAll(Paint.class);

        String[] paintNames = new String[] {"Red", "Green", "Blue", "Yellow", "White", "Black"};
        ArrayList<Paint> paints = new ArrayList<>();
        for (String name : paintNames) {
            Paint p = new Paint();
            p.name = name;
            paints.add(p);
        }
        Paint.saveInTx(paints);
    }
}

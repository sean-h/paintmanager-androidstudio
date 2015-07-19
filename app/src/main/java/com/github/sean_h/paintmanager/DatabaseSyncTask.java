package com.github.sean_h.paintmanager;

import android.os.AsyncTask;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

class DatabaseSyncTask extends AsyncTask<String, Void, Void> {
    @Override
    protected Void doInBackground(String... syncUrls) {
        for (int i = 0; i < syncUrls.length; i++) {
            String url = syncUrls[i];
            if (url.contains("http://")) {
                SyncFromRemoteDatabase(url);
            } else {
                DatabaseFixtureHelper.loadFixtures();
            }
        }
        return null;
    }

    private void SyncFromRemoteDatabase(String syncUrl) {
        Paint.deleteAll(Paint.class);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(syncUrl, new JsonHttpResponseHandler() {
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

}

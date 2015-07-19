package com.github.sean_h.paintmanager;

import android.os.AsyncTask;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

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

        String baseSyncUrl = syncUrl.split("auth=")[0];
        String auth_token = syncUrl.split("auth=")[1];

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("AUTHORIZATION", auth_token);
        client.get(baseSyncUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                DatabaseSyncHelper dbSyncHelper = new DatabaseSyncHelper();
                dbSyncHelper.updateFromJSON(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

}

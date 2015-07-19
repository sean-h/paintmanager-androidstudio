package com.github.sean_h.paintmanager;

import android.os.AsyncTask;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class DatabaseSyncTask extends AsyncTask<String, Void, Void> {
    private List<OnTaskCompleted> mTaskCompleteListeners;

    public DatabaseSyncTask() {
        mTaskCompleteListeners = new ArrayList<>();
    }

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

    @Override
    protected void onPostExecute(Void aVoid) {
        for (OnTaskCompleted listener : mTaskCompleteListeners) {
            listener.onTaskCompleted();
        }
    }

    public void addTaskCompleteListener(OnTaskCompleted listener) {
        mTaskCompleteListeners.add(listener);
    }

    private void SyncFromRemoteDatabase(String syncUrl) {
        Paint.deleteAll(Paint.class);

        String baseSyncUrl = syncUrl.split("auth=")[0];
        String auth_token = syncUrl.split("auth=")[1];

        SyncHttpClient client = new SyncHttpClient();
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

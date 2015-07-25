package com.github.sean_h.paintmanager;

import android.os.AsyncTask;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.orm.query.Select;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
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
        new SyncLog().save();
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
        String baseSyncUrl = syncUrl.split("auth=")[0];
        String auth_token = syncUrl.split("auth=")[1];

        SyncHttpClient client = new SyncHttpClient();
        client.addHeader("AUTHORIZATION", auth_token);

        if (SyncLog.count(SyncLog.class, null, null) == 0) {
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
        } else {
            JSONObject postData = new JSONObject();

            List<Paint> updatedPaints = DatabaseSyncHelper.getUpdatedPaints();
            JSONArray updatedPaintsJSON = new JSONArray();
            for (Paint p : updatedPaints) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", p.guid);
                    jsonObject.put("status", p.status.getValue());
                    updatedPaintsJSON.put(jsonObject);
                } catch (JSONException ex) {
                    Log.e("DatabaseSyncTask", ex.toString());
                }
            }

            try {
                postData.put("paints", updatedPaintsJSON);

                long lastSyncTime = Select.from(SyncLog.class)
                        .orderBy("sync_time DESC")
                        .first().syncTime;
                postData.put("sync_time", lastSyncTime);
            } catch (JSONException ex) {
                Log.e("DatabaseSyncTask", ex.toString());
            }

            RequestParams params = new RequestParams();
            params.put("JSON", postData);

            client.post(baseSyncUrl, params, new JsonHttpResponseHandler() {
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
}

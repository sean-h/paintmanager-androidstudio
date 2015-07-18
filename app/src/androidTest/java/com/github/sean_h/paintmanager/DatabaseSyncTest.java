package com.github.sean_h.paintmanager;

import android.test.InstrumentationTestCase;

import com.orm.query.Condition;
import com.orm.query.Select;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class DatabaseSyncTest extends InstrumentationTestCase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        DatabaseSyncTask dbSyncTask = new DatabaseSyncTask();
        dbSyncTask.doInBackground();
    }

    public void testUpdatedPaintList() {
        Paint redPaint = Select.from(Paint.class).where(Condition.prop("name").eq("Red")).first();
        redPaint.setStatus(Paint.PaintStatus.need);
        List<Paint> updatedPaints = DatabaseSyncTask.getUpdatedPaints();
        assertTrue(updatedPaints.contains(redPaint));
    }

    public void testLoadSyncData() throws IOException, JSONException {
        InputStream open = getInstrumentation().getContext()
                         .getResources().getAssets().open("sync_data.json");

        BufferedReader br = new BufferedReader(new InputStreamReader(open));

        StringBuilder inputStringBuilder = new StringBuilder();

        String inputString;
        while ((inputString = br.readLine()) != null)
            inputStringBuilder.append(inputString);

        JSONObject jsonObject = new JSONObject(inputStringBuilder.toString());
        open.close();

        Brand.deleteAll(Brand.class);
        Range.deleteAll(Range.class);
        Paint.deleteAll(Paint.class);

        DatabaseSyncHelper dbHelper = new DatabaseSyncHelper();
        dbHelper.updateFromJSON(jsonObject);

        assertTrue(Select.from(Brand.class).count() > 0);
        assertTrue(Select.from(Range.class).count() > 0);
        assertTrue(Select.from(Paint.class).count() > 0);
    }
}

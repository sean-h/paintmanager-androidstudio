package com.github.sean_h.paintmanager;

import android.test.AndroidTestCase;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

public class DatabaseSyncTest extends AndroidTestCase {
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
        assert(updatedPaints.contains(redPaint));
    }

    
}

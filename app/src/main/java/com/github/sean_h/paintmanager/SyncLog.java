package com.github.sean_h.paintmanager;

import com.orm.SugarRecord;

import java.util.Date;

public class SyncLog extends SugarRecord<SyncLog> {
    private Date syncTime;
    private boolean wasSuccessful;

    public SyncLog() { }

    public SyncLog(Date syncTime, boolean wasSuccessful) {
        this.syncTime = syncTime;
        this.wasSuccessful = wasSuccessful;
    }
}

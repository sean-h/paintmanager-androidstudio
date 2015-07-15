package com.github.sean_h.paintmanager;

import com.orm.SugarRecord;

import java.util.Date;

class SyncLog extends SugarRecord<SyncLog> {
    Date syncTime;
    boolean wasSuccessful;

    public SyncLog() { }

    public SyncLog(Date syncTime, boolean wasSuccessful) {
        this.syncTime = syncTime;
        this.wasSuccessful = wasSuccessful;
    }

    public Date getSyncTime() {
        return this.syncTime;
    }
}

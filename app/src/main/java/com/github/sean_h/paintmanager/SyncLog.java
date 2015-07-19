package com.github.sean_h.paintmanager;

import com.orm.SugarRecord;

class SyncLog extends SugarRecord<SyncLog> {
    long syncTime;
    boolean wasSuccessful;

    public SyncLog() { }

    public SyncLog(long syncTime, boolean wasSuccessful) {
        this.syncTime = syncTime;
        this.wasSuccessful = wasSuccessful;
    }

    public long getSyncTime() {
        return this.syncTime;
    }
}

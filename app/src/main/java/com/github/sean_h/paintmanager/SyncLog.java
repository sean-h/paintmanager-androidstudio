package com.github.sean_h.paintmanager;

import com.orm.SugarRecord;

class SyncLog extends SugarRecord<SyncLog> {
    long syncTime;

    public SyncLog() {
        this.syncTime = System.currentTimeMillis();
    }

    public SyncLog(long syncTime) {
        this.syncTime = syncTime;
    }

    public long getSyncTime() {
        return this.syncTime;
    }
}

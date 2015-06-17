package com.github.sean_h.paintmanager;

import com.orm.SugarRecord;

public class PaintStatus extends SugarRecord<PaintStatus> {
    String name;

    public PaintStatus() { }

    public PaintStatus(String name) {
        this.name = name;
    }
}

package com.github.sean_h.paintmanager;

import com.orm.SugarRecord;

class Paint extends SugarRecord<Paint> {
    String name;
    Range range;
    PaintStatus status;

    public Paint() {

    }

    public Paint(String name) {
        this.name = name;
    }

    public Paint(String name, Range range, PaintStatus status) {
        this.name = name;
        this.range = range;
        this.status = status;
    }
}

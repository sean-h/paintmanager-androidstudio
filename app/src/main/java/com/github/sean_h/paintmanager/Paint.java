package com.github.sean_h.paintmanager;

import com.orm.SugarRecord;

public class Paint extends SugarRecord<Paint> {
    String name;

    public Paint() {

    }

    public Paint(String name) {
        this.name = name;
    }
}

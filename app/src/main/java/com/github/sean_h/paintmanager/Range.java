package com.github.sean_h.paintmanager;

import com.orm.SugarRecord;

class Range extends SugarRecord<Range> {
    long guid;
    String name;
    Brand brand;

    public Range() {}

    public Range(long guid, String name, Brand brand) {
        this.guid = guid;
        this.name = name;
        this.brand = brand;
    }
}

package com.github.sean_h.paintmanager;

import com.orm.SugarRecord;

public class Range extends SugarRecord<Range> {
    String name;
    Brand brand;

    public Range() {}

    public Range(String name, Brand brand) {
        this.name = name;
        this.brand = brand;
    }
}

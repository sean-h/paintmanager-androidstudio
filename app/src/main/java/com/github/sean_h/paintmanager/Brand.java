package com.github.sean_h.paintmanager;

import com.orm.SugarRecord;

class Brand extends SugarRecord<Brand> {
    long guid;
    String name;

    public Brand() {

    }

    public Brand(long guid, String name) {
        this.guid = guid;
        this.name = name;
    }
}

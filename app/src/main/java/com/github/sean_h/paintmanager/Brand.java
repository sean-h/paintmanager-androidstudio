package com.github.sean_h.paintmanager;

import com.orm.SugarRecord;

public class Brand extends SugarRecord<Brand> {
    String name;

    public Brand() {

    }

    public Brand(String name) {
        this.name = name;
    }
}

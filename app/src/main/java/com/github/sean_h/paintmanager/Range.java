package com.github.sean_h.paintmanager;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

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

    public void setName(String name) {
        this.name = name;
        this.save();
    }

    public void setBrand(long brandId) {
        this.brand = Select.from(Brand.class)
                .where(Condition.prop("guid").eq(brandId))
                .first();
        this.save();
    }

    public void setGuid(long guid) {
        this.guid = guid;
    }

    public long getGuid() {
        return guid;
    }
}

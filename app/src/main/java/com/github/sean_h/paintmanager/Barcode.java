package com.github.sean_h.paintmanager;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

public class Barcode extends SugarRecord<Barcode> {
    long guid;
    Paint paint;
    String barcode;
    long updatedAt;

    public Barcode() {

    }

    public Barcode(long guid, long paintId, String barcode) {
        this.guid = guid;
        this.paint = Select.from(Paint.class)
                .where(Condition.prop("guid").eq(paintId))
                .first();
        this.barcode = barcode;
        this.updatedAt = System.currentTimeMillis();
    }

    public void setAll(long guid, long paintId, String barcode) {
        this.guid = guid;
        this.paint = Select.from(Paint.class)
                .where(Condition.prop("guid").eq(paintId))
                .first();
        this.barcode = barcode;
        this.updatedAt = System.currentTimeMillis();
        this.save();
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() == this.getClass()) {
            Barcode otherBarcode = (Barcode)o;
            return this.getId().equals(otherBarcode.getId());
        }
        return super.equals(o);
    }
}

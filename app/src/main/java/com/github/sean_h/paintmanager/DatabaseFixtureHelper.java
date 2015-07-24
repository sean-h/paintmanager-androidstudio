package com.github.sean_h.paintmanager;

public class DatabaseFixtureHelper {

    public static void loadFixtures() {
        Paint.PaintStatus dontHave = Paint.PaintStatus.dontHave;
        Paint.PaintStatus outOf = Paint.PaintStatus.outOf;
        Paint.PaintStatus need = Paint.PaintStatus.need;
        Paint.PaintStatus have = Paint.PaintStatus.have;

        Brand.deleteAll(Brand.class);
        Brand basicColors = new Brand(1, "Basic Colors");
        basicColors.save();

        Range.deleteAll(Range.class);
        Range primaryColors = new Range(1, "Primary Colors", basicColors);
        primaryColors.save();
        Range secondaryColors = new Range(2, "Secondary Colors", basicColors);
        secondaryColors.save();

        Paint.deleteAll(Paint.class);
        new Paint(1, "Red", primaryColors, have, "#ff0000").save();
        new Paint(2, "Blue", primaryColors, dontHave, "#0000ff").save();
        new Paint(3, "Yellow", primaryColors, dontHave, "#ffff00").save();
        new Paint(4, "Green", secondaryColors, need, "#00ff00").save();
        new Paint(5, "White", secondaryColors, dontHave, "#ffffff").save();
        new Paint(6, "Black", secondaryColors, dontHave, "#000000").save();

        SyncLog.deleteAll(SyncLog.class);
        new SyncLog().save();
    }
}

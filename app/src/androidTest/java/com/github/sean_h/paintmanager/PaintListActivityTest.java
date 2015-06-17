package com.github.sean_h.paintmanager;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Spinner;

public class PaintListActivityTest extends ActivityInstrumentationTestCase2<PaintListActivity> {
    private PaintListActivity mActivity;

    public PaintListActivityTest() {
        super(PaintListActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mActivity = getActivity();
    }

    public void testBrandSpinner() {
        Spinner brandsSpinner = (Spinner) mActivity.findViewById(R.id.brands_spinner);
        assertTrue(brandsSpinner.getCount() > 0);
        String firstItem = (String)brandsSpinner.getItemAtPosition(0);
        assertTrue(mActivity.getString(R.string.brand).equals(firstItem));
    }
}

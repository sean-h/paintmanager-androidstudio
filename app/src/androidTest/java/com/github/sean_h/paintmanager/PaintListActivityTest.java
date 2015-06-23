package com.github.sean_h.paintmanager;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class PaintListActivityTest extends ActivityInstrumentationTestCase2<PaintListActivity> {
    private PaintListActivity mActivity;
    private Spinner mRangesSpinner;
    private ListView mPaintListView;

    public PaintListActivityTest() {
        super(PaintListActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mActivity = getActivity();
        mRangesSpinner = (Spinner)mActivity.findViewById(R.id.ranges_spinner);
        mPaintListView = (ListView)mActivity.findViewById(R.id.paint_list);
    }

    public void testBrandSpinner() {
        Spinner brandsSpinner = (Spinner) mActivity.findViewById(R.id.brands_spinner);
        assertTrue(brandsSpinner.getCount() > 0);
        String firstItem = (String)brandsSpinner.getItemAtPosition(0);
        assertTrue(mActivity.getString(R.string.brand_spinner_default).equals(firstItem));
    }

    public void testRangesSpinner() {
        assertTrue(mRangesSpinner.getCount() > 0);
        String firstItem = (String)mRangesSpinner.getItemAtPosition(0);
        assertTrue(mActivity.getString(R.string.range_spinner_default).equals(firstItem));
    }

    public void testPaintStatusSpinner() {
        Spinner statusSpinner = (Spinner) mActivity.findViewById(R.id.statuses_spinner);
        assertTrue(statusSpinner.getCount() > 0);

        String firstItem = (String)statusSpinner.getItemAtPosition(0);
        assertTrue(mActivity.getString(R.string.status_spinner_default).equals(firstItem));

        List<String> statuses = new ArrayList<>();
        for (int i = 0; i < statusSpinner.getCount(); i++) {
            statuses.add((String)statusSpinner.getItemAtPosition(i));
        }
        assertTrue(statuses.contains("Have"));
    }
}

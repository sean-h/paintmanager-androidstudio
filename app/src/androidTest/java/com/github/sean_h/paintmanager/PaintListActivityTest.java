package com.github.sean_h.paintmanager;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.action.ViewActions;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;

public class PaintListActivityTest extends ActivityInstrumentationTestCase2<PaintListActivity> {
    private PaintListActivity mActivity;

    public PaintListActivityTest() {
        super(PaintListActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        DatabaseFixtureHelper.loadFixtures();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mActivity = getActivity();
    }
}
package com.github.sean_h.paintmanager;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;


public class PaintListActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint_list);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        DatabaseSyncTask dbSyncTask = new DatabaseSyncTask();
        dbSyncTask.doInBackground();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (position) {
            case 0:
                fragmentManager.beginTransaction()
                    .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                    .commit();
                break;
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = "Paint List";
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.paint_list, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        private ListView mPaintList;
        private List<String> mPaintNames;

        private Spinner mBrandSpinner;
        private List<String> mBrandNames;
        private Spinner mRangeSpinner;
        private List<String> mRangeNames;
        private Spinner mStatusSpinner;
        private List<String> mStatusNames;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
            mPaintNames = new ArrayList<>();
            mBrandNames = new ArrayList<>();
            mRangeNames = new ArrayList<>();
            mStatusNames = new ArrayList<>();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_paint_list, container, false);

            mPaintList = (ListView) rootView.findViewById(R.id.paint_list);
            mPaintList.setAdapter(new ArrayAdapter<>(
                    getActivity(),
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1,
                    mPaintNames
            ));

            mBrandSpinner = (Spinner) rootView.findViewById(R.id.brands_spinner);
            mBrandSpinner.setAdapter(new ArrayAdapter<>(
                    getActivity(),
                    android.R.layout.simple_spinner_item,
                    android.R.id.text1,
                    mBrandNames
            ));

            mRangeSpinner = (Spinner) rootView.findViewById(R.id.ranges_spinner);
            mRangeSpinner.setAdapter(new ArrayAdapter<>(
                    getActivity(),
                    android.R.layout.simple_spinner_item,
                    android.R.id.text1,
                    mRangeNames
            ));

            mStatusSpinner = (Spinner) rootView.findViewById(R.id.statuses_spinner);
            mStatusSpinner.setAdapter(new ArrayAdapter<>(
                    getActivity(),
                    android.R.layout.simple_spinner_item,
                    android.R.id.text1,
                    mStatusNames
            ));

            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((PaintListActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));

            mPaintNames.clear();
            List<Paint> paints = Select.from(Paint.class).orderBy("name").list();
            for (Paint p : paints) {
                mPaintNames.add(p.name);
            }

            mBrandNames.clear();
            List<Brand> brands = Select.from(Brand.class).orderBy("name").list();
            mBrandNames.add(getString(R.string.brand));
            for (Brand b : brands) {
                mBrandNames.add(b.name);
            }

            mRangeNames.clear();
            List<Range> ranges = Select.from(Range.class).orderBy("name").list();
            mRangeNames.add(getString(R.string.range));
            for (Range r : ranges) {
                mRangeNames.add(r.name);
            }

            mStatusNames.clear();
            List<PaintStatus> statuses = Select.from(PaintStatus.class).orderBy("name").list();
            mStatusNames.add(getString(R.string.status));
            for (PaintStatus s : statuses) {
                mStatusNames.add(s.name);
            }
        }
    }

}

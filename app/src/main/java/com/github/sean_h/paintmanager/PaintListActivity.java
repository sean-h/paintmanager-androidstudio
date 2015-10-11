package com.github.sean_h.paintmanager;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class PaintListActivity extends ActionBarActivity implements OnTaskCompleted {
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private PaintListFragment mPaintListFragment;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint_list);

        mTitle = getTitle();

        mPaintListFragment = PaintListFragment.newInstance(1);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, mPaintListFragment)
                .commit();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Syncing Data");
        progressDialog.setMessage("Please wait...");
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.paint_list_title);
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
        getMenuInflater().inflate(R.menu.paint_list, menu);
        restoreActionBar();
        return true;
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

        if (item.getItemId() == R.id.sync_button) {
            databaseSync();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void databaseSync() {
        progressDialog.show();

        String auth_token = this.getSharedPreferences("com.github.sean_h.paintmanager", MODE_PRIVATE)
                .getString("auth_token", "");
        String syncUrl = getString(R.string.api_url)
                + getString(R.string.sync_json_url)
                + "auth="
                + auth_token;
        DatabaseSyncTask syncTask = new DatabaseSyncTask();
        syncTask.addTaskCompleteListener(mPaintListFragment);
        syncTask.addTaskCompleteListener(this);
        syncTask.execute(syncUrl);
    }

    @Override
    public void onTaskCompleted() {
        progressDialog.dismiss();
    }
}

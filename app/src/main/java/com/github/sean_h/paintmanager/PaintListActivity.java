package com.github.sean_h.paintmanager;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.sean_h.paintmanager.PaintDetailsDialogFragment.PaintDetailsDialogListener;
import com.orm.query.Select;

import java.util.List;

public class PaintListActivity extends AppCompatActivity implements OnTaskCompleted, PaintFilterDialogFragment.PaintFilterDialogListener, PaintDetailsDialogListener {
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

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.paint_list_title);

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

        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.filter_button) {
            DialogFragment filterDialog = new PaintFilterDialogFragment();
            filterDialog.show(getFragmentManager(), "Filter Paints");
            return true;
        }
        else if (id == R.id.sync_button) {
            databaseSync();
            return true;
        } else if (id == R.id.barcode_button) {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.setPackage("com.google.zxing.client.android");
            intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
            startActivityForResult(intent, 0);
            return true;
        } else if (id == R.id.logout) {
            SharedPreferences prefs = this.getSharedPreferences("com.github.sean_h.paintmanager", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove("auth_token");
            editor.commit();
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
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

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        if (dialog instanceof PaintFilterDialogFragment) {
            PaintFilterDialogFragment paintFilterDialog = (PaintFilterDialogFragment) dialog;
            mPaintListFragment.setPaintQuery(paintFilterDialog.getFilterQuery());
        }
        else if (dialog instanceof PaintDetailsDialogFragment) {
            PaintDetailsDialogFragment paintDetailsDialogFragment = (PaintDetailsDialogFragment) dialog;
            mPaintListFragment.refresh();
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                String format = data.getStringExtra("SCAN_RESULT_FORMAT");

                List<Barcode> barcodes = Barcode.find(Barcode.class, "barcode = ?", contents);
                if (barcodes.size() == 0) {
                    Toast.makeText(this, "Unknown Paint", Toast.LENGTH_LONG).show();
                } else {
                    Paint p = barcodes.get(0).paint;
                    //Toast.makeText(this, p.name, Toast.LENGTH_LONG).show();

                    DialogFragment paintDetailsDialog = PaintDetailsDialogFragment.newInstance(p);
                    paintDetailsDialog.show(getFragmentManager(), "Paint Details");
                }
            }
        }
    }
}

package com.github.sean_h.paintmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;

public class PaintFilterDialogFragment extends DialogFragment {
    /* The activity that creates an instance of this dialog fragment must
        * implement this interface in order to receive event callbacks.
        * Each method passes the DialogFragment in case the host needs to query it. */
    public interface PaintFilterDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    PaintFilterDialogListener mListener;

    protected static final String PREFS_NAME = "PaintFilter";

    Spinner mBrandsSpinner;
    Spinner mRangesSpinner;
    Spinner mStatusesSpinner;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (PaintFilterDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_filter_paints, null);
        builder.setView(dialogView);

        mBrandsSpinner = (Spinner) dialogView.findViewById(R.id.filter_brands_spinner);
        mRangesSpinner = (Spinner) dialogView.findViewById(R.id.filter_ranges_spinner);
        mStatusesSpinner = (Spinner) dialogView.findViewById(R.id.filter_statuses_spinner);
        populateSpinners();
        setSpinnerSelections();

        Button acceptButton = (Button) dialogView.findViewById(R.id.filter_accept);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onDialogPositiveClick(PaintFilterDialogFragment.this);
                getDialog().dismiss();
            }
        });

        Button cancelButton = (Button) dialogView.findViewById(R.id.filter_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().cancel();
            }
        });

        // Create the AlertDialog object and return it
        return builder.create();
    }

    protected  void populateSpinners() {
        List<Brand> brands = Select.from(Brand.class).orderBy("name").list();
        loadSpinner(brands, mBrandsSpinner, getString(R.string.brand_spinner_default));

        List<Range> ranges = Select.from(Range.class).orderBy("name").list();
        loadSpinner(ranges, mRangesSpinner, getString(R.string.range_spinner_default));

        List<String> statuses = new ArrayList<>();
        statuses.add(getString(R.string.status_spinner_default));
        statuses.add(getString(R.string.dont_have));
        statuses.add(getString(R.string.have));
        statuses.add(getString(R.string.need));
        mStatusesSpinner.setAdapter(new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                statuses
        ));
    }

    protected void setSpinnerSelections() {
        SharedPreferences preferences = getActivity().getSharedPreferences(PREFS_NAME, 0);
        String selectedBrand = preferences.getString("SelectedBrand", null);
        String selectedRange = preferences.getString("SelectedRange", null);
        String selectedStatus = preferences.getString("SelectedStatus", null);

        if (selectedBrand != null) {
            for (int i = 0; i < mBrandsSpinner.getCount(); i++) {
                if (selectedBrand.equals(mBrandsSpinner.getItemAtPosition(i))) {
                    mBrandsSpinner.setSelection(i);
                }
            }
        }

        if (selectedRange != null) {
            for (int i = 0; i < mRangesSpinner.getCount(); i++) {
                if (selectedRange.equals(mRangesSpinner.getItemAtPosition(i))) {
                    mRangesSpinner.setSelection(i);
                }
            }
        }

        if (selectedStatus != null) {
            for (int i = 0; i < mStatusesSpinner.getCount(); i++) {
                String s = (String)(mStatusesSpinner.getItemAtPosition(i));
                s = s.toLowerCase();
                if (selectedStatus.toLowerCase().equals(s)) {
                    mStatusesSpinner.setSelection(i);
                }
            }
        }
    }

    protected <T extends SugarRecord> void loadSpinner(List<T> records, Spinner spinner, String defaultText)
    {
        List<String> names = new ArrayList<>();
        names.add(defaultText);
        for (T record : records) {
            names.add(record.toString());
        }
        spinner.setAdapter(new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                names
        ));
    }

    public Select<Paint> getFilterQuery() {
        String selectedBrand = (String)mBrandsSpinner.getSelectedItem();
        String selectedRange = (String)mRangesSpinner.getSelectedItem();
        String selectedStatus = (String)mStatusesSpinner.getSelectedItem();

        Brand brand = Select.from(Brand.class).where(Condition.prop("name").eq(selectedBrand)).first();
        Range range = Select.from(Range.class).where(Condition.prop("name").eq(selectedRange)).first();
        Paint.PaintStatus status = Paint.getStatusIdFromName(selectedStatus, getActivity());

        SharedPreferences preferences = getActivity().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("SelectedBrand", (brand != null) ? brand.name : null);
        editor.putString("SelectedRange", (range != null) ? range.name : null);
        editor.putString("SelectedStatus", (status != null) ? status.name() : null);
        editor.commit();

        Select<Paint> query = Select.from(Paint.class);
        if (range != null) {
            query = query.where(Condition.prop("range").eq(range.getGuid()));
        }
        if (status != null) {
            query = query.where(Condition.prop("status").eq(status));
        }

        return query;
    }
}

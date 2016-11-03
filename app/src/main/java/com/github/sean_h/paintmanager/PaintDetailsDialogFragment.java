package com.github.sean_h.paintmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

public class PaintDetailsDialogFragment extends DialogFragment {

    public interface PaintDetailsDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    PaintDetailsDialogFragment.PaintDetailsDialogListener mListener;

    protected Paint selectedPaint;
    protected Paint.PaintStatus selectedPaintStatus;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (PaintDetailsDialogFragment.PaintDetailsDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    public static PaintDetailsDialogFragment newInstance(Paint paint) {
        PaintDetailsDialogFragment dialogFragment = new PaintDetailsDialogFragment();
        Bundle args = new Bundle();
        args.putLong("paint_id", paint.getId());
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paint paint = Paint.findById(Paint.class, getArguments().getLong("paint_id"));
        selectedPaint = paint;
        selectedPaintStatus = paint.status;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_paint_details, null);
        builder.setView(dialogView);

        TextView brandText = (TextView)dialogView.findViewById(R.id.detail_brand_name);
        brandText.setText(paint.range.brand.name);

        TextView rangeText = (TextView)dialogView.findViewById(R.id.detail_range_name);
        rangeText.setText(paint.range.name);

        TextView paintText = (TextView)dialogView.findViewById(R.id.detail_paint_name);
        paintText.setText(paint.name);

        ImageView paintColor = (ImageView)dialogView.findViewById(R.id.detail_paint_color);
        paintColor.setImageBitmap(paint.getColorBitmap(50, 50));

        Button acceptButton = (Button) dialogView.findViewById(R.id.paint_details_accept);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPaint.setStatus(selectedPaintStatus);
                mListener.onDialogPositiveClick(PaintDetailsDialogFragment.this);
                getDialog().dismiss();
            }
        });

        RadioGroup paintStatusGroup = (RadioGroup)dialogView.findViewById(R.id.paint_status_radio_group);
        paintStatusGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                if (id == R.id.dont_have_radio_button) {
                    selectedPaintStatus = Paint.PaintStatus.dontHave;
                } else if (id == R.id.need_radio_button) {
                    selectedPaintStatus = Paint.PaintStatus.need;
                } else if (id == R.id.have_radio_button) {
                    selectedPaintStatus = Paint.PaintStatus.have;
                }
            }
        });

        switch (paint.status) {
            case dontHave:
                paintStatusGroup.check(R.id.dont_have_radio_button);
                break;
            case need:
                paintStatusGroup.check(R.id.need_radio_button);
                break;
            case have:
                paintStatusGroup.check(R.id.have_radio_button);
                break;
        }

        Button cancelButton = (Button) dialogView.findViewById(R.id.paint_details_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().cancel();
            }
        });

        return builder.create();
    }
}

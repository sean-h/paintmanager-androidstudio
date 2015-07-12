package com.github.sean_h.paintmanager;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

public class PaintListAdapter extends ArrayAdapter<Paint> {
    private Activity mActivity;

    public PaintListAdapter(Activity activity, List<Paint> paints) {
        super(activity, 0, paints);
        mActivity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mActivity.getLayoutInflater().inflate(R.layout.list_item_paint, null);
        }

        Paint paint = getItem(position);

        TextView paintName = (TextView)convertView.findViewById(R.id.paint_name);
        paintName.setText(paint.name);

        ImageView paintColor = (ImageView)convertView.findViewById(R.id.paint_color);
        paintColor.setImageBitmap(paint.getColorBitmap(50, 50));

        Button haveButton = (Button)convertView.findViewById(R.id.have_button);
        setupButton(haveButton, paint, Paint.PaintStatus.have);

        Button dontHaveButton = (Button)convertView.findViewById(R.id.dont_have_button);
        setupButton(dontHaveButton, paint, Paint.PaintStatus.dontHave);

        Button needButton = (Button)convertView.findViewById(R.id.need_button);
        setupButton(needButton, paint, Paint.PaintStatus.need);

        return convertView;
    }

    private void setupButton(Button button, Paint paint, Paint.PaintStatus paintStatus) {
        button.setOnClickListener(new PaintStatusOnClickListener(paint, paintStatus));
        if (paint.status == paintStatus) {
            button.setEnabled(false);
        } else {
            button.setEnabled(true);
        }
    }
}

package com.github.sean_h.paintmanager;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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

        return convertView;
    }
}

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
        PaintStatus haveStatus = Select.from(PaintStatus.class)
                .where(Condition.prop("name").eq("Have"))
                .first();
        setupButton(haveButton, paint, haveStatus);

        Button dontHaveButton = (Button)convertView.findViewById(R.id.dont_have_button);
        PaintStatus dontHaveStatus = Select.from(PaintStatus.class)
                .where(Condition.prop("name").eq("Don't Have"))
                .first();
        setupButton(dontHaveButton, paint, dontHaveStatus);

        Button needButton = (Button)convertView.findViewById(R.id.need_button);
        PaintStatus needStatus = Select.from(PaintStatus.class)
                .where(Condition.prop("name").eq("Need"))
                .first();
        setupButton(needButton, paint, needStatus);

        return convertView;
    }

    private void setupButton(Button button, Paint paint, PaintStatus paintStatus) {
        button.setOnClickListener(new PaintStatusOnClickListener(paint, paintStatus));
        if (paint.status.getId().longValue() == paintStatus.getId().longValue()) {
            button.setEnabled(false);
        } else {
            button.setEnabled(true);
        }
    }
}

package com.riggle.plug.utils.chart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.riggle.plug.R;

@SuppressLint("ViewConstructor")
public class CustomMarkerView extends MarkerView {

    private TextView tvContent;

    public CustomMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        tvContent = (TextView) findViewById(R.id.tvContent);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        if (e.getY() > 0) {
            tvContent.setText(String.valueOf(e.getY()));
        } else {
            tvContent.setText("");
        }
    }

    public int getXOffset() {
        // this will center the marker-view horizontally
        return -(getWidth() / 2);
    }

    public int getYOffset() {
        // this will cause the marker-view to be above the selected value
        return -getHeight();
    }
}
package com.riggle.plug.utils.chart;

import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;

public class IntValueFormatter1 extends ValueFormatter {

    private DecimalFormat mFormat;

    public IntValueFormatter1() {
        mFormat = new DecimalFormat("###,###,##0"); // use one decimal
    }

    @Override
    public String getFormattedValue(float value) {
        return  mFormat.format(value);
    }
}
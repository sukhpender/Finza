package com.riggle.plug.utils.chart;

import android.graphics.Canvas;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class CustomXAxisRendererWeekly extends XAxisRenderer {
    public CustomXAxisRendererWeekly(ViewPortHandler viewPortHandler, XAxis xAxis, Transformer trans) {
        super(viewPortHandler, xAxis, trans);
    }

    @Override
    protected void drawLabel(Canvas c, String formattedLabel, float x, float y, MPPointF anchor, float angleDegrees) {
      //  String line[] = formattedLabel.split("-");
        Utils.drawXAxisValue(c, formattedLabel, x, y, mAxisLabelPaint, anchor, -65);
      //  Utils.drawXAxisValue(c, line[0], x, y, mAxisLabelPaint, anchor, angleDegrees);
      //  Utils.drawXAxisValue(c, line[1], x , y + 30, mAxisLabelPaint, anchor, angleDegrees);
    }
}
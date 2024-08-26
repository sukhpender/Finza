package com.riggle.plug.utils.chart

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.DecimalFormat

class MyValueFormatter : ValueFormatter() {
    private val format = DecimalFormat("###")

    override fun getPointLabel(entry: Entry?): String {
        return format.format(entry?.y)
    }
    // override this for custom formatting of XAxis or YAxis labels
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
      //  return format.format(value)
        return format.format(value)
    }
}
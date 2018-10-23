package com.example.zhongzhoujianshe.healthapp;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

public class MyLineChartFormatter implements IAxisValueFormatter {
    private final String[] mLabels;
    public MyLineChartFormatter(String[] labels) {
        mLabels = labels;
    }
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        try {
            return mLabels[(int) value];
        } catch (Exception e) {
            e.printStackTrace();
            return mLabels[0];
        }
    }
}
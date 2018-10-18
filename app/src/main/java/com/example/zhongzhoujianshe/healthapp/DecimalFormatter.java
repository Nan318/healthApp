package com.example.zhongzhoujianshe.healthapp;

import android.icu.text.DecimalFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

public class DecimalFormatter implements IAxisValueFormatter {
    private DecimalFormat format;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public DecimalFormatter() {
        format = new DecimalFormat("0.00");

    }

    @RequiresApi(api = Build.VERSION_CODES.N)  ///???
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return format.format(value) ;
    }
}



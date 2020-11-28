package com.example.mytoday;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;

public class MyYAxisValueFormatter implements YAxisValueFormatter {

    private String[] score;

    //생성자
    public MyYAxisValueFormatter(String[] score) {
        this.score = score;
    }

    @Override
    public String getFormattedValue(float value, YAxis yAxis) {


        return score[(int)value];
    }




}

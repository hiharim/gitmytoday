package com.example.mytoday;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

// x축에 내가 원하는 일을 설정하기 위해 만든 클래스
public class GraphAxisValueFormatter extends ValueFormatter implements IAxisValueFormatter {

    private String[] mDays;

    //생성자 초기화
    public GraphAxisValueFormatter(String[] value) {
        this.mDays = value;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        value=value/10;

        return mDays[(int)value];
    }

}

package com.example.mytoday;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

// y축에 내가 원하는 기분점수를 설정하기 위해 만든 클래스
public class GraphYAxisValueFormatter extends ValueFormatter implements IAxisValueFormatter {

    private String[] mFeelings;

    //생성자 초기화
    public GraphYAxisValueFormatter(String[] values) {
        this.mFeelings = values;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        return mFeelings[(int) value];
    }


}

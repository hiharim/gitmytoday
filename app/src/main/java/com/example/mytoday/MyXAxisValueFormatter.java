package com.example.mytoday;

import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.github.mikephil.charting.formatter.XAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.Date;

public class MyXAxisValueFormatter implements XAxisValueFormatter {

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public String getXValue(String dateInMillisecons, int index, ViewPortHandler viewPortHandler) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy");

            return sdf.format(new Date(Long.parseLong(dateInMillisecons)));
        } catch (Exception e) {
            return dateInMillisecons;
        }

    }



//    @RequiresApi(api = Build.VERSION_CODES.N)
//    long utcToDates(long timeMillis) {
//        // utc를 입력 받아서 날수를 계산
//        // TimeZone을 고려하지 않고 그냥 하루의 시간으로 나누게 되면 지역별 시간차에 의해 최대 1일의 오차가 발생
//        return (timeMillis + Calendar.getInstance().get(Calendar.ZONE_OFFSET)) / (1000 * 60 * 60 * 24);
//    }
//
//    long datesToUtc(long dates) {
//        return dates * (1000 * 60 * 60 * 24);
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    public String getFormattedDate(long value) {
//        String strDate;
//        Date mDate = new Date();
//        Calendar mCalendar = Calendar.getInstance();
//
//        mDate.setTime(datesToUtc(value));
//        mCalendar.setTime(mDate);
//
//        strDate = "" + mCalendar.get(Calendar.YEAR) +
//                "-" + (mCalendar.get(Calendar.MONTH) + 1) +
//                "-" + mCalendar.get(Calendar.DATE);
//
//        return strDate;
//    }






}
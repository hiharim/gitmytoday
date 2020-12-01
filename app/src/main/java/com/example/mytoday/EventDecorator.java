package com.example.mytoday;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

//캘린더뷰 일기쓴 날은 점찍게 데코해주는 클래스
public class EventDecorator implements DayViewDecorator {

    private final int color;
    private final Set<CalendarDay> dates;


    public EventDecorator(int color, Set<CalendarDay> dates) {
        this.color = color;
        this.dates = dates;
    }



    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(5,color));
    }




}//class

package com.example.mytoday;

import java.io.Serializable;

// 차트에 필요한 데이터를 담는 데이터클래스
// 년,월,일,기분점수
public class ChartData implements Serializable {

    String year;
    String month;
    String day;
    String score;

    public ChartData(String year, String month, String day, String score) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.score = score;
    }


    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

}

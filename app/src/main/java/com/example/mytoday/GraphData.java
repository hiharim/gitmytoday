package com.example.mytoday;

import java.io.Serializable;

public class GraphData implements Serializable {


    private String dayValue;
    private float scoreValue;

    public GraphData(String dayValue, float scoreValue) {
        this.dayValue = dayValue;
        this.scoreValue = scoreValue;
    }




    public String getDayValue() {
        return dayValue;
    }

    public void setDayValue(String dayValue) {
        this.dayValue = dayValue;
    }

    public float getScoreValue() {
        return scoreValue;
    }

    public void setScoreValue(float scoreValue) {
        this.scoreValue = scoreValue;
    }



}

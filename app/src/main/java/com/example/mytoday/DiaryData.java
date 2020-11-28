package com.example.mytoday;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class DiaryData implements Serializable {

    private String content;
    private String feelings;
    private String date;
    private String time;
    private String photo;
    private String location;

    public DiaryData(String content, String feelings, String date, String time, String photo, String location) {
        this.content = content;
        this.feelings = feelings;
        this.date = date;
        this.time = time;
        this.photo = photo;
        this.location = location;
    }

    public DiaryData(String feelings,String date){
        this.feelings = feelings;
        this.date = date;
    }


    //arrayList 를 JSONObject로 변경시키는 메서드
    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("saveContent",this.getContent());
            obj.put("saveFeelings", this.getFeelings());
            obj.put("saveDate", this.getDate());
            obj.put("saveTime", this.getTime());
            obj.put("savePhoto", this.getPhoto());
            obj.put("saveLocation", this.getLocation());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }




    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFeelings() {
        return feelings;
    }

    public void setFeelings(String feelings) {
        this.feelings = feelings;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

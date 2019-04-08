package com.example.appnote.data.model;

import android.graphics.Color;
import java.util.Date;
import io.realm.RealmList;
import io.realm.RealmObject;

public class Note extends RealmObject{
    private int mId;
    private String mTitle;
    private String mContent;
    private boolean isAlarm;
    private String mDay;
    private String mHour;
    private int color;
    private Date mCreateDate;
    private RealmList<String> mUriStrings = new RealmList<>();

    public Note() {
        isAlarm = false;
        color = Color.WHITE;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public RealmList<String> getUriStrings() {
        return mUriStrings;
    }

    public Date getCreateDate() {
        return mCreateDate;
    }

    public void setCreateDate(Date createDate) {
        mCreateDate = createDate;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getDay() {
        return mDay;
    }

    public void setDay(String day) {
        this.mDay = day;
    }

    public String getHour() {
        return mHour;
    }

    public void setHour(String hour) {
        this.mHour = hour;
    }


    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public boolean isAlarm() {
        return isAlarm;
    }

    public void setAlarm(boolean alarm) {
        isAlarm = alarm;
    }

}

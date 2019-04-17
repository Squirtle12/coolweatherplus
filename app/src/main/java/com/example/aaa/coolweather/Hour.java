package com.example.aaa.coolweather;

/**
 * Created by aaa on 2019/4/16.
 */

public class Hour {
    private int hourly_icon_id;
    private String hourly_time;
    private String hourly_tmp;

    public String getHourly_time() {
        return hourly_time;
    }

    public void setHourly_time(String hourly_time) {
        this.hourly_time = hourly_time;
    }

    public String getHourly_tmp() {
        return hourly_tmp;
    }

    public void setHourly_tmp(String hourly_tmp) {
        this.hourly_tmp = hourly_tmp;
    }

    public int getHourly_icon_id() {
        return hourly_icon_id;
    }

    public void setHourly_icon_id(int hourly_icon_id) {
        this.hourly_icon_id = hourly_icon_id;
    }
}

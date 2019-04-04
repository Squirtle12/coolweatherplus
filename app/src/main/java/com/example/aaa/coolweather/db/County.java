package com.example.aaa.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by aaa on 2019/3/3.
 */

public class County extends DataSupport{
    private int id;
    private String conutyName;
    private String weatherId;
    private  int cityId;

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getConutyName() {
        return conutyName;
    }

    public void setConutyName(String conutryName) {
        this.conutyName = conutryName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }
}

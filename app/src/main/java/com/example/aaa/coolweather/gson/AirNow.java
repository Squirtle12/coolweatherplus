package com.example.aaa.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by aaa on 2019/4/20.
 */

public class AirNow {

    /**
     * aqi : 67
     * qlty : 良
     * main : PM2.5
     * pm25 : 48
     * pm10 : 54
     * no2 : 41
     * so2 : 3
     * co : 0.6
     * o3 : 43
     * pub_time : 2019-04-20 16:00
     */
    private String cid;
    private String aqi;
    @SerializedName("qlty")
    private String quality;
    private String pm25;
    private String pm10;
    private String no2;



    private String so2;
    private String co;
    private String o3;
    private String status;
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getAqi() {
        return aqi;
    }

    public void setAqi(String aqi) {
        this.aqi = aqi;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getPm25() {
        return pm25;
    }

    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }

    public String getPm10() {
        return pm10;
    }

    public void setPm10(String pm10) {
        this.pm10 = pm10;
    }

    public String getNo2() {
        return no2;
    }

    public void setNo2(String no2) {
        this.no2 = no2;
    }

    public String getSo2() {
        return so2;
    }

    public void setSo2(String so2) {
        this.so2 = so2;
    }

    public String getCo() {
        return co;
    }

    public void setCo(String co) {
        this.co = co;
    }

    public String getO3() {
        return o3;
    }

    public void setO3(String o3) {
        this.o3 = o3;
    }
}

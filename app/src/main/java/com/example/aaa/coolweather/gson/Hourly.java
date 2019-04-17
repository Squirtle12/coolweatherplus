package com.example.aaa.coolweather.gson;

/**
 * Created by aaa on 2019/4/16.
 */

public class Hourly {

    /**
     * cloud : 13
     * cond_code : 100
     * cond_txt : 晴
     * dew : 2
     * hum : 29
     * pop : 0
     * pres : 997
     * time : 2019-04-16 16:00
     * tmp : 25
     * wind_deg : 174
     * wind_dir : 南风
     * wind_sc : 4-5
     * wind_spd : 30
     */

    private String cond_code;
    private String cond_txt;
    private String pop;
    private String time;
    private String tmp;

    public String getCond_code() {
        return cond_code;
    }

    public void setCond_code(String cond_code) {
        this.cond_code = cond_code;
    }

    public String getCond_txt() {
        return cond_txt;
    }

    public void setCond_txt(String cond_txt) {
        this.cond_txt = cond_txt;
    }

    public String getPop() {
        return pop;
    }

    public void setPop(String pop) {
        this.pop = pop;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTmp() {
        return tmp;
    }

    public void setTmp(String tmp) {
        this.tmp = tmp;
    }
}

package com.example.aaa.coolweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by aaa on 2019/3/15.
 */
/**
 * 数据类型
 * {
 *     "HeWeather":[
 *     {
 *         "status":"ok",
 *         "basic":{},
 *         "aqi":{},
 *         "now":{},
 *         "suggestion":{},
 *         "daily_forecast":{}
 *     }
 *     ]
 * }
 */
public class Weather {
    //成功返回ok，失败返回原因。
    public  String status;
    public  Basic basic;
    public AQI aqi;
    public Now now;
    public Suggestion suggestion;
    //由于其包含的是数组，因此使用集合来引用该类。
    @SerializedName("daily_forecast")
    public List<Forecast>forecastList;
}

package com.example.aaa.coolweather.gson;

/**
 * Created by aaa on 2019/3/15.
 */

/**
 *  "aqi":{
 *      "city":{
 *          "aqi":"44",
 *          "pm25":"13"
 *      }
 *  }
 */
public class AQI {
    public AQICity city;
    public class AQICity{
        public String aqi;
        public  String pm25;
    }
}

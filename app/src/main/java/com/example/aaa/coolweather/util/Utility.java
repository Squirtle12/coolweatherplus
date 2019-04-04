package com.example.aaa.coolweather.util;

import android.text.TextUtils;

import com.example.aaa.coolweather.db.City;
import com.example.aaa.coolweather.db.County;
import com.example.aaa.coolweather.db.Province;
import com.example.aaa.coolweather.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by aaa on 2019/3/3.
 */

public class Utility {


    /**
     * 数据类型为：[{"id":1,"name":"北京"},{。。。},{。。。},{。。。}]
     */
    /**
     * 解析和处理服务器返回的省级数据
     */
    public static boolean handleProvinceResponse(String response){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray allProvinces=new JSONArray(response);
                for(int i=0;i<allProvinces.length();i++)
                {
                    JSONObject object=allProvinces.getJSONObject(i);
                    Province province=new Province();
                    province.setProvinceCode(object.getInt("id"));
                    province.setProvinceName(object.getString("name"));
                    province.save();
                }
                return true;
            }
            catch (JSONException je){
                je.printStackTrace();
            }
        }
        return false;
    }
    /**
     * 数据类型为：[{"id":113,"name":"南京"},{。。。},{。。。},{。。。}]
     */
    /**
     * 解析和处理服务器返回的市级数据
     */
    public static boolean handleCityResponse(String response,int provinceid){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray allcitices=new JSONArray(response);
                for(int i=0;i<allcitices.length();i++)
                {
                    JSONObject object=allcitices.getJSONObject(i);
                    City city=new City();
                    city.setCityCode(object.getInt("id"));
                    city.setCityName(object.getString("name"));
                    city.setProvinceId(provinceid);
                    city.save();
                }
                return true;
            }
            catch (JSONException je){
                je.printStackTrace();
            }
        }
        return false;
    }
    /**
     * 数据类型为：[{"id":937,"name":"苏州","weather_id":"CN101190401"},{。。。},{。。。},{。。。}]
     */
    /**
     * 解析和处理服务器返回的县级数据
     */
    public static boolean handleCountyResponse(String response,int cityid){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray allcounties=new JSONArray(response);
                for(int i=0;i<allcounties.length();i++)
                {
                    JSONObject object=allcounties.getJSONObject(i);
                    County county=new County();
                    county.setConutyName(object.getString("name"));
                    county.setWeatherId(object.getString("weather_id"));
                    county.setCityId(cityid);
                    county.save();
                }
                return true;
            }
            catch (JSONException je){
                je.printStackTrace();
            }
        }
        return false;
    }
    /**
     * 将返回的JSON数据解析成Weather实体类
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
    public static Weather handleWeatherResponse(String response) {
        try {
            JSONObject jsonObject=new JSONObject(response);
            JSONArray jsonArray=jsonObject.getJSONArray("HeWeather");
            String weatherContent=jsonArray.getJSONObject(0).toString();
            //之间返回Weather类型的变量
            //之前是使用TypeToken将期望解析成的数据类型传入到fromJson()方法
            //Gson gson=new Gson();
            //LIst<Person>people=gson.fromJson(jsonData,new TypeToken<LIst<Person>>(){}.getType());
            return new Gson().fromJson(weatherContent,Weather.class);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
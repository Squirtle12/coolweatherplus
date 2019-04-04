package com.example.aaa.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by aaa on 2019/3/15.
 */

/**
 * json形式
 * "basic":{
 *     "city":"苏州"，
 *     "id":"CN101190401"
 *     "update":{
 *         "loc":"2016-08-08 21:58"
 *     }
 * }
 */
public class Basic {
    //使用@SerializedName注解的方式来让JSON字段和java字段之间建立映射关系。
    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherid;

    public Update update;
    public class Update{

        @SerializedName("loc")
        public  String updateTime;

    }

}

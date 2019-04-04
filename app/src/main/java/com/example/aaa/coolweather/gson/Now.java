package com.example.aaa.coolweather.gson;

/**
 * Created by aaa on 2019/3/15.
 */

import com.google.gson.annotations.SerializedName;

/**
 *  "now":{
 *      "tmp":"29",
 *      "cond":{
 *          "txt":"阵雨"
 *      }
 *  }
 */
public class Now {
    @SerializedName("tmp")
    public  String temperature;

    @SerializedName("cond")
    public More more;
    public class More{
        @SerializedName("txt")
        public String info;
    }
}

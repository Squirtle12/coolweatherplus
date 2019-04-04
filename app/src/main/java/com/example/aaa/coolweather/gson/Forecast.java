package com.example.aaa.coolweather.gson;

/**
 * Created by aaa on 2019/3/15.
 */

import com.google.gson.annotations.SerializedName;

/**
 * 包含的是一个数组，数组中的每一项都代表着未来一天的天气信息。只需定义出单日天气的实体类。
 *  "daily_forecast":[
 *  {
 *     "date":"2016-08-08" ,
 *     "cond":{
 *         "txt_d":"阵雨"
 *     }，
 *     "tmp":{
 *         "max":"34",
 *         "min":"27"
 *     }
 *  },
 *  {
 *     "date":"2016-08-09" ,
 *     "cond":{
 *         "txt_d":"多云"
 *     }，
 *     "tmp":{
 *         "max":"35",
 *         "min":"29"
 *     }
 *  },
 *  ...
 *  ]
 */
public class Forecast {
    public  String date;
    @SerializedName("tmp")
    public Temperature temperature;

    @SerializedName("cond")
    public More more;

    public class Temperature{
        public  String max;
        public String min;
    }

    public class  More{
        @SerializedName("txt_d")
        public  String info;
    }
}

package com.example.aaa.coolweather.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aaa on 2019/4/11.
 */

public class CommonWeatherDeserializer implements JsonDeserializer<CommonWeather> {
    @Override
    public CommonWeather deserialize(final JsonElement jsonElement, final Type typeOfT,
                                     final JsonDeserializationContext context)throws JsonParseException{
        final JsonObject jsonObject = jsonElement.getAsJsonObject();
        final JsonArray jsonArray=jsonObject.get("HeWeather6").getAsJsonArray();
        final JsonObject jsonObject1=jsonArray.get(0).getAsJsonObject();
        String status=context.deserialize(jsonObject1.get("status"),String.class);
        CommonWeather.BasicBean basic=context.deserialize(jsonObject1.get("basic"), CommonWeather.BasicBean.class);
        CommonWeather.UpdateBean update=context.deserialize(jsonObject1.get("update"), CommonWeather.UpdateBean.class);
        CommonWeather.NowBean now=context.deserialize(jsonObject1.get("now"), CommonWeather.NowBean.class);
        final JsonArray daily_forecast_array=jsonObject1.get("daily_forecast").getAsJsonArray();
        final JsonArray lifestyle_array=jsonObject1.get("lifestyle").getAsJsonArray();
        final JsonArray hourly=jsonObject1.get("hourly").getAsJsonArray();
        List<CommonWeather.DailyForecastBean> dailyForecastBeanslist=new ArrayList<>();
        List<CommonWeather.LifestyleBean>lifestyleBeanslist=new ArrayList<>();
        List<Hourly>hourlyList=new ArrayList<>();
        for (JsonElement jsonElement1:daily_forecast_array)
        {
            CommonWeather.DailyForecastBean dailyForecastBean=context.deserialize(jsonElement1, CommonWeather.DailyForecastBean.class);
            dailyForecastBeanslist.add(dailyForecastBean);
        }
        for (JsonElement jsonElement1:lifestyle_array)
        {
            CommonWeather.LifestyleBean lifestyleBean=context.deserialize(jsonElement1, CommonWeather.LifestyleBean.class);
            lifestyleBeanslist.add(lifestyleBean);
        }
        for(JsonElement jsonElement1:hourly)
        {
            Hourly hourly1=context.deserialize(jsonElement1,Hourly.class);
            hourlyList.add(hourly1);
        }
        CommonWeather commonWeather=new CommonWeather();
        commonWeather.setStatus(status);
        commonWeather.setBasic(basic);
        commonWeather.setUpdate(update);
        commonWeather.setNow(now);
        commonWeather.setDaily_forecast(dailyForecastBeanslist);
        commonWeather.setLifestyle(lifestyleBeanslist);
        commonWeather.setHourlyList(hourlyList);
        return commonWeather;
    }
}

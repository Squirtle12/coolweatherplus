package com.example.aaa.coolweather.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * Created by aaa on 2019/4/20.
 */

public class AirNowDeserializer implements JsonDeserializer<AirNow> {
    @Override
    public AirNow deserialize(final JsonElement jsonElement, final Type typeOfT,
                              final JsonDeserializationContext context)throws JsonParseException {
       final JsonObject jsonObject=jsonElement.getAsJsonObject();
        final JsonArray jsonArray=jsonObject.get("HeWeather6").getAsJsonArray();
        final JsonObject jsonObject1=jsonArray.get(0).getAsJsonObject();
        String status=context.deserialize(jsonObject1.get("status"),String.class);
        AirNow airNow = new AirNow();
        if ("ok".equals(status)) {
            final JsonObject jsonObject2 = jsonObject1.get("air_now_city").getAsJsonObject();
            String cid = context.deserialize(jsonObject1.get("basic").getAsJsonObject().get("cid"), String.class);

            String aqi = context.deserialize(jsonObject2.get("aqi"), String.class);
            String qlty = context.deserialize(jsonObject2.get("qlty"), String.class);
            String pm25=context.deserialize(jsonObject2.get("pm25"),String.class);
            String pm10=context.deserialize(jsonObject2.get("pm10"),String.class);
            String no2=context.deserialize(jsonObject2.get("no2"),String.class);
            String so2=context.deserialize(jsonObject2.get("so2"),String.class);
            String co=context.deserialize(jsonObject2.get("co"),String.class);
            String o3=context.deserialize(jsonObject2.get("o3"),String.class);

            airNow.setAqi(aqi);
            airNow.setQuality(qlty);
            airNow.setCid(cid);
            airNow.setPm10(pm10);
            airNow.setPm25(pm25);
            airNow.setSo2(so2);
            airNow.setO3(o3);
            airNow.setCo(co);
            airNow.setNo2(no2);
        }
        airNow.setStatus(status);
        return airNow;
    }

}

package com.example.aaa.coolweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.aaa.coolweather.gson.Weather;
import com.example.aaa.coolweather.util.HttpUtil;
import com.example.aaa.coolweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AutoUpdateService extends Service {
    public AutoUpdateService() {

    }
    @Override
    public  int onStartCommand(Intent intent,int flags,int startId){
        //更新天气信息
        updateWeather();
        //更新每日一图
        updateBingPic();
        //定时任务
        AlarmManager manager=(AlarmManager)getSystemService(ALARM_SERVICE);
        SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
        String times=prefs.getString("times",null);
        int anHour;
        if (times==null)
            anHour=8*60*60*1000;//8 hours
        else
        {
            int i=times.indexOf('/');
            int hours=Integer.valueOf(times.substring(0,i));
            int minutes=Integer.valueOf(times.substring(i+1,times.length()));
            anHour=(hours*60*60*1000)+minutes*60*1000;
        }
        Log.d("times",anHour/(60*1000*60)+"小时 "+anHour%(60*1000*60)/(60*1000)+"分钟");
        //elapsedRealtime()从系统开机开始算
        long triggerAtTime= SystemClock.elapsedRealtime()+anHour;
        Intent i=new Intent(this,AutoUpdateService.class);
        PendingIntent pendingIntent=PendingIntent.getService(this,0,i,0);
        manager.cancel(pendingIntent);
        //set的参数要对应，这里用了ELAPSED_REALTIME_WAKEUP代表从开机后计时
        //那么第二个参数也要对应：定时时间加上elapsedRealtime()。
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pendingIntent);
        return super.onStartCommand(intent,flags,startId);
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    /**
     * 更新天气信息
     */
    private void updateWeather(){

        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        //这个就是解析下来的json数据。
        String weatherString=prefs.getString("weather",null);
        if (weatherString!=null)
        {
            //有缓存直接解析
            Weather weather= Utility.handleWeatherResponse(weatherString);
            final String weatherId=weather.basic.weatherid;
            String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId +
                    "&key=a15bff1949104f8ba6d4553c611ac2f7";
            HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                        String responseText=response.body().string();
                        Weather weather=Utility.handleWeatherResponse(responseText);
                        if (weather!=null&&"ok".equals(weather.status)){
                            //更新SharedPrefrences即可，不需要取更改，
                            // 因为在打开WeatherActivty中每次都会先调用缓存
                            SharedPreferences.Editor editor=PreferenceManager.
                                    getDefaultSharedPreferences(AutoUpdateService.this)
                                    .edit();
                            editor.putString("weather",responseText);
                            editor.apply();
                        }
                }
            });
        }
    }
    /**
     * 更新必应每日一图
     */
    private void updateBingPic(){
        String requestBingPic ="http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String bingPic=response.body().string();
                SharedPreferences.Editor editor=PreferenceManager.
                        getDefaultSharedPreferences(AutoUpdateService.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
            }
        });
    }
}

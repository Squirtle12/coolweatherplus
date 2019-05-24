package com.example.aaa.coolweather.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.NotificationCompat;

import com.example.aaa.coolweather.R;
import com.example.aaa.coolweather.WeatherActivity;
import com.example.aaa.coolweather.gson.CommonWeather;
import com.example.aaa.coolweather.util.HttpUtil;
import com.example.aaa.coolweather.util.Utility;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class NotificationService extends Service {
    private HashMap<String,Integer>  map;
    private NotificationCompat.Builder builder;
    private   NotificationManager notificationManager;
    public  void init(){
        map=new HashMap<>();
        String[]strings=new String[]{"晴","多云", "少云",
                "晴间多云",
                "阴",
                "有风",
                "平静",
                "微风",
                "和风",
                "清风",
                "强风/劲风",
                "疾风",
                "大风",
                "烈风",
                "风暴",
                "狂爆风",
                "飓风",
                "龙卷风",
                "热带风暴",
                "阵雨",
                "强阵雨",
                "雷阵雨",
                "强雷阵雨",
                "雷阵雨伴有冰雹",
                "小雨",
                "中雨",
                "大雨",
                "极端降雨",
                "毛毛雨/细雨",
                "暴雨",
                "大暴雨",
                "特大暴雨",
                "冻雨",
                "小到中雨",
                "中到大雨",
                "大到暴雨",
                "暴雨到大暴雨",
                "大暴雨到特大暴雨",
                "雨",
                "小雪",
                "中雪",
                "大雪",
                "暴雪",
                "雨夹雪",
                "雨雪天气",
                "阵雨夹雪",
                "阵雪",
                "小到中雪",
                "中到大雪",
                "大到暴雪",
                "雪",
                "薄雾",
                "雾",
                "霾",
                "扬沙",
                "浮尘",
                "沙尘暴",
                "强沙尘暴",
                "浓雾",
                "强浓雾",
                "中度霾",
                "重度霾",
                "严重霾",
                "大雾",
                "特强浓雾",
                "热",
                "冷",
                "未知"
        };
        int []ints=new int[]{
                R.mipmap.w100,
                R.mipmap.w101,
                R.mipmap.w102,
                R.mipmap.w103,
                R.mipmap.w104,
                R.mipmap.w200,
                R.mipmap.w201,
                R.mipmap.w202,
                R.mipmap.w203,
                R.mipmap.w204,
                R.mipmap.w205,
                R.mipmap.w206,
                R.mipmap.w207,
                R.mipmap.w208,
                R.mipmap.w209,
                R.mipmap.w210,
                R.mipmap.w211,
                R.mipmap.w212,
                R.mipmap.w213,
                R.mipmap.w300,
                R.mipmap.w301,
                R.mipmap.w302,
                R.mipmap.w303,
                R.mipmap.w304,
                R.mipmap.w305,
                R.mipmap.w306,
                R.mipmap.w307,
                R.mipmap.w308,
                R.mipmap.w309,
                R.mipmap.w310,
                R.mipmap.w311,
                R.mipmap.w312,
                R.mipmap.w313,
                R.mipmap.w314,
                R.mipmap.w315,
                R.mipmap.w316,
                R.mipmap.w317,
                R.mipmap.w318,
                R.mipmap.w399,
                R.mipmap.w400,
                R.mipmap.w401,
                R.mipmap.w402,
                R.mipmap.w403,
                R.mipmap.w404,
                R.mipmap.w405,
                R.mipmap.w406,
                R.mipmap.w407,
                R.mipmap.w408,
                R.mipmap.w409,
                R.mipmap.w410,
                R.mipmap.w499,
                R.mipmap.w500,
                R.mipmap.w501,
                R.mipmap.w502,
                R.mipmap.w503,
                R.mipmap.w504,
                R.mipmap.w507,
                R.mipmap.w508,
                R.mipmap.w509,
                R.mipmap.w510,
                R.mipmap.w511,
                R.mipmap.w512,
                R.mipmap.w513,
                R.mipmap.w514,
                R.mipmap.w515,
                R.mipmap.w900,
                R.mipmap.w901,
                R.mipmap.w999

        };
        for (int i=0;i<strings.length;i++)
        {
            map.put(strings[i],ints[i]);
        }

    }
    public NotificationService() {
        init();

    }
    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        //定时任务。
        getCommonWeather();
        SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(this);
        String weatherstring=sp.getString("weather",null);
        if(weatherstring!=null){
            CommonWeather commonWeather=Utility.handleCommonWeatherResponse(weatherstring);
            setNotification(commonWeather);
        }

        //耗时5分钟
        AlarmManager manager=(AlarmManager)getSystemService(ALARM_SERVICE);
        int times= 5*60*1000;
        long triggerAtTime= SystemClock.elapsedRealtime()+times;
        Intent i=new Intent(this,NotificationService.class);
        PendingIntent pendingIntent=PendingIntent.getService(this,0,i,0);
        manager.cancel(pendingIntent);
        //set的参数要对应，这里用了ELAPSED_REALTIME_WAKEUP代表从开机后计时
        //那么第二个参数也要对应：定时时间加上elapsedRealtime()。
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pendingIntent);
        return super.onStartCommand(intent,flags,startId);

    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    /**
     * 获取天气集合，去更改通知栏的数据。
     */
    public void getCommonWeather(){
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        //其实只是想要它的id，来去获取服务器中的数据
        String weatherString=prefs.getString("weather",null);
        if (weatherString!=null){
            CommonWeather oldcommonWeather= Utility.handleCommonWeatherResponse(weatherString);
            String weatherid=oldcommonWeather.getBasic().getCid();
            String weatherUrl="https://free-api.heweather.net/s6/weather?location=" + weatherid +
                    "&key=a15bff1949104f8ba6d4553c611ac2f7";
            HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                        String responseText=response.body().string();
                    CommonWeather newCommonWeather=Utility.handleCommonWeatherResponse(responseText);
                    if (newCommonWeather!=null&&"ok".equals(newCommonWeather.getStatus()))
                    {
                        SharedPreferences.Editor editor=PreferenceManager.
                                getDefaultSharedPreferences(NotificationService.this)
                                .edit();
                        editor.putString("weather",responseText);
                        editor.apply();
                    }
                }
            });
        }
    }


    /**
     * 通知
     */
    public void setNotification(CommonWeather commonWeather){
        Intent intent=new Intent(this, WeatherActivity.class);
        PendingIntent pi=PendingIntent.getActivity(this,0,intent,0);
        builder =new NotificationCompat.Builder(this);
        builder.setContentIntent(pi);
        int Weatherid=map.get(commonWeather.getNow().getCond_txt());
        String nowweather=""+commonWeather.getNow().getTmp();
        String nowcity=""+commonWeather.getBasic().getCity();
        builder.setSmallIcon(R.mipmap.logo);
        //解决：需要的是mipmap的问题，
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),Weatherid));
        builder.setContentTitle("当前天气");
        builder.setContentText(nowcity+":"+nowweather);
        startForeground(1,builder.build());
    }
}

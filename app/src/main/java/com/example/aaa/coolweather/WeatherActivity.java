package com.example.aaa.coolweather;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ScrollingView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.aaa.coolweather.db.Province;
import com.example.aaa.coolweather.gson.Forecast;
import com.example.aaa.coolweather.gson.Weather;
import com.example.aaa.coolweather.service.AutoUpdateService;
import com.example.aaa.coolweather.util.HttpUtil;
import com.example.aaa.coolweather.util.Utility;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity implements View.OnClickListener {
    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;
    private ImageView bingPicImg;
    private Button setting;
    public SwipeRefreshLayout swipeRefresh;
    public DrawerLayout drawerLayout;
    private Button navButton;
    private  String weatherId;
    private SharedPreferences prefs;
    private Map<String,Integer> map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT>=21)
        {
            View decorView=getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        //初始化控件
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);
        comfortText = (TextView) findViewById(R.id.comfort_text);
        carWashText = (TextView) findViewById(R.id.car_wash_text);
        sportText = (TextView) findViewById(R.id.sport_text);
        bingPicImg=(ImageView)findViewById(R.id.bing_pic_img);
        swipeRefresh=(SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        navButton=(Button)findViewById(R.id.nav_button);
        setting=(Button)findViewById(R.id.setting);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        init();
        //prefs=getSharedPreferences("Weather", Context.MODE_PRIVATE);
        String weatherString = prefs.getString("weather", null);

        //判断缓存中是否右
        if (weatherString != null) {
            //有缓存时直接解析天气数据
            //不需要再去上网获取了json形式数据。
            Weather weather = Utility.handleWeatherResponse(weatherString);
            //这里也需要赋值，因为是要给下拉刷新用的
            weatherId=weather.basic.weatherid;
            showWeahterInfo(weather);

        } else {
            //无缓存时去服务器查询天气
            //weather_id是county级别的
            //这个weather_id是在之前的碎片转到这个活动的时候，一起通过intent传过来的。
            weatherId = getIntent().getStringExtra("weather_id");
            //不显示
            weatherLayout.setVisibility(View.INVISIBLE);
            //去服务器查询
            requestWeather(weatherId);
        }
        //下拉刷新控件的对应事件。
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.
                OnRefreshListener() {
            @Override
            public void onRefresh() {

                weatherId=prefs.getString("weather_id",null);
                requestWeather(weatherId);
            }
        });
        String bingPic=prefs.getString("bing_pic",null);
        if(bingPic!=null)
        {
            Glide.with(this).load(bingPic).into(bingPicImg);
        }
        else {
            //加载必应每日一图
            loadBingPic();
        }
        navButton.setOnClickListener(this);
        setting.setOnClickListener(this);
    }
    /**
     * 初始化数据
     */
    public void init(){
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
    /**
     * button的相应事件
     */
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.nav_button:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.setting:
                Intent intent=new Intent(this,SettingActivity.class);
                startActivity(intent);
                break;
        }
    }
    /**
     * 根据天气id请求城市天气信息
     */
    public void requestWeather(final String weatherId) {

        String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId +
                "&key=a15bff1949104f8ba6d4553c611ac2f7";
        //请求一次更改一次（应对换城市后，在刷新就会刷新到上一个城市的bug）
        //this.weatherId=weatherId;
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败",
                                Toast.LENGTH_SHORT).show();
                        //刷新事件结束，隐藏刷新进度条
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                //获取weather类
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //状态码
                        if (weather != null && "ok".equals(weather.status)) {
                            //回去看一下SharedPrefernces
                            SharedPreferences.Editor editor = PreferenceManager.
                                    getDefaultSharedPreferences(WeatherActivity.this).edit();
                            //必须传这个。
                            editor.putString("weather", responseText);
                            editor.apply();
                            //展示天气
                            showWeahterInfo(weather);
                        } else {
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败",
                                    Toast.LENGTH_SHORT).show();
                        }
                        //刷新事件结束，隐藏刷新进度条
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
        //加载必应
        loadBingPic();
    }

    /**
     * 处理并展示Weather实体类中的数据(顺序为titile，now,forecast,aqi,suggestion)
     */
    private void showWeahterInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        //不要年份了，原始是2016-08-08 21:58
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        String temperature = weather.now.temperature + "°C";
        String info = weather.now.more.info;
        degreeText.setText(temperature);
        weatherInfoText.setText(info);
        forecastLayout.removeAllViews();
        //看有几个新的天气预报
        //动态加载布局，并设置相应的数据
        for (Forecast forecast : weather.forecastList) {
            //类似于ListView,一会尝试下ListView的getView()
            View view = LayoutInflater.from(this).inflate(R.layout.forecasr_item,
                    forecastLayout, false);
            TextView date_text = (TextView) view.findViewById(R.id.date_text);
            ImageView weather_icon=(ImageView)view.findViewById(R.id.weather_icon);
            //TextView info_text = (TextView) view.findViewById(R.id.info_text);
            TextView max_text = (TextView) view.findViewById(R.id.max_text);
            TextView min_text = (TextView) view.findViewById(R.id.min_text);
            date_text.setText(forecast.date);
            String weather_name=forecast.more.info;

            weather_icon.setImageResource(map.get(weather_name));
            max_text.setText(forecast.temperature.max);
            min_text.setText(forecast.temperature.min);
            forecastLayout.addView(view);
        }
        aqiText.setText(weather.aqi.city.aqi);
        pm25Text.setText(weather.aqi.city.pm25);
        comfortText.setText(weather.suggestion.comfort.info);
        carWashText.setText(weather.suggestion.carWash.info);
        sportText.setText(weather.suggestion.sport.info);
        //设置可见
        weatherLayout.setVisibility(View.VISIBLE);

        Intent intent=new Intent(this, AutoUpdateService.class);
        startService(intent);
    }
    /**
     * 加载必应每日一图
     */
    private void loadBingPic(){
        String requestBingPic="http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                    final String bingPic=response.body().string();
                SharedPreferences.Editor editor=PreferenceManager.
                        getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }
        });
    }
}
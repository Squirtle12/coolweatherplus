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
            TextView info_text = (TextView) view.findViewById(R.id.info_text);
            TextView max_text = (TextView) view.findViewById(R.id.max_text);
            TextView min_text = (TextView) view.findViewById(R.id.min_text);
            date_text.setText(forecast.date);
            info_text.setText(forecast.more.info);
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
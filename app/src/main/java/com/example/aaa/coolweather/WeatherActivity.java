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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.example.aaa.coolweather.gson.CommonWeather;
import com.example.aaa.coolweather.gson.Hourly;
import com.example.aaa.coolweather.service.AutoUpdateService;
import com.example.aaa.coolweather.util.HttpUtil;
import com.example.aaa.coolweather.util.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private LinearLayout suggestionLayout;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;


    private  TextView body_tmpText;
    private TextView humText;
    private TextView windText;
    private TextView sunriseText;
    private TextView sunsetText;
    private TextView paText;


    private ImageView bingPicImg;
    private Button setting;
    public SwipeRefreshLayout swipeRefresh;
    public DrawerLayout drawerLayout;
    private Button navButton;
    private  String weatherId;
    private SharedPreferences prefs;
    private Map<String,Integer> map;
    private String[] suggestion_string;

    private List<Hour>hourList=new ArrayList<>();
    private RecyclerView hourRecyclerView;
    private HourlyAdapter hourlyAdapter;

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
        suggestionLayout=(LinearLayout)findViewById(R.id.suggestion_layout);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);

        humText=(TextView)findViewById(R.id.hum);
        body_tmpText=(TextView)findViewById(R.id.body_tmp);
        windText=(TextView)findViewById(R.id.wind);
        sunriseText=(TextView)findViewById(R.id.sunrise);
        sunsetText=(TextView)findViewById(R.id.sunset);
        paText=(TextView)findViewById(R.id.pa);


        bingPicImg=(ImageView)findViewById(R.id.bing_pic_img);
        swipeRefresh=(SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        navButton=(Button)findViewById(R.id.nav_button);
        setting=(Button)findViewById(R.id.setting);


        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        hourRecyclerView =(RecyclerView)findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        //设置横向
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        hourRecyclerView.setLayoutManager(layoutManager);
        hourlyAdapter =new HourlyAdapter(hourList);
        hourRecyclerView.setAdapter(hourlyAdapter);


        init();

        String weatherString = prefs.getString("weather", null);

        //判断缓存中是否右
        if (weatherString != null) {
            //有缓存时直接解析天气数据
            //不需要再去上网获取了json形式数据。
            CommonWeather commonweather = Utility.handleCommonWeatherResponse(weatherString);
            //这里也需要赋值，因为是要给下拉刷新用的
            weatherId=commonweather.getBasic().getCid();
            showWeahterInfo(commonweather);

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
        map.put("comf",R.drawable.icon_comf);
        map.put("drsg",R.drawable.icon_cloth);
        map.put("flu",R.drawable.icon_flu);
        map.put("sport",R.drawable.icon_sport);
        map.put("trav",R.drawable.icon_travel);
        map.put("cw",R.drawable.icon_carwash);
        map.put("air",R.drawable.icon_air);
        map.put("uv",R.drawable.icon_uv);
        suggestion_string=new String[]{"舒适指数---","穿衣指数---",
                "感冒指数---","运动指数---","出行指数---","紫外线指数---",
                "洗车指数---","天气指数---"};
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

        String weatherUrl = "https://free-api.heweather.net/s6/weather?location=" + weatherId +
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
                final CommonWeather commonweather = Utility.handleCommonWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //状态码
                        if (commonweather != null && "ok".equals(commonweather.getStatus())) {
                            //回去看一下SharedPrefernces
                            SharedPreferences.Editor editor = PreferenceManager.
                                    getDefaultSharedPreferences(WeatherActivity.this).edit();
                            //必须传这个,存储至sp
                            editor.putString("weather", responseText);
                            editor.apply();
                            //展示天气
                            showWeahterInfo(commonweather);
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
    private void showWeahterInfo(CommonWeather commonweather) {
        /**
         * 获取几个类
         */
        CommonWeather.NowBean now=commonweather.getNow();
        CommonWeather.BasicBean basic=commonweather.getBasic();
        List<CommonWeather.DailyForecastBean> daily_forecast=commonweather.getDaily_forecast();
        List<CommonWeather.LifestyleBean>lifestyle=commonweather.getLifestyle();
        String status=commonweather.getStatus();
        CommonWeather.UpdateBean update=commonweather.getUpdate();
        List<Hourly> hourlyList=commonweather.getHourlyList();
        /**
         * 赋值hourly.xml
         * hourlyList是从服务器上获取的list，
         * 我们要将其中数据赋值给HourlyAdapter的数据源hourlist。
         */
        hourList.clear();
        for(Hourly hourly:hourlyList)
        {
            Hour newhour=new Hour();
            //获取到小时。（比如22）
            int hourtime=Integer.valueOf(hourly.getTime().split(" ")[1].split(":")[0]);
            //0代表早上（1：00-7：00），1代表上午（8：00-12：00），
            // 2代表下午（13：00-17：00），3代表晚上（18：00-24：00）。
            String time;

            if (hourtime>=18)
                time="晚上";
            else if(hourtime>=13)
                time="下午";
            else if (hourtime>=8)
                time="上午";
            else
                time="早上";
            //超过12就减去12
            hourtime=hourtime>12?hourtime-12:hourtime;
            String hourString=time+hourtime+":00";
            String hourtmp=hourly.getTmp();
            int hour_icon_id=map.get(hourly.getCond_txt());
            newhour.setHourly_icon_id(hour_icon_id);
            newhour.setHourly_time(hourString);
            newhour.setHourly_tmp(hourtmp);
            hourList.add(newhour);
        }
        hourlyAdapter.notifyDataSetChanged();
        /**
        * 赋值title.xml
         */
        String cityName = basic.getCity();
        //不要年份了，原始是2016-08-08 21:58
        String updateTime = update.getUpdate_time().split(" ")[1];
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        /**
        * 赋值now.xml
         */
        String temperature = now.getTmp() + "°C";
        String info = now.getCond_txt();
        degreeText.setText(temperature);
        weatherInfoText.setText(info);


        /**
         * 预报，赋值forecast.xml
         */
        //每一次都要删除layout之前的view
        forecastLayout.removeAllViews();
        //看有几个新的天气预报
        //动态加载布局，并设置相应的数据
        //今天的不同，不写日期而写今天
        boolean todayflag=true;

        for (CommonWeather.DailyForecastBean dailyForecastBean : daily_forecast) {
            //类似于ListView,一会尝试下ListView的getView()
            String date="";
            View view = LayoutInflater.from(this).inflate(R.layout.forecasr_item,
                    forecastLayout, false);

            TextView date_text = (TextView) view.findViewById(R.id.date_text);
            ImageView weather_icon=(ImageView)view.findViewById(R.id.weather_icon);
            //TextView info_text = (TextView) view.findViewById(R.id.info_text);
            TextView max_text = (TextView) view.findViewById(R.id.max_text);
            TextView min_text = (TextView) view.findViewById(R.id.min_text);
            if(todayflag) {
                date="今天";
                todayflag=false;
            }
            else
                date=dailyForecastBean.getDate();
            date_text.setText(date);
            String weather_name=dailyForecastBean.getCond_txt_d();

            weather_icon.setImageResource(map.get(weather_name));
            max_text.setText(dailyForecastBean.getTmp_max());
            min_text.setText(dailyForecastBean.getTmp_min());
            forecastLayout.addView(view);
        }

        //aqiText.setText(commonweather.aqi.city.aqi);
        //pm25Text.setText(weather.aqi.city.pm25);
      //  windDirText.setText(commonweather.getNow().getWind_dir());
       // windScText.setText(commonweather.getNow().getWind_sc());

        /**
         * 建议，赋值suggestion.xml
         */
        //每一次都要删除layout之前的view
        suggestionLayout.removeAllViews();
        //六条建议有不同的标题比如xx指数
        int suggestion_number=0;
        for (CommonWeather.LifestyleBean lifestyleBean:commonweather.getLifestyle())
        {
            View view = LayoutInflater.from(this).inflate(R.layout.suggestion_item,suggestionLayout,false);
            ImageView suggestion_icon=(ImageView) view.findViewById(R.id.suggestion_icon);
            TextView suggestion_main_idea=(TextView)view.findViewById(R.id.suggestion_main_idea);
            TextView suggestion_txt=(TextView)view.findViewById(R.id.suggestion_txt);
            String mainIdeaText=suggestion_string[suggestion_number++]+lifestyleBean.getMain_idea();
            suggestion_main_idea.setText(mainIdeaText);
            suggestion_txt.setText(lifestyleBean.getTxt());
            suggestion_icon.setImageResource(map.get(lifestyleBean.getType()));

            //这点很重要，别忘了
            suggestionLayout.addView(view);
        }
       /**
        * 赋值weather_set.xml
        */
        String wind_String=now.getWind_dir()+commonweather.getNow().getWind_sc()
                +"级";
        String hum_String=now.getHum()+"%";
        String tmp_String=now.getBody_tmp()+"°C";
        String sunrise_String=daily_forecast.get(0).getSr();
        String sunset_String=daily_forecast.get(0).getSs();
        String pa_String=now.getPressure()+" hpa";
        windText.setText(wind_String);
        humText.setText(hum_String);
        body_tmpText.setText(tmp_String);
        sunsetText.setText(sunset_String);
        sunriseText.setText(sunrise_String);
        paText.setText(pa_String);

        //设置可见
        weatherLayout.setVisibility(View.VISIBLE);
        //开启自动服务
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
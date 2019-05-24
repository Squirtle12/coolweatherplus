package com.example.aaa.coolweather;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.aaa.coolweather.gson.AirNow;
import com.example.aaa.coolweather.gson.CommonWeather;
import com.example.aaa.coolweather.util.Utility;



import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public LocationClient mLocationClient=null;

    private String nowcity;
    private SharedPreferences.Editor editor;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mLocationClient=new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        LocationClientOption option=new LocationClientOption();
        //可选，是否需要地址信息，默认为不需要，即参数为false
        //如果开发者需要获得当前点的地址信息，此处必须为true
        option.setIsNeedAddress(true);

        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        mLocationClient.setLocOption(option);
        List<String>permissionList=new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)!=
                PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_PHONE_STATE)
                !=PackageManager.PERMISSION_GRANTED)
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=
                PackageManager.PERMISSION_GRANTED)
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (!permissionList.isEmpty())
        {
            String[]permissions=permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this,permissions,1);
        }else {


            mLocationClient.start();

        }

         editor = PreferenceManager.
                getDefaultSharedPreferences(MainActivity.this).edit();



    }

    public void jumpweather(String city,String county){

        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        //根据定位找到了当前所在的县，去跳转到当前县的
        String lastweathercity="";

        //上一次的区县有缓存(不用去判断空气质量的城市)
        //如果weather的区县没变，那空气质量的城市肯定也没变。
        if (prefs.getString("weather",null)!=null)
        {

            String weatherjson=prefs.getString("weather",null);
            CommonWeather weather=Utility.handleCommonWeatherResponse(weatherjson);
            lastweathercity=weather.getBasic().getCity();
        }
        //
        Intent intent=new Intent(this,WeatherActivity.class);
        //定位变了
        if (!lastweathercity.equals(city))
        {
            editor.putString("airnow",null);
            editor.putString("weather",null);
            editor.apply();
            intent.putExtra("airnow_city",city);
            intent.putExtra("weather_id",county);
        }

        //定位没变直接打开就行了
            startActivity(intent);
            finish();
    }

    /**
     * 回调函数
     * 判断权限是否获取到
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意所有权限才能使用定位功能", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                   mLocationClient.start();
                } else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }
    public class MyLocationListener extends BDAbstractLocationListener{
        @Override
        public void onReceiveLocation(BDLocation location){
            String city=location.getCity();
            String county=location.getDistrict();
            nowcity=city;
            jumpweather(nowcity,county);
        }
    }
    /**
     * 显示进度对话框
     */
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getApplicationContext());
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
//        progressDialog.show();
    }

    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

}

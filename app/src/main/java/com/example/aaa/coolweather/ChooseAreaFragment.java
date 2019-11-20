package com.example.aaa.coolweather;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aaa.coolweather.MainActivity;
import com.example.aaa.coolweather.R;
import com.example.aaa.coolweather.WeatherActivity;
import com.example.aaa.coolweather.db.City;
import com.example.aaa.coolweather.db.County;
import com.example.aaa.coolweather.db.Province;
import com.example.aaa.coolweather.service.AutoUpdateService;
import com.example.aaa.coolweather.util.HttpUtil;
import com.example.aaa.coolweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by aaa on 2019/3/9.
 */

public class ChooseAreaFragment extends Fragment {
    public static final int LEVEL_PROVINCE=0;
    public static final int LEVEL_CITY=1;
    public static final int LEVEL_COUNTY =2;


    private ProgressDialog progressDialog;
    private TextView titleText;
    private Button backButton;
    private ListView listView;
    private ArrayAdapter<String>adapter;
    private List<String>dataList=new ArrayList<>();
    /**
     * 省列表
     */
    private  List<Province>provinceList;
    /**
     * 市列表
     */
    private List<City>cityList;
    /**
    * 县列表
     */
    private List<County>countyList;
    /**
     * 选中的省份
     */
    private Province selectedProvince;
    /**
     * 选中的城市
     */
    private City selectedCity;
    /**
     * 当前选中的级别
     */
    private int currentLevel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.choose_area,container,false);
        titleText =(TextView)view.findViewById(R.id.title_text);
        backButton=(Button)view.findViewById(R.id.back_button);
        listView=(ListView)view.findViewById(R.id.list_view);
        adapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);
        return  view;
    }
    @Override
    public void  onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        //listview的点击函数
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(currentLevel==LEVEL_PROVINCE)
                {
                    //选中的是省级。
                    selectedProvince=provinceList.get(position);
                    //城市函数
                    queryCities();
                }
                else if(currentLevel==LEVEL_CITY)
                {
                    //选中的是市级
                    selectedCity=cityList.get(position);
                    //县级函数
                    queryCounties();
                }
                else if(currentLevel==LEVEL_COUNTY){
                    //选中了县级城市
                    String weatherid=countyList.get(position).getWeatherId();
                    String cityname=countyList.get(position).getCityName();
                    //int cityid=countyList.get(position).getCityId();
                    //判断碎片在哪个活动。如果是初始函数时，则跳转到天气Acitivity,
                    //intent跳转时要携带weatherid，也就是县级城市的id
                    if (getActivity()instanceof MainActivity) {
                        Intent intent = new Intent(getActivity(), WeatherActivity.class);
                        //跳转的过程将数据存入intent
                        intent.putExtra("weather_id", weatherid);
                        intent.putExtra("airnow_id", weatherid);
                        startActivity(intent);
                        //查一下意思
                        //销毁活动
                        getActivity().finish();
                    }
                    //如果是在显示县级城市的天气界面(weather Activtity)，则不需要再跳转了，
                    //关闭滑动菜单，可以下拉刷新，请求新天气的信息。
                    else if (getActivity()instanceof WeatherActivity){
                        WeatherActivity activity=(WeatherActivity)getActivity();
                        activity.drawerLayout.closeDrawers();
                        activity.swipeRefresh.setRefreshing(true);
                        activity.requestWeather(weatherid);
                        //空气质量得权限最高只能到城市，
                        // 所以即使到县级我们也直接传入城市的id。
                        activity.requestAir(cityname);

                    }
                }
            }
        });
        //后退按钮
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLevel==LEVEL_CITY)
                {
                    //省级函数
                    queryProvinces();
                }
                else if(currentLevel==LEVEL_COUNTY)
                {
                    //市级函数
                    queryCities();
                }
            }
        });
        //最初始时：省级函数
        queryProvinces();
    }
    /**
     *  查询全国所有的省，优先从数据库查询，如果没有查询到再去服务器上查询
     */
    private void queryProvinces(){
        titleText.setText("中国");
        //不可以后退
        backButton.setVisibility(View.GONE);
        //查询所有的省级数据
        provinceList= DataSupport.findAll(Province.class);
        if(provinceList.size()>0)
        {
            dataList.clear();
            for(Province province:provinceList){
                dataList.add(province.getProvinceName());
            }
            //更新adapter
            adapter.notifyDataSetChanged();

            listView.setSelection(0);
            currentLevel=LEVEL_PROVINCE;
        }
        else
        {
            String address="http://guolin.tech/api/china";
            //从服务器中读取
            queryFromServer(address,"province");
        }
    }
    /**
     *  查询选中省的全部市级数据，优先从数据库查询，如果没有查询到再去服务器上查询。
     */
    public void queryCities(){
        titleText.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        //加限制
        cityList=DataSupport.where("provinceid=?",String.valueOf(selectedProvince.getId())).find(City.class);
        if(cityList.size()>0)
        {
            dataList.clear();
            for(City city:cityList){
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel=LEVEL_CITY;
        }
        else {
            //
            int provinceCode=selectedProvince.getProvinceCode();
             String address="http://guolin.tech/api/china/"+provinceCode;

            //使用服务器查询
            queryFromServer(address,"city");
        }

    }
    /**
     * 查询选中市的全部县级数据，优先从数据库查询，如果没有查询到再去服务器上查询。
     */
    public void queryCounties()
    {
        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        countyList=DataSupport.where("cityid=?",String.valueOf(selectedCity.getId())).find(County.class);
        if(countyList.size()>0)
        {
            dataList.clear();
            for(County county:countyList){
                dataList.add(county.getConutyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel=LEVEL_COUNTY;
        }
        else
        {

           int provincecode=selectedProvince.getProvinceCode();
           int citycode=selectedCity.getCityCode();
            String address="http://guolin.tech/api/china/"+provincecode+"/"+citycode;

            //使用服务器
            queryFromServer(address,"county");
        }
    }
    /**
     * 根据传入的地址何类型从服务器上查询省市县数据
     */
    private void  queryFromServer(String address, final String type){
        //打开进度框
        //showProgressBar();
       showProgressDialog();
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            //
            @Override
            public void onFailure(Call call, IOException e) {
                //失败了，返回UI线程处理逻辑
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //关闭加载框
                       // closeProgressBar();
                       closeProgressDialog();
                        Toast.makeText(getContext(),"加载失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            //得到服务器返回的具体内容
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText=response.body().string();
                boolean result=false;
                if("province".equals(type))
                    result = Utility.handleProvinceResponse(responseText);
                else if("city".equals(type)){
                    result=Utility.handleCityResponse(responseText,selectedProvince.getId());
                }
                else if("county".equals(type)){
                    result=Utility.handleCountyResponse(responseText,selectedCity.getId(),selectedCity.getCityName());
                }
                //解析完数据牵涉到UI操作，因此必须要在主线程中调用。
                //数据库中有了，可以显示了。
                if(result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ///关闭进度框
                          //  closeProgressBar();
                           closeProgressDialog();
                            if("province".equals(type))
                                queryProvinces();
                            else if("city".equals(type)){
                               queryCities();
                            }
                            else if("county".equals(type)){
                               queryCounties();
                            }

                        }
                    });
                }
            }
        });
    }
    /**
     * 显示进度对话框
     */
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
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

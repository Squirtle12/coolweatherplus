package com.example.aaa.coolweather.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by aaa on 2019/3/3.
 */

public class HttpUtil {
    public static void sendOkHttpRequest(String address,okhttp3.Callback callback){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(address).build();
        //enqueue()方法的内部已经开好子线程了，在子线程中去执行HTTP请求，并将最终的请求结果回调
        //OKhttp.Callback当中，在调用它的时候要重写onResponse(),得到服务器返回的具体内容，重写
        //onFailure(),在这里对异常情况进行处理。不可以再这里进行UI操作。
        client.newCall(request).enqueue(callback);
    }
}

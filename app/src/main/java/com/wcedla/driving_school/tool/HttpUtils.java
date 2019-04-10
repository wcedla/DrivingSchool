package com.wcedla.driving_school.tool;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * HTTP请求工具类
 */
public class HttpUtils {

    /*根据网址联网获取参数*/
    public static void doHttpRequest(String url, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(callback);//enqueue方法内部已经实现了创建子线程处理网络连接服务，并把数据返回给回调函数。
    }

    public static String setParameterForUrl(String url,String... parameter)
    {
        StringBuilder urlWithParameter=new StringBuilder();
        urlWithParameter.append(url);
        urlWithParameter.append("?");
        for(int i=0;i<parameter.length/2;i++)
        {
            urlWithParameter.append(parameter[(i*2)]);
            urlWithParameter.append("=");
            urlWithParameter.append(parameter[(i*2)+1]);
            if(i!=(parameter.length/2)-1) {
                urlWithParameter.append("&");
            }
        }
        return urlWithParameter.toString();
    }

}

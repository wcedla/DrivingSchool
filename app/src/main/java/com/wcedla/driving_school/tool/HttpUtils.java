package com.wcedla.driving_school.tool;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

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

    public static void uploadFile(String url, List<File> fileList, String uploadFileName, Callback callback)
    {
        OkHttpClient okHttpClient=new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
        MultipartBody.Builder mBodybuilder=new MultipartBody.Builder().setType(MultipartBody.FORM);
        for(File file:fileList){
            if(file.exists()){
//                Log.i("imageName:",file.getName());//经过测试，此处的名称不能相同，如果相同，只能保存最后一个图片，不知道那些同名的大神是怎么成功保存图片的。
                mBodybuilder.addFormDataPart("upload", uploadFileName, RequestBody.create(MediaType.parse("image/jpg"), file));
            }
        }

        RequestBody requestBody =mBodybuilder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
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

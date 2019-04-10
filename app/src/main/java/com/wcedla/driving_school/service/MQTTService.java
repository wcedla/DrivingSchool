
package com.wcedla.driving_school.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.orhanobut.hawk.Hawk;
import com.orhanobut.logger.Logger;
import com.wcedla.driving_school.R;
import com.wcedla.driving_school.activity.AuthenticationStatusActivity;
import com.wcedla.driving_school.listener.MQTTMessageCallback;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MQTTService extends Service {


    //MQTT实例
    private MqttAndroidClient client;
    //MQTT配置
    private MqttConnectOptions conOpt;

    //MQTT服务器网址
    private String host = "tcp://192.168.191.1:1883";
    //连接到MQTT服务器的用户名
    private String userName = "wcedla";
    //连接到MQTT服务器的密码
    private String passWord = "wcedla";
    //要订阅的主题
    private String topic = Hawk.get("loginUser","");
    //客户端标识(保持唯一性否则新的连接将覆盖旧连接)
    private String clientId = "DrivingSchool-"+Hawk.get("loginUser","");
    //服务质量
    //0=至多一次
    //消息根据底层因特网协议网络尽最大努力进行传递。可能会丢失消息。
    //例如，将此服务质量与通信环境传感器数据一起使用。 对于是否丢失个别读取或是否稍后立即发布新的读取并不重要。
    //1=至少一次
    //保证消息抵达，但可能会出现重复。
    //2=刚好一次
    //确保只收到一次消息。
    //例如，将此服务质量与记帐系统一起使用。
    //重复或丢失消息可能会导致不便或收取错误费用。
    private int qos=1;
    //消息传递callback
    private MQTTMessageCallback mqttMessageCallback;




    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d("MQTT服务启动！");
        init();
    }

    private void publish(String topic,String msg) {
        Logger.d("MQTT的public方法执行"+msg);
        Integer qos = 1;
        Boolean retained = false;
        try {
            if (client != null) {
                client.publish(topic, msg.getBytes(), qos.intValue(), retained.booleanValue());
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        Logger.d("MQTT服务init");
        //建立MQTT连接
        client = new MqttAndroidClient(this, host, clientId);
        // 设置MQTT监听并且接受消息
        client.setCallback(mqttCallback);
        //配置连接配置参数
        conOpt = new MqttConnectOptions();
        // 清除服务器的本次连接记录，每次新建连接记录
        conOpt.setCleanSession(true);
        // 设置超时时间，单位：秒
        conOpt.setConnectionTimeout(10);
        // 心跳包发送间隔，单位：秒
        conOpt.setKeepAliveInterval(20);
        // 用户名
        conOpt.setUserName(userName);
        // 密码
        conOpt.setPassword(passWord.toCharArray());     //将字符串转换为字符串数组
        conOpt.setAutomaticReconnect(true);
        // 最后的遗嘱
        // MQTT本身就是为信号不稳定的网络设计的，所以难免一些客户端会无故的和Broker断开连接。
        //当客户端连接到Broker时，可以指定LWT，Broker会定期检测客户端是否有异常。
        //当客户端异常掉线时，Broker就往连接时指定的topic里推送当时指定的LWT消息。
        conOpt.setWill(topic, "已掉线".getBytes(), 0, false);
        //连接服务器
        doClientConnection();
//        }

    }


    @Override
    public void onDestroy() {
        Logger.d("MQTT服务停止，onDestroy");
        //停止服务
        stopForeground(true);
        stopSelf();

        //断开连接
        if (client != null) {
            try {
                client.disconnect();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }

    /**
     * 连接MQTT服务器
     */
    private void doClientConnection() {

        //客户端未连接到服务器并且网络可用
        if (!client.isConnected() && isNetworkConted()) {
            try {
                Logger.d("MQTT的connect执行");
                client.connect(conOpt, null, mqttActionListener);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

    }

    // MQTT是否连接成功
    private IMqttActionListener mqttActionListener = new IMqttActionListener() {

        @Override
        public void onSuccess(IMqttToken arg0) {
           Logger.d("MQTT连接成功！");
            try {
                // 订阅myTopic话题
                client.subscribe(topic, qos);
                changeToForground();
                Logger.d("MQTT成功订阅话题"+topic);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(IMqttToken arg0, Throwable arg1) {
            // 连接失败，重连
            doClientConnection();
            Logger.d("MQTT连接失败，重新连接"+arg1);
        }
    };

    // MQTT监听并且接受消息
    private MqttCallback mqttCallback = new MqttCallback() {

        //收到下发的消息时
        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {

            String messagePayload = new String(message.getPayload());
            mqttMessageCallback.MessageArrive(messagePayload);
            Logger.d("收到下发消息，转回调执行"+messagePayload);
        }

        //消息分发结束调用
        @Override
        public void deliveryComplete(IMqttDeliveryToken arg0) {

        }

        //失去连接时调用
        @Override
        public void connectionLost(Throwable arg0) {
            // 失去连接，重连
            doClientConnection();
            Logger.d("MQTT消息监听中连接丢失，执行重新连接"+arg0);
        }
    };

    /**
     * 判断网络是否连接
     */
    private boolean isNetworkConted() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            String name = info.getTypeName();
            Logger.d("MQTT当前网络名称：" + name);
            return true;
        } else {
            Logger.d("MQTT没有可用网络");
            return false;
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return new MQTTBinder();
    }


    public class MQTTBinder extends Binder {

        public void setMessageCallback(MQTTMessageCallback messageCallback)
        {
            mqttMessageCallback=messageCallback;
            Logger.d("MQTT成功设置回调");
        }

        public void publishMessage(String topic,String message)
        {
            publish(topic,message);
        }

        public void stopMQTT()
        {
            client.unregisterResources();
            client.close();
            stopSelf();
        }

        public void setNotification(String message)
        {
            notification(message);
        }
    }

    public void changeToForground() {

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, new Intent(this, MQTTService.class), PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(MQTTService.this, "1")
                .setContentTitle("wcedla")
                .setContentText("看到这个说明你看到bug了")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(0,notification);
        Logger.d("MQTT开启前台服务");
    }

    private void notification(String message)
    {
        NotificationManager notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent authStatusIntent=new Intent(this, AuthenticationStatusActivity.class);
        authStatusIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putString("userName", Hawk.get("loginUser",""));
        authStatusIntent.putExtras(bundle);
        PendingIntent authStatusPendingIntent=PendingIntent.getActivity(this,12,authStatusIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(MQTTService.this, "1")
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(authStatusPendingIntent)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .build();
        notificationManager.notify(1,notification);
    }




}
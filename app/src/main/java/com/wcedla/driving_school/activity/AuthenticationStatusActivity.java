package com.wcedla.driving_school.activity;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;
import com.orhanobut.logger.Logger;
import com.wcedla.driving_school.R;
import com.wcedla.driving_school.bean.AuthCheckBean;
import com.wcedla.driving_school.listener.MQTTMessageCallback;
import com.wcedla.driving_school.service.MQTTService;
import com.wcedla.driving_school.tool.HttpUtils;
import com.wcedla.driving_school.tool.JsonUtils;
import com.wcedla.driving_school.tool.ToolUtils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.wcedla.driving_school.constant.Config.AUTHENTICATION_CANCEL_URL;
import static com.wcedla.driving_school.constant.Config.AUTHENTICSTION_CHECK_URL;

public class AuthenticationStatusActivity extends AppCompatActivity implements MQTTMessageCallback {

    @BindView(R.id.auth_status_title)
    TextView authStatusTitle;
    @BindView(R.id.auth_status_name_text)
    TextView authStatusNameText;
    @BindView(R.id.auth_status_no_text)
    TextView authStatusNoText;
    @BindView(R.id.auth_status_type_text)
    TextView authStatusTypeText;
    @BindView(R.id.auth_status_cancel)
    Button authStatusCancel;
    @BindView(R.id.auth_status_layout)
    ConstraintLayout authStatusLayout;

    String userName = "";
    AuthCheckBean authCheckBean;

    MQTTService.MQTTBinder mqttBinder = null;
    MQTTServiceConnection mqttServiceConnection;
    @BindView(R.id.auth_status_user_text)
    TextView authStatusUserText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolUtils.setNavigationBarStatusBarTranslucent(this, true, false);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            userName = bundle.getString("userName");
            Logger.d("认证检查获取用户名" + userName);
        }
        checkAuthStatus();
        setContentView(R.layout.activity_authentication_status);
        ButterKnife.bind(this);



        mqttServiceConnection = new MQTTServiceConnection();
        Intent MQTTServiceIntent = new Intent(AuthenticationStatusActivity.this, MQTTService.class);
        startService(MQTTServiceIntent);
        bindService(MQTTServiceIntent, mqttServiceConnection, BIND_AUTO_CREATE);
    }

//    @Override
//    public void finish() {
//        unbindService(mqttServiceConnection);
//        super.finish();
//
//    }

    @Override
    protected void onDestroy() {
        unbindService(mqttServiceConnection);
        super.onDestroy();

    }

    @OnClick(R.id.auth_status_cancel)
    public void cancelClick() {
//        if (authStatusCancel.getText().toString().length() > 4) {
            Logger.d("用户重新提交认证信息，跳转到认证信息提交活动");
            Intent authIntent = new Intent(AuthenticationStatusActivity.this, AuthenticationActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("nickName", userName);
            authIntent.putExtras(bundle);
            startActivity(authIntent);
            finish();
//        } else {
//            Logger.d("用户取消认证，跳转到认证提交页面重新提交认证信息");
//            cancelAuth();
//        }
    }

    private void checkAuthStatus() {
        String url = HttpUtils.setParameterForUrl(AUTHENTICSTION_CHECK_URL, "userName", userName);
        HttpUtils.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Logger.d("认证检查，网络接口回调onfailed");
                        Toast.makeText(AuthenticationStatusActivity.this, "很抱歉，出现了一些错误！", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseString = response.body().string();
                authCheckBean = JsonUtils.getAuthCheckStatus(responseString);
                Logger.d("认证检查获取服务器返回结果" + authCheckBean.getStatus());
                if (authCheckBean.getStatus().equals("审核通过")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Logger.d("用户认证审核通过");
                            authStatusTitle.setText("审核通过");
                            authStatusCancel.setText("审核已经通过");
                            authStatusCancel.setEnabled(false);
                            Hawk.put("loginType", authCheckBean.getType());
                            Hawk.put("loginNo", authCheckBean.getNo());
                            Toast.makeText(AuthenticationStatusActivity.this, "审核通过", Toast.LENGTH_SHORT).show();
                            Intent mainShowIntent=new Intent(AuthenticationStatusActivity.this,MainShowActivity.class);
                            startActivity(mainShowIntent);
                            finish();
                        }
                    });
                } else if (authCheckBean.getStatus().equals("审核未通过")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Logger.d("用户认证审核未通过");
                            authStatusTitle.setText("审核未通过");
                            authStatusUserText.setText(authCheckBean.getUser());
                            authStatusNameText.setText(authCheckBean.getName());
                            authStatusNoText.setText(authCheckBean.getNo());
                            authStatusTypeText.setText(authCheckBean.getType());
                            authStatusCancel.setText("重新提交审核信息");
                            authStatusLayout.setVisibility(View.VISIBLE);
                        }
                    });

                } else if (authCheckBean.getStatus().equals("审核中")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Logger.d("用户认证审核中");
                            authStatusTitle.setText("审核中");
                            authStatusUserText.setText(authCheckBean.getUser());
                            authStatusNameText.setText(authCheckBean.getName());
                            authStatusNoText.setText(authCheckBean.getNo());
                            authStatusTypeText.setText(authCheckBean.getType());
                            authStatusCancel.setText("重新提交审核信息");
                            authStatusLayout.setVisibility(View.VISIBLE);
                        }
                    });

                } else if (authCheckBean.getStatus().equals("noData")) {
                    Logger.d("用户认证还未提交，跳转到认证提交活动" + userName);
                    Hawk.delete("loginUser");
                    Intent loginIntent=new Intent(AuthenticationStatusActivity.this,LoginActivity.class);
                    startActivity(loginIntent);
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Logger.d("用户认证返回数据不满足任何条件");
                            Toast.makeText(AuthenticationStatusActivity.this, "很抱歉出现了一些问题", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });

                }
            }
        });
    }

//    private void cancelAuth() {
//        String url = HttpUtils.setParameterForUrl(AUTHENTICATION_CANCEL_URL, "userName", userName);
//        HttpUtils.doHttpRequest(url, new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Logger.d("用户认证取消认证网络接口回调onfailed");
//                        Toast.makeText(AuthenticationStatusActivity.this, "取消失败", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String responseString = response.body().string();
//                int result = JsonUtils.getAuthenticationStatus(responseString);
//                if (result == -1) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Logger.d("用户认证取消认证成功跳转到认证提交活动" + userName);
//                            Toast.makeText(AuthenticationStatusActivity.this, "取消成功", Toast.LENGTH_SHORT).show();
//                            Intent authIntent = new Intent(AuthenticationStatusActivity.this, AuthenticationActivity.class);
//                            Bundle bundle = new Bundle();
//                            bundle.putString("userName", userName);
//                            authIntent.putExtras(bundle);
//                            startActivity(authIntent);
//                            finish();
//                        }
//                    });
//                } else {
//                    Logger.d("用户认证取消失败");
//                    Toast.makeText(AuthenticationStatusActivity.this, "取消失败", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }

    /**
     * MQTT收到消息回调
     *
     * @param message
     */
    @Override
    public void MessageArrive(String message) {
        String result = JsonUtils.getMQTTStatus(message);
        if (result.equals("审核通过")) {
            authStatusTitle.setText("审核通过");
            authStatusCancel.setText("审核通过");
            authStatusCancel.setEnabled(false);
            mqttBinder.setNotification("您的身份审核已经通过了，赶紧去登陆系统吧。");
            Hawk.put("loginType", authCheckBean.getType());
            Hawk.put("loginNo", authCheckBean.getNo());
            Intent mainShowIntent=new Intent(AuthenticationStatusActivity.this,MainShowActivity.class);
            startActivity(mainShowIntent);
            finish();
        } else if (result.equals("审核未通过")) {
            authStatusTitle.setText("审核未通过");
            authStatusCancel.setText("重新提交审核信息");
            mqttBinder.setNotification("的身份审核未通过，请重新提交审核信息。");
        }
    }

    class MQTTServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Logger.d("MQTTServiceConnection连接");
            mqttBinder = (MQTTService.MQTTBinder) service;
            mqttBinder.setMessageCallback(AuthenticationStatusActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }


}

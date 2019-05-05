package com.wcedla.driving_school.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.orhanobut.logger.Logger;
import com.wcedla.driving_school.R;
import com.wcedla.driving_school.adapter.AuthInfoShowAdapter;
import com.wcedla.driving_school.bean.AuthInfoBean;
import com.wcedla.driving_school.customview.CustomProgressDialog;
import com.wcedla.driving_school.service.MQTTService;
import com.wcedla.driving_school.tool.HttpUtils;
import com.wcedla.driving_school.tool.ToolUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.wcedla.driving_school.constant.Config.AUTHENTICAtion_ALL_URL;
import static com.wcedla.driving_school.constant.Config.USERNAME_MAX_LENGTH;
import static com.wcedla.driving_school.constant.Config.USERNAME_REGEX;

public class ShowAuthInfoActivity extends AppCompatActivity {

    @BindView(R.id.auth_check_all)
    RadioButton authCheckAll;
    @BindView(R.id.auth_check_doing)
    RadioButton authCheckDoing;
    @BindView(R.id.auth_check_failed)
    RadioButton authCheckFailed;
    @BindView(R.id.auth_check_success)
    RadioButton authCheckSuccess;
    @BindView(R.id.auth_check_radio_group)
    RadioGroup authCheckRadioGroup;
    @BindView(R.id.auth_check_search)
    EditText authCheckSearch;
    @BindView(R.id.auth_check_search_btn)
    Button authCheckSearchBtn;
    @BindView(R.id.auth_check_recycler_view)
    RecyclerView authCheckRecyclerView;
    @BindView(R.id.auth_check_root)
    ConstraintLayout authCheckRoot;

    CustomProgressDialog progressDialog;
    MQTTService.MQTTBinder mqttBinder = null;
    MQTTServiceConnection mqttServiceConnection;

    List<AuthInfoBean.AuthBean> authBeanList = new ArrayList<>();
    List<AuthInfoBean.AuthBean> doingBeanList;
    List<AuthInfoBean.AuthBean> successBeanList;
    List<AuthInfoBean.AuthBean> failedBeanList;
    List<AuthInfoBean.AuthBean> userBeanList;
    int clickId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolUtils.setNavigationBarStatusBarTranslucent(this, false, false);
        setContentView(R.layout.activity_show_auth_info);
        ButterKnife.bind(this);
        progressDialog = CustomProgressDialog.create(this, "正在获取认证数据...", false);
        getAllAuthInfo();
        progressDialog.showProgressDialog();
        setInputFilter(authCheckSearch,USERNAME_REGEX,USERNAME_MAX_LENGTH);
        clickId=0;
        mqttServiceConnection = new MQTTServiceConnection();
        Intent MQTTServiceIntent = new Intent(ShowAuthInfoActivity.this, MQTTService.class);
        bindService(MQTTServiceIntent, mqttServiceConnection, BIND_AUTO_CREATE);
    }

    @OnClick(R.id.auth_check_back)
    public void backClick() {
        finish();
    }

    @OnClick(R.id.auth_check_search)
    public void searchEditTextClick()
    {
        showInputMethod(authCheckSearch);
    }

    @OnClick(R.id.auth_check_root)
    public void outLayoutClick(View view)
    {
        hideInputMethod(view);
    }

    public void recyclerViewHideInput()
    {
        hideInputMethod(authCheckRecyclerView);
    }

    @OnClick(R.id.auth_check_search_btn)
    public void doSearchClick()
    {
        clickId=4;
        hideInputMethod(authCheckSearchBtn);
        String user=authCheckSearch.getText().toString().trim();
        if(user.length()<3)
        {
            Toast.makeText(ShowAuthInfoActivity.this,"请输入正确的用户名！",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Logger.d("用户名筛选");
                userBeanList=new ArrayList<>();
                for(AuthInfoBean.AuthBean authBean:authBeanList)
                {
                    if (authBean.getRealName().equals(user))
                    {
                        userBeanList.add(authBean);
                    }
                }
//                authCheckRecyclerView.scrollToPosition(0);
//                ((AuthInfoShowAdapter)authCheckRecyclerView.getAdapter()).refreshData(userBeanList);
                setAdapterData(userBeanList);
        }
    }

    @OnClick({R.id.auth_check_doing,R.id.auth_check_all,R.id.auth_check_success,R.id.auth_check_failed})
    public void doingCheck(View view) {
        hideInputMethod(view);
        authCheckSearch.setText("");
        switch (view.getId()) {
            case R.id.auth_check_all:

                if(authCheckAll.isChecked())
                {
                    if (clickId!=0) {
                        Logger.d("我再点击全部"+authCheckAll.isChecked());
                        clickId=0;
//                        authCheckRecyclerView.scrollToPosition(0);
//                        ((AuthInfoShowAdapter) authCheckRecyclerView.getAdapter()).refreshData(authBeanList);
                        setAdapterData(authBeanList);
                    }
                }
                break;
            case R.id.auth_check_doing:
                if(authCheckDoing.isChecked()) {
                    if (clickId != 1) {
                        clickId=1;
                        Logger.d("我再点击审核中"+authCheckDoing.isChecked());
                        doingBeanList = new ArrayList<>();
                        for (AuthInfoBean.AuthBean authBean : authBeanList) {
                            if (authBean.getAuthStatus().equals("审核中")) {
                                doingBeanList.add(authBean);
                            }
                        }
//                        authCheckRecyclerView.scrollToPosition(0);
//                        ((AuthInfoShowAdapter) authCheckRecyclerView.getAdapter()).refreshData(doingBeanList);
                        setAdapterData(doingBeanList);
                    }
                }
                break;
            case R.id.auth_check_success:
                if(authCheckSuccess.isChecked()) {
                    if (clickId != 2) {
                        clickId=2;
                        Logger.d("我再点击审核通过"+authCheckSuccess.isChecked());
                        successBeanList = new ArrayList<>();
                        for (AuthInfoBean.AuthBean authBean : authBeanList) {
                            if (authBean.getAuthStatus().equals("审核通过")) {
                                successBeanList.add(authBean);
                            }
                        }
//                        authCheckRecyclerView.scrollToPosition(0);
//                        ((AuthInfoShowAdapter) authCheckRecyclerView.getAdapter()).refreshData(successBeanList);
                        setAdapterData(successBeanList);
                    }
                }
                break;
            case R.id.auth_check_failed:

                if(authCheckFailed.isChecked()) {
                    if (clickId != 3) {
                        clickId=3;
                        Logger.d("我再点击审核未通过"+authCheckFailed.isChecked());
                        failedBeanList = new ArrayList<>();
                        for (AuthInfoBean.AuthBean authBean : authBeanList) {
                            if (authBean.getAuthStatus().equals("审核未通过")) {
                                failedBeanList.add(authBean);
                            }
                        }
//                        authCheckRecyclerView.scrollToPosition(0);
//                        ((AuthInfoShowAdapter) authCheckRecyclerView.getAdapter()).refreshData(failedBeanList);
                        setAdapterData(failedBeanList);
                    }
                }
                break;
            default:
                break;
        }
    }

    public void setAuthStatusMQTT(String topic,String message)
    {
//        String notificationMessage;
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("status",message);
        mqttBinder.publishMessage(topic,jsonObject.toString());
//        if(message.equals("审核通过"))
//        {
//            notificationMessage=topic+"您的身份审核已经通过了，赶紧去登陆系统吧。";
//        }
//        else
//        {
//            notificationMessage=topic+"您的身份审核未通过，请重新提交审核信息。";
//        }
//        mqttBinder.setNotification(notificationMessage);
    }


    class MQTTServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Logger.d("管理员审核界面MQTTServiceConnection连接");
            mqttBinder = (MQTTService.MQTTBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    public void getAllAuthInfo() {
        HttpUtils.doHttpRequest(AUTHENTICAtion_ALL_URL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ShowAuthInfoActivity.this, "获取认证数据失败！", Toast.LENGTH_SHORT).show();
                        progressDialog.cancelProgressDialog();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseString = response.body().string();
                AuthInfoBean authInfoBean = new Gson().fromJson(responseString, AuthInfoBean.class);
                authBeanList.clear();
                for (AuthInfoBean.AuthBean authBean : authInfoBean.getAuth()) {
                    authBeanList.add(authBean);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AuthInfoShowAdapter authInfoShowAdapter = new AuthInfoShowAdapter(ShowAuthInfoActivity.this, authBeanList);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShowAuthInfoActivity.this);
                        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        authCheckRecyclerView.setLayoutManager(linearLayoutManager);
                        authCheckRecyclerView.setAdapter(authInfoShowAdapter);
                        progressDialog.cancelProgressDialog();
                    }
                });

            }
        });
    }

    private void setAdapterData(List<AuthInfoBean.AuthBean> authBeanList)
    {
        AuthInfoShowAdapter authInfoShowAdapter = new AuthInfoShowAdapter(ShowAuthInfoActivity.this, authBeanList);

        authCheckRecyclerView.setAdapter(authInfoShowAdapter);
    }

    /**
     * 为Edittext设置筛选器
     *
     * @param editText 指定那个EditText
     * @param regEx    正则表达式字符串
     */
    private void setInputFilter(EditText editText, final String regEx, int maxLength) {
        editText.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (!Pattern.matches(regEx, source.toString())) {
                    return "";
                }
                return null;
            }
        }, new InputFilter.LengthFilter(maxLength)});
    }

    /**
     * 弹出输入法
     *
     * @param targetView 目标控件
     */
    private void showInputMethod(View targetView) {
        targetView.setFocusable(true);
        targetView.setFocusableInTouchMode(true);
        targetView.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(targetView, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 隐藏输入法
     *
     * @param targetView 目标控件
     */
    private void hideInputMethod(View targetView) {
        authCheckSearch.setFocusable(false);
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            if (inputMethodManager.isActive()) {
                inputMethodManager.hideSoftInputFromWindow(targetView.getWindowToken(), 0);
            }
        }
    }

    @Override
    protected void onDestroy() {
        unbindService(mqttServiceConnection);
        super.onDestroy();
    }
}

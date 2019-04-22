package com.wcedla.driving_school.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;
import com.orhanobut.logger.Logger;
import com.wcedla.driving_school.R;
import com.wcedla.driving_school.customview.CustomProgressDialog;
import com.wcedla.driving_school.tool.CodeUtils;
import com.wcedla.driving_school.tool.HttpUtils;
import com.wcedla.driving_school.tool.JsonUtils;
import com.wcedla.driving_school.tool.ToolUtils;

import java.io.IOException;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.wcedla.driving_school.constant.Config.AUTHENTCATION_URL;
import static com.wcedla.driving_school.constant.Config.EMAIL_CHECK_REGEX;
import static com.wcedla.driving_school.constant.Config.INPUTMETHOD_DELAY;
import static com.wcedla.driving_school.constant.Config.USERNAME_REGEX;
import static com.wcedla.driving_school.constant.Config.YZM_MAX_LENGTH;
import static com.wcedla.driving_school.constant.Config.YZM_REGEX;

public class AuthenticationActivity extends AppCompatActivity {


    @BindView(R.id.authentication_radio_group)
    RadioGroup authenticationRadioGroup;
    @BindView(R.id.authentication_name_text)
    EditText authenticationNameText;
    @BindView(R.id.authentication_no_text)
    EditText authenticationNoText;
    @BindView(R.id.authentication_yzm_text)
    EditText authenticationYzmText;
    @BindView(R.id.authentication_yzm_img)
    ImageView authenticationYzmImg;
    @BindView(R.id.authentication_btn)
    Button authenticationBtn;
    @BindView(R.id.authentication_layout)
    ConstraintLayout authenticationLayout;

    @BindView(R.id.radio_coach)
    RadioButton radioCoach;
    @BindView(R.id.radio_student)
    RadioButton radioStudent;
    @BindView(R.id.authentication_no_head)
    TextView authenticationNoHead;

    String userName;
    String password;
    String email;

    CustomProgressDialog customProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolUtils.setNavigationBarStatusBarTranslucent(this, true, false);
        setContentView(R.layout.activity_authentication);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userName = bundle.getString("nickName", "null");
            password = bundle.getString("password", "null");
            email = bundle.getString("email", "null");
            Logger.d("用户认证提交活动获取用户名" + userName);
        } else {
            userName = "";
            password = "";
            email = "";
        }
        authenticationYzmImg.setImageBitmap(CodeUtils.getInstance().createBitmap());
        radioCoach.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    authenticationNoHead.setText("工号:");
                    authenticationNoText.setText("");
                    authenticationNoText.setHint("填写您的工号");
                }
            }
        });

        radioStudent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    authenticationNoHead.setText("学员号:");
                    authenticationNoText.setText("");
                    authenticationNoText.setHint("填写您的学员号");
                }
            }
        });

        setInputFilter(authenticationNameText, USERNAME_REGEX, 5);
        setInputFilter(authenticationNoText, EMAIL_CHECK_REGEX, 11);
        setInputFilter(authenticationYzmText, YZM_REGEX, YZM_MAX_LENGTH);
    }

    @OnClick(R.id.authentication_name_text)
    public void nameClick() {
        authenticationNameText.setFocusable(true);
        showInputMethod(authenticationNameText);
    }

    @OnClick(R.id.authentication_no_text)
    public void noClick() {
        authenticationNoText.setFocusable(true);
        showInputMethod(authenticationNoText);
    }

    @OnClick(R.id.authentication_yzm_text)
    public void yzmClick() {
        authenticationYzmText.setFocusable(true);
        showInputMethod(authenticationYzmText);
    }

    @OnClick(R.id.authentication_layout)
    public void layoutClick() {
        hideInputMethod(authenticationLayout);
    }

    @OnClick(R.id.authentication_yzm_img)
    public void yzmImgClick() {
        authenticationYzmImg.setImageBitmap(CodeUtils.getInstance().createBitmap());
    }

    @OnClick(R.id.authentication_btn)
    public void sendClick() {
        hideInputMethod(authenticationBtn);
        String type = "", name = "", no = "", yzm = "";
        if (authenticationRadioGroup.getCheckedRadioButtonId() == radioCoach.getId()) {
            type = "教练";
        } else if (authenticationRadioGroup.getCheckedRadioButtonId() == radioStudent.getId()) {
            type = "学员";
        }
        name = authenticationNameText.getText().toString().trim();
        no = authenticationNoText.getText().toString().trim();
        yzm = authenticationYzmText.getText().toString().trim();
        if (name.length() >= 2 && no.length() == 11 && yzm.length() == 4) {
            if (yzm.toLowerCase().equals(CodeUtils.getInstance().getCode().toLowerCase())) {
                customProgressDialog = CustomProgressDialog.create(AuthenticationActivity.this, "正在发送...", false);
                customProgressDialog.showProgressDialog();
                String url = HttpUtils.setParameterForUrl(AUTHENTCATION_URL, "nickName", userName, "type", type, "password", password, "email", email, "no", no, "realName", name);
                HttpUtils.doHttpRequest(url, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Logger.d("用户认证提交活动网络回调onfailed");
                                customProgressDialog.cancelProgressDialog();
                                Toast.makeText(AuthenticationActivity.this, "发送认证失败！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = response.body().string();
                        int result = JsonUtils.getAuthenticationStatus(responseData);
                        if (result == -1) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Logger.d("用户认证提交活动提交成功,跳转认证检查活动" + userName);
                                    customProgressDialog.cancelProgressDialog();
                                    Toast.makeText(AuthenticationActivity.this, "发送认证成功！", Toast.LENGTH_SHORT).show();
                                    String loginUser=Hawk.get("loginUser","");
                                    if(loginUser.length()<3)
                                    {
                                        Intent loginIntent=new Intent(AuthenticationActivity.this,LoginActivity.class);
                                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(loginIntent);
                                        finish();
                                    }
                                    else {
                                        Intent checkAuthIntent = new Intent(AuthenticationActivity.this, AuthenticationStatusActivity.class);
                                        checkAuthIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("userName", userName);
                                        checkAuthIntent.putExtras(bundle);
                                        startActivity(checkAuthIntent);
                                        finish();
                                    }
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Logger.d("用户认证提交活动提交失败" + responseData);
                                    customProgressDialog.cancelProgressDialog();
                                    Toast.makeText(AuthenticationActivity.this, "发送认证失败！", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            } else {
                Toast.makeText(AuthenticationActivity.this, "验证码不正确！", Toast.LENGTH_SHORT).show();
                authenticationYzmText.setText("");
                yzmImgClick();
            }
        } else {
            Toast.makeText(AuthenticationActivity.this, "请输入正确的内容！", Toast.LENGTH_SHORT).show();
        }

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
        authenticationNameText.setFocusable(false);
        authenticationNoText.setFocusable(false);
        authenticationYzmText.setFocusable(false);
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            if (inputMethodManager.isActive()) {
                inputMethodManager.hideSoftInputFromWindow(targetView.getWindowToken(), 0);
            }
        }
    }

    /**
     * 为Edittext设置筛选器,加这个LengthFilter筛选是为了控制最长输入长度的
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
     * 简单封装的Handler延迟任务方法
     *
     * @param runnable    需要延迟运行的任务
     * @param delayMillis 延迟时间（毫秒）
     */
    private void delayTask(Runnable runnable, long delayMillis) {
        new Handler().postDelayed(runnable, delayMillis);
    }


    //跳转其他活动或者后台返回时能够重新自动弹出输入法框
    @Override
    protected void onResume() {
        super.onResume();
        //延迟打开输入法，不用用户点击弹出输入法，增加易用性
        delayTask(new Runnable() {
            @Override
            public void run() {
                showInputMethod(authenticationNameText);

            }
        }, INPUTMETHOD_DELAY);
    }


}

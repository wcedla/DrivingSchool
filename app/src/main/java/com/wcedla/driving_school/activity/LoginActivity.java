package com.wcedla.driving_school.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.logger.Logger;
import com.wcedla.driving_school.R;
import com.wcedla.driving_school.customview.CustomProgressDialog;
import com.wcedla.driving_school.tool.HttpUtils;
import com.wcedla.driving_school.tool.JsonUtils;
import com.wcedla.driving_school.tool.ToolUtils;

import java.io.IOException;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.wcedla.driving_school.constant.Config.DIALOG_DELAY;
import static com.wcedla.driving_school.constant.Config.INPUTMETHOD_DELAY;
import static com.wcedla.driving_school.constant.Config.LOGIN_URL;
import static com.wcedla.driving_school.constant.Config.PASSWORD_MAX_LENGTH;
import static com.wcedla.driving_school.constant.Config.PASSWORD_MIN_LENGTH;
import static com.wcedla.driving_school.constant.Config.PASSWORD_REGEX;
import static com.wcedla.driving_school.constant.Config.USERNAME_MAX_LENGTH;
import static com.wcedla.driving_school.constant.Config.USERNAME_MIN_LENGTH;
import static com.wcedla.driving_school.constant.Config.USERNAME_REGEX;


public class LoginActivity extends AppCompatActivity {

    //    登录界面布局
    @BindView(R.id.login_layout)
    ConstraintLayout loginLayout;

    //    账号输入框
    @BindView(R.id.login_username)
    EditText userNameText;

    //    密码输入框
    @BindView(R.id.login_pwd)
    EditText passwordText;

    @BindView(R.id.login_btn)
    Button loginButton;

    //    忘记密码按钮
    @BindView(R.id.forget_password_btn)
    TextView forgetPasswordButton;

    //    注册按钮
    @BindView(R.id.register_btn)
    TextView registerButton;


    CustomProgressDialog loginProgressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 避免从桌面启动程序后，会重新实例化入口类的activity
        if (!this.isTaskRoot()) { // 当前类不是该Task的根部，那么之前启动
            Intent intent = getIntent();
            if (intent != null) {
                String action = intent.getAction();
                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) { // 当前类是从桌面启动的
                    finish(); // finish掉该类，直接打开该Task中现存的Activity
                    return;
                }
            }
        }


        String loginUser = Hawk.get("loginUser", "");
        if (loginUser.length() > 2) {
            if (loginUser.equals("admin")) {
                Logger.d("获取到之前登陆的是管理员账号，跳转到管理员" + loginUser);
                Intent demo = new Intent(this, AdminMainActivity.class);
                startActivity(demo);
                finish();
            } else {
                Logger.d("用户登录活动读取到用户之前已经成功登陆过，准备跳转到认证检查活动" + loginUser);
                Intent authIntent = new Intent(LoginActivity.this, AuthenticationStatusActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("userName", loginUser);
                authIntent.putExtras(bundle);
                startActivity(authIntent);
                finish();
            }
        }
        //透明状态栏和导航栏
        ToolUtils.setNavigationBarStatusBarTranslucent(this, false, true);
        setContentView(R.layout.activity_login);
        //ToolUtils.initLogger(LOG_TAG, DEBUG);//初始化日志打印工具
        ButterKnife.bind(this);//绑定BUtterKnife
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);//开局就打开输入法
        //设置登录界面的背景图片，并毛玻璃化
        RequestOptions options = new RequestOptions()
                .override(getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels)
                .transform(new MultiTransformation(new BlurTransformation(5, 3), new CenterCrop()));
        Glide.with(this)
                .load(R.drawable.mbh)
                .apply(options)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                        loginLayout.setBackground(resource);
                    }
                });
        //延迟打开输入法，不用用户点击弹出输入法，增加易用性
        delayTask(new Runnable() {
            @Override
            public void run() {
                showInputMethod(userNameText);

            }
        }, INPUTMETHOD_DELAY);

        //设置用户名输入框输入筛选，拒绝标点符号的输入
        setInputFilter(userNameText, USERNAME_REGEX, USERNAME_MAX_LENGTH);

        //设置密码输入框输入筛选，允许字母输入
        setInputFilter(passwordText, PASSWORD_REGEX, PASSWORD_MAX_LENGTH);


    }

    //登录布局点击事件，主要是为了点击时清除账号密码输入框的焦点，并关闭输入法
    @OnClick(R.id.login_layout)
    public void outSideClick() {
        hideInputMethod(loginLayout);
    }

    /**
     * 登录按钮点击事件
     */
    @OnClick(R.id.login_btn)
    public void loginButtonClick() {
//        Intent demoIntent=new Intent(LoginActivity.this,AuthenticationStatusActivity.class);
//        startActivity(demoIntent);
//        finish();
        final String inputUserName = userNameText.getText().toString().trim();
        String inputPassword = passwordText.getText().toString().trim();
        hideInputMethod(loginButton);
        if (inputUserName.length() >= USERNAME_MIN_LENGTH && inputPassword.length() >= PASSWORD_MIN_LENGTH) {
            loginProgressDialog = CustomProgressDialog.create(LoginActivity.this, "正在登陆...", false);
            loginProgressDialog.showProgressDialog();
            if ("admin".equals(inputUserName) && "adminwcedla".equals(inputPassword)) {
                Intent adminIntent = new Intent(LoginActivity.this, AdminMainActivity.class);
                Hawk.put("loginUser", inputUserName);
                loginProgressDialog.cancelProgressDialog();
                startActivity(adminIntent);
                finish();
            } else {

                inputPassword = ToolUtils.MD5Encrypted(inputPassword);
                String loginCheckUrl = HttpUtils.setParameterForUrl(LOGIN_URL, "userName", inputUserName, "password", inputPassword);
                //String loginCheckUrl = "http://192.168.191.1:8080/DrivingSchoolServer/UserLogin?userName=" + inputUserName + "&password=" + inputPassword;
                HttpUtils.doHttpRequest(loginCheckUrl, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Logger.d("用户登录网络onFailed，可能是没有网络！");
                                loginProgressDialog.cancelProgressDialog();
                                Toast.makeText(LoginActivity.this, "很抱歉！出现了意料之外的情况（可能是服务器问题！）", Toast.LENGTH_LONG).show();
                            }
                        });

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseString = response.body().string();
                        int result = JsonUtils.getLoginResult(responseString);
                        Logger.d("登录结果是：" + responseString + "处理后的结果是:" + result);
                        switch (result) {
                            case -1:
                                Hawk.put("loginUser", inputUserName);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        delayTask(new Runnable() {
                                            @Override
                                            public void run() {
                                                Logger.d("用户登录成功，并且审核通过，准备跳转主界面活动" + inputUserName);
                                                loginProgressDialog.cancelProgressDialog();
                                                Intent mainIntent = new Intent(LoginActivity.this, MainShowActivity.class);
                                                startActivity(mainIntent);
                                                finish();
                                            }
                                        }, DIALOG_DELAY);

                                    }
                                });
                                break;
                            case 0:
                                Hawk.put("loginUser", inputUserName);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        delayTask(new Runnable() {
                                            @Override
                                            public void run() {
                                                Logger.d("用户登录成功，但是审核未通过或者正在审核，准备跳转审核装填查看界面" + inputUserName);
                                                loginProgressDialog.cancelProgressDialog();
                                                Intent authIntent = new Intent(LoginActivity.this, AuthenticationStatusActivity.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putString("userName", inputUserName);
                                                authIntent.putExtras(bundle);
                                                startActivity(authIntent);
                                                finish();
                                            }
                                        }, DIALOG_DELAY);
                                    }
                                });
                                break;

                            case 1:
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        delayTask(new Runnable() {
                                            @Override
                                            public void run() {
                                                Logger.d("用户登录账号或者密码错误！");
                                                loginProgressDialog.cancelProgressDialog();
                                                Toast.makeText(LoginActivity.this, "登录失败，请检查输入的账号或密码是否正确！", Toast.LENGTH_SHORT).show();
                                            }
                                        }, DIALOG_DELAY);
                                    }
                                });
                                break;
                            default:
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        delayTask(new Runnable() {
                                            @Override
                                            public void run() {
                                                Logger.d("用户登录返回的json不满足任何条件");
                                                loginProgressDialog.cancelProgressDialog();
                                                Toast.makeText(LoginActivity.this, "请检查输入的内容，在处理输入内容时出现了一些问题！", Toast.LENGTH_SHORT).show();
                                            }
                                        }, DIALOG_DELAY);
                                    }
                                });

                        }
                    }
                });
            }
        } else {
            Toast.makeText(LoginActivity.this, "请输入账户和密码！", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 账户输入框点击事件，主要是为了点击时能够弹出输入法
     */
    @OnClick(R.id.login_username)
    public void userNameClick() {
        showInputMethod(userNameText);
    }


    /**
     * 密码输入框点击事件，主要是为了点击时能够弹出输入法
     */
    @OnClick(R.id.login_pwd)
    public void passwordClick() {
        showInputMethod(passwordText);
    }

    /**
     * 忘记密码按钮点击事件
     */
    @OnClick(R.id.forget_password_btn)
    public void forgetPasswordClick() {
        Intent forgotIntent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(forgotIntent);
//        Intent demo=new Intent(LoginActivity.this,ResetPasswordActivity.class);
//        Bundle bundle=new Bundle();
//        bundle.putString("userName","aaaaa");
//        demo.putExtras(bundle);
//        startActivity(demo);
    }

    /**
     * 注册按钮点击事件
     */
    @OnClick(R.id.register_btn)
    public void registerClick() {
        Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(registerIntent);
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
        userNameText.setFocusable(false);
        passwordText.setFocusable(false);
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
                showInputMethod(userNameText);

            }
        }, INPUTMETHOD_DELAY);
    }

}

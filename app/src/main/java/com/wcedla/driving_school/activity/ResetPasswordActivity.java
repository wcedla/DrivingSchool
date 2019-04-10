package com.wcedla.driving_school.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.wcedla.driving_school.constant.Config.INPUTMETHOD_DELAY;
import static com.wcedla.driving_school.constant.Config.PASSWORD_MAX_LENGTH;
import static com.wcedla.driving_school.constant.Config.PASSWORD_MIN_LENGTH;
import static com.wcedla.driving_school.constant.Config.PASSWORD_REGEX;
import static com.wcedla.driving_school.constant.Config.RESET_PASSWORD_URL;

public class ResetPasswordActivity extends AppCompatActivity {

    @BindView(R.id.new_password_text)
    EditText newPasswordText;
    @BindView(R.id.reset_btn)
    Button resetBtn;
    @BindView(R.id.reset_password_layout)
    ConstraintLayout resetPasswordLayout;

    String userName = "";

    CustomProgressDialog progressDialog=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolUtils.setNavigationBarStatusBarTranslucent(this, true, false);
        setContentView(R.layout.activity_reset_password);
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            Toast.makeText(ResetPasswordActivity.this, "出现了一些问题！", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            userName = bundle.getString("userName");
            Log.d("wcedlaLog", "用户名: " + userName);
        }
        ButterKnife.bind(this);
        delayTask(new Runnable() {
            @Override
            public void run() {
                showInputMethod(newPasswordText);
            }
        }, INPUTMETHOD_DELAY);
        setInputFilter(newPasswordText, PASSWORD_REGEX, PASSWORD_MAX_LENGTH);

    }

    @OnClick(R.id.reset_password_layout)
    public void resetLayoutClick() {
        hideInputMethod(resetPasswordLayout);
    }

    @OnClick(R.id.reset_btn)
    public void resetButtonClick() {
        hideInputMethod(resetPasswordLayout);
        progressDialog=CustomProgressDialog.create(this,"正在重置密码...",false);
        String newPassword = newPasswordText.getText().toString().trim();
        if (newPassword.length() >= PASSWORD_MIN_LENGTH && newPassword.length() <= PASSWORD_MAX_LENGTH) {
            newPassword=ToolUtils.MD5Encrypted(newPassword);
            progressDialog.showProgressDialog();
            String url = HttpUtils.setParameterForUrl(RESET_PASSWORD_URL, "userName", userName, "password", newPassword);
            Log.d("wcedlaLog", "密码网址: "+url);
            HttpUtils.doHttpRequest(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ResetPasswordActivity.this,"连接出现了一些问题！",Toast.LENGTH_SHORT).show();
                            progressDialog.cancelProgressDialog();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseString =response.body().string();
                    int result= JsonUtils.getResetStatus(responseString);
                    if(result==-1)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.cancelProgressDialog();
                                finish();
                                Toast.makeText(ResetPasswordActivity.this,"密码重置成功！",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.cancelProgressDialog();
                                Toast.makeText(ResetPasswordActivity.this,"密码重置失败！",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }
        else
        {
            Toast.makeText(ResetPasswordActivity.this,"请输入新密码！",Toast.LENGTH_SHORT).show();
        }

    }

    @OnClick(R.id.new_password_text)
    public void newPasswordClick() {
        newPasswordText.setFocusable(true);
        showInputMethod(newPasswordText);
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
        newPasswordText.setFocusable(false);
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            if (inputMethodManager.isActive()) {
                inputMethodManager.hideSoftInputFromWindow(targetView.getWindowToken(), 0);
            }
        }
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
     * 简单封装的Handler延迟任务方法
     *
     * @param runnable    需要延迟运行的任务
     * @param delayMillis 延迟时间（毫秒）
     */
    private void delayTask(Runnable runnable, long delayMillis) {
        new Handler().postDelayed(runnable, delayMillis);
    }

    @Override
    protected void onResume() {
        super.onResume();
        delayTask(new Runnable() {
            @Override
            public void run() {
                showInputMethod(newPasswordText);
            }
        }, INPUTMETHOD_DELAY);
    }
}

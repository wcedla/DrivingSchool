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
import android.widget.EditText;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;
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

import static com.wcedla.driving_school.constant.Config.CHECK_PASSWORD;
import static com.wcedla.driving_school.constant.Config.INPUTMETHOD_DELAY;
import static com.wcedla.driving_school.constant.Config.PASSWORD_MAX_LENGTH;
import static com.wcedla.driving_school.constant.Config.PASSWORD_MIN_LENGTH;
import static com.wcedla.driving_school.constant.Config.PASSWORD_REGEX;

public class ChangePasswordActivity extends AppCompatActivity {

    @BindView(R.id.origin_password_text)
    EditText originPasswordText;
    @BindView(R.id.confirm_btn)
    Button confirmBtn;
    @BindView(R.id.change_password_layout)
    ConstraintLayout changePasswordLayout;

    CustomProgressDialog customProgressDialog;

    String loginUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolUtils.setNavigationBarStatusBarTranslucent(this, true, false);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
        loginUser = Hawk.get("loginUser", "");
        customProgressDialog = CustomProgressDialog.create(this, "正在验证...", false);
        delayTask(new Runnable() {
            @Override
            public void run() {
                showInputMethod(originPasswordText);
            }
        }, INPUTMETHOD_DELAY);
        setInputFilter(originPasswordText, PASSWORD_REGEX, PASSWORD_MAX_LENGTH);
    }

    @OnClick(R.id.change_password_layout)
    public void cangeRootClick() {
        hideInputMethod(changePasswordLayout);
    }

    @OnClick(R.id.origin_password_text)
    public void originTextClick() {
        showInputMethod(originPasswordText);
    }

    @OnClick(R.id.confirm_btn)
    public void confimClick() {
        hideInputMethod(confirmBtn);
        customProgressDialog.showProgressDialog();
        checkPassword();
    }

    private void checkPassword() {
        String password = originPasswordText.getText().toString().trim();

        if (password.length() >= PASSWORD_MIN_LENGTH && password.length() <= PASSWORD_MAX_LENGTH) {
            password = ToolUtils.MD5Encrypted(password);
            String url = HttpUtils.setParameterForUrl(CHECK_PASSWORD, "userName", loginUser, "password", password);
            HttpUtils.doHttpRequest(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            customProgressDialog.cancelProgressDialog();
                            Toast.makeText(ChangePasswordActivity.this, "验证失败，请重试！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    int result = JsonUtils.getCheckPasswordStatus(response.body().string());
                    if (result == -1) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                customProgressDialog.cancelProgressDialog();
                                Intent newPasswordIntent = new Intent(ChangePasswordActivity.this, ResetPasswordActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("userName", loginUser);
                                newPasswordIntent.putExtras(bundle);
                                startActivity(newPasswordIntent);
                                finish();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                customProgressDialog.cancelProgressDialog();
                                Toast.makeText(ChangePasswordActivity.this, "验证失败，请确认密码正确！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }


                }
            });
        } else {
            customProgressDialog.cancelProgressDialog();
            Toast.makeText(ChangePasswordActivity.this, "请输入正确的密码", Toast.LENGTH_SHORT).show();
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
        originPasswordText.setFocusable(false);
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
                showInputMethod(originPasswordText);
            }
        }, INPUTMETHOD_DELAY);
    }
}

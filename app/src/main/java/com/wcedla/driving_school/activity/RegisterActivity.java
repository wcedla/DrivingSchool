package com.wcedla.driving_school.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.wcedla.driving_school.R;
import com.wcedla.driving_school.customview.CustomProgressDialog;
import com.wcedla.driving_school.listener.EmailSendStatusInterface;
import com.wcedla.driving_school.tool.CodeUtils;
import com.wcedla.driving_school.tool.HttpUtils;
import com.wcedla.driving_school.tool.JsonUtils;
import com.wcedla.driving_school.tool.MailUtils;
import com.wcedla.driving_school.tool.SharePreferenceUtils;
import com.wcedla.driving_school.tool.ToolUtils;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.wcedla.driving_school.constant.Config.DIALOG_DELAY;
import static com.wcedla.driving_school.constant.Config.EMAIL_CHECK_MAX_LENGTH;
import static com.wcedla.driving_school.constant.Config.EMAIL_CHECK_REGEX;
import static com.wcedla.driving_school.constant.Config.EMAIL_CODE_TIME;
import static com.wcedla.driving_school.constant.Config.EMAIL_CODE_TIME_KEY;
import static com.wcedla.driving_school.constant.Config.EMAIL_LEGAL_REGEX;
import static com.wcedla.driving_school.constant.Config.EMAIL_MAX_LENGTH;
import static com.wcedla.driving_school.constant.Config.EMAIL_REGEX;
import static com.wcedla.driving_school.constant.Config.EMAIL_SUBJECT;
import static com.wcedla.driving_school.constant.Config.INPUTMETHOD_DELAY;
import static com.wcedla.driving_school.constant.Config.PASSWORD_MAX_LENGTH;
import static com.wcedla.driving_school.constant.Config.PASSWORD_MIN_LENGTH;
import static com.wcedla.driving_school.constant.Config.PASSWORD_REGEX;
import static com.wcedla.driving_school.constant.Config.REGISTER_URL;
import static com.wcedla.driving_school.constant.Config.USERNAME_MAX_LENGTH;
import static com.wcedla.driving_school.constant.Config.USERNAME_MIN_LENGTH;
import static com.wcedla.driving_school.constant.Config.USERNAME_REGEX;
import static com.wcedla.driving_school.constant.Config.YZM_MAX_LENGTH;
import static com.wcedla.driving_school.constant.Config.YZM_REGEX;

public class RegisterActivity extends AppCompatActivity implements EmailSendStatusInterface {


    @BindView(R.id.register_layout)
    ConstraintLayout registerLayout;
    @BindView(R.id.register_account_text)
    EditText accountText;
    @BindView(R.id.register_password_text)
    EditText passwordText;
    @BindView(R.id.register_email_text)
    EditText emailText;
    @BindView(R.id.register_email_check_text)
    EditText emailCheckText;
    @BindView(R.id.register_email_check_btn)
    Button emailCheckButton;
    @BindView(R.id.register_yzm_text)
    EditText yzmText;
    @BindView(R.id.register_yzm_img)
    ImageView yzmImg;
    @BindView(R.id.register_btn)
    Button registerButton;

    CustomProgressDialog progressDialog = null;

    boolean emailCheckClick = false;
    int time = 60;
    Timer timer;
    TimerTask timerTask;
    String emailCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolUtils.setNavigationBarStatusBarTranslucent(RegisterActivity.this, true, false);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        long periodTime = SharePreferenceUtils.getData(RegisterActivity.this, EMAIL_CODE_TIME, EMAIL_CODE_TIME_KEY, 0L);
        if (System.currentTimeMillis() - periodTime < 1000 * 60) {
            time = 60 - (int) ((System.currentTimeMillis() - periodTime) / 1000);
            setEmailClick();
        }
        //设置验证码图片源
        yzmImg.setImageBitmap(CodeUtils.getInstance().createBitmap());
        //延迟500毫秒自动弹出输入法
        delayTask(new Runnable() {
            @Override
            public void run() {
                showInputMethod(accountText);
            }
        }, INPUTMETHOD_DELAY);

        //邮箱验证正则表达式"^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$";
        setInputFilter(accountText, USERNAME_REGEX, USERNAME_MAX_LENGTH);
        setInputFilter(passwordText, PASSWORD_REGEX, PASSWORD_MAX_LENGTH);
        setInputFilter(emailText, EMAIL_REGEX, EMAIL_MAX_LENGTH);
        setInputFilter(emailCheckText, EMAIL_CHECK_REGEX, EMAIL_CHECK_MAX_LENGTH);
        setInputFilter(yzmText, YZM_REGEX, YZM_MAX_LENGTH);

    }

    @OnClick(R.id.register_layout)
    public void outsideClick() {
        hideInputMethod(registerLayout);
    }

    @OnClick(R.id.register_account_text)
    public void accountTextClick() {
        showInputMethod(accountText);
    }

    @OnClick(R.id.register_password_text)
    public void passwordTextClick() {
        showInputMethod(passwordText);
    }

    @OnClick(R.id.register_email_text)
    public void emailTextClick() {
        showInputMethod(emailText);
    }

    @OnClick(R.id.register_email_check_text)
    public void emailCheckTextClick() {
        showInputMethod(emailCheckText);
    }

    @OnClick(R.id.register_email_check_btn)
    public void emailCheckBtnClick() {
        String userName = accountText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();
        final String email = emailText.getText().toString().trim();
        if (email.length() == 0 || userName.length() == 0 || password.length() == 0) {
            Toast.makeText(RegisterActivity.this, "请先输入用户名密码以及邮箱地址!", Toast.LENGTH_SHORT).show();
        } else if (!Pattern.matches(EMAIL_LEGAL_REGEX, email)) {
            Toast.makeText(RegisterActivity.this, "请检查输入的邮箱是否正确！", Toast.LENGTH_SHORT).show();
        } else {
            if (!emailCheckClick) {
                emailCode = ToolUtils.getEmailCode();
//                Log.d("wcedlaLog", "邮箱码: " + gtEmailContent(emailCode));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MailUtils.getInstance().sendEmail(email, "", EMAIL_SUBJECT, gtEmailContent(emailCode), RegisterActivity.this);
                    }
                }).start();
                SharePreferenceUtils.setData(RegisterActivity.this, EMAIL_CODE_TIME, EMAIL_CODE_TIME_KEY, System.currentTimeMillis());
                progressDialog = CustomProgressDialog.create(RegisterActivity.this, "正在发送...", false);
                progressDialog.showProgressDialog();
            }

        }
    }

    @OnClick(R.id.register_yzm_text)
    public void yzmTextClick() {
        showInputMethod(yzmText);
    }

    @OnClick(R.id.register_yzm_img)
    public void yzmImgClick() {
        yzmImg.setImageBitmap(CodeUtils.getInstance().createBitmap());
    }

    @OnClick(R.id.register_btn)
    public void registerBtnClick() {
        hideInputMethod(registerButton);
        String userName = accountText.getText().toString().trim();
        final String password = passwordText.getText().toString().trim();
        String email = emailText.getText().toString().trim();
        String emailCheck = emailCheckText.getText().toString().trim();
        String yzm = yzmText.getText().toString().trim();
        if (userName.length() == 0 || password.length() == 0 || email.length() == 0 || emailCheck.length() == 0 || yzm.length() == 0) {
            Toast.makeText(RegisterActivity.this, "请按要求输入数据！", Toast.LENGTH_SHORT).show();
        } else if (userName.length() < USERNAME_MIN_LENGTH || userName.length() > USERNAME_MAX_LENGTH) {
            Toast.makeText(RegisterActivity.this, "请按要求输入账号！", Toast.LENGTH_SHORT).show();
        } else if (password.length() < PASSWORD_MIN_LENGTH || password.length() > PASSWORD_MAX_LENGTH) {
            Toast.makeText(RegisterActivity.this, "请按要求输入密码！", Toast.LENGTH_SHORT).show();
        } else if (!Pattern.matches(EMAIL_LEGAL_REGEX, email)) {
            Toast.makeText(RegisterActivity.this, "请输入正确的邮箱！", Toast.LENGTH_SHORT).show();
        } else if (!emailCheck.equals(emailCode)) {
            Toast.makeText(RegisterActivity.this, "请检查输入的邮箱码是否正确！", Toast.LENGTH_SHORT).show();
        } else if (!yzm.toLowerCase().equals(CodeUtils.getInstance().getCode().toLowerCase())) {
            Toast.makeText(RegisterActivity.this, "验证码输入错误!", Toast.LENGTH_SHORT).show();
            yzmText.setText("");
            yzmImg.setImageBitmap(CodeUtils.getInstance().createBitmap());
        } else {
            progressDialog = CustomProgressDialog.create(RegisterActivity.this, "正在注册...", false);
            progressDialog.showProgressDialog();
            String encryptedPassword = ToolUtils.MD5Encrypted(password);
            //String registerUrl = "http://192.168.191.1:8080/DrivingSchoolServer/UserRegister?userName=" + userName + "&password=" + encryptedPassword + "&email=" + email;
            String registerUrl = HttpUtils.setParameterForUrl(REGISTER_URL, "userName", userName, "password", encryptedPassword, "email", email);
            HttpUtils.doHttpRequest(registerUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.cancelProgressDialog();
                            Toast.makeText(RegisterActivity.this, "注册失败！可能是服务器出现了问题！", Toast.LENGTH_SHORT).show();
                            yzmText.setText("");
                            yzmImg.performClick();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseString = response.body().string();
                    int jsonResult = JsonUtils.getRegisterResult(responseString);
                    switch (jsonResult) {
                        case -1:
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    yzmImg.performClick();
                                    yzmText.setText("");
                                    delayTask(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressDialog.cancelProgressDialog();
                                            Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                                            Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                                            loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(loginIntent);
                                        }
                                    }, DIALOG_DELAY);

                                }
                            });
                            break;
                        case 0:
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    delayTask(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressDialog.cancelProgressDialog();
                                            Toast.makeText(RegisterActivity.this, "很抱歉，该用户名已被注册！", Toast.LENGTH_SHORT).show();
                                            yzmText.setText("");
                                            yzmImg.performClick();
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
                                            progressDialog.cancelProgressDialog();
                                            Toast.makeText(RegisterActivity.this, "很抱歉，注册失败！", Toast.LENGTH_SHORT).show();
                                            yzmText.setText("");
                                            yzmImg.performClick();
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
                                            progressDialog.cancelProgressDialog();
                                            Toast.makeText(RegisterActivity.this, "很抱歉，出现了未知错误！", Toast.LENGTH_SHORT).show();
                                            yzmText.setText("");
                                            yzmImg.performClick();
                                        }
                                    }, DIALOG_DELAY);
                                }
                            });
                            break;
                    }

                }
            });
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
        accountText.setFocusable(false);
        passwordText.setFocusable(false);
        emailText.setFocusable(false);
        emailCheckText.setFocusable(false);
        yzmText.setFocusable(false);
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

    private String gtEmailContent(String emailCode) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<h2>您的驾校易管理邮箱码为：");
        stringBuilder.append("<span style=\"color:#2a2a2a;font-weight:bold;font-size:30px\">");
        stringBuilder.append(emailCode);
        stringBuilder.append("</span><h2>");
        return stringBuilder.toString();
    }

    private void setEmailClick() {
        emailCheckClick = true;
        emailCheckButton.setEnabled(false);
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        time -= 1;
                        emailCheckButton.setText(time + "秒后重发");
                        if (time <= 0) {
                            emailCheckButton.setEnabled(true);
                            emailCheckClick = false;
                            time = 60;
                            timer.cancel();
                            emailCheckButton.setText("发送邮箱码");
                        }
                    }
                });
            }
        };
        timer.schedule(timerTask, 0, 1000);
    }

    @Override
    public void finish() {
        super.finish();
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    //跳转其他活动或者后台返回时能够重新自动弹出输入法框
    @Override
    protected void onResume() {
        super.onResume();
        //延迟打开输入法，不用用户点击弹出输入法，增加易用性
        delayTask(new Runnable() {
            @Override
            public void run() {
                showInputMethod(accountText);

            }
        }, INPUTMETHOD_DELAY);
    }


    @Override
    public void onSendSuccess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null) {
                    progressDialog.cancelProgressDialog();
                    setEmailClick();
                }
            }
        });

    }

    @Override
    public void onSendFailed() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null) {
                    progressDialog.cancelProgressDialog();
                }
            }
        });

    }
}

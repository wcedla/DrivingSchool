package com.wcedla.driving_school.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wcedla.driving_school.R;
import com.wcedla.driving_school.customview.CustomProgressDialog;
import com.wcedla.driving_school.listener.EmailSendStatusInterface;
import com.wcedla.driving_school.tool.HttpUtils;
import com.wcedla.driving_school.tool.JsonUtils;
import com.wcedla.driving_school.tool.MailUtils;
import com.wcedla.driving_school.tool.ToolUtils;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.wcedla.driving_school.constant.Config.EMAIL_CHECK_MAX_LENGTH;
import static com.wcedla.driving_school.constant.Config.EMAIL_CHECK_REGEX;
import static com.wcedla.driving_school.constant.Config.EMAIL_SUBJECT;
import static com.wcedla.driving_school.constant.Config.GET_EMAIL_URL;
import static com.wcedla.driving_school.constant.Config.INPUTMETHOD_DELAY;
import static com.wcedla.driving_school.constant.Config.USERNAME_MAX_LENGTH;
import static com.wcedla.driving_school.constant.Config.USERNAME_MIN_LENGTH;
import static com.wcedla.driving_school.constant.Config.USERNAME_REGEX;

public class ForgotPasswordActivity extends AppCompatActivity implements EmailSendStatusInterface {


    @BindView(R.id.forgot_account_text)
    EditText accountText;
    @BindView(R.id.forgot_email_text)
    EditText emailText;
    @BindView(R.id.forgot_btn)
    Button changePassword;
    MyHandler handler = new MyHandler(this);
    @BindView(R.id.forgot_email_head)
    TextView emailHead;
    @BindView(R.id.forgot_layout)
    ConstraintLayout forgotLayout;

    CustomProgressDialog progressDialog;

    String checkUserName=null;

    String emailCode=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolUtils.setNavigationBarStatusBarTranslucent(ForgotPasswordActivity.this, true, false);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
        accountText.addTextChangedListener(myTextWatcher);
        //延迟500毫秒自动弹出输入法
        delayTask(new Runnable() {
            @Override
            public void run() {
                showInputMethod(accountText);
            }
        }, INPUTMETHOD_DELAY);
        setInputFilter(accountText, USERNAME_REGEX, USERNAME_MAX_LENGTH);
        setInputFilter(emailText, EMAIL_CHECK_REGEX, EMAIL_CHECK_MAX_LENGTH);
        checkUserName="";
        emailCode="";

    }

    @OnClick(R.id.forgot_btn)
    public void changeButtonClick() {
        if(changePassword.getText().length()==5) {
            String userName = accountText.getText().toString().trim();
            hideInputMethod(changePassword);
            if (userName.length() >= USERNAME_MIN_LENGTH && userName.length() <= USERNAME_MAX_LENGTH) {
                //String emailCode = emailText.getText().toString().trim();
                progressDialog = CustomProgressDialog.create(ForgotPasswordActivity.this, "正在发送...", false);
                progressDialog.showProgressDialog();
                getEmailAdress(userName);
            } else {
                Toast.makeText(ForgotPasswordActivity.this, "请输入正确的用户名!", Toast.LENGTH_SHORT).show();
            }
        }else
        {
            hideInputMethod(changePassword);
            String emailCodeText=emailText.getText().toString().trim();
            String userName = accountText.getText().toString().trim();
            if(emailCodeText.equals(emailCode)&&userName.equals(checkUserName))
            {
//                Toast.makeText(ForgotPasswordActivity.this,"验证成功！",Toast.LENGTH_SHORT).show();
                Intent newPassword=new Intent(ForgotPasswordActivity.this,ResetPasswordActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("userName",userName);
                newPassword.putExtras(bundle);
                finish();
                startActivity(newPassword);
            }
            else
            {
                Toast.makeText(ForgotPasswordActivity.this,"请检查用户名与邮箱是否匹配！",Toast.LENGTH_SHORT).show();

            }
        }
    }

    @OnClick(R.id.forgot_account_text)
    public void accountTextClick()
    {
        accountText.setFocusable(true);
        showInputMethod(accountText);
    }

    @OnClick(R.id.forgot_email_text)
    public void emailTextClick()
    {
        emailText.setFocusable(true);
        showInputMethod(emailText);
    }

    @OnClick(R.id.forgot_layout)
    public void outLayoutClick()
    {
        hideInputMethod(forgotLayout);
    }

    private void getEmailAdress(String userName) {
        final String url = HttpUtils.setParameterForUrl(GET_EMAIL_URL, "userName", userName);
        HttpUtils.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ForgotPasswordActivity.this, "发生了一些错误！", Toast.LENGTH_SHORT).show();
                        progressDialog.cancelProgressDialog();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                String emailAdress = JsonUtils.getEmailResult(responseData);
                Message message = new Message();
                message.obj = emailAdress;
                message.what = 1;
                handler.sendMessage(message);
//               Log.d("wcedlaLog", "邮箱地址: "+emailAdress+",,"+responseData+",,"+url);

            }
        });
    }



    @Override
    public void onSendSuccess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.cancelProgressDialog();
                emailHead.setVisibility(View.VISIBLE);
                emailText.setVisibility(View.VISIBLE);
                changePassword.setText("修改密码");
            }
        });
    }

    @Override
    public void onSendFailed() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.cancelProgressDialog();
            }
        });
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
        emailText.setFocusable(false);
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

    TextWatcher myTextWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (emailHead.getVisibility() == View.VISIBLE) {
                emailHead.setVisibility(View.GONE);
                emailText.setVisibility(View.GONE);
                changePassword.setText("发送邮箱码");

            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            checkUserName=accountText.getText().toString().trim();
        }
    };

    public static class MyHandler extends Handler {
        // WeakReference to the outer class's instance.
        private WeakReference<ForgotPasswordActivity> mOuter;

        public MyHandler(ForgotPasswordActivity activity) {
            mOuter = new WeakReference<ForgotPasswordActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {

            final ForgotPasswordActivity outer = mOuter.get();
            if (outer != null) {
                switch (msg.what) {
                    case 1:
                        final String emailAdress = (String) msg.obj;
                        if ("".equals(emailAdress)) {
                            Toast.makeText(outer, "未找到对应邮箱，请检查用户名！", Toast.LENGTH_SHORT).show();
                            outer.progressDialog.cancelProgressDialog();
                        } else {
                            outer.emailCode = ToolUtils.getEmailCode();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    MailUtils.getInstance().sendEmail(emailAdress, "", EMAIL_SUBJECT, gtEmailContent(outer.emailCode), outer);
                                }
                            }).start();
                            //Toast.makeText(outer, emailAdress, Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        break;

                }
            }
        }
    }

    private static String gtEmailContent(String emailCode) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<h2>您正在进行修改密码操作，本次邮箱码为：");
        stringBuilder.append("<span style=\"color:#2a2a2a;font-weight:bold;font-size:30px\">");
        stringBuilder.append(emailCode);
        stringBuilder.append("</span><h2>");
        return stringBuilder.toString();
    }

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

}

package com.wcedla.driving_school.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.orhanobut.hawk.Hawk;
import com.wcedla.driving_school.R;
import com.wcedla.driving_school.adapter.MessageShowAdapter;
import com.wcedla.driving_school.bean.MessageShowBean;
import com.wcedla.driving_school.customview.CustomProgressDialog;
import com.wcedla.driving_school.tool.HttpUtils;
import com.wcedla.driving_school.tool.JsonUtils;
import com.wcedla.driving_school.tool.ToolUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.wcedla.driving_school.constant.Config.GET_MESSAGE_URL;
import static com.wcedla.driving_school.constant.Config.SEND_MESSAGE_URL;

public class MessageActivity extends AppCompatActivity {

    @BindView(R.id.message_back)
    ImageView messageBack;
    @BindView(R.id.message_edit)
    EditText messageEdit;
    @BindView(R.id.message_send_btn)
    Button messageSendBtn;
    @BindView(R.id.message_detial)
    RecyclerView messageDetial;

    CustomProgressDialog customProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolUtils.setNavigationBarStatusBarTranslucent(this, false, Color.parseColor("#000000"));
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        initData();
    }

    @OnClick(R.id.message_back)
    public void backClick()
    {
        hideInputMethod(messageEdit);
        finish();
    }

    @OnClick(R.id.message_root)
    public void messageRootClick()
    {
        hideInputMethod(messageEdit);
    }

    @OnClick(R.id.message_edit)
    public void editClick()
    {
        showInputMethod(messageEdit);
    }

    @OnClick(R.id.message_send_btn)
    public void sendClick()
    {
        hideInputMethod(messageEdit);
        customProgressDialog=CustomProgressDialog.create(this,"发送中...",false);
        customProgressDialog.showProgressDialog();
        String contentString=messageEdit.getText().toString().trim();
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String url= HttpUtils.setParameterForUrl(SEND_MESSAGE_URL,"content",contentString,"time",dateFormat.format(new Date(System.currentTimeMillis())));
        HttpUtils.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MessageActivity.this,"发送失败，请重试！",Toast.LENGTH_SHORT).show();
                        if(!MessageActivity.this.isFinishing()&&customProgressDialog!=null&&customProgressDialog.isShowing())
                        {
                            customProgressDialog.cancelProgressDialog();
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseString=response.body().string();
                int result= JsonUtils.getMessageSendStatus(responseString);
                if(result==-1)
                {
                    getMessageData();
                }
                else
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MessageActivity.this, "留言发送失败！", Toast.LENGTH_SHORT).show();
                            if(!MessageActivity.this.isFinishing()&&customProgressDialog!=null&&customProgressDialog.isShowing())
                            {
                                customProgressDialog.cancelProgressDialog();
                            }
                        }
                    });
                }
            }
        });
    }

    private void initData()
    {
        MessageShowBean messageShowBean=new MessageShowBean();
        String dataString= Hawk.get("messageData");
        try
        {
            messageShowBean=new Gson().fromJson(dataString,MessageShowBean.class);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        MessageShowAdapter messageShowAdapter=new MessageShowAdapter(this,messageShowBean);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        messageDetial.setLayoutManager(linearLayoutManager);
        messageDetial.setAdapter(messageShowAdapter);
    }

    private void getMessageData()
    {
        HttpUtils.doHttpRequest(GET_MESSAGE_URL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (!MessageActivity.this.isFinishing()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MessageActivity.this, "留言发送成功，但是刷新失败！请退出再进入！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Hawk.put("messageData",response.body().string());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initData();
                        if(!MessageActivity.this.isFinishing()&&customProgressDialog!=null&&customProgressDialog.isShowing())
                        {

                            customProgressDialog.cancelProgressDialog();
                        }
                    }
                });
            }
        });
    }

    public void closeInputMethod()
    {
        messageRootClick();
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
        messageEdit.setFocusable(false);
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
}

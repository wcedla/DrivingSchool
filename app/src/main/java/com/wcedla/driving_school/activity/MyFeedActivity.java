package com.wcedla.driving_school.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.orhanobut.hawk.Hawk;
import com.wcedla.driving_school.R;
import com.wcedla.driving_school.adapter.MyFeedAdapter;
import com.wcedla.driving_school.bean.MyFeedDataBean;
import com.wcedla.driving_school.customview.CustomProgressDialog;
import com.wcedla.driving_school.tool.ExamUtils;
import com.wcedla.driving_school.tool.HttpUtils;
import com.wcedla.driving_school.tool.StudyUtils;
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

import static com.wcedla.driving_school.constant.Config.GET_MY_FEED;
import static com.wcedla.driving_school.constant.Config.USERNAME_REGEX;

public class MyFeedActivity extends AppCompatActivity {

    @BindView(R.id.my_feed_back)
    ImageView myFeedBack;
    @BindView(R.id.my_feed_search_input)
    EditText myFeedSearchInput;
    @BindView(R.id.my_feed_search_btn)
    Button myFeedSearchBtn;
    @BindView(R.id.my_feed_recycler_view)
    RecyclerView myFeedRecyclerView;
    @BindView(R.id.my_feed_root)
    ConstraintLayout myFeedRoot;

    String loginNo;

    CustomProgressDialog customProgressDialog;

    public MyFeedDataBean myFeedDataBean;
    MyFeedDataBean searchResultBean = new MyFeedDataBean();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolUtils.setNavigationBarStatusBarTranslucent(this, false, Color.parseColor("#000000"));
        setContentView(R.layout.activity_my_feed);
        ButterKnife.bind(this);
        loginNo = Hawk.get("loginNo", "");
        setInputFilter(myFeedSearchInput, USERNAME_REGEX, 8);
        customProgressDialog = CustomProgressDialog.create(this, "正在获取数据...", false);
        customProgressDialog.showProgressDialog();
        getMyFeed();
        myFeedSearchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() < 1) {
                    showAllStudent();
                }
            }
        });

    }

    @OnClick(R.id.my_feed_back)
    public void myFeedBackClick() {
        finish();
    }

    @OnClick(R.id.my_feed_root)
    public void myFeedRootClick() {
        hideInputMethod(myFeedRoot);
    }

    @OnClick(R.id.my_feed_search_input)
    public void myFeedInputClick() {
        showInputMethod(myFeedSearchInput);
    }

    @OnClick(R.id.my_feed_search_btn)
    public void myFeedBtnClick() {
        hideInputMethod(myFeedSearchBtn);
        String name = myFeedSearchInput.getText().toString().trim();
        if (name.length() >= 1) {
            searchResult(name);
        } else {
            Toast.makeText(this, "请输入正确的项目名称!", Toast.LENGTH_SHORT).show();
        }
    }

    public void hideInput() {
        hideInputMethod(myFeedRoot);
    }

    private void searchResult(String name) {
        if (myFeedDataBean != null && myFeedDataBean.getMyFeedInfo() != null) {
            List<MyFeedDataBean.MyFeedInfoBean> myFeedInfoBeanList = new ArrayList<>();
            for (MyFeedDataBean.MyFeedInfoBean myFeedInfoBean : myFeedDataBean.getMyFeedInfo()) {
                if (StudyUtils.getStudyProgress(myFeedInfoBean.getProgress()).contains(name) || StudyUtils.getStudyProgress(myFeedInfoBean.getMessage()).contains(name) || ExamUtils.getExamProgress(myFeedInfoBean.getProgress()).contains(name)) {
                    myFeedInfoBeanList.add(myFeedInfoBean);
                }
            }
            searchResultBean.setMyFeedInfo(myFeedInfoBeanList);
        }
        ((MyFeedAdapter) myFeedRecyclerView.getAdapter()).updateData(searchResultBean);
    }

    private void showAllStudent() {
        ((MyFeedAdapter) myFeedRecyclerView.getAdapter()).updateData(myFeedDataBean);
    }

    private void getMyFeed() {
        String url = HttpUtils.setParameterForUrl(GET_MY_FEED, "no", loginNo);
        HttpUtils.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyFeedActivity.this, "获取失败!", Toast.LENGTH_SHORT).show();
                        if (!MyFeedActivity.this.isFinishing() && customProgressDialog != null && customProgressDialog.isShowing()) {
                            customProgressDialog.cancelProgressDialog();
                        }

                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                myFeedDataBean = new Gson().fromJson(response.body().string(), MyFeedDataBean.class);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyFeedActivity.this);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                MyFeedAdapter myFeedAdapter = new MyFeedAdapter(MyFeedActivity.this, myFeedDataBean);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myFeedRecyclerView.setLayoutManager(linearLayoutManager);
                        myFeedRecyclerView.setAdapter(myFeedAdapter);
                        if (!MyFeedActivity.this.isFinishing() && customProgressDialog != null && customProgressDialog.isShowing()) {
                            customProgressDialog.cancelProgressDialog();
                        }
                    }
                });
            }
        });
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
        myFeedSearchInput.setFocusable(false);
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            if (inputMethodManager.isActive()) {
                inputMethodManager.hideSoftInputFromWindow(targetView.getWindowToken(), 0);
            }
        }
    }
}

package com.wcedla.driving_school.activity;

import android.content.Context;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wcedla.driving_school.R;
import com.wcedla.driving_school.adapter.AllFeedAdapter;
import com.wcedla.driving_school.bean.AllFeedDataBean;
import com.wcedla.driving_school.customview.CustomProgressDialog;
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

import static com.wcedla.driving_school.constant.Config.GET_ALL_FEED_INFO;

public class UserFeedActivity extends AppCompatActivity {

    @BindView(R.id.user_feed_back)
    ImageView userFeedBack;
    @BindView(R.id.user_feed_time)
    RadioButton userFeedTime;
    @BindView(R.id.user_feed_all)
    RadioButton userFeedAll;
    @BindView(R.id.user_feed_progress)
    RadioButton userFeedProgress;
    @BindView(R.id.user_feed_exam)
    RadioButton userFeedExam;
    @BindView(R.id.user_feed_radio_group)
    RadioGroup userFeedRadioGroup;
    @BindView(R.id.user_feed_search_input)
    EditText userFeedSearchInput;
    @BindView(R.id.user_feed_search_btn)
    Button userFeedSearchBtn;
    @BindView(R.id.user_feed_recycler_view)
    RecyclerView userFeedRecyclerView;
    @BindView(R.id.user_feed_root)
    ConstraintLayout userFeedRoot;

    public CustomProgressDialog customProgressDialog;

    public AllFeedDataBean allFeedDataBean;
    AllFeedDataBean searchResultDataBean = new AllFeedDataBean();
    AllFeedDataBean timeResult = new AllFeedDataBean();
    AllFeedDataBean progressResult = new AllFeedDataBean();
    AllFeedDataBean examResult = new AllFeedDataBean();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolUtils.setNavigationBarStatusBarTranslucent(this, false, false);
        setContentView(R.layout.activity_user_feed);
        ButterKnife.bind(this);
        customProgressDialog = CustomProgressDialog.create(this, "正在获取数据...", false);
        customProgressDialog.showProgressDialog();
        getAllFeed();
    }

    @OnClick(R.id.user_feed_back)
    public void backClick() {
        finish();
    }

    @OnClick(R.id.user_feed_root)
    public void allFeedRootClick() {
        hideInputMethod(userFeedRoot);
    }

    @OnClick(R.id.user_feed_search_input)
    public void feedInputClick() {
        showInputMethod(userFeedSearchInput);
    }

    @OnClick(R.id.user_feed_all)
    public void allClick() {
        hideInputMethod(userFeedAll);
        userFeedSearchInput.setText("");
        ((AllFeedAdapter) userFeedRecyclerView.getAdapter()).updateData(allFeedDataBean);
    }

    @OnClick(R.id.user_feed_time)
    public void timeClick() {
        hideInputMethod(userFeedTime);
        List<AllFeedDataBean.AllFeedInfoBean> allFeedInfoBeanList = new ArrayList<>();
        for (AllFeedDataBean.AllFeedInfoBean allFeedInfoBean : allFeedDataBean.getAllFeedInfo()) {
            if (allFeedInfoBean.getType().equals("1")) {
                allFeedInfoBeanList.add(allFeedInfoBean);
            }
        }
        timeResult.setAllFeedInfo(allFeedInfoBeanList);
        ((AllFeedAdapter) userFeedRecyclerView.getAdapter()).updateData(timeResult);
    }

    @OnClick(R.id.user_feed_progress)
    public void progressClick() {
        hideInputMethod(userFeedProgress);
        userFeedSearchInput.setText("");
        List<AllFeedDataBean.AllFeedInfoBean> allFeedInfoBeanList = new ArrayList<>();
        for (AllFeedDataBean.AllFeedInfoBean allFeedInfoBean : allFeedDataBean.getAllFeedInfo()) {
            if (allFeedInfoBean.getType().equals("2")) {
                allFeedInfoBeanList.add(allFeedInfoBean);
            }
        }
        progressResult.setAllFeedInfo(allFeedInfoBeanList);
        ((AllFeedAdapter) userFeedRecyclerView.getAdapter()).updateData(progressResult);
    }

    @OnClick(R.id.user_feed_exam)
    public void examClick() {
        hideInputMethod(userFeedExam);
        userFeedSearchInput.setText("");
        List<AllFeedDataBean.AllFeedInfoBean> allFeedInfoBeanList = new ArrayList<>();
        for (AllFeedDataBean.AllFeedInfoBean allFeedInfoBean : allFeedDataBean.getAllFeedInfo()) {
            if (allFeedInfoBean.getType().equals("3")) {
                allFeedInfoBeanList.add(allFeedInfoBean);
            }
        }
        examResult.setAllFeedInfo(allFeedInfoBeanList);
        ((AllFeedAdapter) userFeedRecyclerView.getAdapter()).updateData(examResult);
    }

    @OnClick(R.id.user_feed_search_btn)
    public void feedBtnClick() {
        hideInputMethod(userFeedSearchBtn);
        String name = userFeedSearchInput.getText().toString().trim();
        if (name.length() > 0) {
            searchResult(name);
        } else {
            Toast.makeText(UserFeedActivity.this, "请输入内容！", Toast.LENGTH_SHORT).show();
        }
    }

    private void searchResult(String name) {
        List<AllFeedDataBean.AllFeedInfoBean> allFeedInfoBeanList = new ArrayList<>();
        for (AllFeedDataBean.AllFeedInfoBean allFeedInfoBean : allFeedDataBean.getAllFeedInfo()) {
            if (allFeedInfoBean.getRealName().contains(name) || allFeedInfoBean.getStudentNo().contains(name)) {
                allFeedInfoBeanList.add(allFeedInfoBean);
            }
        }
        searchResultDataBean.setAllFeedInfo(allFeedInfoBeanList);
        ((AllFeedAdapter) userFeedRecyclerView.getAdapter()).updateData(searchResultDataBean);
    }


    private void getAllFeed() {
        HttpUtils.doHttpRequest(GET_ALL_FEED_INFO, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(UserFeedActivity.this, "获取失败！", Toast.LENGTH_SHORT).show();
                        if (!UserFeedActivity.this.isFinishing() && customProgressDialog != null && customProgressDialog.isShowing()) {
                            customProgressDialog.cancelProgressDialog();
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                allFeedDataBean = new Gson().fromJson(response.body().string(), AllFeedDataBean.class);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UserFeedActivity.this);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                AllFeedAdapter allFeedAdapter = new AllFeedAdapter(UserFeedActivity.this, allFeedDataBean);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        userFeedRecyclerView.setLayoutManager(linearLayoutManager);
                        userFeedRecyclerView.setAdapter(allFeedAdapter);
                        if (!UserFeedActivity.this.isFinishing() && customProgressDialog != null && customProgressDialog.isShowing()) {
                            customProgressDialog.cancelProgressDialog();
                        }
                    }
                });
            }
        });
    }


    public void hideInput() {
        hideInputMethod(userFeedSearchInput);
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
        userFeedSearchInput.setFocusable(false);
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            if (inputMethodManager.isActive()) {
                inputMethodManager.hideSoftInputFromWindow(targetView.getWindowToken(), 0);
            }
        }
    }
}

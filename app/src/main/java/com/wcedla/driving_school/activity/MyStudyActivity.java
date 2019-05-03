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
import com.wcedla.driving_school.adapter.MyStudyInfoAdapter;
import com.wcedla.driving_school.bean.MyStudyInfoDataBean;
import com.wcedla.driving_school.customview.CustomProgressDialog;
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

import static com.wcedla.driving_school.constant.Config.GET_ALL_STUDY_INFO;
import static com.wcedla.driving_school.constant.Config.USERNAME_REGEX;

public class MyStudyActivity extends AppCompatActivity {

    @BindView(R.id.my_study_back)
    ImageView myStudyBack;
    @BindView(R.id.recycler_view_all_study_info)
    RecyclerView recyclerViewAllStudyInfo;
    @BindView(R.id.my_study_root)
    ConstraintLayout myStudyRoot;
    @BindView(R.id.my_study_search_input)
    EditText myStudySearchInput;
    @BindView(R.id.my_study_search_btn)
    Button myStudySearchBtn;

    String loginNo;
    public CustomProgressDialog customProgressDialog;
    public MyStudyInfoDataBean myStudyInfoDataBean;
    MyStudyInfoDataBean searchResultBean = new MyStudyInfoDataBean();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolUtils.setNavigationBarStatusBarTranslucent(this, false, Color.parseColor("#000000"));
        setContentView(R.layout.activity_my_study);
        ButterKnife.bind(this);
        loginNo = Hawk.get("loginNo", "");
        setInputFilter(myStudySearchInput, USERNAME_REGEX, 8);
        customProgressDialog = CustomProgressDialog.create(this, "正在获取数据...", false);
        customProgressDialog.showProgressDialog();
        getAllStudyInfo();
        myStudySearchInput.addTextChangedListener(new TextWatcher() {
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

    @OnClick(R.id.my_study_root)
    public void myStudyClick() {
        hideInputMethod(myStudyRoot);
    }

    @OnClick(R.id.my_study_search_input)
    public void inputClick() {
        showInputMethod(myStudySearchInput);
    }

    @OnClick(R.id.my_study_search_btn)
    public void searchBtnClick() {
        hideInputMethod(myStudySearchBtn);
        String name = myStudySearchInput.getText().toString().trim();
        if (name.length() >= 1) {
            searchResult(name);
        } else {
            Toast.makeText(this, "请输入正确的进度!", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.my_study_back)
    public void studyBackClick() {
        finish();
    }

    private void getAllStudyInfo() {
        String url = HttpUtils.setParameterForUrl(GET_ALL_STUDY_INFO, "no", loginNo);
        HttpUtils.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyStudyActivity.this, "获取失败!", Toast.LENGTH_SHORT).show();
                        if (!MyStudyActivity.this.isFinishing() && customProgressDialog != null && customProgressDialog.isShowing()) {
                            customProgressDialog.cancelProgressDialog();
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                myStudyInfoDataBean = new Gson().fromJson(response.body().string(), MyStudyInfoDataBean.class);
                if (myStudyInfoDataBean != null && myStudyInfoDataBean.getAllStudyInfo() != null && !myStudyInfoDataBean.getAllStudyInfo().isEmpty()) {
                    MyStudyInfoAdapter myStudyInfoAdapter = new MyStudyInfoAdapter(MyStudyActivity.this, myStudyInfoDataBean);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyStudyActivity.this);
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!MyStudyActivity.this.isFinishing() && customProgressDialog != null && customProgressDialog.isShowing()) {
                                customProgressDialog.cancelProgressDialog();
                            }
                            recyclerViewAllStudyInfo.setLayoutManager(linearLayoutManager);
                            recyclerViewAllStudyInfo.setAdapter(myStudyInfoAdapter);
                        }
                    });
                }

            }
        });
    }

    public void hideInput() {
        hideInputMethod(myStudyRoot);
    }

    private void searchResult(String name) {
        if (myStudyInfoDataBean != null && myStudyInfoDataBean.getAllStudyInfo() != null) {
            List<MyStudyInfoDataBean.AllStudyInfoBean> allStudyInfoBeanList = new ArrayList<>();
            for (MyStudyInfoDataBean.AllStudyInfoBean allStudyInfoBean : myStudyInfoDataBean.getAllStudyInfo()) {
                if (StudyUtils.getStudyProgress(allStudyInfoBean.getStudyProgress()).contains(name)) {
                    allStudyInfoBeanList.add(allStudyInfoBean);
                }
            }
            searchResultBean.setAllStudyInfo(allStudyInfoBeanList);
        }
        ((MyStudyInfoAdapter) recyclerViewAllStudyInfo.getAdapter()).updateData(searchResultBean);
    }

    private void showAllStudent() {
        ((MyStudyInfoAdapter) recyclerViewAllStudyInfo.getAdapter()).updateData(myStudyInfoDataBean);
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
        myStudySearchInput.setFocusable(false);
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            if (inputMethodManager.isActive()) {
                inputMethodManager.hideSoftInputFromWindow(targetView.getWindowToken(), 0);
            }
        }
    }
}

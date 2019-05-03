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
import com.wcedla.driving_school.R;
import com.wcedla.driving_school.adapter.StudyInfoDetialAdapter;
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

public class StudyInfoDetialActivity extends AppCompatActivity {

    String no;
    public CustomProgressDialog customProgressDialog;
    MyStudyInfoDataBean myStudyInfoDataBean;
    MyStudyInfoDataBean searchResultBean = new MyStudyInfoDataBean();
    ;
    @BindView(R.id.study_detial_back)
    ImageView studyDetialBack;
    @BindView(R.id.study_detial_search_input)
    EditText studyDetialSearchInput;
    @BindView(R.id.study_detial_search_btn)
    Button studyDetialSearchBtn;
    @BindView(R.id.study_detial_recycler_view)
    RecyclerView studyDetialRecyclerView;
    @BindView(R.id.study_detial_root)
    ConstraintLayout studyDetialRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolUtils.setNavigationBarStatusBarTranslucent(this, false, Color.parseColor("#000000"));
        setContentView(R.layout.activity_study_info_detial);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            no = bundle.getString("no");
        }
        customProgressDialog = CustomProgressDialog.create(this, "正在获取数据...", false);
        customProgressDialog.showProgressDialog();
        getStudyDetial();
        studyDetialSearchInput.addTextChangedListener(new TextWatcher() {
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

    @OnClick(R.id.study_detial_back)
    public void backClick() {
        finish();
    }

    @OnClick(R.id.study_detial_root)
    public void detialRootClick() {
        hideInputMethod(studyDetialRoot);
    }

    @OnClick(R.id.study_detial_search_input)
    public void inputClick() {
        showInputMethod(studyDetialSearchInput);
    }

    @OnClick(R.id.study_detial_search_btn)
    public void searchBtnclick() {
        hideInputMethod(studyDetialSearchBtn);
        String name = studyDetialSearchInput.getText().toString().trim();
        if (name.length() >= 1) {
            searchResult(name);
        } else {
            Toast.makeText(this, "请输入正确的项目名称!", Toast.LENGTH_SHORT).show();
        }
    }

    private void getStudyDetial() {
        String url = HttpUtils.setParameterForUrl(GET_ALL_STUDY_INFO, "no", no);
        HttpUtils.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(StudyInfoDetialActivity.this, "获取失败！", Toast.LENGTH_SHORT).show();
                        if (!StudyInfoDetialActivity.this.isFinishing() && customProgressDialog != null && customProgressDialog.isShowing()) {
                            customProgressDialog.cancelProgressDialog();
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                myStudyInfoDataBean = new Gson().fromJson(response.body().string(), MyStudyInfoDataBean.class);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(StudyInfoDetialActivity.this);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                StudyInfoDetialAdapter studyInfoDetialAdapter = new StudyInfoDetialAdapter(StudyInfoDetialActivity.this, myStudyInfoDataBean);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!StudyInfoDetialActivity.this.isFinishing() && customProgressDialog != null && customProgressDialog.isShowing()) {
                            customProgressDialog.cancelProgressDialog();
                        }
                        studyDetialRecyclerView.setLayoutManager(linearLayoutManager);
                        studyDetialRecyclerView.setAdapter(studyInfoDetialAdapter);
                    }
                });
            }
        });
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
        ((StudyInfoDetialAdapter) studyDetialRecyclerView.getAdapter()).updateData(searchResultBean);
    }

    private void showAllStudent() {
        ((StudyInfoDetialAdapter) studyDetialRecyclerView.getAdapter()).updateData(myStudyInfoDataBean);
    }


    public void hideInput() {
        hideInputMethod(studyDetialRoot);
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
        studyDetialSearchInput.setFocusable(false);
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            if (inputMethodManager.isActive()) {
                inputMethodManager.hideSoftInputFromWindow(targetView.getWindowToken(), 0);
            }
        }
    }
}

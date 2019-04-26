package com.wcedla.driving_school.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.gson.Gson;
import com.orhanobut.hawk.Hawk;
import com.wcedla.driving_school.R;
import com.wcedla.driving_school.adapter.StudyInfoShowAdapter;
import com.wcedla.driving_school.bean.StudyInfoDataBean;
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

import static com.wcedla.driving_school.constant.Config.GET_STUDY_INFO;
import static com.wcedla.driving_school.constant.Config.USERNAME_REGEX;

public class StudyProgressActivity extends AppCompatActivity {

    @BindView(R.id.study_progress_back)
    ImageView studyProgressBack;
    @BindView(R.id.study_search_input)
    EditText studySearchInput;
    @BindView(R.id.study_search_btn)
    Button studySearchBtn;
    @BindView(R.id.study_info_show)
    RecyclerView studyInfoShow;
    @BindView(R.id.study_root)
    ConstraintLayout studyRoot;

    public CustomProgressDialog customProgressDialog;

    StudyInfoDataBean studyInfoDataBean;
    StudyInfoDataBean searchResultBean = new StudyInfoDataBean();


    String no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolUtils.setNavigationBarStatusBarTranslucent(this, false, Color.parseColor("#000000"));
        setContentView(R.layout.activity_study_progress);
        ButterKnife.bind(this);
        no = Hawk.get("loginNo", "");
        setInputFilter(studySearchInput, USERNAME_REGEX, 5);
        getStudyData();
        customProgressDialog = CustomProgressDialog.create(this, "正在查找...", false);
        customProgressDialog.showProgressDialog();
        studySearchInput.addTextChangedListener(new TextWatcher() {
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

    @OnClick(R.id.study_root)
    public void studyRootClick() {
        hideInputMethod(studyRoot);
    }

    @OnClick(R.id.study_progress_back)
    public void studyProgressBackClick() {
        finish();
    }

    @OnClick(R.id.study_search_input)
    public void searchClick() {
        showInputMethod(studySearchInput);
    }

    @OnClick(R.id.study_search_btn)
    public void searchBtnClick() {
        hideInputMethod(studySearchBtn);
        String name = studySearchInput.getText().toString().trim();
        if (name.length() >= 2) {
            searchResult(name);
        } else {
            Toast.makeText(this, "请输入正确的姓名!", Toast.LENGTH_SHORT).show();
        }
    }

    public void hideInput() {
        hideInputMethod(studySearchInput);
    }

    private void searchResult(String name) {
        if (studyInfoDataBean != null && studyInfoDataBean.getStudyInfo() != null) {
            List<StudyInfoDataBean.StudyInfoBean> studyInfoBeanList = new ArrayList<>();
            for (StudyInfoDataBean.StudyInfoBean studyInfoBean : studyInfoDataBean.getStudyInfo()) {
                if (name.equals(studyInfoBean.getRealName())) {
                    studyInfoBeanList.add(studyInfoBean);
                }
            }
            searchResultBean.setStudyInfo(studyInfoBeanList);
        }
        ((StudyInfoShowAdapter) studyInfoShow.getAdapter()).updateData(searchResultBean);
    }

    private void showAllStudent() {
        if (studyInfoShow != null & studyInfoShow.getAdapter() != null) {
            ((StudyInfoShowAdapter) studyInfoShow.getAdapter()).updateData(studyInfoDataBean);
        }
    }


    private void getStudyData() {
        String url = HttpUtils.setParameterForUrl(GET_STUDY_INFO, "no", no);
        HttpUtils.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(StudyProgressActivity.this, "查找学习进度出错!", Toast.LENGTH_SHORT).show();
                        if (!StudyProgressActivity.this.isFinishing() && customProgressDialog != null && customProgressDialog.isShowing()) {
                            customProgressDialog.cancelProgressDialog();
                            finish();
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                studyInfoDataBean = new Gson().fromJson(response.body().string(), StudyInfoDataBean.class);
                StudyInfoShowAdapter studyInfoShowAdapter = new StudyInfoShowAdapter(StudyProgressActivity.this, studyInfoDataBean);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(StudyProgressActivity.this);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        studyInfoShow.setLayoutManager(linearLayoutManager);
                        studyInfoShow.setAdapter(studyInfoShowAdapter);
                        if (!StudyProgressActivity.this.isFinishing() && customProgressDialog != null && customProgressDialog.isShowing()) {
                            customProgressDialog.cancelProgressDialog();
                        }
                    }
                });
            }
        });
    }

    public void showPop() {
        View progressView = LayoutInflater.from(this).inflate(R.layout.study_progress_select_pop, null);
        PopupWindow progressPop = new PopupWindow(progressView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置PopupWindow的背景
        progressPop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 设置PopupWindow是否能响应外部点击事件
        progressPop.setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        progressPop.setTouchable(true);
        progressPop.setFocusable(true);
        progressPop.showAtLocation(studyRoot, Gravity.BOTTOM, 0, 0);
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
        studySearchInput.setFocusable(false);
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            if (inputMethodManager.isActive()) {
                inputMethodManager.hideSoftInputFromWindow(targetView.getWindowToken(), 0);
            }
        }
    }
}

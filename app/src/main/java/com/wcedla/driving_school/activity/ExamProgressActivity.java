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
import com.wcedla.driving_school.adapter.ExamInfoShowAdapter;
import com.wcedla.driving_school.bean.ExamInfoDataBean;
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

import static com.wcedla.driving_school.constant.Config.GET_EXAM_PROGRESS;
import static com.wcedla.driving_school.constant.Config.USERNAME_REGEX;

public class ExamProgressActivity extends AppCompatActivity {

    @BindView(R.id.exam_progress_back)
    ImageView examProgressBack;
    @BindView(R.id.exam_search_input)
    EditText examSearchInput;
    @BindView(R.id.exam_search_btn)
    Button examSearchBtn;
    @BindView(R.id.exam_info_show)
    RecyclerView examInfoShow;
    @BindView(R.id.exam_root)
    ConstraintLayout examRoot;

    public CustomProgressDialog customProgressDialog;

    String no;

    public ExamInfoDataBean examInfoDataBean;
    ExamInfoDataBean searchResultBean = new ExamInfoDataBean();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolUtils.setNavigationBarStatusBarTranslucent(this, false, Color.parseColor("#000000"));
        setContentView(R.layout.activity_exam_progress);
        ButterKnife.bind(this);
        no = Hawk.get("loginNo", "");
        setInputFilter(examSearchInput, USERNAME_REGEX, 10);
        customProgressDialog = CustomProgressDialog.create(this, "正在查找...", false);
        customProgressDialog.showProgressDialog();
        getExamInfoData();
        examSearchInput.addTextChangedListener(new TextWatcher() {
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

    @OnClick(R.id.exam_progress_back)
    public void examBackClick() {
        finish();
    }

    @OnClick(R.id.exam_root)
    public void examRootClick() {
        hideInputMethod(examRoot);
    }

    @OnClick(R.id.exam_search_input)
    public void examSearchInputClick() {
        showInputMethod(examSearchInput);
    }

    @OnClick(R.id.exam_search_btn)
    public void examSearchBtnClick() {
        hideInputMethod(examSearchBtn);
        String name = examSearchInput.getText().toString().trim();
        if (name.length() >= 1) {
            searchResult(name);
        } else {
            Toast.makeText(this, "请输入正确的姓名!", Toast.LENGTH_SHORT).show();
        }
    }

    private void searchResult(String name) {
        if (examInfoDataBean != null && examInfoDataBean.getExamInfo() != null) {
            List<ExamInfoDataBean.ExamInfoBean> examInfoBeanList = new ArrayList<>();
            for (ExamInfoDataBean.ExamInfoBean examInfoBean : examInfoDataBean.getExamInfo()) {
                if (examInfoBean.getRealName().contains(name) || examInfoBean.getStudentNo().contains(name)) {
                    examInfoBeanList.add(examInfoBean);
                }
            }
            searchResultBean.setExamInfo(examInfoBeanList);
        }
        ((ExamInfoShowAdapter) examInfoShow.getAdapter()).updateData(searchResultBean);
    }

    private void showAllStudent() {
        if (examInfoDataBean != null & examInfoShow.getAdapter() != null) {
            ((ExamInfoShowAdapter) examInfoShow.getAdapter()).updateData(examInfoDataBean);
        }
    }


    public void hideInput() {
        hideInputMethod(examSearchInput);
    }

    private void getExamInfoData() {
        String url = HttpUtils.setParameterForUrl(GET_EXAM_PROGRESS, "no", no);
        HttpUtils.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ExamProgressActivity.this, "查找考试进度出错!", Toast.LENGTH_SHORT).show();
                        if (!ExamProgressActivity.this.isFinishing() && customProgressDialog != null && customProgressDialog.isShowing()) {
                            customProgressDialog.cancelProgressDialog();
                            finish();
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                examInfoDataBean = new Gson().fromJson(response.body().string(), ExamInfoDataBean.class);
                ExamInfoShowAdapter adapter = new ExamInfoShowAdapter(ExamProgressActivity.this, examInfoDataBean);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ExamProgressActivity.this);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        examInfoShow.setLayoutManager(linearLayoutManager);
                        examInfoShow.setAdapter(adapter);
                        if (!ExamProgressActivity.this.isFinishing() && customProgressDialog != null && customProgressDialog.isShowing()) {
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
        examSearchInput.setFocusable(false);
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            if (inputMethodManager.isActive()) {
                inputMethodManager.hideSoftInputFromWindow(targetView.getWindowToken(), 0);
            }
        }
    }
}

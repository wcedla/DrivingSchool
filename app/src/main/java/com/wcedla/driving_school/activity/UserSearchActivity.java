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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.orhanobut.hawk.Hawk;
import com.wcedla.driving_school.R;
import com.wcedla.driving_school.adapter.StduentInfoAdapter;
import com.wcedla.driving_school.bean.StudentDataBean;
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

import static com.wcedla.driving_school.constant.Config.GET_STUDENT_INFO;
import static com.wcedla.driving_school.constant.Config.USERNAME_REGEX;

public class UserSearchActivity extends AppCompatActivity {


    @BindView(R.id.search_input)
    EditText searchInput;
    @BindView(R.id.search_btn)
    Button searchBtn;
    @BindView(R.id.search_root)
    LinearLayout searchRoot;
    @BindView(R.id.user_info_show)
    RecyclerView userInfoShow;
    @BindView(R.id.user_search_root)
    ConstraintLayout userSearchRoot;

    public CustomProgressDialog customProgressDialog;

    String no;

    public StudentDataBean studentDataBean;
    StudentDataBean searchResultBean = new StudentDataBean();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolUtils.setNavigationBarStatusBarTranslucent(this, false, Color.parseColor("#000000"));
        setContentView(R.layout.activity_user_search);
        ButterKnife.bind(this);
        no = Hawk.get("loginNo", "");
        setInputFilter(searchInput, USERNAME_REGEX, 10);
        getStudentInfo();
        customProgressDialog = CustomProgressDialog.create(this, "正在查找...", false);
        customProgressDialog.showProgressDialog();
        searchInput.addTextChangedListener(new TextWatcher() {
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

    @OnClick(R.id.user_search_root)
    public void rootClick() {
        hideInputMethod(userSearchRoot);
    }

    @OnClick(R.id.search_input)
    public void searchInputClick() {
        showInputMethod(searchInput);
    }

    @OnClick(R.id.search_btn)
    public void searchBtnClick() {
        hideInputMethod(searchBtn);
        String name = searchInput.getText().toString().trim();
        if (name.length() >= 1) {
            searchResult(name);
        } else {
            Toast.makeText(this, "请输入正确的姓名!", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.user_search_back)
    public void searchBack()
    {
        finish();
    }


    private void getStudentInfo() {
        String url = HttpUtils.setParameterForUrl(GET_STUDENT_INFO, "no", no);
        HttpUtils.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(UserSearchActivity.this, "查找学员出错!", Toast.LENGTH_SHORT).show();
                        if (!UserSearchActivity.this.isFinishing() && customProgressDialog != null && customProgressDialog.isShowing()) {
                            customProgressDialog.cancelProgressDialog();
                            finish();
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                studentDataBean = new Gson().fromJson(response.body().string(), StudentDataBean.class);
                StduentInfoAdapter stduentInfoAdapter = new StduentInfoAdapter(UserSearchActivity.this, studentDataBean);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UserSearchActivity.this);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        userInfoShow.setLayoutManager(linearLayoutManager);
                        userInfoShow.setAdapter(stduentInfoAdapter);
                        if (!UserSearchActivity.this.isFinishing() && customProgressDialog != null && customProgressDialog.isShowing()) {
                            customProgressDialog.cancelProgressDialog();
                        }
                    }
                });

            }
        });
    }

    private void searchResult(String name) {
        if (studentDataBean != null && studentDataBean.getStudentInfo() != null) {
            List<StudentDataBean.StudentInfoBean> studentInfoBeanList = new ArrayList<>();
            for (StudentDataBean.StudentInfoBean studentInfoBean : studentDataBean.getStudentInfo()) {
                if (studentInfoBean.getRealName().contains(name) || studentInfoBean.getStudentNo().contains(name)) {
                    studentInfoBeanList.add(studentInfoBean);
                }
            }
            searchResultBean.setStudentInfo(studentInfoBeanList);
        }
        ((StduentInfoAdapter) userInfoShow.getAdapter()).updateData(searchResultBean);
    }

    private void showAllStudent() {
        ((StduentInfoAdapter) userInfoShow.getAdapter()).updateData(studentDataBean);
    }


    public void hideInput() {
        hideInputMethod(userSearchRoot);
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
        searchInput.setFocusable(false);
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            if (inputMethodManager.isActive()) {
                inputMethodManager.hideSoftInputFromWindow(targetView.getWindowToken(), 0);
            }
        }
    }
}

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
import com.orhanobut.logger.Logger;
import com.wcedla.driving_school.R;
import com.wcedla.driving_school.adapter.AllUserInfoAdapter;
import com.wcedla.driving_school.bean.AllUserInfoDataBean;
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

import static com.wcedla.driving_school.constant.Config.GET_ALL_USER_INFO;

public class ShowUserInfoActivity extends AppCompatActivity {

    @BindView(R.id.user_info_back)
    ImageView userInfoBack;
    @BindView(R.id.user_info_all)
    RadioButton userInfoAll;
    @BindView(R.id.user_info_coach)
    RadioButton userInfoCoach;
    @BindView(R.id.user_info_student)
    RadioButton userInfoStudent;
    @BindView(R.id.user_info_radio_group)
    RadioGroup userInfoRadioGroup;
    @BindView(R.id.user_info_search_input)
    EditText userInfoSearchInput;
    @BindView(R.id.user_info_search_btn)
    Button userInfoSearchBtn;
    @BindView(R.id.user_info_recycler_view)
    RecyclerView userInfoRecyclerView;
    @BindView(R.id.user_info_root)
    ConstraintLayout userInfoRoot;

    public CustomProgressDialog customProgressDialog;
    public AllUserInfoDataBean allUserInfoDataBean;
    AllUserInfoDataBean coachInfoDataBean = new AllUserInfoDataBean();
    AllUserInfoDataBean studentInfoDataBean = new AllUserInfoDataBean();
    AllUserInfoDataBean searchInfoDataBean = new AllUserInfoDataBean();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolUtils.setNavigationBarStatusBarTranslucent(this, false, false);
        setContentView(R.layout.activity_show_user_info);
        ButterKnife.bind(this);
        customProgressDialog = CustomProgressDialog.create(this, "正在获取数据...", false);
        customProgressDialog.showProgressDialog();
        getAllUserInfo();
    }

    @OnClick(R.id.user_info_back)
    public void backClick() {
        finish();
    }


    @OnClick(R.id.user_info_root)
    public void userInfoRootClick() {
        hideInputMethod(userInfoRoot);
    }


    @OnClick(R.id.user_info_search_input)
    public void inputClick() {
        showInputMethod(userInfoSearchInput);
    }

    @OnClick(R.id.user_info_all)
    public void allClick() {
        ((AllUserInfoAdapter) userInfoRecyclerView.getAdapter()).updateData(allUserInfoDataBean, 0);
    }

    @OnClick(R.id.user_info_coach)
    public void coachClick() {
        coachInfoDataBean.setCoachUser(allUserInfoDataBean.getCoachUser());
        ((AllUserInfoAdapter) userInfoRecyclerView.getAdapter()).updateData(coachInfoDataBean, 1);
    }

    @OnClick(R.id.user_info_student)
    public void studedntClick() {
        studentInfoDataBean.setStudentUser(allUserInfoDataBean.getStudentUser());
        ((AllUserInfoAdapter) userInfoRecyclerView.getAdapter()).updateData(studentInfoDataBean, 2);
    }

    @OnClick(R.id.user_info_search_btn)
    public void userSearchClick() {
        hideInputMethod(userInfoSearchBtn);
        String name = userInfoSearchInput.getText().toString().trim();
        if (name.length() > 0) {
            List<AllUserInfoDataBean.CoachUserBean> coachUserBeanList = new ArrayList<>();
            List<AllUserInfoDataBean.StudentUserBean> studentUserBeanList = new ArrayList<>();
            for (AllUserInfoDataBean.CoachUserBean coachUserBean : allUserInfoDataBean.getCoachUser()) {
                if (coachUserBean.getRealName().contains(name) || coachUserBean.getCoachNo().contains(name)) {
                    coachUserBeanList.add(coachUserBean);
                }
            }
            for (AllUserInfoDataBean.StudentUserBean studentUserBean : allUserInfoDataBean.getStudentUser()) {
                if (studentUserBean.getRealName().contains(name) || studentUserBean.getStudentNo().contains(name)) {
                    studentUserBeanList.add(studentUserBean);
                }
            }
            searchInfoDataBean.setStudentUser(studentUserBeanList);
            searchInfoDataBean.setCoachUser(coachUserBeanList);
            Logger.d("数据:" + studentUserBeanList + "," + coachUserBeanList + "," + searchInfoDataBean.getCoachUser().size() + "," + searchInfoDataBean.getStudentUser().size());
            ((AllUserInfoAdapter) userInfoRecyclerView.getAdapter()).updateData(searchInfoDataBean, 0);
        } else {
            Toast.makeText(ShowUserInfoActivity.this, "请输入内容！", Toast.LENGTH_SHORT).show();
        }
    }


    private void getAllUserInfo() {
        HttpUtils.doHttpRequest(GET_ALL_USER_INFO, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ShowUserInfoActivity.this, "获取数据失败！", Toast.LENGTH_SHORT).show();
                        if (!ShowUserInfoActivity.this.isFinishing() && customProgressDialog != null && customProgressDialog.isShowing()) {
                            customProgressDialog.cancelProgressDialog();
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                allUserInfoDataBean = new Gson().fromJson(response.body().string(), AllUserInfoDataBean.class);
                AllUserInfoAdapter allUserInfoAdapter = new AllUserInfoAdapter(ShowUserInfoActivity.this, allUserInfoDataBean);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShowUserInfoActivity.this);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        userInfoRecyclerView.setLayoutManager(linearLayoutManager);
                        userInfoRecyclerView.setAdapter(allUserInfoAdapter);
                        if (!ShowUserInfoActivity.this.isFinishing() && customProgressDialog != null && customProgressDialog.isShowing()) {
                            customProgressDialog.cancelProgressDialog();
                        }
                    }
                });
            }
        });
    }

    public void hideInput() {
        hideInputMethod(userInfoSearchInput);
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
        userInfoSearchInput.setFocusable(false);
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            if (inputMethodManager.isActive()) {
                inputMethodManager.hideSoftInputFromWindow(targetView.getWindowToken(), 0);
            }
        }
    }
}

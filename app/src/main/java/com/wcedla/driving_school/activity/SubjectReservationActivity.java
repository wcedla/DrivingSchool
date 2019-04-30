package com.wcedla.driving_school.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.wcedla.driving_school.R;
import com.wcedla.driving_school.tool.ToolUtils;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SubjectReservationActivity extends AppCompatActivity {

    @BindView(R.id.subject_back)
    ImageView subjectBack;
    @BindView(R.id.subject_search_input)
    EditText subjectSearchInput;
    @BindView(R.id.subject_search_btn)
    Button subjectSearchBtn;
    @BindView(R.id.subject_info_show)
    RecyclerView subjectInfoShow;
    @BindView(R.id.subject_root)
    ConstraintLayout subjectRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolUtils.setNavigationBarStatusBarTranslucent(this, false, Color.parseColor("#000000"));
        setContentView(R.layout.activity_subject_reservation);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.subject_back)
    public void subjectBackClick() {
        finish();
    }

    @OnClick(R.id.subject_root)
    public void subjectRootClick() {
        hideInputMethod(subjectRoot);
    }

    @OnClick(R.id.subject_search_input)
    public void subjctInputClick() {
        showInputMethod(subjectSearchInput);
    }

    @OnClick(R.id.subject_search_btn)
    public void subjectBtnClick() {
        hideInputMethod(subjectSearchBtn);
    }


    private void getSubjectInfo() {

    }


    public void hideInput() {
        hideInputMethod(subjectSearchInput);
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
        subjectSearchInput.setFocusable(false);
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            if (inputMethodManager.isActive()) {
                inputMethodManager.hideSoftInputFromWindow(targetView.getWindowToken(), 0);
            }
        }
    }
}

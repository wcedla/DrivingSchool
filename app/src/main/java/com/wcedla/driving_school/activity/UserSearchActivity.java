package com.wcedla.driving_school.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;
import com.wcedla.driving_school.R;
import com.wcedla.driving_school.customview.CustomProgressDialog;
import com.wcedla.driving_school.tool.HttpUtils;
import com.wcedla.driving_school.tool.ToolUtils;

import java.io.IOException;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.wcedla.driving_school.constant.Config.GET_STUDENT_INFO;
import static com.wcedla.driving_school.constant.Config.INPUTMETHOD_DELAY;
import static com.wcedla.driving_school.constant.Config.USERNAME_MAX_LENGTH;
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

    CustomProgressDialog customProgressDialog;

    String no;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolUtils.setNavigationBarStatusBarTranslucent(this, false, Color.parseColor("#000000"));
        setContentView(R.layout.activity_user_search);
        ButterKnife.bind(this);
        no= Hawk.get("loginNo","");
        setInputFilter(searchInput,USERNAME_REGEX,5);

        customProgressDialog=CustomProgressDialog.create(this,"正在查找...",false);
        customProgressDialog.showProgressDialog();

        //延迟打开输入法，不用用户点击弹出输入法，增加易用性
//        delayTask(new Runnable() {
//            @Override
//            public void run() {
//                showInputMethod(searchInput);
//
//            }
//        }, INPUTMETHOD_DELAY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //延迟打开输入法，不用用户点击弹出输入法，增加易用性
//        delayTask(new Runnable() {
//            @Override
//            public void run() {
//                showInputMethod(searchInput);
//
//            }
//        }, INPUTMETHOD_DELAY);
    }

    @OnClick(R.id.user_search_root)
    public void rootClick()
    {
        hideInputMethod(userSearchRoot);
    }

    @OnClick(R.id.search_input)
    public void searchInputClick() {
        showInputMethod(searchInput);
    }

    @OnClick(R.id.search_btn)
    public void searchBtnClick()
    {
        hideInputMethod(searchBtn);
    }

    private void getStudentInfo()
    {
        String url= HttpUtils.setParameterForUrl(GET_STUDENT_INFO,"no",no);
        HttpUtils.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(UserSearchActivity.this,"查找学员出错!",Toast.LENGTH_SHORT).show();
                        if(!UserSearchActivity.this.isFinishing()&&customProgressDialog!=null&&customProgressDialog.isShowing())
                        {
                            customProgressDialog.cancelProgressDialog();
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

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
        searchInput.setFocusable(false);
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            if (inputMethodManager.isActive()) {
                inputMethodManager.hideSoftInputFromWindow(targetView.getWindowToken(), 0);
            }
        }
    }

    /**
     * 简单封装的Handler延迟任务方法
     *
     * @param runnable    需要延迟运行的任务
     * @param delayMillis 延迟时间（毫秒）
     */
    private void delayTask(Runnable runnable, long delayMillis) {
        new Handler().postDelayed(runnable, delayMillis);
    }
}

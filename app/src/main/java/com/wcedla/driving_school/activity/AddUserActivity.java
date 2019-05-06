package com.wcedla.driving_school.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wcedla.driving_school.R;
import com.wcedla.driving_school.customview.CustomProgressDialog;
import com.wcedla.driving_school.tool.HttpUtils;
import com.wcedla.driving_school.tool.JsonUtils;
import com.wcedla.driving_school.tool.ToolUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.wcedla.driving_school.constant.Config.ADD_USER_INFO;
import static com.wcedla.driving_school.constant.Config.EMAIL_CHECK_REGEX;
import static com.wcedla.driving_school.constant.Config.MOBILE_REGEX;
import static com.wcedla.driving_school.constant.Config.USERNAME_REGEX;

public class AddUserActivity extends AppCompatActivity {

    @BindView(R.id.add_user_back)
    ImageView addUserBack;
    @BindView(R.id.add_user_coach)
    RadioButton addUserCoach;
    @BindView(R.id.add_user_student)
    RadioButton addUserStudent;
    @BindView(R.id.add_user_no_head)
    TextView addUserNoHead;
    @BindView(R.id.add_user_no_text)
    EditText addUserNoText;
    @BindView(R.id.add_user_name_text)
    EditText addUserNameText;
    @BindView(R.id.add_user_time_head)
    TextView addUserTimeHead;
    @BindView(R.id.add_user_time_text)
    EditText addUserTimeText;
    @BindView(R.id.add_user_mobile_text)
    EditText addUserMobileText;
    @BindView(R.id.add_user_coach_head)
    TextView addUserCoachHead;
    @BindView(R.id.add_user_coach_text)
    EditText addUserCoachText;
    @BindView(R.id.coach_split_view)
    View coachSplitView;
    @BindView(R.id.add_user_btn)
    Button addUserBtn;
    @BindView(R.id.add_user_enroll_head)
    TextView addUserEnrollHead;
    @BindView(R.id.add_user_enroll_text)
    EditText addUserEnrollText;
    @BindView(R.id.enroll_split)
    View enrollSplit;
    @BindView(R.id.time_split)
    View timeSplit;
    @BindView(R.id.add_user_root)
    ConstraintLayout addUserRoot;

    Calendar calendar;
    SimpleDateFormat simpleDateFormat;

    CustomProgressDialog customProgressDialog;
    @BindView(R.id.add_user_radio_group)
    RadioGroup addUserRadioGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolUtils.setNavigationBarStatusBarTranslucent(this, false, false);
        setContentView(R.layout.activity_add_user);
        ButterKnife.bind(this);
        calendar = Calendar.getInstance();
        setInputFilter(addUserNoText, EMAIL_CHECK_REGEX, 10);
        setInputFilter(addUserNameText, USERNAME_REGEX, 6);
        setInputFilter(addUserTimeText, EMAIL_CHECK_REGEX, 2);
        setInputFilter(addUserMobileText, EMAIL_CHECK_REGEX, 11);
        setInputFilter(addUserCoachText, EMAIL_CHECK_REGEX, 10);
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        addUserEnrollText.setText(simpleDateFormat.format(calendar.getTime()));
        customProgressDialog = CustomProgressDialog.create(AddUserActivity.this, "正在处理...", false);
    }

    @OnClick(R.id.add_user_back)
    public void backClick() {
        hideInputMethod(addUserBack);
        finish();
    }

    @OnClick(R.id.add_user_root)
    public void rootClick() {
        hideInputMethod(addUserRoot);
    }

    @OnClick(R.id.add_user_no_text)
    public void noClick() {
        showInputMethod(addUserNoText);
    }

    @OnClick(R.id.add_user_name_text)
    public void nameClick() {
        showInputMethod(addUserNameText);
    }

    @OnClick(R.id.add_user_time_text)
    public void timeClick() {
        showInputMethod(addUserTimeText);
    }

    @OnClick(R.id.add_user_mobile_text)
    public void mobileClick() {
        showInputMethod(addUserMobileText);
    }

    @OnClick(R.id.add_user_coach_text)
    public void coachInputClick() {
        showInputMethod(addUserCoachText);
    }

    @OnClick(R.id.add_user_btn)
    public void adduserBtnClick() {
        hideInputMethod(addUserBtn);
        String type;
        String time;
        String coach;
        if (addUserRadioGroup.getCheckedRadioButtonId() == addUserCoach.getId()) {
            type = "教练";
            time = addUserTimeText.getText().toString().trim() + "年";
            coach = "无";
        } else {
            type = "学员";
            time = addUserEnrollText.getText().toString().trim();
            coach = addUserCoachText.getText().toString().trim();
        }
        String no = addUserNoText.getText().toString().trim();
        String name = addUserNameText.getText().toString().trim();
        String mobile = addUserMobileText.getText().toString().trim();
        if (no.length() < 1 || name.length() < 1 || time.length() < 2 || mobile.length() < 1 || coach.length() < 1) {
            Toast.makeText(AddUserActivity.this, "请输入内容！", Toast.LENGTH_SHORT).show();
        } else {
            if (no.length() < 10 || (type.equals("学员") && coach.length() < 10)) {
                Toast.makeText(AddUserActivity.this, "编号为10位，请检查编号！", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!Pattern.matches(MOBILE_REGEX, mobile)) {
                Toast.makeText(AddUserActivity.this, "请输入正确的手机号！", Toast.LENGTH_SHORT).show();
                return;
            }
            addUser(no, name, type, time, mobile, coach);
        }
    }

    @OnClick(R.id.add_user_coach)
    public void coachClick() {
        hideInputMethod(addUserCoach);
        addUserNoHead.setText("教练号:");
        addUserNoText.setText("");
        addUserNameText.setText("");
        addUserTimeText.setText("");
        addUserMobileText.setText("");
        addUserTimeText.setVisibility(View.VISIBLE);
        addUserTimeHead.setVisibility(View.VISIBLE);
        timeSplit.setVisibility(View.VISIBLE);
        enrollSplit.setVisibility(View.GONE);
        addUserEnrollHead.setVisibility(View.GONE);
        addUserEnrollText.setVisibility(View.GONE);
        addUserCoachHead.setVisibility(View.GONE);
        addUserCoachText.setVisibility(View.GONE);
        coachSplitView.setVisibility(View.GONE);
    }

    @OnClick(R.id.add_user_student)
    public void studentClick() {
        hideInputMethod(addUserStudent);
        addUserNoHead.setText("学员号:");
        addUserNoText.setText("");
        addUserNameText.setText("");
        addUserTimeText.setText("");
        addUserMobileText.setText("");
        addUserCoachText.setText("");
        addUserTimeText.setVisibility(View.GONE);
        addUserTimeHead.setVisibility(View.GONE);
        timeSplit.setVisibility(View.GONE);
        enrollSplit.setVisibility(View.VISIBLE);
        addUserEnrollHead.setVisibility(View.VISIBLE);
        addUserEnrollText.setVisibility(View.VISIBLE);
        addUserCoachHead.setVisibility(View.VISIBLE);
        addUserCoachText.setVisibility(View.VISIBLE);
        coachSplitView.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.add_user_enroll_text)
    public void enrollClick() {
        View view = LayoutInflater.from(AddUserActivity.this).inflate(R.layout.pop_date_picker, null);
        DatePicker datePicker = view.findViewById(R.id.date_picker);


        PopupWindow datePopWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置PopupWindow的背景
        datePopWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 设置PopupWindow是否能响应外部点击事件
        datePopWindow.setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        datePopWindow.setTouchable(true);
        datePopWindow.setFocusable(true);
        datePopWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                try {
                    addUserEnrollText.setText(simpleDateFormat.format(simpleDateFormat.parse(date)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                datePopWindow.dismiss();
            }
        });

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();

        layoutParams.alpha = 0.7f;

        getWindow().setAttributes(layoutParams);
        datePopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams layoutParams = getWindow().getAttributes();

                layoutParams.alpha = 1.0f;

                getWindow().setAttributes(layoutParams);
            }
        });
    }

    private void addUser(String no, String name, String type, String time, String mobile, String coach) {
        customProgressDialog.showProgressDialog();
        String url = HttpUtils.setParameterForUrl(ADD_USER_INFO, "no", no, "type", type, "name", name, "time", time, "mobile", mobile, "coach", coach);
        HttpUtils.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AddUserActivity.this, "添加失败，请重试！", Toast.LENGTH_SHORT).show();
                        customProgressDialog.cancelProgressDialog();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int result = JsonUtils.getResetStatus(response.body().string());
                if (result == -1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AddUserActivity.this, "添加成功！", Toast.LENGTH_SHORT).show();
                            customProgressDialog.cancelProgressDialog();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AddUserActivity.this, "添加失败，请重试！", Toast.LENGTH_SHORT).show();
                            customProgressDialog.cancelProgressDialog();
                        }
                    });
                }
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
        addUserNoText.setFocusable(false);
        addUserNameText.setFocusable(false);
        addUserTimeText.setFocusable(false);
        addUserMobileText.setFocusable(false);
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            if (inputMethodManager.isActive()) {
                inputMethodManager.hideSoftInputFromWindow(targetView.getWindowToken(), 0);
            }
        }
    }
}

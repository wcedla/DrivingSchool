package com.wcedla.driving_school.activity;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.google.gson.Gson;
import com.orhanobut.hawk.Hawk;
import com.wcedla.driving_school.R;
import com.wcedla.driving_school.bean.CoachInfoDataBean;
import com.wcedla.driving_school.bean.StudentInfoDataBean;
import com.wcedla.driving_school.customview.CustomProgressDialog;
import com.wcedla.driving_school.tool.HttpUtils;
import com.wcedla.driving_school.tool.JsonUtils;
import com.wcedla.driving_school.tool.ToolUtils;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.wcedla.driving_school.constant.Config.EMAIL_CHECK_REGEX;
import static com.wcedla.driving_school.constant.Config.GET_USER_INFO;
import static com.wcedla.driving_school.constant.Config.MOBILE_REGEX;
import static com.wcedla.driving_school.constant.Config.UPDATE_MOBILE_NUMBER;

public class UserInfoActivity extends AppCompatActivity {

    @BindView(R.id.user_info_head)
    CircleImageView userInfoHead;
    @BindView(R.id.user_info_name)
    TextView userInfoName;
    @BindView(R.id.nick_text)
    TextView nickText;
    @BindView(R.id.student_no_head)
    TextView studentNoHead;
    @BindView(R.id.student_no_text)
    TextView studentNoText;
    @BindView(R.id.student_no_split)
    View studentNoSplit;
    @BindView(R.id.coach_no_head)
    TextView coachNoHead;
    @BindView(R.id.coach_no_text)
    TextView coachNoText;
    @BindView(R.id.coach_no_split)
    View coachNoSplit;
    @BindView(R.id.drive_year_head)
    TextView driveYearHead;
    @BindView(R.id.drive_year_text)
    TextView driveYearText;
    @BindView(R.id.drive_year_split)
    View driveYearSplit;
    @BindView(R.id.enroll_head)
    TextView enrollHead;
    @BindView(R.id.enroll_text)
    TextView enrollText;
    @BindView(R.id.enroll_split)
    View enrollSplit;
    @BindView(R.id.mobile_text)
    TextView mobileText;
    @BindView(R.id.mobile_change)
    TextView mobileChange;

    CustomProgressDialog customProgressDialog;

    String loginType;
    String loginUser;
    String loginNo;
    File headFile;
    float denisty;
    int displayWidth;

    CoachInfoDataBean coachInfoDataBean;
    StudentInfoDataBean studentInfoDataBean;

    PopupWindow mobileChangePopWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolUtils.setNavigationBarStatusBarTranslucent(this, false, Color.parseColor("#000000"));
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        denisty = getResources().getDisplayMetrics().density;
        displayWidth = getResources().getDisplayMetrics().widthPixels;
        loginUser = Hawk.get("loginUser", "");
        loginType = Hawk.get("loginType", "");
        loginNo = Hawk.get("loginNo", "");
        headFile = Hawk.get("newHead", null);
        customProgressDialog = CustomProgressDialog.create(this, "正在查找数据...", false);
        customProgressDialog.showProgressDialog();
        getUserInfo();
    }

    @OnClick(R.id.mobile_change)
    public void mobileChangeClick() {
        View mobileChangePopView = LayoutInflater.from(this).inflate(R.layout.pop_mobile_change, null);
        EditText mobileInput = mobileChangePopView.findViewById(R.id.mobile_change_input);
        TextView mobileSend = mobileChangePopView.findViewById(R.id.mobile_send);
        setInputFilter(mobileInput, EMAIL_CHECK_REGEX, 11);
        mobileChangePopWindow = new PopupWindow(mobileChangePopView, displayWidth - (int) (20 * denisty), ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置PopupWindow的背景
        mobileChangePopWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 设置PopupWindow是否能响应外部点击事件
        mobileChangePopWindow.setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        mobileChangePopWindow.setTouchable(true);
        mobileChangePopWindow.setFocusable(true);
        mobileChangePopWindow.showAtLocation(mobileChangePopView, Gravity.CENTER, 0, 0);
        showInputMethod(mobileInput);

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();

        layoutParams.alpha = 0.7f;

        getWindow().setAttributes(layoutParams);
        mobileChangePopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams layoutParams = getWindow().getAttributes();

                layoutParams.alpha = 1.0f;

                getWindow().setAttributes(layoutParams);
                hideInputMethod(mobileChangePopView);
            }
        });
        mobileInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputMethod(mobileInput);
            }
        });

        mobileChangePopView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideInputMethod(mobileInput);
            }
        });

        mobileSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobileText = mobileInput.getText().toString().trim();
                if (Pattern.matches(MOBILE_REGEX, mobileText)) {
                    customProgressDialog.showProgressDialog();
                    updateMobile(mobileText);
                    //Logger.d("对的");
                } else {
                    Toast.makeText(UserInfoActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getUserInfo() {
        String url = HttpUtils.setParameterForUrl(GET_USER_INFO, "type", loginType, "no", loginNo);
        HttpUtils.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!UserInfoActivity.this.isFinishing() && customProgressDialog != null && customProgressDialog.isShowing()) {
                            customProgressDialog.cancelProgressDialog();
                        }
                        Toast.makeText(UserInfoActivity.this, "获取失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if ("教练".equals(loginType)) {
                    coachInfoDataBean = new Gson().fromJson(response.body().string(), CoachInfoDataBean.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (headFile != null) {
                                Glide.with(UserInfoActivity.this).load(BitmapFactory.decodeFile(headFile.getPath())).apply(new RequestOptions()
                                        .dontAnimate()
                                        .placeholder(R.drawable.bsj)
                                        .override((int) (80 * denisty), (int) (80 * denisty))).into(userInfoHead);
                            } else {
                                if (coachInfoDataBean.getCoachInfo().get(0).getHeadImg() != null && ((String) coachInfoDataBean.getCoachInfo().get(0).getHeadImg()).length() > 0) {
                                    Glide.with(UserInfoActivity.this).load((String) coachInfoDataBean.getCoachInfo().get(0).getHeadImg())
                                            .apply(new RequestOptions()
                                                    .dontAnimate()
                                                    .placeholder(R.drawable.bsj)
                                                    .signature(new ObjectKey(System.currentTimeMillis())))
                                            .into(userInfoHead);

                                }
                            }
                            userInfoName.setText(coachInfoDataBean.getCoachInfo().get(0).getRealName());
                            nickText.setText(coachInfoDataBean.getCoachInfo().get(0).getNickName());
                            studentNoHead.setVisibility(View.GONE);
                            studentNoText.setVisibility(View.GONE);
                            studentNoSplit.setVisibility(View.GONE);
                            coachNoText.setText(coachInfoDataBean.getCoachInfo().get(0).getCoachNo());
                            driveYearText.setText(coachInfoDataBean.getCoachInfo().get(0).getDriveAge());
                            enrollHead.setVisibility(View.GONE);
                            enrollText.setVisibility(View.GONE);
                            enrollSplit.setVisibility(View.GONE);
                            mobileText.setText(coachInfoDataBean.getCoachInfo().get(0).getMobile());

                            if (!UserInfoActivity.this.isFinishing() && customProgressDialog != null && customProgressDialog.isShowing()) {
                                customProgressDialog.cancelProgressDialog();
                            }
                        }
                    });

                } else if ("学员".equals(loginType)) {
                    studentInfoDataBean = new Gson().fromJson(response.body().string(), StudentInfoDataBean.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (headFile != null) {
                                Glide.with(UserInfoActivity.this).load(BitmapFactory.decodeFile(headFile.getPath())).apply(new RequestOptions()
                                        .dontAnimate()
                                        .placeholder(R.drawable.bsj)
                                        .override((int) (80 * denisty), (int) (80 * denisty))).into(userInfoHead);
                            } else {
                                if (studentInfoDataBean.getStudentInfo().get(0).getHeadImg() != null && ((String) studentInfoDataBean.getStudentInfo().get(0).getHeadImg()).length() > 0) {
                                    Glide.with(UserInfoActivity.this).load((String) studentInfoDataBean.getStudentInfo().get(0).getHeadImg())
                                            .apply(new RequestOptions()
                                                    .dontAnimate()
                                                    .placeholder(R.drawable.bsj)
                                                    .signature(new ObjectKey(System.currentTimeMillis())))
                                            .into(userInfoHead);

                                }
                            }
                            userInfoName.setText(studentInfoDataBean.getStudentInfo().get(0).getRealName());
                            nickText.setText(studentInfoDataBean.getStudentInfo().get(0).getNickName());
                            studentNoText.setText(studentInfoDataBean.getStudentInfo().get(0).getStudentNo());
                            coachNoHead.setVisibility(View.GONE);
                            coachNoText.setVisibility(View.GONE);
                            coachNoSplit.setVisibility(View.GONE);
                            driveYearHead.setVisibility(View.GONE);
                            driveYearText.setVisibility(View.GONE);
                            driveYearSplit.setVisibility(View.GONE);
                            enrollText.setText(studentInfoDataBean.getStudentInfo().get(0).getEnrollTime());
                            mobileText.setText(studentInfoDataBean.getStudentInfo().get(0).getMobile());
                            if (!UserInfoActivity.this.isFinishing() && customProgressDialog != null && customProgressDialog.isShowing()) {
                                customProgressDialog.cancelProgressDialog();
                            }
                        }
                    });
                }


            }
        });
    }

    private void updateMobile(String number) {
        String url = HttpUtils.setParameterForUrl(UPDATE_MOBILE_NUMBER, "type", loginType, "no", loginNo, "mobile", number);
        HttpUtils.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(UserInfoActivity.this, "更新手机号码失败！", Toast.LENGTH_SHORT).show();
                        if (!UserInfoActivity.this.isFinishing() && customProgressDialog != null && customProgressDialog.isShowing()) {
                            customProgressDialog.cancelProgressDialog();
                        }
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
                            mobileText.setText(number);
                            Toast.makeText(UserInfoActivity.this, "更新手机号码成功！", Toast.LENGTH_SHORT).show();
                            if (!UserInfoActivity.this.isFinishing() && customProgressDialog != null && customProgressDialog.isShowing()) {
                                customProgressDialog.cancelProgressDialog();
                            }
                            if (mobileChangePopWindow != null && mobileChangePopWindow.isShowing()) {
                                mobileChangePopWindow.dismiss();
                            }
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
        targetView.setFocusable(false);
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            if (inputMethodManager.isActive()) {
                inputMethodManager.hideSoftInputFromWindow(targetView.getWindowToken(), 0);
            }
        }
    }
}

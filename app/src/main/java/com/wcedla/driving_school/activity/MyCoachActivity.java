package com.wcedla.driving_school.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.orhanobut.hawk.Hawk;
import com.wcedla.driving_school.R;
import com.wcedla.driving_school.bean.MyCoachInfoDataBean;
import com.wcedla.driving_school.customview.CustomProgressDialog;
import com.wcedla.driving_school.tool.HttpUtils;
import com.wcedla.driving_school.tool.JsonUtils;
import com.wcedla.driving_school.tool.ToolUtils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.wcedla.driving_school.constant.Config.GET_MY_COACH;
import static com.wcedla.driving_school.constant.Config.SEND_COACH_APPRAISE;

public class MyCoachActivity extends AppCompatActivity {

    String loginNo;
    CustomProgressDialog customProgressDialog;
    @BindView(R.id.my_coach_back)
    ImageView myCoachBack;
    @BindView(R.id.my_coach_name)
    TextView myCoachName;
    @BindView(R.id.my_coach_no_text)
    TextView myCoachNoText;
    @BindView(R.id.my_coach_mobile_text)
    TextView myCoachMobileText;
    @BindView(R.id.my_coach_drive_age_text)
    TextView myCoachDriveAgeText;
    @BindView(R.id.my_coach_star_text)
    TextView myCoachStarText;
    @BindView(R.id.my_coach_star_count_text)
    TextView myCoachStarCountText;
    @BindView(R.id.my_coach_call)
    Button myCoachCall;
    @BindView(R.id.my_coach_star)
    Button myCoachStar;
    @BindView(R.id.my_coach_root)
    CardView myCoachRoot;

    PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolUtils.setNavigationBarStatusBarTranslucent(this, false, Color.parseColor("#000000"));
        setContentView(R.layout.activity_my_coach);
        ButterKnife.bind(this);
        loginNo = Hawk.get("loginNo", "");
        customProgressDialog = CustomProgressDialog.create(this, "正在查找...", false);
        customProgressDialog.showProgressDialog();
        getMyCoach();
    }

    @OnClick(R.id.my_coach_back)
    public void coachBackClick() {
        finish();
    }

    @OnClick(R.id.my_coach_call)
    public void coachCallClick() {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + myCoachMobileText.getText().toString()));
        startActivity(callIntent);
    }

    @OnClick(R.id.my_coach_star)
    public void starClick() {
        doApprais();
    }

    private void getMyCoach() {
        String url = HttpUtils.setParameterForUrl(GET_MY_COACH, "no", loginNo);
        HttpUtils.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        customProgressDialog.cancelProgressDialog();
                        Toast.makeText(MyCoachActivity.this, "查找失败!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                MyCoachInfoDataBean myCoachInfoDataBean = new Gson().fromJson(response.body().string(), MyCoachInfoDataBean.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (myCoachInfoDataBean != null) {
                            myCoachName.setText(myCoachInfoDataBean.getCoachInfo().get(0).getRealName());
                            myCoachNoText.setText(myCoachInfoDataBean.getCoachInfo().get(0).getCoachNo());
                            myCoachMobileText.setText(myCoachInfoDataBean.getCoachInfo().get(0).getMobile());
                            myCoachDriveAgeText.setText(myCoachInfoDataBean.getCoachInfo().get(0).getDriveAge());
                            if (myCoachInfoDataBean.getCoachInfo().get(0).getStar() != null) {
                                myCoachStarText.setText(myCoachInfoDataBean.getCoachInfo().get(0).getStar() + "星");
                                myCoachStarCountText.setText(myCoachInfoDataBean.getCoachInfo().get(0).getAppraiseCount() + "次");
                            } else {
                                myCoachStarText.setText("暂无评价");
                                myCoachStarCountText.setText("暂无评价");
                            }
                            if (myCoachInfoDataBean.getCoachInfo().get(0).getAppraiseNo() != null) {
                                if (myCoachInfoDataBean.getCoachInfo().get(0).getAppraiseNo().equals("done")) {
                                    myCoachStar.setEnabled(false);
                                    myCoachStar.setTextColor(Color.DKGRAY);
                                } else {
                                    myCoachStar.setEnabled(true);
                                    myCoachStar.setTextColor(Color.parseColor("#3F51B5"));
                                }
                            }

                            myCoachRoot.setVisibility(View.VISIBLE);
                        }
                        customProgressDialog.cancelProgressDialog();
                    }
                });
            }
        });
    }

    private void doApprais() {

        View view = LayoutInflater.from(this).inflate(R.layout.appraise_pop_window, null);
        TextView star1 = view.findViewById(R.id.star_1);
        TextView star2 = view.findViewById(R.id.star_2);
        TextView star3 = view.findViewById(R.id.star_3);
        TextView star4 = view.findViewById(R.id.star_4);
        TextView star5 = view.findViewById(R.id.star_5);
        TextView send = view.findViewById(R.id.star_send);
        star5.setSelected(true);
        star5.setTextSize(23);
        star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setSelected(true);
                star1.setTextSize(23);
                star2.setSelected(false);
                star2.setTextSize(20);
                star3.setSelected(false);
                star3.setTextSize(20);
                star4.setSelected(false);
                star4.setTextSize(20);
                star5.setSelected(false);
                star5.setTextSize(20);
            }
        });
        star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setSelected(false);
                star1.setTextSize(20);
                star2.setSelected(true);
                star2.setTextSize(23);
                star3.setSelected(false);
                star3.setTextSize(20);
                star4.setSelected(false);
                star4.setTextSize(20);
                star5.setSelected(false);
                star5.setTextSize(20);
            }
        });
        star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setSelected(false);
                star1.setTextSize(20);
                star2.setSelected(false);
                star2.setTextSize(20);
                star3.setSelected(true);
                star3.setTextSize(23);
                star4.setSelected(false);
                star4.setTextSize(20);
                star5.setSelected(false);
                star5.setTextSize(20);
            }
        });
        star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setSelected(false);
                star1.setTextSize(20);
                star2.setSelected(false);
                star2.setTextSize(20);
                star3.setSelected(false);
                star3.setTextSize(20);
                star4.setSelected(true);
                star4.setTextSize(23);
                star5.setSelected(false);
                star5.setTextSize(20);
            }
        });
        star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setSelected(false);
                star1.setTextSize(20);
                star2.setSelected(false);
                star2.setTextSize(20);
                star3.setSelected(false);
                star3.setTextSize(20);
                star4.setSelected(false);
                star4.setTextSize(20);
                star5.setSelected(true);
                star5.setTextSize(23);
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (star1.isSelected()) {
                    appraiseStar("1");
                } else if (star2.isSelected()) {
                    appraiseStar("2");
                } else if (star3.isSelected()) {
                    appraiseStar("3");
                } else if (star4.isSelected()) {
                    appraiseStar("4");
                } else if (star5.isSelected()) {
                    appraiseStar("5");
                } else {
                    Toast.makeText(MyCoachActivity.this, "选择错误", Toast.LENGTH_SHORT).show();
                    popupWindow.dismiss();
                }
            }
        });

        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置PopupWindow的背景
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 设置PopupWindow是否能响应外部点击事件
        popupWindow.setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.alpha = 0.7f;
        getWindow().setAttributes(layoutParams);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                layoutParams.alpha = 1.0f;
                getWindow().setAttributes(layoutParams);
            }
        });
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    private void appraiseStar(String star) {
        popupWindow.dismiss();
        customProgressDialog.showProgressDialog();
        String url = HttpUtils.setParameterForUrl(SEND_COACH_APPRAISE, "studentNo", loginNo, "star", star);
        HttpUtils.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        customProgressDialog.cancelProgressDialog();
                        Toast.makeText(MyCoachActivity.this, "评价失败！", Toast.LENGTH_SHORT).show();
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
                            customProgressDialog.cancelProgressDialog();
                            getMyCoach();
                            Toast.makeText(MyCoachActivity.this, "评价成功！", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            customProgressDialog.cancelProgressDialog();
                            Toast.makeText(MyCoachActivity.this, "评价失败！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            }
        });
    }
}

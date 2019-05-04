package com.wcedla.driving_school.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.orhanobut.hawk.Hawk;
import com.wcedla.driving_school.R;
import com.wcedla.driving_school.adapter.StudyProgressSelectAdapter;
import com.wcedla.driving_school.bean.MyExamInfoDataBean;
import com.wcedla.driving_school.customview.CustomProgressDialog;
import com.wcedla.driving_school.tool.ExamUtils;
import com.wcedla.driving_school.tool.HttpUtils;
import com.wcedla.driving_school.tool.JsonUtils;
import com.wcedla.driving_school.tool.ToolUtils;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.wcedla.driving_school.constant.Config.CHECK_EXAM_FEED;
import static com.wcedla.driving_school.constant.Config.GET_MY_EXAM;
import static com.wcedla.driving_school.constant.Config.SEND_FEED;

public class MyExamActivity extends AppCompatActivity {

    @BindView(R.id.my_exam_back)
    ImageView myExamBack;
    @BindView(R.id.my_exam_name)
    TextView myExamName;
    @BindView(R.id.my_exam_no_text)
    TextView myExamNoText;
    @BindView(R.id.my_exam_progress_text)
    TextView myExamProgressText;
    @BindView(R.id.my_exam_feed)
    Button myExamFeed;
    @BindView(R.id.my_exam_info_root)
    CardView myExamInfoRoot;

    String loginNo;
    CustomProgressDialog customProgressDialog;
    MyExamInfoDataBean myExamInfoDataBean;

    PopupWindow myExamPop;

    boolean examFeed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolUtils.setNavigationBarStatusBarTranslucent(this, false, Color.parseColor("#000000"));
        setContentView(R.layout.activity_my_exam);
        ButterKnife.bind(this);
        loginNo = Hawk.get("loginNo", "");
        customProgressDialog = CustomProgressDialog.create(this, "正在获取数据...", false);
        customProgressDialog.showProgressDialog();
        getMyExam();
    }

    @OnClick(R.id.my_exam_back)
    public void backClick() {
        finish();
    }

    @OnClick(R.id.my_exam_feed)
    public void myExamFeedClick() {
        checkExamFeed(myExamInfoDataBean.getMyExamInfo().get(0).getExamProgress());
    }

    private void myExamFeed(String progress) {
        int displayWidth = getResources().getDisplayMetrics().widthPixels;
        float denisty = getResources().getDisplayMetrics().density;
        View updateProgressShow = LayoutInflater.from(this).inflate(R.layout.pop_exam_progress_update, null);
        TextView progressText = updateProgressShow.findViewById(R.id.progress_text);
        progressText.setText(ExamUtils.getExamProgress(progress));
        TextView sendUpdate = updateProgressShow.findViewById(R.id.send_exam_update);
        myExamPop = new PopupWindow(updateProgressShow, displayWidth - (int) (20 * denisty), ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置PopupWindow的背景
        myExamPop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 设置PopupWindow是否能响应外部点击事件
        myExamPop.setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        myExamPop.setTouchable(true);
        myExamPop.setFocusable(true);
        if (examFeed) {
            sendUpdate.setEnabled(false);
            sendUpdate.setText("已提交");
            sendUpdate.setTextColor(Color.DKGRAY);
        } else {
            sendUpdate.setEnabled(true);
            sendUpdate.setText("提交");
            sendUpdate.setTextColor(Color.parseColor("#3F51B5"));
        }
        sendUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFeed("3", ExamUtils.getProgressCode(progressText.getText().toString()) + "", "无");
            }
        });

        progressText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View progressSelect = LayoutInflater.from(MyExamActivity.this).inflate(R.layout.study_progress_select_pop, null);
                ListView listView = progressSelect.findViewById(R.id.study_list_view);
                List<String> examProgressList = ExamUtils.examProgressList();
                StudyProgressSelectAdapter studyProgressSelectAdapter = new StudyProgressSelectAdapter(MyExamActivity.this, examProgressList, -1);
                listView.setAdapter(studyProgressSelectAdapter);
                PopupWindow selectPop = new PopupWindow(progressSelect, progressText.getMeasuredWidth(), (int) (300 * denisty));
                // 设置PopupWindow的背景
                selectPop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                // 设置PopupWindow是否能响应外部点击事件
                selectPop.setOutsideTouchable(true);
                // 设置PopupWindow是否能响应点击事件
                selectPop.setTouchable(true);
                selectPop.setFocusable(true);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        progressText.setText(examProgressList.get(position));
                        selectPop.dismiss();
                    }
                });
                selectPop.showAtLocation(progressSelect, Gravity.CENTER, 0, 0);
            }
        });

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();

        layoutParams.alpha = 0.7f;

        getWindow().setAttributes(layoutParams);
        myExamPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams layoutParams = getWindow().getAttributes();

                layoutParams.alpha = 1.0f;

                getWindow().setAttributes(layoutParams);
            }
        });

        myExamPop.showAtLocation(updateProgressShow, Gravity.CENTER, 0, 0);
    }

    private void getMyExam() {
        String url = HttpUtils.setParameterForUrl(GET_MY_EXAM, "no", loginNo);
        HttpUtils.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyExamActivity.this, "获取失败!", Toast.LENGTH_SHORT).show();
                        if (!MyExamActivity.this.isFinishing() && customProgressDialog != null && customProgressDialog.isShowing()) {
                            customProgressDialog.cancelProgressDialog();
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                myExamInfoDataBean = new Gson().fromJson(response.body().string(), MyExamInfoDataBean.class);
                if (myExamInfoDataBean != null && myExamInfoDataBean.getMyExamInfo() != null && !myExamInfoDataBean.getMyExamInfo().isEmpty()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            myExamName.setText(myExamInfoDataBean.getMyExamInfo().get(0).getRealName());
                            myExamNoText.setText(myExamInfoDataBean.getMyExamInfo().get(0).getStudentNo());
                            myExamProgressText.setText(ExamUtils.getExamProgress(myExamInfoDataBean.getMyExamInfo().get(0).getExamProgress()));
                            myExamInfoRoot.setVisibility(View.VISIBLE);
                            if (!MyExamActivity.this.isFinishing() && customProgressDialog != null && customProgressDialog.isShowing()) {
                                customProgressDialog.cancelProgressDialog();
                            }
                        }
                    });

                }
            }
        });
    }

    private void sendFeed(String type, String progress, String message) {
        String url = HttpUtils.setParameterForUrl(SEND_FEED, "no", loginNo, "type", type, "message", message, "progress", progress);
        HttpUtils.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyExamActivity.this, "出现错误,请重试！", Toast.LENGTH_SHORT).show();
                        myExamPop.dismiss();
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
                            Toast.makeText(MyExamActivity.this, "提交成功，请耐心等待审核通过", Toast.LENGTH_SHORT).show();
                            myExamPop.dismiss();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MyExamActivity.this, "提交失败，请重试！", Toast.LENGTH_SHORT).show();
                            myExamPop.dismiss();
                        }
                    });
                }
            }
        });
    }

    private void checkExamFeed(String progress) {
        String url = HttpUtils.setParameterForUrl(CHECK_EXAM_FEED, "no", loginNo);
        HttpUtils.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyExamActivity.this, "出现错误", Toast.LENGTH_SHORT).show();
                        myExamPop.dismiss();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int result = JsonUtils.getFeedCheck(response.body().string());
                if (result == -1) {
                    examFeed = true;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            myExamFeed(progress);
                        }
                    });

                } else if (result == 0) {
                    examFeed = false;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            myExamFeed(progress);
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MyExamActivity.this, "出现错误", Toast.LENGTH_SHORT).show();
                            myExamPop.dismiss();
                        }
                    });
                }
            }
        });
    }
}

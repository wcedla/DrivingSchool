package com.wcedla.driving_school.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;
import com.wcedla.driving_school.R;
import com.wcedla.driving_school.activity.MyStudyActivity;
import com.wcedla.driving_school.bean.MyStudyInfoDataBean;
import com.wcedla.driving_school.tool.HttpUtils;
import com.wcedla.driving_school.tool.JsonUtils;
import com.wcedla.driving_school.tool.StudyUtils;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.wcedla.driving_school.constant.Config.CHECK_FEED;
import static com.wcedla.driving_school.constant.Config.CHECK_FEED_TIME;
import static com.wcedla.driving_school.constant.Config.EMAIL_CHECK_REGEX;
import static com.wcedla.driving_school.constant.Config.SEND_FEED;

public class MyStudyInfoAdapter extends RecyclerView.Adapter<MyStudyInfoAdapter.MyStudyInfoHolder> {

    Context context;
    MyStudyInfoDataBean myStudyInfoDataBean;

    PopupWindow feedPopWindow;
    EditText timeInput;
    View popFeedView;

    boolean feedStatus = false;
    boolean timeFeed = false;

    public MyStudyInfoAdapter(Context context, MyStudyInfoDataBean myStudyInfoDataBean) {
        this.context = context;
        this.myStudyInfoDataBean = myStudyInfoDataBean;

    }

    @NonNull
    @Override
    public MyStudyInfoHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_my_study_info_item, viewGroup, false);
        MyStudyInfoHolder myStudyInfoHolder = new MyStudyInfoHolder(view);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((MyStudyActivity) context).hideInput();
                return false;
            }
        });
        return myStudyInfoHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyStudyInfoHolder myStudyInfoHolder, int i) {
        myStudyInfoHolder.name.setText(myStudyInfoDataBean.getAllStudyInfo().get(i).getRealName());
        myStudyInfoHolder.no.setText(myStudyInfoDataBean.getAllStudyInfo().get(i).getStudentNo());
        myStudyInfoHolder.progress.setText(StudyUtils.getStudyProgress(myStudyInfoDataBean.getAllStudyInfo().get(i).getStudyProgress()));
        myStudyInfoHolder.time.setText(myStudyInfoDataBean.getAllStudyInfo().get(i).getTime() + "小时");
        myStudyInfoHolder.feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFeedTime(myStudyInfoDataBean.getAllStudyInfo().get(i).getStudyProgress(), myStudyInfoDataBean.getAllStudyInfo().get(i).getTime());
                ((MyStudyActivity) context).customProgressDialog.showProgressDialog();
            }
        });
    }

    private void showFeedPop(String progress, String time) {
        int displayWidth = context.getResources().getDisplayMetrics().widthPixels;
        float denisty = context.getResources().getDisplayMetrics().density;
        popFeedView = LayoutInflater.from(context).inflate(R.layout.pop_study_feed, null);
        TextView feedType = popFeedView.findViewById(R.id.feed_type_text);
        TextView timeHead = popFeedView.findViewById(R.id.feed_time_head);
        TextView timeUnit = popFeedView.findViewById(R.id.feed_time_unit);
        TextView feedSubmit = popFeedView.findViewById(R.id.feed_submit);
        timeInput = popFeedView.findViewById(R.id.feed_time_text);
        setInputFilter(timeInput, EMAIL_CHECK_REGEX, 2);
        TextView progressHead = popFeedView.findViewById(R.id.feed_progress_head);
        TextView progressText = popFeedView.findViewById(R.id.feed_progress_text);
        progressText.setText(progress);
        timeInput.setText(time);
        timeInput.setSelection(time.length());
        if (timeFeed) {
            feedSubmit.setEnabled(false);
            feedSubmit.setTextColor(Color.DKGRAY);
            feedSubmit.setText("已提交");
        } else {
            feedSubmit.setEnabled(true);
            feedSubmit.setTextColor(Color.parseColor("#3F51B5"));
            feedSubmit.setText("提交");
        }
        popFeedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideInputMethod(popFeedView);
            }
        });
        timeInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputMethod(timeInput);
            }
        });

        feedType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideInputMethod(feedType);
                View typeView = LayoutInflater.from(context).inflate(R.layout.pop_study_feed_type, null);
                TextView timeError = typeView.findViewById(R.id.feeed_type_time_error);
                TextView progressError = typeView.findViewById(R.id.feed_type_progress_error);

                PopupWindow typeSelectPop = new PopupWindow(typeView, feedType.getMeasuredWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
                // 设置PopupWindow的背景
                typeSelectPop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                // 设置PopupWindow是否能响应外部点击事件
                typeSelectPop.setOutsideTouchable(true);
                // 设置PopupWindow是否能响应点击事件
                typeSelectPop.setTouchable(true);
                typeSelectPop.setFocusable(true);

                timeError.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        feedType.setText("时长错误");
                        timeHead.setVisibility(View.VISIBLE);
                        timeInput.setVisibility(View.VISIBLE);
                        timeUnit.setVisibility(View.VISIBLE);
                        timeInput.setText(time);
                        progressHead.setVisibility(View.GONE);
                        progressText.setVisibility(View.GONE);
                        if (timeFeed) {
                            feedSubmit.setEnabled(false);
                            feedSubmit.setTextColor(Color.DKGRAY);
                            feedSubmit.setText("已提交");
                        } else {
                            feedSubmit.setEnabled(true);
                            feedSubmit.setTextColor(Color.parseColor("#3F51B5"));
                            feedSubmit.setText("提交");
                        }
                        typeSelectPop.dismiss();

                    }
                });
                progressError.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideInputMethod(timeInput);
                        feedType.setText("进度错误");
                        timeHead.setVisibility(View.GONE);
                        timeInput.setVisibility(View.GONE);
                        timeUnit.setVisibility(View.GONE);
                        progressHead.setVisibility(View.VISIBLE);
                        progressText.setVisibility(View.VISIBLE);
                        if (feedStatus) {
                            feedSubmit.setEnabled(false);
                            feedSubmit.setTextColor(Color.DKGRAY);
                            feedSubmit.setText("已提交");
                        } else {
                            feedSubmit.setEnabled(true);
                            feedSubmit.setTextColor(Color.parseColor("#3F51B5"));
                            feedSubmit.setText("提交");
                        }
                        typeSelectPop.dismiss();
                    }
                });

                typeSelectPop.showAtLocation(typeView, Gravity.CENTER, 0, 0);
            }
        });

        progressText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View progressView = LayoutInflater.from(context).inflate(R.layout.study_progress_select_pop, null);
                ListView listView = progressView.findViewById(R.id.study_list_view);
                List<String> studyProgressList = StudyUtils.studyProgressList();
                StudyProgressSelectAdapter studyProgressSelectAdapter = new StudyProgressSelectAdapter(context, studyProgressList, -1);
                listView.setAdapter(studyProgressSelectAdapter);

                PopupWindow progressPop = new PopupWindow(progressView, progressText.getMeasuredWidth(), (int) (300 * denisty));
                // 设置PopupWindow的背景
                progressPop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                // 设置PopupWindow是否能响应外部点击事件
                progressPop.setOutsideTouchable(true);
                // 设置PopupWindow是否能响应点击事件
                progressPop.setTouchable(true);
                progressPop.setFocusable(true);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        progressText.setText(studyProgressList.get(position));
                        progressPop.dismiss();
                    }
                });
                progressPop.showAtLocation(progressView, Gravity.CENTER, 0, 0);
            }
        });

        feedSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (feedType.getText().equals("时长错误")) {
                    sendFeed("1", (StudyUtils.getStudyProgressLevel(progress) + 1) + "", timeInput.getText().toString().trim());
                } else {
                    sendFeed("2", "-1", (StudyUtils.getStudyProgressLevel(progressText.getText().toString()) + 1) + "");
                }
            }
        });

        feedPopWindow = new PopupWindow(popFeedView, displayWidth - (int) (20 * denisty), ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置PopupWindow的背景
        feedPopWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 设置PopupWindow是否能响应外部点击事件
        feedPopWindow.setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        feedPopWindow.setTouchable(true);
        feedPopWindow.setFocusable(true);
        feedPopWindow.showAtLocation(popFeedView, Gravity.CENTER, 0, 0);

        WindowManager.LayoutParams layoutParams = ((MyStudyActivity) context).getWindow().getAttributes();

        layoutParams.alpha = 0.7f;

        ((MyStudyActivity) context).getWindow().setAttributes(layoutParams);
        feedPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams layoutParams = ((MyStudyActivity) context).getWindow().getAttributes();

                layoutParams.alpha = 1.0f;

                ((MyStudyActivity) context).getWindow().setAttributes(layoutParams);
                hideInputMethod(popFeedView);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (myStudyInfoDataBean != null && myStudyInfoDataBean.getAllStudyInfo() != null) {
            return myStudyInfoDataBean.getAllStudyInfo().size();
        }
        return 0;
    }

    public void updateData(MyStudyInfoDataBean myStudyInfoDataBean) {
        this.myStudyInfoDataBean = myStudyInfoDataBean;
        notifyDataSetChanged();
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
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(targetView, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 隐藏输入法
     *
     * @param targetView 目标控件
     */
    private void hideInputMethod(View targetView) {
        timeInput.setFocusable(false);
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            if (inputMethodManager.isActive()) {
                inputMethodManager.hideSoftInputFromWindow(targetView.getWindowToken(), 0);
            }
        }
    }

    private void checkFeed(String progress, String time) {
        String url = HttpUtils.setParameterForUrl(CHECK_FEED, "no", Hawk.get("loginNo", ""));
        HttpUtils.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ((MyStudyActivity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "出现错误", Toast.LENGTH_SHORT).show();
                        ((MyStudyActivity) context).finish();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int result = JsonUtils.getFeedCheck(response.body().string());
                if (result == -1) {
                    feedStatus = true;
                    ((MyStudyActivity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (((MyStudyActivity) context).customProgressDialog != null && ((MyStudyActivity) context).customProgressDialog.isShowing()) {
                                ((MyStudyActivity) context).customProgressDialog.cancelProgressDialog();
                            }
                            showFeedPop(progress, time);
                        }
                    });
                } else if (result == 0) {
                    feedStatus = false;
                    ((MyStudyActivity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (((MyStudyActivity) context).customProgressDialog != null && ((MyStudyActivity) context).customProgressDialog.isShowing()) {
                                ((MyStudyActivity) context).customProgressDialog.cancelProgressDialog();
                            }
                            showFeedPop(progress, time);

                        }
                    });
                } else {
                    ((MyStudyActivity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "出现错误", Toast.LENGTH_SHORT).show();
                            ((MyStudyActivity) context).finish();
                        }
                    });
                }
            }
        });
    }

    private void checkFeedTime(String progress, String time) {
        String url = HttpUtils.setParameterForUrl(CHECK_FEED_TIME, "no", Hawk.get("loginNo", ""), "progress", progress);
        HttpUtils.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ((MyStudyActivity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "出现错误", Toast.LENGTH_SHORT).show();
                        feedPopWindow.dismiss();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int result = JsonUtils.getFeedCheck(response.body().string());
                if (result == -1) {
                    timeFeed = true;
                    checkFeed(StudyUtils.getStudyProgress(progress), time);

                } else if (result == 0) {
                    timeFeed = false;
                    checkFeed(StudyUtils.getStudyProgress(progress), time);

                } else {
                    ((MyStudyActivity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "出现错误", Toast.LENGTH_SHORT).show();
                            feedPopWindow.dismiss();
                        }
                    });
                }
            }
        });
    }

    private void sendFeed(String type, String progress, String message) {
        String url = HttpUtils.setParameterForUrl(SEND_FEED, "no", Hawk.get("loginNo", ""), "type", type, "message", message, "progress", progress);
        HttpUtils.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ((MyStudyActivity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "出现错误,请重试！", Toast.LENGTH_SHORT).show();
                        feedPopWindow.dismiss();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int result = JsonUtils.getResetStatus(response.body().string());
                if (result == -1) {
                    ((MyStudyActivity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "提交成功，请耐心等待审核通过", Toast.LENGTH_SHORT).show();
                            feedPopWindow.dismiss();
                        }
                    });
                } else {
                    ((MyStudyActivity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "提交失败，请重试！", Toast.LENGTH_SHORT).show();
                            feedPopWindow.dismiss();
                        }
                    });
                }
            }
        });
    }

    public static class MyStudyInfoHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView no;
        TextView progress;
        TextView time;
        Button feed;


        public MyStudyInfoHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.my_study_name);
            no = itemView.findViewById(R.id.my_no_text);
            progress = itemView.findViewById(R.id.my_study_progress_text);
            time = itemView.findViewById(R.id.my_study_time_text);
            feed = itemView.findViewById(R.id.my_study_feed);
        }
    }
}

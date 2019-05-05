package com.wcedla.driving_school.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.wcedla.driving_school.R;
import com.wcedla.driving_school.activity.UserFeedActivity;
import com.wcedla.driving_school.bean.AllFeedDataBean;
import com.wcedla.driving_school.tool.ExamUtils;
import com.wcedla.driving_school.tool.HttpUtils;
import com.wcedla.driving_school.tool.JsonUtils;
import com.wcedla.driving_school.tool.StudyUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.wcedla.driving_school.constant.Config.GET_ALL_FEED_INFO;
import static com.wcedla.driving_school.constant.Config.SET_FEED_STATUS;

public class AllFeedAdapter extends RecyclerView.Adapter<AllFeedAdapter.AllFeedHolder> {

    Context context;
    AllFeedDataBean allFeedDataBean;

    public AllFeedAdapter(Context context, AllFeedDataBean allFeedDataBean) {
        this.context = context;
        this.allFeedDataBean = allFeedDataBean;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public AllFeedHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_all_feed, viewGroup, false);
        AllFeedHolder allFeedHolder = new AllFeedHolder(view);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((UserFeedActivity) context).hideInput();
                return false;
            }
        });
        return allFeedHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AllFeedHolder allFeedHolder, int i) {

        if (allFeedDataBean.getAllFeedInfo().get(i).getType().equals("1")) {
            allFeedHolder.name.setText(allFeedDataBean.getAllFeedInfo().get(i).getRealName());
            allFeedHolder.no.setText(allFeedDataBean.getAllFeedInfo().get(i).getStudentNo());
            allFeedHolder.type.setText("时长错误");
            allFeedHolder.progress.setText(StudyUtils.getStudyProgress(allFeedDataBean.getAllFeedInfo().get(i).getProgress()));
            allFeedHolder.time.setVisibility(View.VISIBLE);
            allFeedHolder.timeHead.setVisibility(View.VISIBLE);
            allFeedHolder.time.setText(allFeedDataBean.getAllFeedInfo().get(i).getMessage() + "小时");
            allFeedHolder.status.setText(allFeedDataBean.getAllFeedInfo().get(i).getStatus());
        } else if (allFeedDataBean.getAllFeedInfo().get(i).getType().equals("2")) {
            allFeedHolder.name.setText(allFeedDataBean.getAllFeedInfo().get(i).getRealName());
            allFeedHolder.no.setText(allFeedDataBean.getAllFeedInfo().get(i).getStudentNo());
            allFeedHolder.type.setText("进度错误");
            allFeedHolder.progress.setText(StudyUtils.getStudyProgress(allFeedDataBean.getAllFeedInfo().get(i).getProgress()));
//            allFeedHolder.time.setVisibility(View.GONE);
//            allFeedHolder.timeHead.setVisibility(View.GONE);
            allFeedHolder.time.setText(allFeedDataBean.getAllFeedInfo().get(i).getMessage() + "小时");
            allFeedHolder.status.setText(allFeedDataBean.getAllFeedInfo().get(i).getStatus());
        } else if (allFeedDataBean.getAllFeedInfo().get(i).getType().equals("3")) {
            allFeedHolder.name.setText(allFeedDataBean.getAllFeedInfo().get(i).getRealName());
            allFeedHolder.no.setText(allFeedDataBean.getAllFeedInfo().get(i).getStudentNo());
            allFeedHolder.type.setText("考试进度");
            allFeedHolder.progress.setText(ExamUtils.getExamProgress(allFeedDataBean.getAllFeedInfo().get(i).getProgress()));
            allFeedHolder.time.setVisibility(View.GONE);
            allFeedHolder.timeHead.setVisibility(View.GONE);
            allFeedHolder.time.setText("无");
            allFeedHolder.status.setText(allFeedDataBean.getAllFeedInfo().get(i).getStatus());
        }
        if (allFeedHolder.status.getText().toString().equals("审核通过")) {
            allFeedHolder.pass.setText("取消通过");
            allFeedHolder.failed.setVisibility(View.GONE);
        } else if (allFeedHolder.status.getText().toString().equals("审核未通过")) {
            allFeedHolder.pass.setText("通过审核");
            allFeedHolder.failed.setVisibility(View.GONE);
        } else if (allFeedHolder.status.getText().toString().equals("审核中")) {
            allFeedHolder.pass.setText("通过审核");
            allFeedHolder.failed.setVisibility(View.VISIBLE);
        }
        allFeedHolder.pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allFeedHolder.pass.getText().toString().equals("通过审核")) {
                    if (!allFeedHolder.type.getText().toString().equals("考试进度")) {
                        passFeed(allFeedHolder.no.getText().toString(),
                                allFeedHolder.type.getText().toString(),
                                allFeedHolder.time.getText().toString().replace("小时", ""),
                                (StudyUtils.getStudyProgressLevel(allFeedHolder.progress.getText().toString()) + 1) + "",
                                "1");
                    } else {
                        passFeed(allFeedHolder.no.getText().toString(),
                                allFeedHolder.type.getText().toString(),
                                allFeedHolder.time.getText().toString(),
                                ExamUtils.getProgressCode(allFeedHolder.progress.getText().toString()) + "",
                                "1");

                    }
                } else {
                    if (!allFeedHolder.type.getText().toString().equals("考试进度")) {
                        passFeed(allFeedHolder.no.getText().toString(),
                                allFeedHolder.type.getText().toString(),
                                allFeedHolder.time.getText().toString().replace("小时", ""),
                                (StudyUtils.getStudyProgressLevel(allFeedHolder.progress.getText().toString()) + 1) + "",
                                "2");
                    } else {
                        passFeed(allFeedHolder.no.getText().toString(),
                                allFeedHolder.type.getText().toString(),
                                allFeedHolder.time.getText().toString(),
                                ExamUtils.getProgressCode(allFeedHolder.progress.getText().toString()) + "",
                                "2");

                    }
                }
            }
        });
        allFeedHolder.failed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!allFeedHolder.type.getText().toString().equals("考试进度")) {
                    passFeed(allFeedHolder.no.getText().toString(),
                            allFeedHolder.type.getText().toString(),
                            allFeedHolder.time.getText().toString().replace("小时", ""),
                            (StudyUtils.getStudyProgressLevel(allFeedHolder.progress.getText().toString()) + 1) + "",
                            "2");
                } else {
                    passFeed(allFeedHolder.no.getText().toString(),
                            allFeedHolder.type.getText().toString(),
                            allFeedHolder.time.getText().toString(),
                            ExamUtils.getProgressCode(allFeedHolder.progress.getText().toString()) + "",
                            "2");

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (allFeedDataBean != null && allFeedDataBean.getAllFeedInfo() != null && !allFeedDataBean.getAllFeedInfo().isEmpty()) {
            return allFeedDataBean.getAllFeedInfo().size();
        }
        return 0;
    }

    private void passFeed(String no, String type, String message, String progress, String status) {
        ((UserFeedActivity) context).customProgressDialog.showProgressDialog();
        String url = HttpUtils.setParameterForUrl(SET_FEED_STATUS, "no", no, "type", type, "message", message, "progress", progress, "status", status);
        Logger.d("网址:" + url);
        HttpUtils.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ((UserFeedActivity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "审核失败！", Toast.LENGTH_SHORT).show();
                        ((UserFeedActivity) context).customProgressDialog.cancelProgressDialog();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Logger.d("原因"+response.body().string());
                int result = JsonUtils.getResetStatus(response.body().string());
                if (result == -1) {
                    refreshdata();
                } else {

                    ((UserFeedActivity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "审核失败！", Toast.LENGTH_SHORT).show();
                            ((UserFeedActivity) context).customProgressDialog.cancelProgressDialog();
                        }
                    });
                }
            }
        });
    }

    private void refreshdata() {
        HttpUtils.doHttpRequest(GET_ALL_FEED_INFO, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ((UserFeedActivity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "审核通过，但是刷新数据失败！", Toast.LENGTH_SHORT).show();
                        ((UserFeedActivity) context).customProgressDialog.cancelProgressDialog();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                allFeedDataBean = new Gson().fromJson(response.body().string(), AllFeedDataBean.class);
                ((UserFeedActivity) context).allFeedDataBean = allFeedDataBean;
                ((UserFeedActivity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                        ((UserFeedActivity) context).customProgressDialog.cancelProgressDialog();
                        ((UserFeedActivity) context).allClick();
                    }
                });
            }
        });
    }

    public void updateData(AllFeedDataBean allFeedDataBean) {
        this.allFeedDataBean = allFeedDataBean;
        notifyDataSetChanged();
    }

    public static class AllFeedHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView no;
        TextView type;
        TextView progress;
        TextView time;
        TextView timeHead;
        TextView status;
        Button pass;
        Button failed;

        public AllFeedHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.all_feed_name);
            no = itemView.findViewById(R.id.all_feed_no_text);
            type = itemView.findViewById(R.id.all_feed_type_text);
            progress = itemView.findViewById(R.id.all_feed_progress_text);
            time = itemView.findViewById(R.id.all_feed_time_text);
            timeHead = itemView.findViewById(R.id.all_feed_time_head);
            status = itemView.findViewById(R.id.all_feed_status_text);
            pass = itemView.findViewById(R.id.all_feed_ok);
            failed = itemView.findViewById(R.id.all_feed_failed);
        }
    }
}

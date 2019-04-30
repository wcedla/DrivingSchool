package com.wcedla.driving_school.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.orhanobut.hawk.Hawk;
import com.wcedla.driving_school.R;
import com.wcedla.driving_school.activity.ExamProgressActivity;
import com.wcedla.driving_school.bean.ExamInfoDataBean;
import com.wcedla.driving_school.tool.ExamUtils;
import com.wcedla.driving_school.tool.HttpUtils;
import com.wcedla.driving_school.tool.JsonUtils;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.wcedla.driving_school.constant.Config.GET_EXAM_PROGRESS;
import static com.wcedla.driving_school.constant.Config.UPDATE_EXAM_PROGRESS;

public class ExamInfoShowAdapter extends RecyclerView.Adapter<ExamInfoShowAdapter.ExamInfoHolder> {

    Context context;
    ExamInfoDataBean examInfoDataBean;
    PopupWindow examTextPop;

    public ExamInfoShowAdapter(Context context, ExamInfoDataBean examInfoDataBean) {
        this.context = context;
        this.examInfoDataBean = examInfoDataBean;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public ExamInfoHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.adater_exam_info_item, viewGroup, false);
        ExamInfoHolder examInfoHolder = new ExamInfoHolder(view);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((ExamProgressActivity) context).hideInput();
                return false;
            }
        });
        return examInfoHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ExamInfoHolder examInfoHolder, int i) {
        examInfoHolder.name.setText(examInfoDataBean.getExamInfo().get(i).getRealName());
        examInfoHolder.studntNo.setText(examInfoDataBean.getExamInfo().get(i).getStudentNo());
        examInfoHolder.examProgress.setText(ExamUtils.getExamProgress(examInfoDataBean.getExamInfo().get(i).getExamProgress()));
        examInfoHolder.updateExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProgress(examInfoDataBean.getExamInfo().get(i).getStudentNo(), examInfoDataBean.getExamInfo().get(i).getExamProgress());
            }
        });
    }

    private void updateProgress(String studentNo, String progressIndex) {
        if (!"取得驾照".equals(ExamUtils.getExamProgress(progressIndex))) {
            int displayWidth = context.getResources().getDisplayMetrics().widthPixels;
            float denisty = context.getResources().getDisplayMetrics().density;
            View updateProgressShow = LayoutInflater.from(context).inflate(R.layout.pop_exam_progress_update, null);
            TextView progressText = updateProgressShow.findViewById(R.id.progress_text);
            TextView sendUpdate = updateProgressShow.findViewById(R.id.send_exam_update);
            progressText.setText(ExamUtils.getExamProgress(String.valueOf(Integer.valueOf(progressIndex) + 1)));
            //Logger.d(String.valueOf(Integer.valueOf(progressIndex+1)));
            progressText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View progressSelect = LayoutInflater.from(context).inflate(R.layout.study_progress_select_pop, null);
                    ListView listView = progressSelect.findViewById(R.id.study_list_view);
                    List<String> examProgressList = ExamUtils.examProgressList();
                    StudyProgressSelectAdapter studyProgressSelectAdapter = new StudyProgressSelectAdapter(context, examProgressList, Integer.valueOf(progressIndex) - 1);
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
                            if (position >= Integer.valueOf(progressIndex)) {
                                progressText.setText(examProgressList.get(position));
                                selectPop.dismiss();
                            } else {
                                Toast.makeText(context, "该项目已通过", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    selectPop.showAtLocation(progressSelect, Gravity.CENTER, 0, 0);
                }
            });


            examTextPop = new PopupWindow(updateProgressShow, displayWidth - (int) (20 * denisty), ViewGroup.LayoutParams.WRAP_CONTENT);
            // 设置PopupWindow的背景
            examTextPop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            // 设置PopupWindow是否能响应外部点击事件
            examTextPop.setOutsideTouchable(true);
            // 设置PopupWindow是否能响应点击事件
            examTextPop.setTouchable(true);
            examTextPop.setFocusable(true);
            sendUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ExamProgressActivity) context).customProgressDialog.showProgressDialog();
                    sendprogress(studentNo, ExamUtils.getProgressCode(progressText.getText().toString()));
                }
            });

            WindowManager.LayoutParams layoutParams = ((ExamProgressActivity) context).getWindow().getAttributes();

            layoutParams.alpha = 0.7f;

            ((ExamProgressActivity) context).getWindow().setAttributes(layoutParams);
            examTextPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    WindowManager.LayoutParams layoutParams = ((ExamProgressActivity) context).getWindow().getAttributes();

                    layoutParams.alpha = 1.0f;

                    ((ExamProgressActivity) context).getWindow().setAttributes(layoutParams);
                }
            });

            examTextPop.showAtLocation(updateProgressShow, Gravity.CENTER, 0, 0);
        } else {
            Toast.makeText(context, "已取得驾照!", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendprogress(String studentNo, int progressCode) {
        String url = HttpUtils.setParameterForUrl(UPDATE_EXAM_PROGRESS, "studentNo", studentNo, "progress", progressCode + "");
        HttpUtils.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ((ExamProgressActivity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "提交失败！", Toast.LENGTH_SHORT).show();
                        ((ExamProgressActivity) context).customProgressDialog.cancelProgressDialog();
                        if (examTextPop != null && examTextPop.isShowing()) {
                            examTextPop.dismiss();
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int result = JsonUtils.getMessageSendStatus(response.body().string());
                if (result == -1) {
                    refreshData();
                } else {
                    ((ExamProgressActivity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "提交失败！", Toast.LENGTH_SHORT).show();
                            ((ExamProgressActivity) context).customProgressDialog.cancelProgressDialog();
                            if (examTextPop != null && examTextPop.isShowing()) {
                                examTextPop.dismiss();
                            }
                        }
                    });
                }
            }
        });
    }

    private void refreshData() {
        String url = HttpUtils.setParameterForUrl(GET_EXAM_PROGRESS, "no", Hawk.get("loginNo", ""));
        HttpUtils.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ((ExamProgressActivity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "提交失败！", Toast.LENGTH_SHORT).show();
                        ((ExamProgressActivity) context).customProgressDialog.cancelProgressDialog();
                        if (examTextPop != null && examTextPop.isShowing()) {
                            examTextPop.dismiss();
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ExamInfoDataBean newData = new Gson().fromJson(response.body().string(), ExamInfoDataBean.class);
                ((ExamProgressActivity) context).examInfoDataBean = newData;
                examInfoDataBean = newData;
                ((ExamProgressActivity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                        Toast.makeText(context, "提交成功！", Toast.LENGTH_SHORT).show();
                        ((ExamProgressActivity) context).customProgressDialog.cancelProgressDialog();
                        if (examTextPop != null && examTextPop.isShowing()) {
                            examTextPop.dismiss();
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        if (examInfoDataBean != null && examInfoDataBean.getExamInfo() != null) {
            return examInfoDataBean.getExamInfo().size();
        }
        return 0;
    }

    public void updateData(ExamInfoDataBean examInfoDataBean) {
        this.examInfoDataBean = examInfoDataBean;
        notifyDataSetChanged();
    }

    public static class ExamInfoHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView studntNo;
        TextView examProgress;
        TextView updateExam;

        public ExamInfoHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.exam_student_name);
            studntNo = itemView.findViewById(R.id.exam_student_no_text);
            examProgress = itemView.findViewById(R.id.exam_progress_text);
            updateExam = itemView.findViewById(R.id.exam_update);
        }
    }
}

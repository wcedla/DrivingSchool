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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.orhanobut.hawk.Hawk;
import com.wcedla.driving_school.R;
import com.wcedla.driving_school.activity.StudyProgressActivity;
import com.wcedla.driving_school.bean.StudyInfoDataBean;
import com.wcedla.driving_school.tool.HttpUtils;
import com.wcedla.driving_school.tool.JsonUtils;
import com.wcedla.driving_school.tool.StudyUtils;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.wcedla.driving_school.constant.Config.EMAIL_CHECK_REGEX;
import static com.wcedla.driving_school.constant.Config.GET_STUDY_INFO;
import static com.wcedla.driving_school.constant.Config.UPDATE_STUDY_PROGRESS;

public class StudyInfoShowAdapter extends RecyclerView.Adapter<StudyInfoShowAdapter.StudyInfoHolder> {

    Context context;
    StudyInfoDataBean studyInfoDataBean;
    PopupWindow updatePopWindow;

    public StudyInfoShowAdapter(Context context, StudyInfoDataBean studyInfoDataBean) {
        this.context = context;
        this.studyInfoDataBean = studyInfoDataBean;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public StudyInfoHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_study_info_item, viewGroup, false);
        StudyInfoHolder studyInfoHolder = new StudyInfoHolder(view);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((StudyProgressActivity) context).hideInput();
                return false;
            }
        });
        return studyInfoHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StudyInfoHolder studyInfoHolder, int i) {
        studyInfoHolder.name.setText(studyInfoDataBean.getStudyInfo().get(i).getRealName());
        studyInfoHolder.studentNo.setText(studyInfoDataBean.getStudyInfo().get(i).getStudentNo());
        studyInfoHolder.studyProgress.setText(StudyUtils.getStudyProgress(studyInfoDataBean.getStudyInfo().get(i).getStudyProgress()));
        studyInfoHolder.studyTime.setText(studyInfoDataBean.getStudyInfo().get(i).getTime() + "小时");
        studyInfoHolder.updateStudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProgress(studyInfoHolder.studentNo.getText().toString(), studyInfoHolder.studyProgress.getText().toString());
            }
        });

    }

    @Override
    public int getItemCount() {
        if (studyInfoDataBean.getStudyInfo() != null) {
            return studyInfoDataBean.getStudyInfo().size();
        }
        return 0;
    }

    private void updateProgress(String studentNo, String progressText) {
        if (!"全部课程学习完成".equals(progressText)) {
            int displayWidth = context.getResources().getDisplayMetrics().widthPixels;
            float denisty = context.getResources().getDisplayMetrics().density;
            View view = LayoutInflater.from(context).inflate(R.layout.study_progress_update_pop, null);
            TextView progressSelect = view.findViewById(R.id.progress_text);
            EditText timeInput = view.findViewById(R.id.time_text);
            TextView recommendedTime = view.findViewById(R.id.time_tips);
            TextView updateProgress = view.findViewById(R.id.send_study_update);
            String nextProgressText = StudyUtils.getStudyProgress(String.valueOf(StudyUtils.getStudyProgressLevel(progressText) + 2));
            progressSelect.setText(nextProgressText);
            setInputFilter(timeInput, EMAIL_CHECK_REGEX, 2);
            recommendedTime.setText("本项目规定学习时长不得低于" + StudyUtils.getStudyMinTime(nextProgressText) + "小时");
            if (nextProgressText.equals("全部课程学习完成")) {
                timeInput.setEnabled(false);
                timeInput.setText("0");
                timeInput.setClickable(false);
            }
            progressSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideInputMethod(timeInput);
                    View progressView = LayoutInflater.from(context).inflate(R.layout.study_progress_select_pop, null);
                    ListView listView = progressView.findViewById(R.id.study_list_view);
                    List<String> studyProgressList = StudyUtils.studyProgressList();
                    StudyProgressSelectAdapter studyProgressSelectAdapter = new StudyProgressSelectAdapter(context, studyProgressList, StudyUtils.getStudyProgressLevel(progressText));
                    listView.setAdapter(studyProgressSelectAdapter);
                    PopupWindow progressPop = new PopupWindow(progressView, progressSelect.getMeasuredWidth(), (int) (300 * denisty));
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
                            if (position > StudyUtils.getStudyProgressLevel(progressText)) {
                                progressSelect.setText(studyProgressList.get(position));
                                recommendedTime.setText("本项目规定学习时长不得低于" + StudyUtils.getStudyMinTime(studyProgressList.get(position)) + "小时");
                                if ("全部课程学习完成".equals(studyProgressList.get(position))) {
                                    timeInput.setEnabled(false);
                                    timeInput.setText("0");
                                    timeInput.setClickable(false);
                                } else {
                                    timeInput.setEnabled(true);
                                    timeInput.setText("");
                                    timeInput.setClickable(true);
                                }
                                progressPop.dismiss();
                            } else {
                                Toast.makeText(context, "该课程已经学习过啦!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    progressPop.showAtLocation(progressView, Gravity.CENTER, 0, 0);
                }
            });

            timeInput.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showInputMethod(timeInput);
                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideInputMethod(timeInput);
                }
            });

            updateProgress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideInputMethod(timeInput);
                    ((StudyProgressActivity) context).customProgressDialog.showProgressDialog();
                    updateStudyProgress(studentNo, String.valueOf(StudyUtils.getStudyProgressLevel(progressSelect.getText().toString()) + 1), timeInput.getText().toString());
                }
            });

            updatePopWindow = new PopupWindow(view, displayWidth - (int) (20 * denisty), ViewGroup.LayoutParams.WRAP_CONTENT);
            // 设置PopupWindow的背景
            updatePopWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            // 设置PopupWindow是否能响应外部点击事件
            updatePopWindow.setOutsideTouchable(true);
            // 设置PopupWindow是否能响应点击事件
            updatePopWindow.setTouchable(true);
            updatePopWindow.setFocusable(true);
            updatePopWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

            WindowManager.LayoutParams layoutParams = ((StudyProgressActivity) context).getWindow().getAttributes();

            layoutParams.alpha = 0.7f;

            ((StudyProgressActivity) context).getWindow().setAttributes(layoutParams);
            updatePopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    WindowManager.LayoutParams layoutParams = ((StudyProgressActivity) context).getWindow().getAttributes();

                    layoutParams.alpha = 1.0f;

                    ((StudyProgressActivity) context).getWindow().setAttributes(layoutParams);
                    hideInputMethod(timeInput);
                }
            });

        } else {
            Toast.makeText(context, "所有课程已经学习完毕!", Toast.LENGTH_SHORT).show();
        }

    }

    private void updateStudyProgress(String studentNo, String progress, String time) {
        String url = HttpUtils.setParameterForUrl(UPDATE_STUDY_PROGRESS, "studentNo", studentNo, "progress", progress, "time", time);
        HttpUtils.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                    ((StudyProgressActivity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "提交失败！", Toast.LENGTH_SHORT).show();
                            ((StudyProgressActivity) context).customProgressDialog.cancelProgressDialog();
                            if (updatePopWindow != null && updatePopWindow.isShowing()) {
                                updatePopWindow.dismiss();
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
                    ((StudyProgressActivity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "提交失败！", Toast.LENGTH_SHORT).show();
                            ((StudyProgressActivity) context).customProgressDialog.cancelProgressDialog();
                            if (updatePopWindow != null && updatePopWindow.isShowing()) {
                                updatePopWindow.dismiss();
                            }
                        }
                    });
                }
            }
        });
    }

    private void refreshData() {
        String url = HttpUtils.setParameterForUrl(GET_STUDY_INFO, "no", Hawk.get("loginNo", ""));
        HttpUtils.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ((StudyProgressActivity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "刷新失败！", Toast.LENGTH_SHORT).show();
                        ((StudyProgressActivity) context).customProgressDialog.cancelProgressDialog();
                        if (updatePopWindow != null && updatePopWindow.isShowing()) {
                            updatePopWindow.dismiss();
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                StudyInfoDataBean newData = new Gson().fromJson(response.body().string(), StudyInfoDataBean.class);
                ((StudyProgressActivity) context).studyInfoDataBean = newData;
                studyInfoDataBean = newData;
                ((StudyProgressActivity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "提交成功！", Toast.LENGTH_SHORT).show();
                        ((StudyProgressActivity) context).customProgressDialog.cancelProgressDialog();
                        notifyDataSetChanged();
                        if (updatePopWindow != null && updatePopWindow.isShowing()) {
                            updatePopWindow.dismiss();
                        }
                    }
                });

            }
        });
    }

    public void updateData(StudyInfoDataBean studyInfoDataBean) {
        this.studyInfoDataBean = studyInfoDataBean;
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
        targetView.setFocusable(false);
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            if (inputMethodManager.isActive()) {
                inputMethodManager.hideSoftInputFromWindow(targetView.getWindowToken(), 0);
            }
        }
    }

    public static class StudyInfoHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView studentNo;
        TextView studyProgress;
        TextView studyTime;
        TextView updateStudy;

        public StudyInfoHolder(@NonNull View view) {
            super(view);
            name = view.findViewById(R.id.study_title);
            studentNo = view.findViewById(R.id.study_student_no_text);
            studyProgress = view.findViewById(R.id.study_progress_text);
            studyTime = view.findViewById(R.id.study_time_text);
            updateStudy = view.findViewById(R.id.study_update);

        }
    }
}

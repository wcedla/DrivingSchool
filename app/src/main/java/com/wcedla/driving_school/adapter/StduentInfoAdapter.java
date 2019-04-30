package com.wcedla.driving_school.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.orhanobut.hawk.Hawk;
import com.wcedla.driving_school.R;
import com.wcedla.driving_school.activity.UserSearchActivity;
import com.wcedla.driving_school.bean.StudentDataBean;
import com.wcedla.driving_school.tool.ExamUtils;
import com.wcedla.driving_school.tool.HttpUtils;
import com.wcedla.driving_school.tool.JsonUtils;
import com.wcedla.driving_school.tool.StudyUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.wcedla.driving_school.constant.Config.GET_STUDENT_INFO;
import static com.wcedla.driving_school.constant.Config.SEND_STUDENT_APPRAISE;

public class StduentInfoAdapter extends RecyclerView.Adapter<StduentInfoAdapter.StudentInfoHolder> {

    Context context;
    StudentDataBean studentDataBean;
    View view;
    PopupWindow popupWindow;

    public StduentInfoAdapter(Context context, StudentDataBean studentDataBean) {
        this.context = context;
        this.studentDataBean = studentDataBean;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    //    ((UserSearchActivity)context).hideInput();
    @NonNull
    @Override
    public StudentInfoHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(context).inflate(R.layout.student_info_adapter, viewGroup, false);
        StudentInfoHolder studentInfoHolder = new StudentInfoHolder(view);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((UserSearchActivity) context).hideInput();
                return false;
            }
        });
        return studentInfoHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StudentInfoHolder studentInfoHolder, int i) {
        studentInfoHolder.name.setText(studentDataBean.getStudentInfo().get(i).getRealName());
        studentInfoHolder.no.setText(studentDataBean.getStudentInfo().get(i).getStudentNo());
        studentInfoHolder.mobile.setText(studentDataBean.getStudentInfo().get(i).getMobile());
        studentInfoHolder.studyProgress.setText(StudyUtils.getStudyProgress(studentDataBean.getStudentInfo().get(i).getStudyProgress()));
        studentInfoHolder.examProgress.setText(ExamUtils.getExamProgress(studentDataBean.getStudentInfo().get(i).getExamProgress()));
        studentInfoHolder.star.setText(studentDataBean.getStudentInfo().get(i).getStar() != null ? studentDataBean.getStudentInfo().get(i).getStar() + "星" : "暂未评价");
        if (studentDataBean.getStudentInfo().get(i).getStar() != null) {
            studentInfoHolder.appraise.setText("已评价");
            studentInfoHolder.appraise.setTextColor(Color.DKGRAY);
            studentInfoHolder.appraise.setEnabled(false);
        }
        studentInfoHolder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + studentDataBean.getStudentInfo().get(i).getMobile()));
                context.startActivity(callIntent);
            }
        });
        studentInfoHolder.appraise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doApprais(studentDataBean.getStudentInfo().get(i).getStudentNo());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (studentDataBean.getStudentInfo() != null) {
            return studentDataBean.getStudentInfo().size();
        }
        return 0;
    }

    public void updateData(StudentDataBean studentDataBean) {
        this.studentDataBean = studentDataBean;
        notifyDataSetChanged();
    }

    private void doApprais(String studentNo) {
        View view = LayoutInflater.from(context).inflate(R.layout.appraise_pop_window, null);

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
                    appraiseStar(studentNo, "1");
                } else if (star2.isSelected()) {
                    appraiseStar(studentNo, "2");
                } else if (star3.isSelected()) {
                    appraiseStar(studentNo, "3");
                } else if (star4.isSelected()) {
                    appraiseStar(studentNo, "4");
                } else if (star5.isSelected()) {
                    appraiseStar(studentNo, "5");
                } else {
                    Toast.makeText(context, "选择错误", Toast.LENGTH_SHORT).show();
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

        WindowManager.LayoutParams layoutParams = ((UserSearchActivity) context).getWindow().getAttributes();

        layoutParams.alpha = 0.7f;

        ((UserSearchActivity) context).getWindow().setAttributes(layoutParams);


        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                layoutParams.alpha = 1.0f;

                ((UserSearchActivity) context).getWindow().setAttributes(layoutParams);
            }
        });
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    private void appraiseStar(String studentNo, String star) {
        ((UserSearchActivity) context).customProgressDialog.showProgressDialog();
        String url = HttpUtils.setParameterForUrl(SEND_STUDENT_APPRAISE, "studentNo", studentNo, "star", star);
        HttpUtils.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ((UserSearchActivity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "评价失败！", Toast.LENGTH_SHORT).show();
                        if (popupWindow != null && popupWindow.isShowing()) {
                            popupWindow.dismiss();
                        }
                        ((UserSearchActivity) context).customProgressDialog.cancelProgressDialog();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int result = JsonUtils.getResetStatus(response.body().string());
                if (result == -1) {
                    refreshData();
                } else {
                    ((UserSearchActivity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "评价失败！", Toast.LENGTH_SHORT).show();
                            if (popupWindow != null && popupWindow.isShowing()) {
                                popupWindow.dismiss();
                            }
                            ((UserSearchActivity) context).customProgressDialog.cancelProgressDialog();
                        }
                    });
                }
            }
        });
    }

    private void refreshData() {
        String url = HttpUtils.setParameterForUrl(GET_STUDENT_INFO, "no", Hawk.get("loginNo", ""));
        HttpUtils.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(context, "评价失败！", Toast.LENGTH_SHORT).show();
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                ((UserSearchActivity) context).customProgressDialog.cancelProgressDialog();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                StudentDataBean newData = new Gson().fromJson(response.body().string(), StudentDataBean.class);
                ((UserSearchActivity) context).studentDataBean = newData;
                studentDataBean = newData;
                ((UserSearchActivity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "评价成功！", Toast.LENGTH_SHORT).show();
                        if (popupWindow != null && popupWindow.isShowing()) {
                            popupWindow.dismiss();
                        }
                        notifyDataSetChanged();
                        ((UserSearchActivity) context).customProgressDialog.cancelProgressDialog();
                    }
                });

            }
        });
    }

    public static class StudentInfoHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView no;
        TextView mobile;
        TextView studyProgress;
        TextView examProgress;
        TextView star;
        Button call;
        Button appraise;

        public StudentInfoHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.student_title);
            no = itemView.findViewById(R.id.student_no_text);
            mobile = itemView.findViewById(R.id.student_mobile_text);
            studyProgress = itemView.findViewById(R.id.student_study_text);
            examProgress = itemView.findViewById(R.id.student_exam_text);
            star = itemView.findViewById(R.id.student_star_text);
            call = itemView.findViewById(R.id.student_call);
            appraise = itemView.findViewById(R.id.student_star);

        }
    }
}

package com.wcedla.driving_school.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wcedla.driving_school.R;
import com.wcedla.driving_school.activity.StudyInfoDetialActivity;
import com.wcedla.driving_school.bean.MyStudyInfoDataBean;
import com.wcedla.driving_school.tool.StudyUtils;

public class StudyInfoDetialAdapter extends RecyclerView.Adapter<StudyInfoDetialAdapter.StudyInfoDetialHolder> {

    Context context;
    MyStudyInfoDataBean myStudyInfoDataBean;

    public StudyInfoDetialAdapter(Context context, MyStudyInfoDataBean myStudyInfoDataBean) {
        this.context = context;
        this.myStudyInfoDataBean = myStudyInfoDataBean;

    }

    @NonNull
    @Override
    public StudyInfoDetialHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_study_info_detial_item, viewGroup, false);
        StudyInfoDetialHolder studyInfoDetialHolder = new StudyInfoDetialHolder(view);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((StudyInfoDetialActivity) context).hideInput();
                return false;
            }
        });
        return studyInfoDetialHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StudyInfoDetialHolder studyInfoDetialHolder, int i) {
        studyInfoDetialHolder.name.setText(myStudyInfoDataBean.getAllStudyInfo().get(i).getRealName());
        studyInfoDetialHolder.no.setText(myStudyInfoDataBean.getAllStudyInfo().get(i).getStudentNo());
        studyInfoDetialHolder.progress.setText(StudyUtils.getStudyProgress(myStudyInfoDataBean.getAllStudyInfo().get(i).getStudyProgress()));
        studyInfoDetialHolder.time.setText(myStudyInfoDataBean.getAllStudyInfo().get(i).getTime() + "小时");

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

    public static class StudyInfoDetialHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView no;
        TextView progress;
        TextView time;

        public StudyInfoDetialHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.study_detial_name);
            no = itemView.findViewById(R.id.study_detial_no_text);
            progress = itemView.findViewById(R.id.study_detial_progress_text);
            time = itemView.findViewById(R.id.study_detial_time_text);
        }
    }

}

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
import com.wcedla.driving_school.activity.MyFeedActivity;
import com.wcedla.driving_school.bean.MyFeedDataBean;
import com.wcedla.driving_school.tool.ExamUtils;
import com.wcedla.driving_school.tool.StudyUtils;

public class MyFeedAdapter extends RecyclerView.Adapter<MyFeedAdapter.MyFeedHolder> {

    Context context;
    MyFeedDataBean myFeedDataBean;

    public MyFeedAdapter(Context context, MyFeedDataBean myFeedDataBean) {
        this.context = context;
        this.myFeedDataBean = myFeedDataBean;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public MyFeedHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_my_feed, viewGroup, false);
        MyFeedHolder myFeedHolder = new MyFeedHolder(view);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((MyFeedActivity) context).hideInput();
                return false;
            }
        });
        return myFeedHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyFeedHolder myFeedHolder, int i) {
        if (myFeedDataBean.getMyFeedInfo().get(i).getType().equals("1")) {
            myFeedHolder.name.setText(myFeedDataBean.getMyFeedInfo().get(i).getRealName());
            myFeedHolder.no.setText(myFeedDataBean.getMyFeedInfo().get(i).getStudentNo());
            myFeedHolder.type.setText("时长错误");
            myFeedHolder.progress.setText(StudyUtils.getStudyProgress(myFeedDataBean.getMyFeedInfo().get(i).getProgress()));
            myFeedHolder.time.setVisibility(View.VISIBLE);
            myFeedHolder.timeHead.setVisibility(View.VISIBLE);
            myFeedHolder.time.setText(myFeedDataBean.getMyFeedInfo().get(i).getMessage() + "小时");
            myFeedHolder.status.setText(myFeedDataBean.getMyFeedInfo().get(i).getStatus());
        } else if (myFeedDataBean.getMyFeedInfo().get(i).getType().equals("2")) {
            myFeedHolder.name.setText(myFeedDataBean.getMyFeedInfo().get(i).getRealName());
            myFeedHolder.no.setText(myFeedDataBean.getMyFeedInfo().get(i).getStudentNo());
            myFeedHolder.type.setText("进度错误");
            myFeedHolder.progress.setText(StudyUtils.getStudyProgress(myFeedDataBean.getMyFeedInfo().get(i).getMessage()));
            myFeedHolder.time.setVisibility(View.GONE);
            myFeedHolder.timeHead.setVisibility(View.GONE);
            myFeedHolder.time.setText(myFeedDataBean.getMyFeedInfo().get(i).getMessage() + "小时");
            myFeedHolder.status.setText(myFeedDataBean.getMyFeedInfo().get(i).getStatus());
        } else if (myFeedDataBean.getMyFeedInfo().get(i).getType().equals("3")) {
            myFeedHolder.name.setText(myFeedDataBean.getMyFeedInfo().get(i).getRealName());
            myFeedHolder.no.setText(myFeedDataBean.getMyFeedInfo().get(i).getStudentNo());
            myFeedHolder.type.setText("考试进度");
            myFeedHolder.progress.setText(ExamUtils.getExamProgress(myFeedDataBean.getMyFeedInfo().get(i).getProgress()));
            myFeedHolder.time.setVisibility(View.GONE);
            myFeedHolder.timeHead.setVisibility(View.GONE);
            myFeedHolder.status.setText(myFeedDataBean.getMyFeedInfo().get(i).getStatus());
        }
    }

    @Override
    public int getItemCount() {
        if (myFeedDataBean != null && myFeedDataBean.getMyFeedInfo() != null && !myFeedDataBean.getMyFeedInfo().isEmpty()) {
            return myFeedDataBean.getMyFeedInfo().size();
        }
        return 0;
    }

    public void updateData(MyFeedDataBean myFeedDataBean) {
        this.myFeedDataBean = myFeedDataBean;
        notifyDataSetChanged();
    }

    public static class MyFeedHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView no;
        TextView type;
        TextView progress;
        TextView time;
        TextView timeHead;
        TextView status;

        public MyFeedHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.my_feed_name);
            no = itemView.findViewById(R.id.my_feed_no_text);
            type = itemView.findViewById(R.id.my_feed_type_text);
            progress = itemView.findViewById(R.id.my_feed_progress_text);
            time = itemView.findViewById(R.id.my_feed_time_text);
            timeHead = itemView.findViewById(R.id.my_feed_time_head);
            status = itemView.findViewById(R.id.my_feed_status_text);
            ;
        }
    }
}

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
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wcedla.driving_school.R;
import com.wcedla.driving_school.activity.MyStudyActivity;
import com.wcedla.driving_school.bean.MyStudyInfoDataBean;
import com.wcedla.driving_school.tool.StudyUtils;

public class MyStudyInfoAdapter extends RecyclerView.Adapter<MyStudyInfoAdapter.MyStudyInfoHolder> {

    Context context;
    MyStudyInfoDataBean myStudyInfoDataBean;

    PopupWindow feedPopWindow;

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
                showFeedPop();
            }
        });
    }

    private void showFeedPop() {
        int displayWidth = context.getResources().getDisplayMetrics().widthPixels;
        float denisty = context.getResources().getDisplayMetrics().density;
        View view = LayoutInflater.from(context).inflate(R.layout.pop_study_feed, null);
        TextView feedType = view.findViewById(R.id.feed_type_text);
        feedType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View typeView = LayoutInflater.from(context).inflate(R.layout.pop_study_feed_type, null);
                PopupWindow typeSelectPop = new PopupWindow(typeView, feedType.getMeasuredWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
                // 设置PopupWindow的背景
                typeSelectPop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                // 设置PopupWindow是否能响应外部点击事件
                typeSelectPop.setOutsideTouchable(true);
                // 设置PopupWindow是否能响应点击事件
                typeSelectPop.setTouchable(true);
                typeSelectPop.setFocusable(true);
                typeSelectPop.showAtLocation(typeView, Gravity.CENTER, 0, 0);
            }
        });


        feedPopWindow = new PopupWindow(view, displayWidth - (int) (20 * denisty), ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置PopupWindow的背景
        feedPopWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 设置PopupWindow是否能响应外部点击事件
        feedPopWindow.setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        feedPopWindow.setTouchable(true);
        feedPopWindow.setFocusable(true);
        feedPopWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        WindowManager.LayoutParams layoutParams = ((MyStudyActivity) context).getWindow().getAttributes();

        layoutParams.alpha = 0.7f;

        ((MyStudyActivity) context).getWindow().setAttributes(layoutParams);
        feedPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams layoutParams = ((MyStudyActivity) context).getWindow().getAttributes();

                layoutParams.alpha = 1.0f;

                ((MyStudyActivity) context).getWindow().setAttributes(layoutParams);
                //hideInputMethod(timeInput);
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

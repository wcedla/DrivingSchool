package com.wcedla.driving_school.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wcedla.driving_school.R;
import com.wcedla.driving_school.activity.ExamProgressActivity;
import com.wcedla.driving_school.activity.StudyProgressActivity;
import com.wcedla.driving_school.activity.UserSearchActivity;
import com.wcedla.driving_school.bean.FunctionItemBean;

import java.util.List;

public class CoachGridAdapter extends BaseAdapter {

    Context context;
    List<FunctionItemBean> functionItemBeanList;

    public CoachGridAdapter(Context context, List<FunctionItemBean> functionItemBeanList) {
        this.context = context;
        this.functionItemBeanList = functionItemBeanList;
    }

    @Override
    public int getCount() {
        return functionItemBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return functionItemBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FunctionItemAdapterHolder functionItemAdapterHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_admin_item, null);
            functionItemAdapterHolder = new FunctionItemAdapterHolder();
            functionItemAdapterHolder.itemImage = convertView.findViewById(R.id.admin_adapter_img);
            functionItemAdapterHolder.itemName = convertView.findViewById(R.id.admin_adapter_name);
            convertView.setTag(functionItemAdapterHolder);
        }
        functionItemAdapterHolder = (FunctionItemAdapterHolder) convertView.getTag();
        functionItemAdapterHolder.itemImage.setImageResource(functionItemBeanList.get(position).getImg());
        functionItemAdapterHolder.itemName.setText(functionItemBeanList.get(position).getTitle());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getClickEvent(position);
            }
        });
        return convertView;
    }

    private void getClickEvent(int position) {
        switch (position) {
            case 0:
                Intent userSearchIntent = new Intent(context, UserSearchActivity.class);
                context.startActivity(userSearchIntent);
                break;
            case 1:
                Intent studyProgressIntent = new Intent(context, StudyProgressActivity.class);
                context.startActivity(studyProgressIntent);
                break;
            case 2:
                Intent examProgressIntent = new Intent(context, ExamProgressActivity.class);
                context.startActivity(examProgressIntent);
                break;
//            case 3:
//                Intent reservationIntent = new Intent(context, SubjectReservationActivity.class);
//                context.startActivity(reservationIntent);
//                break;
            default:
                break;
        }
    }

    class FunctionItemAdapterHolder {
        ImageView itemImage;

        TextView itemName;
    }
}

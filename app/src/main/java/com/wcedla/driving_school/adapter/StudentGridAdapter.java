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
import com.wcedla.driving_school.activity.MyCoachActivity;
import com.wcedla.driving_school.activity.MyExamActivity;
import com.wcedla.driving_school.activity.MyFeedActivity;
import com.wcedla.driving_school.activity.MyStudyActivity;
import com.wcedla.driving_school.bean.FunctionItemBean;

import java.util.List;

public class StudentGridAdapter extends BaseAdapter {
    Context context;
    List<FunctionItemBean> functionItemBeanList;

    public StudentGridAdapter(Context context, List<FunctionItemBean> functionItemBeanList) {
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
        StudentGridAdapter.FunctionItemAdapterHolder functionItemAdapterHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_admin_item, null);
            functionItemAdapterHolder = new StudentGridAdapter.FunctionItemAdapterHolder();
            functionItemAdapterHolder.itemImage = convertView.findViewById(R.id.admin_adapter_img);
            functionItemAdapterHolder.itemName = convertView.findViewById(R.id.admin_adapter_name);
            convertView.setTag(functionItemAdapterHolder);
        }
        functionItemAdapterHolder = (StudentGridAdapter.FunctionItemAdapterHolder) convertView.getTag();
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
                Intent myCoahIntent = new Intent(context, MyCoachActivity.class);
                context.startActivity(myCoahIntent);
                break;
            case 1:
                Intent myStudyIntent = new Intent(context, MyStudyActivity.class);
                context.startActivity(myStudyIntent);
                break;
            case 2:
                Intent myExamIntent = new Intent(context, MyExamActivity.class);
                context.startActivity(myExamIntent);
                break;
            case 3:
                Intent myFeedIntent = new Intent(context, MyFeedActivity.class);
                context.startActivity(myFeedIntent);
                break;
            default:
                break;
        }
    }

    class FunctionItemAdapterHolder {
        ImageView itemImage;

        TextView itemName;
    }
}

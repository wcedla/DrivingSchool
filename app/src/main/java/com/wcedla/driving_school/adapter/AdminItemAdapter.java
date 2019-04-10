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
import com.wcedla.driving_school.activity.ShowAuthInfoActivity;
import com.wcedla.driving_school.bean.AdminItemAdapterBean;

import java.util.List;

public class AdminItemAdapter extends BaseAdapter {

    Context context;
    List<AdminItemAdapterBean> adminItemAdapterBeanList;

    public AdminItemAdapter(Context context, List<AdminItemAdapterBean> adminItemAdapterBeanList) {
        this.context = context;
        this.adminItemAdapterBeanList = adminItemAdapterBeanList;
    }

    @Override
    public int getCount() {
        return adminItemAdapterBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return adminItemAdapterBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AdminItemAdapterHolder adminItemAdapterHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_admin_item, null);
            adminItemAdapterHolder = new AdminItemAdapterHolder();
            adminItemAdapterHolder.itemImage = convertView.findViewById(R.id.admin_adapter_img);
            adminItemAdapterHolder.itemName = convertView.findViewById(R.id.admin_adapter_name);
            convertView.setTag(adminItemAdapterHolder);
        } else {
            adminItemAdapterHolder = (AdminItemAdapterHolder) convertView.getTag();
        }
        adminItemAdapterHolder.itemImage.setImageResource(adminItemAdapterBeanList.get(position).getItemIcon());
        adminItemAdapterHolder.itemName.setText(adminItemAdapterBeanList.get(position).getItemName());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getClickEvent(position);
            }
        });
        return convertView;
    }

    class AdminItemAdapterHolder {
        ImageView itemImage;

        TextView itemName;
    }

    private void getClickEvent(int position) {
        switch (position) {
            case 0:
                Intent authInfo=new Intent(context, ShowAuthInfoActivity.class);
                context.startActivity(authInfo);
                break;
            default:
                break;
        }
    }
}

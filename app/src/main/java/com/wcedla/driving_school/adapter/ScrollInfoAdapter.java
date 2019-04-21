package com.wcedla.driving_school.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wcedla.driving_school.R;
import com.wcedla.driving_school.activity.ShowInfoActivity;
import com.wcedla.driving_school.bean.ScrollInfoBean;

import java.util.List;

public class ScrollInfoAdapter extends RecyclerView.Adapter<ScrollInfoAdapter.ScrollInfoHolder> {

    Context context;
    ScrollInfoBean scrollInfoBean;


    public ScrollInfoAdapter(Context context, ScrollInfoBean scrollInfoBean) {
        this.context = context;
        this.scrollInfoBean = scrollInfoBean;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public ScrollInfoHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_scroll_info, viewGroup, false);
        ScrollInfoHolder scrollInfoHolder = new ScrollInfoHolder(view);
        return scrollInfoHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ScrollInfoHolder scrollInfoHolder, int i) {
        scrollInfoHolder.typeText.setText("[ "+scrollInfoBean.getSkill().get(i).getType()+" ]");
        scrollInfoHolder.titleText.setText(scrollInfoBean.getSkill().get(i).getTitle());
        scrollInfoHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent infoDetialIntent=new Intent(context, ShowInfoActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("headText","技巧详情");
                bundle.putString("contentText",scrollInfoBean.getSkill().get(i).getContentText());
                infoDetialIntent.putExtras(bundle);
                context.startActivity(infoDetialIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (scrollInfoBean.getSkill() != null) {
            return scrollInfoBean.getSkill().size();
        } else {
            return 0;
        }
    }

    public static class ScrollInfoHolder extends RecyclerView.ViewHolder {

        TextView typeText;

        TextView titleText;

        public ScrollInfoHolder(@NonNull View itemView) {
            super(itemView);
            typeText=itemView.findViewById(R.id.scroll_info_type_text);
            titleText=itemView.findViewById(R.id.scroll_info_title_text);
        }
    }
}

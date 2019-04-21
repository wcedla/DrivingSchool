package com.wcedla.driving_school.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.wcedla.driving_school.R;
import com.wcedla.driving_school.activity.ShowInfoActivity;
import com.wcedla.driving_school.bean.LawInfoBean;

public class LawInfoShowAdapter extends RecyclerView.Adapter<LawInfoShowAdapter.LawInfoHolder> {

    Context context;
    LawInfoBean lawInfoBean;
    float denisty;

    public LawInfoShowAdapter(Context context,LawInfoBean lawInfoBean) {
        this.context=context;
        this.lawInfoBean=lawInfoBean;
        denisty=context.getResources().getDisplayMetrics().density;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public LawInfoHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_law_show,viewGroup,false);
        LawInfoHolder lawInfoHolder=new LawInfoHolder(view);
        return lawInfoHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LawInfoHolder lawInfoHolder, int i) {
        Glide.with(context).load(lawInfoBean.getLaw().get(i).getImg()).apply(requestOptions).into(lawInfoHolder.lawImage);
        lawInfoHolder.titleText.setText(lawInfoBean.getLaw().get(i).getTitle());
        lawInfoHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent infoDetialIntent=new Intent(context, ShowInfoActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("headText","新规详情");
                bundle.putString("contentText",lawInfoBean.getLaw().get(i).getContentText());
                infoDetialIntent.putExtras(bundle);
                context.startActivity(infoDetialIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(lawInfoBean.getLaw()!=null)
        {
            return lawInfoBean.getLaw().size();
        }
        else {
            return 0;
        }
    }

    public static class LawInfoHolder extends RecyclerView.ViewHolder {

        ImageView lawImage;
        TextView titleText;

        public LawInfoHolder(@NonNull View itemView) {
            super(itemView);
            lawImage=itemView.findViewById(R.id.law_info_img);
            titleText=itemView.findViewById(R.id.law_info_title);
        }
    }

    RequestOptions requestOptions=new RequestOptions()
            .override((int)(90*denisty),(int)(70*denisty))
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
}

package com.wcedla.driving_school.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wcedla.driving_school.R;
import com.wcedla.driving_school.activity.MessageActivity;
import com.wcedla.driving_school.bean.MessageShowBean;

public class MessageShowAdapter extends RecyclerView.Adapter<MessageShowAdapter.MessageShowHolder> {

    Context context;
    MessageShowBean messageShowBean;

    public MessageShowAdapter(Context context,MessageShowBean messageShowBean) {
        this.context=context;
        this.messageShowBean=messageShowBean;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public MessageShowHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_message_show,viewGroup,false);
        MessageShowHolder messageShowHolder=new MessageShowHolder(view);
        return messageShowHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageShowHolder messageShowHolder, int i) {
        messageShowHolder.messageContent.setText(messageShowBean.getMessage().get(i).getMessageContent());
        messageShowHolder.messageTime.setText(messageShowBean.getMessage().get(i).getMessageTime());
        messageShowHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MessageActivity)context).closeInputMethod();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(messageShowBean.getMessage()!=null)
        {
            return messageShowBean.getMessage().size();
        }
        else
        {
            return 0;
        }
    }

    public static class MessageShowHolder extends RecyclerView.ViewHolder {

        TextView messageContent;
        TextView messageTime;

        public MessageShowHolder(@NonNull View itemView) {
            super(itemView);
            messageContent=itemView.findViewById(R.id.message_content);
            messageTime=itemView.findViewById(R.id.message_time);
        }
    }

}


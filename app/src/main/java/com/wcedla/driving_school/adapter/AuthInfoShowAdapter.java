package com.wcedla.driving_school.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wcedla.driving_school.R;
import com.wcedla.driving_school.activity.ShowAuthInfoActivity;
import com.wcedla.driving_school.bean.AuthInfoBean;
import com.wcedla.driving_school.tool.HttpUtils;
import com.wcedla.driving_school.tool.JsonUtils;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.wcedla.driving_school.constant.Config.AUTHENTICATION_SET_STATUS_URL;

public class AuthInfoShowAdapter extends RecyclerView.Adapter<AuthInfoShowAdapter.AuthInfoHolder> {

    Context context;
    List<AuthInfoBean.AuthBean> authBeanList;

    public AuthInfoShowAdapter(Context context,List<AuthInfoBean.AuthBean> authBeanList) {
        this.context=context;
        this.authBeanList=authBeanList;
    }

    @NonNull
    @Override
    public AuthInfoHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.auth_info_adapter,viewGroup,false);
        AuthInfoHolder authInfoHolder=new AuthInfoHolder(view);
        return authInfoHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AuthInfoHolder authInfoHolder, int i) {
        authInfoHolder.statusTitle.setText(authBeanList.get(i).getAuthStatus());
        authInfoHolder.userText.setText(authBeanList.get(i).getNickName());
        authInfoHolder.nameText.setText(authBeanList.get(i).getRealName());
        authInfoHolder.noText.setText(authBeanList.get(i).getNo());
        authInfoHolder.typeText.setText(authBeanList.get(i).getType());
        if("审核中".equals(authBeanList.get(i).getAuthStatus()))
        {
            authInfoHolder.authOk.setText("通过审核");
            authInfoHolder.authFailed.setText("拒绝通过");
        }
        else if("审核通过".equals(authBeanList.get(i).getAuthStatus()))
        {
            authInfoHolder.authOk.setText("取消通过");
            authInfoHolder.authFailed.setVisibility(View.GONE);
        }
        else if("审核未通过".equals(authBeanList.get(i).getAuthStatus()))
        {
            authInfoHolder.authOk.setText("通过审核");
            authInfoHolder.authFailed.setVisibility(View.GONE);
        }
        authInfoHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ShowAuthInfoActivity)context).recyclerViewHideInput();
            }
        });
        authInfoHolder.authOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ShowAuthInfoActivity)context).recyclerViewHideInput();
                if (authInfoHolder.authOk.getText().toString().equals("通过审核"))
                {
                    setAuthStatus(authInfoHolder.userText.getText().toString(),"审核通过",authBeanList.get(i).getType(),authBeanList.get(i).getNo());
                }
                else
                {
                    setAuthStatus(authInfoHolder.userText.getText().toString(),"审核未通过",authBeanList.get(i).getType(),authBeanList.get(i).getNo());
                }
            }
        });
        authInfoHolder.authFailed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ShowAuthInfoActivity)context).recyclerViewHideInput();
                setAuthStatus(authInfoHolder.userText.getText().toString(),"审核未通过",authBeanList.get(i).getType(),authBeanList.get(i).getNo());
            }
        });
    }

    @Override
    public int getItemCount() {
        return authBeanList.size();
    }


    public static class AuthInfoHolder extends RecyclerView.ViewHolder
    {
        TextView statusTitle;
        TextView userText;
        TextView nameText;
        TextView noText;
        TextView typeText;
        Button authOk;
        Button authFailed;


        public AuthInfoHolder(View itemView) {
            super(itemView);
            statusTitle=itemView.findViewById(R.id.auth_info_title);
            userText=itemView.findViewById(R.id.auth_info_user_text);
            nameText=itemView.findViewById(R.id.auth_info_name_text);
            noText=itemView.findViewById(R.id.auth_info_no_text);
            typeText=itemView.findViewById(R.id.auth_info_type_text);
            authOk=itemView.findViewById(R.id.auth_info_ok);
            authFailed=itemView.findViewById(R.id.auth_info_failed);
        }
    }

    private void setAuthStatus(String user,String status,String type,String no)
    {
        String url= HttpUtils.setParameterForUrl(AUTHENTICATION_SET_STATUS_URL,"userName",user,"status",status,"type",type,"no",no);
        HttpUtils.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ((ShowAuthInfoActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context,"更改失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseString=response.body().string();
                int result= JsonUtils.getAuthenticationStatus(responseString);
                if(result==-1)
                {
                    ((ShowAuthInfoActivity)context).getAllAuthInfo();
                    ((ShowAuthInfoActivity)context).setAuthStatusMQTT(user,status);
                }
                else
                {
                    ((ShowAuthInfoActivity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context,"更改失败",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}

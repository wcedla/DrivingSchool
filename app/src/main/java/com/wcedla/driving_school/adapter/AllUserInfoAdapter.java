package com.wcedla.driving_school.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wcedla.driving_school.R;
import com.wcedla.driving_school.activity.ShowUserInfoActivity;
import com.wcedla.driving_school.bean.AllUserInfoDataBean;
import com.wcedla.driving_school.tool.HttpUtils;
import com.wcedla.driving_school.tool.JsonUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.wcedla.driving_school.constant.Config.DELETE_USER_INFO;
import static com.wcedla.driving_school.constant.Config.GET_ALL_USER_INFO;

public class AllUserInfoAdapter extends RecyclerView.Adapter<AllUserInfoAdapter.UserInfoHolder> {

    Context context;
    AllUserInfoDataBean allUserInfoDataBean;

    int showType = 0;

    public AllUserInfoAdapter(Context context, AllUserInfoDataBean allUserInfoDataBean) {
        this.context = context;
        this.allUserInfoDataBean = allUserInfoDataBean;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public UserInfoHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_all_user_info, viewGroup, false);
        UserInfoHolder userInfoHolder = new UserInfoHolder(view);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((ShowUserInfoActivity) context).hideInput();
                return false;
            }
        });
        return userInfoHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserInfoHolder userInfoHolder, int i) {
        if (showType == 0) {
            if (i <= allUserInfoDataBean.getCoachUser().size() - 1) {
                userInfoHolder.name.setText(allUserInfoDataBean.getCoachUser().get(i).getRealName());
                userInfoHolder.no.setText(allUserInfoDataBean.getCoachUser().get(i).getCoachNo());
                userInfoHolder.noHead.setText("教练号:");
                userInfoHolder.time.setText(allUserInfoDataBean.getCoachUser().get(i).getDriveAge());
                userInfoHolder.timeHead.setText("驾龄:");
                userInfoHolder.mobile.setText(allUserInfoDataBean.getCoachUser().get(i).getMobile());
            } else {
                userInfoHolder.name.setText(allUserInfoDataBean.getStudentUser().get(i - allUserInfoDataBean.getCoachUser().size()).getRealName());
                userInfoHolder.noHead.setText("学员号:");
                userInfoHolder.no.setText(allUserInfoDataBean.getStudentUser().get(i - allUserInfoDataBean.getCoachUser().size()).getStudentNo());
                userInfoHolder.timeHead.setText("报名时间:");
                userInfoHolder.time.setText(allUserInfoDataBean.getStudentUser().get(i - allUserInfoDataBean.getCoachUser().size()).getEnrollTime());
                userInfoHolder.mobile.setText(allUserInfoDataBean.getStudentUser().get(i - allUserInfoDataBean.getCoachUser().size()).getMobile());
            }
        } else if (showType == 1) {
            userInfoHolder.name.setText(allUserInfoDataBean.getCoachUser().get(i).getRealName());
            userInfoHolder.no.setText(allUserInfoDataBean.getCoachUser().get(i).getCoachNo());
            userInfoHolder.noHead.setText("教练号:");
            userInfoHolder.time.setText(allUserInfoDataBean.getCoachUser().get(i).getDriveAge());
            userInfoHolder.timeHead.setText("驾龄:");
            userInfoHolder.mobile.setText(allUserInfoDataBean.getCoachUser().get(i).getMobile());
        } else if (showType == 2) {
            userInfoHolder.name.setText(allUserInfoDataBean.getStudentUser().get(i).getRealName());
            userInfoHolder.noHead.setText("学员号:");
            userInfoHolder.no.setText(allUserInfoDataBean.getStudentUser().get(i).getStudentNo());
            userInfoHolder.timeHead.setText("报名时间:");
            userInfoHolder.time.setText(allUserInfoDataBean.getStudentUser().get(i).getEnrollTime());
            userInfoHolder.mobile.setText(allUserInfoDataBean.getStudentUser().get(i).getMobile());
        }
        userInfoHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("确认删除");
                alertDialog.setMessage("确定要删除该用户么？");
                alertDialog.setCancelable(true);
                alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (userInfoHolder.timeHead.length() == 3) {
                            ((ShowUserInfoActivity) context).customProgressDialog.showProgressDialog();
                            deleteUser("教练", userInfoHolder.no.getText().toString());
                        } else {
                            ((ShowUserInfoActivity) context).customProgressDialog.showProgressDialog();
                            deleteUser("学员", userInfoHolder.no.getText().toString());
                        }
                        //Toast.makeText(context,"确认删除！"+userInfoHolder.timeHead.length(),Toast.LENGTH_SHORT).show();
                    }
                });
                alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alertDialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        if (showType == 0) {
            if (allUserInfoDataBean != null && allUserInfoDataBean.getCoachUser() != null && allUserInfoDataBean.getStudentUser() != null) {
                return allUserInfoDataBean.getCoachUser().size() + allUserInfoDataBean.getStudentUser().size();
            }
            return 0;
        } else if (showType == 1) {
            if (allUserInfoDataBean != null && allUserInfoDataBean.getCoachUser() != null && !allUserInfoDataBean.getCoachUser().isEmpty()) {
                return allUserInfoDataBean.getCoachUser().size();
            }
            return 0;
        } else if (showType == 2) {
            if (allUserInfoDataBean != null && allUserInfoDataBean.getStudentUser() != null && !allUserInfoDataBean.getStudentUser().isEmpty()) {
                return allUserInfoDataBean.getStudentUser().size();
            }
            return 0;
        }
        return 0;

    }

    public void updateData(AllUserInfoDataBean allUserInfoDataBean, int showType) {
        this.allUserInfoDataBean = allUserInfoDataBean;
        this.showType = showType;
        notifyDataSetChanged();
    }

    private void deleteUser(String type, String no) {
        String url = HttpUtils.setParameterForUrl(DELETE_USER_INFO, "type", type, "no", no);
        HttpUtils.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ((ShowUserInfoActivity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (((ShowUserInfoActivity) context).customProgressDialog != null) {
                            ((ShowUserInfoActivity) context).customProgressDialog.cancelProgressDialog();
                        }
                        Toast.makeText(context, "删除失败请重试！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int result = JsonUtils.getResetStatus(response.body().string());
                if (result == -1) {
                    refreshData();
//                    ((ShowUserInfoActivity)context).runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if(((ShowUserInfoActivity)context).customProgressDialog!=null) {
//                                ((ShowUserInfoActivity) context).customProgressDialog.cancelProgressDialog();
//                            }
//                            Toast.makeText(context,"删除成功！",Toast.LENGTH_SHORT).show();
//                        }
//                    });
                } else {
                    ((ShowUserInfoActivity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (((ShowUserInfoActivity) context).customProgressDialog != null) {
                                ((ShowUserInfoActivity) context).customProgressDialog.cancelProgressDialog();
                            }
                            Toast.makeText(context, "删除失败，请重试！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void refreshData() {
        HttpUtils.doHttpRequest(GET_ALL_USER_INFO, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ((ShowUserInfoActivity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (((ShowUserInfoActivity) context).customProgressDialog != null) {
                            ((ShowUserInfoActivity) context).customProgressDialog.cancelProgressDialog();
                        }
                        Toast.makeText(context, "删除成功，数据刷新失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                allUserInfoDataBean = new Gson().fromJson(response.body().string(), AllUserInfoDataBean.class);
                ((ShowUserInfoActivity) context).allUserInfoDataBean = allUserInfoDataBean;
                ((ShowUserInfoActivity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (((ShowUserInfoActivity) context).customProgressDialog != null) {
                            ((ShowUserInfoActivity) context).customProgressDialog.cancelProgressDialog();
                        }
                        notifyDataSetChanged();
                        Toast.makeText(context, "删除成功！", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public static class UserInfoHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView no;
        TextView noHead;
        TextView time;
        TextView timeHead;
        TextView mobile;
        Button delete;

        public UserInfoHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.all_user_info_name);
            no = itemView.findViewById(R.id.all_user_info_no_text);
            noHead = itemView.findViewById(R.id.all_user_info_no_head);
            time = itemView.findViewById(R.id.all_user_info_time_text);
            timeHead = itemView.findViewById(R.id.all_user_info_time_head);
            mobile = itemView.findViewById(R.id.all_user_info_mobile_text);
            delete = itemView.findViewById(R.id.all_user_info_delete);
        }
    }

}

package com.wcedla.driving_school.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wcedla.driving_school.R;
import com.wcedla.driving_school.adapter.AdminItemAdapter;
import com.wcedla.driving_school.bean.AdminItemAdapterBean;
import com.wcedla.driving_school.customview.SplitLineGridview;
import com.wcedla.driving_school.service.MQTTService;
import com.wcedla.driving_school.tool.ToolUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdminMainActivity extends AppCompatActivity {


    @BindView(R.id.admin_item_grid)
    SplitLineGridview adminItemGrid;
    List<AdminItemAdapterBean> itemAdapterBeanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolUtils.setNavigationBarStatusBarTranslucent(this, false, false);
        setContentView(R.layout.activity_admin_main);
        ButterKnife.bind(this);
        itemAdapterBeanList = new ArrayList<>();
        setData();
        AdminItemAdapter adminItemAdapter = new AdminItemAdapter(this, itemAdapterBeanList);
        adminItemGrid.setAdapter(adminItemAdapter);
        Intent MQTTServiceIntent = new Intent(AdminMainActivity.this, MQTTService.class);
        startService(MQTTServiceIntent);
    }



    private void setData() {
        AdminItemAdapterBean authCheck = new AdminItemAdapterBean(R.drawable.admin_auth, "用户认证");
        itemAdapterBeanList.add(authCheck);
        AdminItemAdapterBean userSearch=new AdminItemAdapterBean(R.drawable.user_search,"用户查找");
        itemAdapterBeanList.add(userSearch);
        AdminItemAdapterBean feedCheck = new AdminItemAdapterBean(R.drawable.admin_feed, "用户反馈");
        itemAdapterBeanList.add(feedCheck);
        AdminItemAdapterBean addUser = new AdminItemAdapterBean(R.drawable.admin_user_add, "用户添加");
        itemAdapterBeanList.add(addUser);
        AdminItemAdapterBean loginout = new AdminItemAdapterBean(R.drawable.admin_logout, "退出登录");
        itemAdapterBeanList.add(loginout);
    }
}

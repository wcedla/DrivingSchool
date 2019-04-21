package com.wcedla.driving_school.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.orhanobut.hawk.Hawk;
import com.wcedla.driving_school.R;
import com.wcedla.driving_school.adapter.LawInfoShowAdapter;
import com.wcedla.driving_school.adapter.ScrollInfoAdapter;
import com.wcedla.driving_school.bean.LawInfoBean;
import com.wcedla.driving_school.bean.ScrollInfoBean;
import com.wcedla.driving_school.tool.ToolUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShowScrollInfoActivity extends AppCompatActivity {

    @BindView(R.id.scroll_info_head_text)
    TextView scrollInfoHeadText;
    @BindView(R.id.scroll_info_back)
    ImageView scrollInfoBack;
    @BindView(R.id.scroll_info_content)
    RecyclerView scrollInfoContent;

    String initType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolUtils.setNavigationBarStatusBarTranslucent(this, false, Color.parseColor("#000000"));
        setContentView(R.layout.activity_show_scroll_info);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            scrollInfoHeadText.setText(bundle.getString("headText"));
            initType=bundle.getString("initType");
        } else {
            scrollInfoHeadText.setText("信息展示");
            initType="";
        }
        initData();


    }

    @OnClick(R.id.scroll_info_back)
    public void backClick() {
        finish();
    }

    private void initData() {

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        if(initType.equals("skill")) {
            ScrollInfoBean scrollInfoBean = new ScrollInfoBean();
            String dataString = Hawk.get("skillData", "");
            try {
                scrollInfoBean = new Gson().fromJson(dataString, ScrollInfoBean.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ScrollInfoAdapter scrollInfoAdapter=new ScrollInfoAdapter(this,scrollInfoBean);

            scrollInfoContent.setLayoutManager(linearLayoutManager);
            scrollInfoContent.setAdapter(scrollInfoAdapter);
        }
        else if(initType.equals("law"))
        {
            LawInfoBean lawInfoBean=new LawInfoBean();
            String dataString =Hawk.get("lawData","");
            try {
                lawInfoBean = new Gson().fromJson(dataString, LawInfoBean.class);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            LawInfoShowAdapter lawInfoShowAdapter=new LawInfoShowAdapter(this,lawInfoBean);
            scrollInfoContent.setLayoutManager(linearLayoutManager);
            scrollInfoContent.setAdapter(lawInfoShowAdapter);
        }
    }
}

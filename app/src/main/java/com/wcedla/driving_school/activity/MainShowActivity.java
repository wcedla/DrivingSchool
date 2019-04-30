package com.wcedla.driving_school.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.hawk.Hawk;
import com.orhanobut.logger.Logger;
import com.wcedla.driving_school.R;
import com.wcedla.driving_school.adapter.ViewPagerFragmentAdapter;
import com.wcedla.driving_school.fragment.CoachFunctionalFragment;
import com.wcedla.driving_school.fragment.HomeFragment;
import com.wcedla.driving_school.fragment.MeFragment;
import com.wcedla.driving_school.fragment.StudentFunctionalFragment;
import com.wcedla.driving_school.tool.HttpUtils;
import com.wcedla.driving_school.tool.JsonUtils;
import com.wcedla.driving_school.tool.ToolUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.wcedla.driving_school.constant.Config.GET_USER_HEAD;

public class MainShowActivity extends AppCompatActivity {

    @BindView(R.id.main_view_pager)
    ViewPager mainViewPager;
    @BindView(R.id.main_tab)
    TabLayout mainTab;

    String loginUser;
    String loginType;
    String loginNo;

    int[] tabDrawableArray = new int[]{R.drawable.tab_home_selecter, R.drawable.tab_function_selecter, R.drawable.tab_me_selecter};
    String[] tabTextArray = new String[]{"首页", "功能", "我的"};

    List<Fragment> fragmentList = new ArrayList<>();
    @BindView(R.id.main_head_text)
    TextView mainHeadText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolUtils.setNavigationBarStatusBarTranslucent(this, false, Color.parseColor("#000000"));
        setContentView(R.layout.activity_main_show);
        ButterKnife.bind(this);
        loginUser= Hawk.get("loginUser","");
        loginType=Hawk.get("loginType","");
        loginNo = Hawk.get("loginNo", "");
        Logger.d("主界面获取登录状态:" + loginUser + "," + loginType + "," + loginNo);
        initView();
        getUserHeadImg();
    }

    private void initView() {
        for (int i = 0; i < 3; i++) {
            TabLayout.Tab tab = mainTab.newTab();
            View view = LayoutInflater.from(this).inflate(R.layout.main_show_tab_layout, null);
            ImageView tabImageview = view.findViewById(R.id.tab_img);
            TextView tabTextView = view.findViewById(R.id.tab_text);
            tabImageview.setImageResource(tabDrawableArray[i]);
            tabTextView.setText(tabTextArray[i]);
            if (i == 0) {
                tab.select();
            }
            tab.setCustomView(view);
            mainTab.addTab(tab);
            mainTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    mainViewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                    mainViewPager.setCurrentItem(tab.getPosition());
                }
            });
        }
        HomeFragment homeFragment = HomeFragment.getInstance(new Bundle());
        fragmentList.add(homeFragment);
        if ("教练".equals(loginType)) {
            CoachFunctionalFragment coachFunctionalFragment = CoachFunctionalFragment.getInstance(new Bundle());
            fragmentList.add(coachFunctionalFragment);
        } else if ("学员".equals(loginType)) {
            StudentFunctionalFragment studentFunctionalFragment = StudentFunctionalFragment.getInstance(new Bundle());
            fragmentList.add(studentFunctionalFragment);
        }

        MeFragment fragment = MeFragment.getInstance(new Bundle());
        fragmentList.add(fragment);
        ViewPagerFragmentAdapter viewPagerFragmentAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), fragmentList);
        mainViewPager.setAdapter(viewPagerFragmentAdapter);
        mainViewPager.setOffscreenPageLimit(1);
        mainViewPager.setCurrentItem(0);
        mainViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                TabLayout.Tab tab = mainTab.getTabAt(i);
                if (tab != null) {
                    tab.select();
                }
                if (i == 1) {
                    mainHeadText.setVisibility(View.VISIBLE);
                    mainHeadText.setText("功能");
                } else if (i == 2) {
                    mainHeadText.setText("");
                    mainHeadText.setVisibility(View.GONE);
                }
                else if(i==0)
                {
                    mainHeadText.setVisibility(View.VISIBLE);
                    mainHeadText.setText("首页");
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        // mainTab.setupWithViewPager(mainViewPager);
    }

    private void getUserHeadImg() {
        String url = HttpUtils.setParameterForUrl(GET_USER_HEAD, "no", loginNo);
        HttpUtils.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String headUrl = JsonUtils.getHeadImgStatus(response.body().string());
                Hawk.put("headImg", headUrl);
            }
        });
    }
}

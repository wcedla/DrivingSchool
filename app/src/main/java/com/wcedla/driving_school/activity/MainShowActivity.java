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

import com.wcedla.driving_school.R;
import com.wcedla.driving_school.adapter.ViewPagerFragmentAdapter;
import com.wcedla.driving_school.fragment.HomeFragment;
import com.wcedla.driving_school.tool.ToolUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainShowActivity extends AppCompatActivity {

    @BindView(R.id.main_view_pager)
    ViewPager mainViewPager;
    @BindView(R.id.main_tab)
    TabLayout mainTab;

    int[] tabDrawableArray= new int[]{R.drawable.tab_home_selecter,R.drawable.tab_function_selecter,R.drawable.tab_me_selecter};
    String[] tabTextArray=new String[]{"首页","功能","我的"};

    List<Fragment> fragmentList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolUtils.setNavigationBarStatusBarTranslucent(this, false, Color.parseColor("#000000"));
        setContentView(R.layout.activity_main_show);
        ButterKnife.bind(this);
        initView();
    }

    private void initView()
    {
        for (int i=0;i<3;i++)
        {
            TabLayout.Tab tab=mainTab.newTab();
            View view= LayoutInflater.from(this).inflate(R.layout.main_show_tab_layout,null);
            ImageView tabImageview=view.findViewById(R.id.tab_img);
            TextView tabTextView=view.findViewById(R.id.tab_text);
            tabImageview.setImageResource(tabDrawableArray[i]);
            tabTextView.setText(tabTextArray[i]);
            if(i==0)
            {
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

                }
            });

            HomeFragment homeFragment=HomeFragment.getInstance(new Bundle());
            fragmentList.add(homeFragment);
        }
        ViewPagerFragmentAdapter viewPagerFragmentAdapter=new ViewPagerFragmentAdapter(getSupportFragmentManager(),fragmentList);
        mainViewPager.setAdapter(viewPagerFragmentAdapter);
        mainViewPager.setOffscreenPageLimit(1);
        mainViewPager.setCurrentItem(0);
       // mainTab.setupWithViewPager(mainViewPager);
    }
}

package com.wcedla.driving_school.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wcedla.driving_school.R;
import com.wcedla.driving_school.adapter.BannerImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    Activity myActivity;
    Banner banner;

    public static HomeFragment getInstance(Bundle bundle)
    {
        HomeFragment homeFragment=new HomeFragment();
        homeFragment.setArguments(bundle);
        return homeFragment;
    }

    @Override
    public void onAttach(Context context) {
        myActivity = (Activity) context;
        if (getArguments() != null) {
//            number = getArguments().getInt("number");  //获取参数
        }
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home,container,false);
        banner=view.findViewById(R.id.home_banner);
        initBanner();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initBanner()
    {
        List<Integer> imageList=new ArrayList<>();
        List<String> titleList=new ArrayList<>();

        for(int i=0;i<5;i++)
        {
            imageList.add(R.drawable.bsj);
            titleList.add("保时捷"+i);
        }

        //实例化图片加载器
        BannerImageLoader bannerImageLoader = new BannerImageLoader();
        //绑定图片加载器
        banner.setImageLoader(bannerImageLoader);
        //设置切换时的动画效果
        banner.setBannerAnimation(Transformer.Default);
        //设置轮播的标题和指示器的位置
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //设置点击监听
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(final int position) {
//                Intent showSearchIntent = new Intent(myActivity, SearchActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("name", imageTitle.get(position));
//                showSearchIntent.putExtras(bundle);
//                startActivity(showSearchIntent);
            }
        });
        //设置轮播间隔时间
        banner.setDelayTime(4000);
        //设置是否为自动轮播，默认是true
        banner.isAutoPlay(true);
        //设置指示器的位置，小点点，居中显示
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //设置是否允许滑动切换
        banner.setViewPagerIsScroll(true);
        //设置轮播标题
        banner.setBannerTitles(titleList);
        //设置图片源
        banner.setImages(imageList);
        //Log.d(TAG, "空" + imagePath.size());
        //开始轮播
        banner.start();
    }
}


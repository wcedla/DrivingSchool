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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.orhanobut.hawk.Hawk;
import com.wcedla.driving_school.R;
import com.wcedla.driving_school.activity.MessageActivity;
import com.wcedla.driving_school.activity.ShowInfoActivity;
import com.wcedla.driving_school.activity.ShowScrollInfoActivity;
import com.wcedla.driving_school.adapter.BannerImageLoader;
import com.wcedla.driving_school.bean.BannerDataBean;
import com.wcedla.driving_school.bean.CoachRecommendedBean;
import com.wcedla.driving_school.bean.StudentRecommendBean;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.wcedla.driving_school.constant.Config.COMPANY_INTRODUCE;

public class HomeFragment extends Fragment {

    Activity myActivity;
    Banner banner;
    LinearLayout coachRoot;
    LinearLayout studentRoot;
    float denisty;

    public static HomeFragment getInstance(Bundle bundle) {
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setArguments(bundle);
        return homeFragment;
    }

    @Override
    public void onAttach(Context context) {
        myActivity = (Activity) context;
        denisty = context.getResources().getDisplayMetrics().density;
        if (getArguments() != null) {
//            number = getArguments().getInt("number");  //获取参数
        }
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        banner = view.findViewById(R.id.home_banner);
        ImageView company = view.findViewById(R.id.item_company);
        ImageView carSkill = view.findViewById(R.id.item_skill);
        ImageView carLaw = view.findViewById(R.id.item_law);
        ImageView message = view.findViewById(R.id.item_message);
        coachRoot = view.findViewById(R.id.coach_root);
        studentRoot = view.findViewById(R.id.student_root);
        initBanner();
        initRecommendedData();
        company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent infoIntent = new Intent(myActivity, ShowInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("headText", "公司简介");
                bundle.putString("contentText", COMPANY_INTRODUCE);
                infoIntent.putExtras(bundle);
                startActivity(infoIntent);
            }
        });
        carSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent scrollInfoIntent = new Intent(myActivity, ShowScrollInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("headText", "学车技巧");
                bundle.putString("initType", "skill");
                scrollInfoIntent.putExtras(bundle);
                startActivity(scrollInfoIntent);
            }
        });
        carLaw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent scrollInfoIntent = new Intent(myActivity, ShowScrollInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("headText", "驾考新规");
                bundle.putString("initType", "law");
                scrollInfoIntent.putExtras(bundle);
                startActivity(scrollInfoIntent);
            }
        });
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent messageIntent = new Intent(myActivity, MessageActivity.class);
                startActivity(messageIntent);
            }
        });


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initBanner() {
        List<String> imageList = new ArrayList<>();
        List<String> titleList = new ArrayList<>();
        String bannerData = Hawk.get("bannerData", "");
        if (bannerData.length() > 0) {
            BannerDataBean bannerDataBean = new Gson().fromJson(bannerData, BannerDataBean.class);
            for (BannerDataBean.BannerBean bannerBean : bannerDataBean.getBanner()) {
                imageList.add(bannerBean.getImg());
                titleList.add(bannerBean.getText());
            }
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

    private void initRecommendedData() {
        CoachRecommendedBean coachRecommendedBean = new CoachRecommendedBean();
        StudentRecommendBean studentRecommendBean = new StudentRecommendBean();
        String coachString = Hawk.get("coachData", "");
        String studentString = Hawk.get("studentData", "");
        try {
            coachRecommendedBean = new Gson().fromJson(coachString, CoachRecommendedBean.class);
            studentRecommendBean = new Gson().fromJson(studentString, StudentRecommendBean.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (coachRecommendedBean.getCoachAppraise() != null) {
            coachRoot.removeAllViews();
            for (int i = 0; i < 5; i++) {
                View view = LayoutInflater.from(myActivity).inflate(R.layout.coach_recommended_item, coachRoot, false);
                CircleImageView circleImageView = view.findViewById(R.id.head_img);
                TextView nameText = view.findViewById(R.id.coach_name_text);
                TextView driverYearText = view.findViewById(R.id.drive_year);
                TextView startText = view.findViewById(R.id.recommend_star);
                //View splitView=view.findViewById(R.id.coach_split);
                if (coachRecommendedBean.getCoachAppraise().get(i).getHeadImg() != null) {
                    Glide.with(myActivity).load(coachRecommendedBean.getCoachAppraise().get(i).getHeadImg()).apply(requestOptions).into(circleImageView);
                } else {
                    Glide.with(myActivity).load(R.drawable.bsj).apply(requestOptions).into(circleImageView);
                }

                nameText.setText("姓名:" + coachRecommendedBean.getCoachAppraise().get(i).getRealName());

                driverYearText.setText("驾龄:" + coachRecommendedBean.getCoachAppraise().get(i).getDriveAge());
                if (coachRecommendedBean.getCoachAppraise().get(i).getStar() != null) {
                    startText.setText("推荐指数:" + coachRecommendedBean.getCoachAppraise().get(i).getStar() + "星");
                } else {
                    startText.setText("推荐指数:暂无指数");
                }
                coachRoot.addView(view);
            }
        }
        if (studentRecommendBean.getStudentAppraise() != null) {
            studentRoot.removeAllViews();
            for (int i = 0; i < 5; i++) {
                View view = LayoutInflater.from(myActivity).inflate(R.layout.student_recommended_item, studentRoot, false);
                CircleImageView circleImageView = view.findViewById(R.id.student_head_img);
                TextView nameText = view.findViewById(R.id.student_name_text);
                TextView driverYearText = view.findViewById(R.id.student_drive_year);
                TextView startText = view.findViewById(R.id.student_recommend_star);
                View splitView = view.findViewById(R.id.student_split);
                if (studentRecommendBean.getStudentAppraise().get(i).getHeadImg() != null) {
                    Glide.with(myActivity).load(studentRecommendBean.getStudentAppraise().get(i).getHeadImg()).apply(requestOptions).into(circleImageView);
                } else {
                    Glide.with(myActivity).load(R.drawable.bsj).apply(requestOptions).into(circleImageView);
                }
                nameText.setText("姓名:" + studentRecommendBean.getStudentAppraise().get(i).getRealName());
                if (studentRecommendBean.getStudentAppraise().get(i).getTime() != null) {
                    driverYearText.setText("学车时长:" + studentRecommendBean.getStudentAppraise().get(i).getTime() + "小时");
                } else {
                    driverYearText.setText("学车时长:暂未学习");
                }
                if (studentRecommendBean.getStudentAppraise().get(i).getStar() != null) {
                    startText.setText("教练评价:" + studentRecommendBean.getStudentAppraise().get(i).getStar() + "星");
                } else {
                    startText.setText("教练评价:暂未评价");
                }
                if (i == 4) {
                    splitView.setVisibility(View.GONE);
                }
                studentRoot.addView(view);
            }
        }
    }

    RequestOptions requestOptions = new RequestOptions()
            .override((int) (denisty * 60), (int) (denisty * 60))
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .dontAnimate();
}


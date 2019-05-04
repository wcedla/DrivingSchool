package com.wcedla.driving_school.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wcedla.driving_school.R;
import com.wcedla.driving_school.adapter.StudentGridAdapter;
import com.wcedla.driving_school.bean.FunctionItemBean;
import com.wcedla.driving_school.customview.SplitLineGridview;

import java.util.ArrayList;
import java.util.List;

public class StudentFunctionalFragment extends Fragment {

    Activity myActivity;
    float denisty;

    public static StudentFunctionalFragment getInstance(Bundle bundle)
    {
        StudentFunctionalFragment studentFunctionalFragment=new StudentFunctionalFragment();
        studentFunctionalFragment.setArguments(bundle);
        return studentFunctionalFragment;
    }

    @Override
    public void onAttach(Context context) {
        myActivity = (Activity) context;
        denisty=context.getResources().getDisplayMetrics().density;
        if (getArguments() != null) {
//            number = getArguments().getInt("number");  //获取参数
        }
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_functional, container, false);
        SplitLineGridview splitLineGridview = view.findViewById(R.id.function_item_grid);
        List<FunctionItemBean> functionItemBeanList = new ArrayList<>();
        functionItemBeanList.add(new FunctionItemBean(R.drawable.my_coach, "我的教练"));
        functionItemBeanList.add(new FunctionItemBean(R.drawable.my_study, "我的学习"));
        functionItemBeanList.add(new FunctionItemBean(R.drawable.my_exam, "我的考试"));
        functionItemBeanList.add(new FunctionItemBean(R.drawable.functional_feed, "我的报错"));
        StudentGridAdapter studentGridAdapter = new StudentGridAdapter(myActivity, functionItemBeanList);
        splitLineGridview.setAdapter(studentGridAdapter);
        return view;
    }
}

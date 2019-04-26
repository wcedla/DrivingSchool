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
import com.wcedla.driving_school.adapter.FunctionItemAdapter;
import com.wcedla.driving_school.bean.FunctionItemBean;
import com.wcedla.driving_school.customview.SplitLineGridview;

import java.util.ArrayList;
import java.util.List;

public class CoachFunctionalFragment extends Fragment {

    Activity myActivity;
    float denisty;

    public static CoachFunctionalFragment getInstance(Bundle bundle)
    {
        CoachFunctionalFragment functionalFragment=new CoachFunctionalFragment();
        functionalFragment.setArguments(bundle);
        return functionalFragment;
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
        View view=inflater.inflate(R.layout.coach_functional_fragment,container,false);

        SplitLineGridview splitLineGridview = view.findViewById(R.id.function_item_grid);
        List<FunctionItemBean> functionItemBeanList = new ArrayList<>();
        functionItemBeanList.add(new FunctionItemBean(R.drawable.function_user_search, "学员查找"));
        functionItemBeanList.add(new FunctionItemBean(R.drawable.function_item_get_progress, "学习进度"));
        functionItemBeanList.add(new FunctionItemBean(R.drawable.function_item_exam_progress, "考试进度"));
        FunctionItemAdapter functionItemAdapter = new FunctionItemAdapter(myActivity, functionItemBeanList);
        splitLineGridview.setAdapter(functionItemAdapter);
        return view;
    }


}

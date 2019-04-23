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
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}

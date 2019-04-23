package com.wcedla.driving_school.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wcedla.driving_school.R;
import com.wcedla.driving_school.activity.UserSearchActivity;

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
        TextView userManger=view.findViewById(R.id.user_manager);
        TextView studyManger=view.findViewById(R.id.study_manager);
        TextView examManger=view.findViewById(R.id.exam_manager);
        LinearLayout itemRoot=view.findViewById(R.id.function_root);
        userManger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userManger.setBackgroundColor(Color.parseColor("#ffffff"));
                studyManger.setBackgroundColor(Color.parseColor("#eeeeee"));
                examManger.setBackgroundColor(Color.parseColor("#eeeeee"));
                itemRoot.removeAllViews();
                View functionItem=LayoutInflater.from(myActivity).inflate(R.layout.user_function_item,itemRoot,false);
                ConstraintLayout userSearch=functionItem.findViewById(R.id.item_search_root);
                ConstraintLayout userAdd=functionItem.findViewById(R.id.item_add_root);
                ConstraintLayout userEdit=functionItem.findViewById(R.id.item_edit_root);
                ConstraintLayout userDelete=functionItem.findViewById(R.id.item_delete_root);
                userSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent userSearchIntent=new Intent(myActivity, UserSearchActivity.class);
                        Bundle bundle=new Bundle();
                        userSearchIntent.putExtras(bundle);
                        startActivity(userSearchIntent);
                    }
                });
                itemRoot.addView(functionItem);
            }
        });
        studyManger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userManger.setBackgroundColor(Color.parseColor("#eeeeee"));
                studyManger.setBackgroundColor(Color.parseColor("#ffffff"));
                examManger.setBackgroundColor(Color.parseColor("#eeeeee"));
                itemRoot.removeAllViews();
                View functionItem=LayoutInflater.from(myActivity).inflate(R.layout.study_function_item,itemRoot,false);
                itemRoot.addView(functionItem);
            }
        });
        examManger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userManger.setBackgroundColor(Color.parseColor("#eeeeee"));
                studyManger.setBackgroundColor(Color.parseColor("#eeeeee"));
                examManger.setBackgroundColor(Color.parseColor("#ffffff"));
                itemRoot.removeAllViews();
                View functionItem=LayoutInflater.from(myActivity).inflate(R.layout.exam_function_item,itemRoot,false);
                itemRoot.addView(functionItem);
            }
        });
        userManger.performClick();
        return view;
    }


}

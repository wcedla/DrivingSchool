package com.wcedla.driving_school.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.method.ScrollingMovementMethod;
import android.text.style.LeadingMarginSpan;
import android.widget.ImageView;
import android.widget.TextView;

import com.wcedla.driving_school.R;
import com.wcedla.driving_school.tool.ToolUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.wcedla.driving_school.constant.Config.COMPANY_INTRODUCE;

public class ShowInfoActivity extends AppCompatActivity {

    @BindView(R.id.info_head_text)
    TextView infoHeadText;
    @BindView(R.id.info_back)
    ImageView infoBack;
    @BindView(R.id.info_text)
    TextView infoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolUtils.setNavigationBarStatusBarTranslucent(this, false, Color.parseColor("#000000"));
        setContentView(R.layout.activity_show_info);
        ButterKnife.bind(this);
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
        {
            infoHeadText.setText(bundle.getString("headText"));
            infoText.setText(bundle.getString("contentText"));
            infoText.setMovementMethod(ScrollingMovementMethod.getInstance());
        }
        else
        {
            infoHeadText.setText("信息展示");
            infoText.setText(bundle.getString("暂无内容"));
        }
    }

    @OnClick(R.id.info_back)
    public void backClick()
    {
        finish();
    }
}

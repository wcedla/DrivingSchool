package com.wcedla.driving_school.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.wcedla.driving_school.R;
import com.wcedla.driving_school.tool.PackageUtils;
import com.wcedla.driving_school.tool.ToolUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutActivity extends AppCompatActivity {

    @BindView(R.id.about_version)
    TextView aboutVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolUtils.setNavigationBarStatusBarTranslucent(this, false, Color.parseColor("#000000"));
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        aboutVersion.setText("V " + PackageUtils.getVersionName(this));

    }

    @OnClick(R.id.about_back)
    public void backClick() {
        finish();
    }
}

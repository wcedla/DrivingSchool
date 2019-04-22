package com.wcedla.driving_school.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;
import com.orhanobut.logger.Logger;
import com.wcedla.driving_school.R;
import com.wcedla.driving_school.bean.AuthCheckBean;
import com.wcedla.driving_school.tool.HttpUtils;
import com.wcedla.driving_school.tool.JsonUtils;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.wcedla.driving_school.constant.Config.AUTHENTICSTION_CHECK_URL;
import static com.wcedla.driving_school.constant.Config.GET_BANNER_URL;
import static com.wcedla.driving_school.constant.Config.GET_COACH_RECOMMENDED;
import static com.wcedla.driving_school.constant.Config.GET_LAW_URL;
import static com.wcedla.driving_school.constant.Config.GET_MESSAGE_URL;
import static com.wcedla.driving_school.constant.Config.GET_SKILL_URL;
import static com.wcedla.driving_school.constant.Config.GET_STUDENT_RECOMMENDED;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.time_text)
    TextView timeText;

    Timer timer;
    TimerTask timerTask;
    int time;
    String loginUser;
    boolean gotAuth;
    boolean gotBanner;
    boolean gotSkill;
    boolean gotLaw;
    boolean gotMessage;
    boolean gotCoach;
    boolean gotStudent;
    AuthCheckBean authCheckBean = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        hideBottomUIMenu();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        gotAuth = false;
        gotBanner = false;
        gotSkill=false;
        gotLaw=false;
        gotMessage=false;
        gotCoach=false;
        gotStudent=false;
        time = 5;
        timer = new Timer();
        //Hawk.put("loginUser","wcedla");
        loginUser = Hawk.get("loginUser", "");
        getBannerData();
        getSkillData();
        getLawData();
        getMessageData();
        getCoachRecommended();
        getStudentRecommended();

        if (loginUser.length() > 2 && !"admin".equals(loginUser)) {
            checkAuthStatus();

        }
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        time -= 1;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                timeText.setText("跳过 " + time + "s");
                                if (time == 0) {
                                    timer.cancel();
                                    resultOption();
                                }
                            }
                        });
                    }
                });
            }
        };
        timer.schedule(timerTask, 1000, 1000);
        timeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                resultOption();
            }
        });
    }

    private void resultOption() {
        if (loginUser.length() < 3) {
            Intent startIntent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(startIntent);
            finish();
        } else if ("admin".equals(loginUser)) {
            Intent demo = new Intent(SplashActivity.this, AdminMainActivity.class);
            startActivity(demo);
            finish();
        } else {
            if (authCheckBean != null && gotBanner&&gotSkill&&gotLaw&&gotMessage&&gotCoach&&gotStudent) {
                if (authCheckBean.getStatus().equals("审核通过")) {
                    Intent mainShowIntent = new Intent(SplashActivity.this, MainShowActivity.class);
                    startActivity(mainShowIntent);
                    finish();
                } else if(authCheckBean.getStatus().equals("审核未通过")||authCheckBean.getStatus().equals("审核中")){
                    Intent noPassAuthIntent = new Intent(SplashActivity.this, AuthenticationStatusActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("userName", loginUser);
                    noPassAuthIntent.putExtras(bundle);
                    startActivity(noPassAuthIntent);
                    finish();
                }
                else
                {
                    Hawk.delete("loginUser");
                    Intent loginIntent=new Intent(this,LoginActivity.class);
                    startActivity(loginIntent);
                }

            } else if (gotAuth && gotBanner&&gotSkill&&gotLaw&&gotMessage&&gotCoach&&gotStudent) {
                finish();
                Toast.makeText(SplashActivity.this, "出现错误", Toast.LENGTH_SHORT).show();
            } else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (gotAuth && gotBanner&&gotSkill&&gotLaw&&gotMessage&&gotCoach&&gotStudent) {
                            if (authCheckBean.getStatus().equals("审核通过")) {
                                Intent mainShowIntent = new Intent(SplashActivity.this, MainShowActivity.class);
                                startActivity(mainShowIntent);
                                finish();
                            } else if(authCheckBean.getStatus().equals("审核未通过")||authCheckBean.getStatus().equals("审核中")){
                                Intent noPassAuthIntent = new Intent(SplashActivity.this, AuthenticationStatusActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("userName", loginUser);
                                noPassAuthIntent.putExtras(bundle);
                                startActivity(noPassAuthIntent);
                                finish();
                            }
                            else
                            {
                                Hawk.delete("loginUser");
                                Intent loginIntent=new Intent(SplashActivity.this,LoginActivity.class);
                                startActivity(loginIntent);
                            }
                            break;
                        }
                    }
                }).start();

            }
        }
    }

    private void checkAuthStatus() {
        String url = HttpUtils.setParameterForUrl(AUTHENTICSTION_CHECK_URL, "userName", loginUser);
        HttpUtils.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!SplashActivity.this.isFinishing()) {
                            finish();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(SplashActivity.this, "出现错误", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseString = response.body().string();
                authCheckBean = JsonUtils.getAuthCheckStatus(responseString);
                gotAuth = true;
            }
        });
    }

    private void getBannerData() {
        HttpUtils.doHttpRequest(GET_BANNER_URL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (!SplashActivity.this.isFinishing()) {
                    finish();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SplashActivity.this, "出现错误", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Hawk.put("bannerData", response.body().string());
                gotBanner = true;
            }
        });
    }

    private void getSkillData()
    {
        HttpUtils.doHttpRequest(GET_SKILL_URL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (!SplashActivity.this.isFinishing()) {
                    finish();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SplashActivity.this, "出现错误", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Hawk.put("skillData", response.body().string());
                gotSkill=true;
            }
        });
    }

    private void getLawData()
    {
        HttpUtils.doHttpRequest(GET_LAW_URL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (!SplashActivity.this.isFinishing()) {
                    finish();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SplashActivity.this, "出现错误", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Hawk.put("lawData",response.body().string());
                gotLaw=true;
            }
        });
    }

    private void getMessageData()
    {
        HttpUtils.doHttpRequest(GET_MESSAGE_URL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (!SplashActivity.this.isFinishing()) {
                    finish();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SplashActivity.this, "出现错误", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Hawk.put("messageData",response.body().string());
                gotMessage=true;
            }
        });
    }

    private void getCoachRecommended()
    {
        HttpUtils.doHttpRequest(GET_COACH_RECOMMENDED, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (!SplashActivity.this.isFinishing()) {
                    finish();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SplashActivity.this, "出现错误", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Hawk.put("coachData",response.body().string());
                gotCoach=true;
            }
        });
    }

    private void getStudentRecommended()
    {
        HttpUtils.doHttpRequest(GET_STUDENT_RECOMMENDED, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (!SplashActivity.this.isFinishing()) {
                    finish();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SplashActivity.this, "出现错误", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Hawk.put("studentData",response.body().string());
                gotStudent=true;
            }
        });
    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }
}

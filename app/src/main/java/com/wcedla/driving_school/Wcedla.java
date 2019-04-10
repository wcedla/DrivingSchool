package com.wcedla.driving_school;

import android.app.Application;

import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.HawkFacade;
import com.orhanobut.hawk.NoEncryption;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.wcedla.driving_school.tool.MyEncryption;

import static com.wcedla.driving_school.constant.Config.DEBUG;
import static com.wcedla.driving_school.constant.Config.LOG_TAG;

public class Wcedla extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化Logger
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(true)      //（可选）是否显示线程信息。 默认值为true
                .methodCount(2)               // （可选）要显示的方法行数。 默认2
                .tag(LOG_TAG)                  //（可选）每个日志的全局标记。 默认PRETTY_LOGGER（如上图）
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return DEBUG;
            }
        });

        Hawk.init(this).build();
    }


}

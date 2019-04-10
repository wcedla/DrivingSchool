package com.wcedla.driving_school.tool;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.view.View;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.BuildConfig;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.wcedla.driving_school.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class ToolUtils {

    /**
     * 导航栏，状态栏透明
     *
     * @param activity
     */
    public static void setNavigationBarStatusBarTranslucent(Activity activity, boolean darkStatusText, boolean transparentNavigationBar) {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = activity.getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            if (darkStatusText)
                option |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            decorView.setSystemUiVisibility(option);
            if (transparentNavigationBar) {
                activity.getWindow().setNavigationBarColor(Color.TRANSPARENT);
            } else {
                activity.getWindow().setNavigationBarColor(activity.getResources().getColor(R.color.custom_primary, null));
            }
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public static void setNavigationBarStatusBarTranslucent(Activity activity, boolean darkStatusText, @ColorInt int navigationColor) {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = activity.getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            if (darkStatusText)
                option |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            decorView.setSystemUiVisibility(option);

            activity.getWindow().setNavigationBarColor(navigationColor);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * 使用MD5加密字符串
     *
     * @param stringForEncrypted 需要加密的字符串
     * @return 加密后的字符串
     */
    public static String MD5Encrypted(String stringForEncrypted) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] result = messageDigest.digest(stringForEncrypted.getBytes()); // 得到加密后的字符组数
            StringBuilder sb = new StringBuilder();
            for (byte b : result) {
                int num = b & 0xff; // 这里的是为了将原本是byte型的数向上提升为int型，从而使得原本的负数转为了正数
                String hex = Integer.toHexString(num); //这里将int型的数直接转换成16进制表示
                //16进制可能是为1的长度，这种情况下，需要在前面补0，
                if (hex.length() == 1) {
                    sb.append(0);
                }
                sb.append(hex);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getEmailCode()
    {
        Random random=new Random(System.currentTimeMillis());
        StringBuilder stringBuilder=new StringBuilder();
        for(int i=0;i<6;i++)
        {

            stringBuilder.append(random.nextInt(10));
        }
        if(stringBuilder.length()==6) {
            return stringBuilder.toString();
        }
        else
        {
            return String.valueOf(System.currentTimeMillis()).substring(0,6);
        }
    }

    /**
     * 初始化日志方法
     *
     * @param tag       日志输出标记
     * @param logStatus 日志输出开关
     */
    public static void initLogger(String tag, final boolean logStatus) {


    }

}

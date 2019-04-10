package com.wcedla.driving_school.tool;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SharePreferenceUtils {

    public static void setData(Context context, String name,String key,String value)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(name, MODE_PRIVATE).edit();
        editor.putString(key,value);
        editor.apply();
    }

    public static void setData(Context context, String name,String key,Boolean value)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(name, MODE_PRIVATE).edit();
        editor.putBoolean(key,value);
        editor.apply();
    }

    public static void setData(Context context, String name,String key,Integer value)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(name, MODE_PRIVATE).edit();
        editor.putInt(key,value);
        editor.apply();
    }

    public static void setData(Context context, String name,String key,Float value)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(name, MODE_PRIVATE).edit();
        editor.putFloat(key,value);
        editor.apply();
    }

    public static void setData(Context context, String name,String key,Long value)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(name, MODE_PRIVATE).edit();
        editor.putLong(key,value);
        editor.apply();
    }

    public static String getData(Context context, String name,String key,String defaultValue)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(name, MODE_PRIVATE);
        return sharedPreferences.getString(key, defaultValue);
    }

    public static Boolean getData(Context context, String name,String key,Boolean defaultValue)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(name, MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public static Integer getData(Context context, String name,String key,Integer defaultValue)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(name, MODE_PRIVATE);
        return sharedPreferences.getInt(key, defaultValue);
    }

    public static Float getData(Context context, String name,String key,Float defaultValue)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(name, MODE_PRIVATE);
        return sharedPreferences.getFloat(key, defaultValue);
    }

    public static Long getData(Context context, String name,String key,Long defaultValue)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(name, MODE_PRIVATE);
        return sharedPreferences.getLong(key, defaultValue);
    }
}

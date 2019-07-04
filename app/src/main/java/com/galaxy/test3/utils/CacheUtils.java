package com.galaxy.test3.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;

import com.galaxy.test3.SplashActivity;
import com.galaxy.test3.activity.GuideActivity;

/*
* 缓存软件的一些参数
* */
public class CacheUtils {
    public static Boolean getBoolean(Context context, String key) {
        SharedPreferences sp =  context.getSharedPreferences("mysp",Context.MODE_PRIVATE);
        //根据key取值，不适用查不到时的默认值！
        LogUtil.e("Cache 这里由问题吗？");
        return sp.getBoolean(key,false);
    }

    public static void putBoolean(Context context, String key, boolean value) {
        //使用sharePreference需要得知调用者时谁？
        SharedPreferences sp = context.getSharedPreferences("mysp",context.MODE_PRIVATE);
        sp.edit().putBoolean(key,value).commit();
    }

    /**
     * 缓存文本数据
     * @param context 上下文
     * @param key   网站url
     * @param value 缓存数据
     */
    public static void putString(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences("mysp",context.MODE_PRIVATE);
        sp.edit().putString(key,value).commit();
    }

    /**
     * 获取缓存的文本信息
     * @param context
     * @param key
     * @return
     */
    public static String getString(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("mysp",Context.MODE_PRIVATE);
        //注意，这里使用null和空字符串是完全不一样的，前者会导致崩溃！
        return sp.getString(key,"");
    }
}

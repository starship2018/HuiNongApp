package com.galaxy.test3;

import android.app.Application;

import org.xutils.x;

public class HuiNongApplication extends Application {
    /**
     * 所有的组件被创建前执行
     */
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
    }
}

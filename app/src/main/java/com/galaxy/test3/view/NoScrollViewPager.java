package com.galaxy.test3.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NoScrollViewPager extends ViewPager {
    /**
     * 通常在代码中实例化的时候使用该方法
     * @param context
     */
    public NoScrollViewPager(@NonNull Context context) {
        super(context);
    }

    /**
     * 重写触摸事件，消费掉
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return true;
    }

    /**
     * 在布局文件中使用该类的时候，实例化该类用该构造方法，这个方法吗不能少，否则会崩溃！
     * @param context
     * @param attrs
     */
    public NoScrollViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
}

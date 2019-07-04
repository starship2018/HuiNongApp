package com.galaxy.test3.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class HorizontalScrollViewPager extends ViewPager {
    /**
     * 起始坐标！
     */
    private float startX;
    private float startY;


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                //请求父层视图不拦截当前控件的事件
                getParent().requestDisallowInterceptTouchEvent(true);
                //1.记录起始坐标
                startX =ev.getX();
                startY=ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //2.来到新的坐标
                float endX = ev.getX();
                float endY = ev.getY();
                //3.计算偏移量
                float distanceX = endX - startX;
                float distanceY = endY - startY;
                //4.判断滑动方向
                if (Math.abs(distanceX) > Math.abs(distanceY)){
                    //水平方向滑动
                   if (getCurrentItem()==0 && distanceX>0){
                       getParent().requestDisallowInterceptTouchEvent(false);
                   }else  if(getCurrentItem() == (getAdapter().getCount()-1) && distanceX<0){
                       getParent().requestDisallowInterceptTouchEvent(false);
                   }else {
                       getParent().requestDisallowInterceptTouchEvent(true);
                   }
                }else {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;default:
                    break;
        }
        return super.dispatchTouchEvent(ev);
    }

    public HorizontalScrollViewPager(@NonNull Context context) {
        super(context);
    }

    public HorizontalScrollViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
}

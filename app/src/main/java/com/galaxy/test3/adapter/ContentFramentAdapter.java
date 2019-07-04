package com.galaxy.test3.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.galaxy.test3.base.BasePager;

import java.util.ArrayList;

public class ContentFramentAdapter extends PagerAdapter {
    private final ArrayList<BasePager> basepagers;

    public ContentFramentAdapter(ArrayList<BasePager> basePagers){
        this.basepagers = basePagers;
    }
    @Override
    public int getCount() {
        return basepagers.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        BasePager basePager = basepagers.get(position);//各个页面的实例
        View rootview =  basePager.rootView;//各个子页面
        //调用各个页面的子页面，
//            basePager.initData();
        container.addView(rootview);
        return rootview;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view==o;
    }
}

package com.galaxy.test3.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 基本的fragment，leftfragment和contentfragment继承于它
 */
public abstract class BaseFragment extends Fragment {

     public Activity context;

    /**
     * 当fragment被创建时回调这个方法
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    /**
     * 当视图被创建时回调
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initView();
    }
    //让子类实现自己的图，达到自己特有的效果
    public abstract View initView();




    /**
     * 当activity被创建后回调
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }
    /*
    1.如果界面没有数据
     */
    public void initData() {

    }


}

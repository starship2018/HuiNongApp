package com.galaxy.test3.base;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.galaxy.test3.R;
import com.galaxy.test3.activity.MainActivity;

/**
 *
 */
public class BasePager {

    /**
     * 上下文（记载出生地）
     * 在子类创建出新的View控件时，必须传入的参数就是上下文context！
     */
    public final Context context;  //MainActivity


    //写出各个View中的各个控件，方便通过其子类来操作这些控件！
    /**
     * 视图，代表各个不同的界面
     */
    public View rootView;
    /**
     * 显示标题
     */
    public TextView tv_title;
    /**
     * 点击侧滑
     */
    public ImageButton ib_menu;
    /**
     * 加载子页面
     */
    public FrameLayout fl_content;

    public ImageButton ib_switch_list_grid;

    private View initView(){
        //基类的页面
        View view = View.inflate(context, R.layout.base_pager,null);
        tv_title= (TextView) view.findViewById(R.id.tv_title);
        ib_menu = (ImageButton) view.findViewById(R.id.ib_menu);
        //设置隐藏
        ib_menu.setVisibility(View.GONE);
        ib_switch_list_grid = (ImageButton) view.findViewById(R.id.ib_switch_list_grid);
        /**
         * 给三横的菜单按钮添加点击事件！
         * 清除菜单键的功能!
         */
//        ib_menu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MainActivity mainActivity = (MainActivity) context;
//                mainActivity.getSlidingMenu().toggle();
//            }
//        });
        fl_content = (FrameLayout) view.findViewById(R.id.fl_content);
        return view;
    }

    public BasePager(Context context){
        this.context = context;
        //构造方法一执行，视图就被初始化了
        rootView = initView();
    }

    /**
     * 初始化数据，当孩子需要初始化数据时或者时绑定数据，联网请求数据并绑定的时候，重写该方法
     */
    public void initData(){

    }
}

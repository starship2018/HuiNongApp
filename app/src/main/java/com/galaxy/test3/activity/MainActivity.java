package com.galaxy.test3.activity;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;


import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.galaxy.test3.R;
import com.galaxy.test3.base.BaseFragment;
import com.galaxy.test3.fragment.ContentFragment;
import com.galaxy.test3.fragment.LeftMenuFragment;
import com.galaxy.test3.utils.DensityUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity {

    public static final String MAIN_PAGE_TAG = "main_page_tag";
    public static final String LEFT_MENU = "left_menu";




    /**
     * 对fragment进行初始化，毕竟fragment是一个中间件！在复杂页面使用fragment进行布局时
     * 要对fragment单独新建一个类
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //消除虚拟按键对于布局的影响
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        //清除标题栏！
        requestWindowFeature(Window.FEATURE_NO_TITLE);


        /**
         * 设置页面的大致布局，包括左右滑动页面以及滑动模式和滑动页所占的比例
         */
        initSlidingMenu(savedInstanceState);
        /**
         * 将初始的布局文件xml替换为Fragment.java 文件，在Java文件进行更为详细的布局和逻辑设置！
         */
        //初始化fragment
        initFragment();

    }

    private void initSlidingMenu(Bundle savedInstanceState) {


        //1.设置主页面
        setContentView(R.layout.activity_main);
        //2.设置左侧菜单
        setBehindContentView(R.layout.activity_leftmenu);
//        3.设置右侧菜单 不能new
        SlidingMenu slidingMenu = getSlidingMenu();

        //4.设置显示模式：左侧菜单+主页 ，左侧菜单+主页+ 右侧菜单 ， 主页面 + 右侧菜单

//        slidingMenu.setSecondaryMenu(R.layout.activity_rightmenu);

        slidingMenu.setMode(slidingMenu.LEFT);
//        slidingMenu.setVisibility(View.GONE);
        //5.设置滑动模式：滑动边缘，全屏滑动，不可滑动
        slidingMenu.setTouchModeAbove(slidingMenu.TOUCHMODE_NONE);
        //6.设置主页占据的宽度
        slidingMenu.setBehindOffset(DensityUtil.dip2px(MainActivity.this,200));
    }

    private void initFragment() {
        //1.得到FragmentManager
        FragmentManager fm = getSupportFragmentManager();
        //2.开启事务
        FragmentTransaction ft = fm.beginTransaction();
        //3.替换
        ft.replace(R.id.main_page, new ContentFragment(), MAIN_PAGE_TAG);
        ft.replace(R.id.left_menu, new LeftMenuFragment(), LEFT_MENU);
        //4.提交
        ft.commit();
    }

    /**
     * 得到左侧菜单
     * @return
     */
    public LeftMenuFragment getLeftMenuFragment() {
        FragmentManager fm = getSupportFragmentManager();
        return (LeftMenuFragment) fm.findFragmentByTag(LEFT_MENU);
    }

    /**
     * 得到正文的Fragment!
     * @return
     */
    public ContentFragment getContentFragment() {
        return (ContentFragment) getSupportFragmentManager().findFragmentByTag(MAIN_PAGE_TAG);
    }

    /**
     * 双击退出
     */
    private long exitTime = 0;
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                 //弹出提示，可以有多种方式
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

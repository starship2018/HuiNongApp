package com.galaxy.test3.fragment;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.galaxy.test3.R;
import com.galaxy.test3.activity.MainActivity;
import com.galaxy.test3.adapter.ContentFramentAdapter;
import com.galaxy.test3.base.BaseFragment;
import com.galaxy.test3.base.BasePager;
import com.galaxy.test3.pager.DataPager;
import com.galaxy.test3.pager.NewsPager;
import com.galaxy.test3.pager.SmartServicePagaer;
import com.galaxy.test3.pager.UserPager;
import com.galaxy.test3.view.NoScrollViewPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * 控制页面的主要显示内容（指定加载的VIew）和数据的加载
 */
public class ContentFragment extends BaseFragment {


    /**
     * fragment只是一个框架，若是它想操作布局xml中的各种元素时，必须先实例化View，通过这个View.findViewById()来找到其中的
     * 各个元素，例如按钮，显示框等等，然后给这些子元素绑定各种的点击事件或者是监视器！
     */


    //3.初始化控件
    @ViewInject(R.id.viewpager)
    private NoScrollViewPager  viewPager;
    @ViewInject(R.id.rg_main)

    private RadioGroup rg_group;


    private ArrayList<BasePager> basepagers;
    //抽象类方法，一定要重写
    @Override
    public View initView() {
        //1.从content_fragment.xml源文件实例化泛化为一个View，返回之可以将其可视化！
        View view = View.inflate(context, R.layout.content_fragment, null);
//        viewPager =  (ViewPager) view.findViewById(R.id.viewpager);
//        rg_group = (RadioGroup) view.findViewById(R.id.rg_main);
        //2.把视图注入到框架中去，让ContentFragment.this和view关联起来
        x.view().inject(ContentFragment.this,view);

        return view;

    }
    //可以实现，也可以不实现
    @Override
    public void initData() {
        super.initData();
        //初始化五个页面，并且放入集合中去
        basepagers = new ArrayList<>();
        basepagers.add(new DataPager(context));//数据页
        basepagers.add(new NewsPager(context));//新闻页
        basepagers.add(new SmartServicePagaer(context));//惠农页
        basepagers.add(new UserPager(context));//个人中心页面

        //设置默认选中首页
        rg_group.check(R.id.rb_data);


        //设置View pager的适配器
        viewPager.setAdapter(new ContentFramentAdapter());
        //设置radiogroup选中状态下的监听
        rg_group.setOnCheckedChangeListener(new MyCheckedChangeListener());
        //监听某个页面被选中，初始对应的页面数据
        viewPager.addOnPageChangeListener(new MyOnPageChangeLsitener());
        //默认情况下选择第一页初始化数据，进入应用的时候就开始初始化数据
        basepagers.get(0).initData();

    }

    public NewsPager getNewsCenterPager() {
        return (NewsPager) basepagers.get(1);
    }

    class MyOnPageChangeLsitener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        /**
         * 当页面被选中时回调位置信息
         * @param i
         */
        @Override
        public void onPageSelected(int i) {
            //谁被选中就开始初始化
            basepagers.get(i).initData();
        }

        /**
         * 当页面滚动状态发生改变时返回的位置信息
         * @param i
         */
        @Override
        public void onPageScrollStateChanged(int i) {

        }
    }


    /**
     * 新加入的模块在这里进行适配页面id和数据
     */
    class MyCheckedChangeListener implements RadioGroup.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId){
                case R.id.rb_data:
                    viewPager.setCurrentItem(0,false);
                    //这里允许滑动
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;
                case R.id.rb_home:
                    viewPager.setCurrentItem(1,false);
                    //这里允许滑动
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;

                case R.id.rb_calculate:
                    viewPager.setCurrentItem(2,false);
                    //这里不允许滑动
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;
                case R.id.rb_usercenter:
                    viewPager.setCurrentItem(3,false);
                    break;
            }
        }

        /**
         * 根据传入的参数设置是否可以滑动！
         * @param touchmodeFullscreen
         */
        private void isEnableSlidingMenu(int touchmodeFullscreen) {
            MainActivity mainActivity = (MainActivity) context;
            mainActivity.getSlidingMenu().setTouchModeAbove(touchmodeFullscreen);
        }
    }

    /**
     *
     */
    class ContentFramentAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return basepagers.size();
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            BasePager basePager = basepagers.get(position);//各个页面的实例
            View rootview = basePager.rootView;//各个子页面
            //调用各个页面的子页面，
            basePager.initData();
            container.addView(rootview);
            return rootview;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }
    }
}

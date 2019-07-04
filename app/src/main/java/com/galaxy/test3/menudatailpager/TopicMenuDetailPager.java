package com.galaxy.test3.menudatailpager;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.galaxy.test3.R;
import com.galaxy.test3.activity.MainActivity;
import com.galaxy.test3.base.MenuDetailBasePager;
import com.galaxy.test3.domain.NewsCenterPagerBean;
import com.galaxy.test3.menudatailpager.tabledetailpager.TableDetailPager;
import com.galaxy.test3.menudatailpager.tabledetailpager.TopicDetailPager;
import com.galaxy.test3.utils.LogUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class TopicMenuDetailPager extends MenuDetailBasePager {
    //实例化过程
    @ViewInject(R.id.tabIndicator)
    private TabPageIndicator tabPageIndicator;

    //这个叫viewpager的id被使用过很多次了！警告！
    @ViewInject(R.id.viewpager)
    private ViewPager viewPager;

    @ViewInject(R.id.ib_tab_next)
    private ImageButton ib_tab_next;


    /**
     * 页签页面数据的空集合！
     */
    private List<NewsCenterPagerBean.DataBean.ChildrenBean> children;
    /**
     * 页签页面的集合！或者说是容器的空集合
     */
    private ArrayList<TopicDetailPager> tableDetailPagers;

    public TopicMenuDetailPager(Context context,NewsCenterPagerBean.DataBean dataBean) {
        super(context);
        children = dataBean.getChildren();
    }

    /**
     * 负责外观方面，View在这里
     * @return
     */
    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.topicmenu_detail_pager,null);
        x.view().inject(TopicMenuDetailPager.this,view);

        //设置点击事件
        ib_tab_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
            }
        });

        return view;
    }

    /**
     * 负责文字方面，适配器在这里的哦
     */
    @Override
    public void initData() {
        super.initData();
        LogUtil.e("新闻详情页面内容被初始化了！");

        //准备新闻详情页面的数据
        tableDetailPagers = new ArrayList<>();
        for (int i = 0; i < children.size(); i++) {
            tableDetailPagers.add(new TopicDetailPager(context, children.get(i)));
        }
        //设置适配器
        viewPager.setAdapter(new MyNewsMenuDetailPagerAdapter());
        //ViewPager和TabPageIndicator关联
        tabPageIndicator.setViewPager(viewPager);


        //以后监听页面的变化，TabPageIndicator监听页面的变化！
        tabPageIndicator.setOnPageChangeListener(new MyOnPageChangeListener());

    }


    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            //处于第一个位置的时候！
            if (i==0){
                //slideingmenu可以全屏滑动
                isEnableSlidingMenu(SlidingMenu.TOUCHMODE_FULLSCREEN);
            }else {
                //否则就不能了
                isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    }

    /**
     * 根据传入的参数设置是否能让sliding menu进行滑动
     */

    private void isEnableSlidingMenu(int touchModeFullscreen){
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.getSlidingMenu().setTouchModeAbove(touchModeFullscreen);
    }

    /**
     * 适配器的4个必须重写的方法
     */
    class MyNewsMenuDetailPagerAdapter extends PagerAdapter {

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            TopicDetailPager tableDetailPager = tableDetailPagers.get(position);
            View view = tableDetailPager.rootView;
            tableDetailPager.initData();//初始化数据
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return tableDetailPagers.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view==o;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return children.get(position).getTitle();
        }
    }
}

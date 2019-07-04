package com.galaxy.test3.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.galaxy.test3.R;
import com.galaxy.test3.SplashActivity;
import com.galaxy.test3.utils.CacheUtils;
import com.galaxy.test3.utils.DensityUtil;

import java.util.ArrayList;

public class GuideActivity extends AppCompatActivity {
    private ViewPager viewpager;
    private Button btn_start_main;
    private LinearLayout point_group;
    private ArrayList<ImageView> imageViews;
    private ImageView red_point;
    private int widthdpi;
    private int heightdpi;
    /**
     * 两点的间距
     */
    private int leftmax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        viewpager = (ViewPager) findViewById(R.id.viewpager);
        btn_start_main = (Button) findViewById(R.id.btn_start_main);
        point_group = (LinearLayout)findViewById(R.id.point_group);
        red_point = (ImageView)findViewById(R.id.iv_red_point);


        //准备数据
        int [] ids = new int[]{
                R.drawable.guide1,
                R.drawable.guide2,
                R.drawable.guide3
        };

        imageViews = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            ImageView imageView = new ImageView(this);
            //要选择设置背景资源而不是资源，否则无法填充完整个屏幕
            imageView.setBackgroundResource(ids[i]);
            //添加到集合中
            imageViews.add(imageView);

            //创建点
            ImageView point = new ImageView(this);
            point.setBackgroundResource(R.drawable.point_normal);

            //转化px到sp
            widthdpi = DensityUtil.dip2px(GuideActivity.this,10);
            heightdpi = DensityUtil.dip2px(GuideActivity.this,10);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthdpi,heightdpi);
            if (i!=0){
                params.leftMargin=10;
            }
            point.setLayoutParams(params);
            //添加到线性布局中去
            point_group.addView(point);
        }

        //给布局文件ViewPager配置适配器（将数据可视化）
        viewpager.setAdapter(new MyPagerAdapter());


        //根据View的生命周期，当试图执行到layout或者是ondraw的时候，试图的高和宽，边距都有了
        red_point.getViewTreeObserver().addOnGlobalLayoutListener(new MyOnGlobalLayoutListener());


        btn_start_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //保存并记录参数
                CacheUtils.putBoolean(GuideActivity.this, SplashActivity.START_MAIN,true);
                //跳转到登陆页面，因为此时一定没有登陆过！
                startActivity(new Intent(GuideActivity.this,LoginAndRegisterActivity.class));
                //关闭当前引导界面
                finish();
            }
        });


        //得到屏幕滑动的百分比
        //页面变换监听器
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            /**
             * 当页面滚动会发生调用
             * @param i 当前滑动页面的位置
             * @param v 页面滑动的百分比
             * @param i1 滑动的像素
             */
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                //两点间移动的距离 =屏幕滑动百分比 * 间距
//                int leftmargin = (int)(i1*leftmax);
                //两点间滑动距离对应的坐标 = 原始的起始位置 + 两点间移动的距离
                int leftmargin = (int)(i * leftmax + (v*leftmax));
                //params.leftmargin = 两点间滑动距离对应的坐标
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)red_point.getLayoutParams();
                params.leftMargin = leftmargin;
                red_point.setLayoutParams(params);
            }

            /**
             * 当页面被选中时的回调方法
             * @param i
             */
            @Override
            public void onPageSelected(int i) {
                if (i==imageViews.size()-1){
                    btn_start_main.setVisibility(View.VISIBLE);
                }else {
                    btn_start_main.setVisibility(View.GONE);
                }
            }

            /**
             * 当页面滑动状态发生变化的时候调用
             * @param i
             */
            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    class MyPagerAdapter extends PagerAdapter{
        /**
         * 返回数据的个数
         * @return
         */
        @Override
        public int getCount() {
            return imageViews.size();
        }

        /**
         *  作用，getview
         * @param container viewpager
         * @param position  要创建页面的位置
         * @return  返回和创建当前页面有关系的值
         */
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView imageView = imageViews.get(position);
            //添加到容器中去
            container.addView(imageView);
            return imageView;
        }

        /**
         *  销毁页面
         * @param container Viewpager
         * @param position  要销毁的位置
         * @param object    要销毁的页面
         */
        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//            super.destroyItem(container, position, object);
            container.removeView((View)object);
        }

        /**
         * 判断
         * @param view 当前创建的视图
         * @param o 上面instantiateitem返回的结果值
         * @return
         */
        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view==o;
        }
    }

    class MyOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        @Override
        public void onGlobalLayout() {
            //执行了不止一次
            red_point.getViewTreeObserver().removeGlobalOnLayoutListener(this);

            //间距 = 第1个点的距离左边的距离  - 第0个点距离左边的距离
            leftmax = point_group.getChildAt(1).getLeft() - point_group.getChildAt(0).getLeft();
        }
    }


}

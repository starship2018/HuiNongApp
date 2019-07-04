package com.galaxy.refreshlistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;



import java.text.SimpleDateFormat;
import java.util.Date;

public class RefreshListview extends ListView {
    /**
     * 下拉刷新和顶部轮播图
     */
    private LinearLayout headerView;


    /**
     * 对布局中的元素进行实例化！
     */
    private View ll_pull_down_refresh;

    private ImageView iv_arrow;

    private ProgressBar pb_status;

    private TextView tv_status;

    private TextView tv_time;
    /**
     * 下拉刷新控件的高度
     */
    private int pullDownRefreshHeight;


    private Animation upAnimation;

    private Animation downAnimation;

    /**
     * 加载更多控件
     */
    private View footView;


    /*
     * 刷新状态的定义！
     * */
    //下拉刷新
    public static final int PULL_DOWN_REFRESH = 0;
    //手动刷新
    public static final int RELEASE_REFRESH = 1;
    //正在刷新
    public static final int REFRESHING = 2;
    //当前刷新状态
    private int currentStatus = PULL_DOWN_REFRESH;

    //底部刷新栏的高度！
    private int footViewHeight;

    private boolean isLoadMore = false;

    //顶部轮播图部分
    private View topNewsView;
    /**
     * listview在Y轴上的坐标
     */
    private int listViewOnScreenY = -1;


    public RefreshListview(Context context) {
        this(context, null);
    }

    public RefreshListview(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshListview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView(context);
        initAnimation();
        initFooterView(context);
    }

    private void initFooterView(Context context) {
        footView = View.inflate(context,R.layout.refresh_footer,null);
        footView.measure(0,0);
        footViewHeight = footView.getMeasuredHeight();
        footView.setPadding(0,-footViewHeight,0,0);
        //在listView的尾部添加footView
        addFooterView(footView);

        //监听ListView的滚动
        setOnScrollListener(new MyOnScrollListener());
    }

    /**
     * 添加顶部轮播图
     * @param topnews
     */
    public void addTopNewsView(View topnews) {
        if (topnews!=null){
            this.topNewsView = topnews;
            headerView.addView(topNewsView);
        }

    }

    class MyOnScrollListener implements OnScrollListener{

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            //当静止或惯性滚动的时候
            if (scrollState==OnScrollListener.SCROLL_STATE_IDLE || scrollState==OnScrollListener.SCROLL_STATE_FLING){
                //并且是最后一条可见
                if (getLastVisiblePosition()>=getCount()-1){
                    //1.显示加载更多的布局
                    footView.setPadding(8,8,8,8);
                    //2.状态改变
                    isLoadMore = true;
                    //3.回调接口
                    if (mOnRefreshListener!=null){
                        mOnRefreshListener.OnLoadMore();
                    }
                }
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    }


    private void initAnimation() {

        upAnimation = new RotateAnimation(0,-180,RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
        upAnimation.setDuration(500);
        upAnimation.setFillAfter(true);


        downAnimation = new RotateAnimation(-180,-360,RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
        downAnimation.setDuration(500);
        downAnimation.setFillAfter(true);
    }

    private void initHeaderView(Context context) {
        headerView = (LinearLayout) View.inflate(context, R.layout.refresh_header, null);
        ll_pull_down_refresh = (View) headerView.findViewById(R.id.ll_pulldown_refresh);
        iv_arrow = (ImageView) headerView.findViewById(R.id.iv_arrow);
        pb_status = (ProgressBar) headerView.findViewById(R.id.pb_status);
        tv_status = (TextView) headerView.findViewById(R.id.tv_status);
        tv_time = (TextView) headerView.findViewById(R.id.tv_time);


        ll_pull_down_refresh.measure(0, 0);
        pullDownRefreshHeight = ll_pull_down_refresh.getMeasuredHeight();
        //默认隐藏下拉控件
        //View.setPadding(0,-控件高度,0,0); 完全隐藏
        ll_pull_down_refresh.setPadding(0, -pullDownRefreshHeight, 0, 0);
        //添加头
        addHeaderView(headerView);
    }

    private float startY = -1;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //1.记录起始坐标点
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (startY == -1) {
                    startY = ev.getY();
                }

                //判断顶部轮播图是否完全显示！，完全显示后才会有下拉刷新
                boolean isDisplayTopNews = isDisplayTopNews();
                if (!isDisplayTopNews){
                    //加载更多
                    break;
                }


                if (currentStatus==REFRESHING){
                    break;
                }
                //2.来到新的坐标
                float endY = ev.getY();
                //3.记录滑动的距离
                float distanceY = endY - startY;
                if (distanceY > 0) {//下拉的情况
//                        View.setPadding()
                    int paddingTop = (int) (-pullDownRefreshHeight + distanceY);

                    if (paddingTop < 0 && currentStatus != PULL_DOWN_REFRESH) {
                        currentStatus = PULL_DOWN_REFRESH;
                        refreshStateView();
                    } else if (paddingTop > 0 && currentStatus != RELEASE_REFRESH) {
                        currentStatus = RELEASE_REFRESH;
                    }


                    ll_pull_down_refresh.setPadding(0, paddingTop, 0, 0);
                }
                break;
            case MotionEvent.ACTION_UP:
                startY = -1;
                if (currentStatus == PULL_DOWN_REFRESH) {
                    ll_pull_down_refresh.setPadding(0, -pullDownRefreshHeight, 0, 0);
                } else if (currentStatus == RELEASE_REFRESH) {
                    currentStatus = REFRESHING;
                    refreshStateView();

                    ll_pull_down_refresh.setPadding(0, 0, 0, 0);

                    //回调接口
                    if (mOnRefreshListener !=null){
                        mOnRefreshListener.onPullDownRefresh();
                    }
                }
                break;

        }
        return super.onTouchEvent(ev);
    }

    /**
     * 判断是否显示顶部轮播图
     * @return
     */
    private boolean isDisplayTopNews() {

        if (topNewsView!=null){
            //1.得到listView在屏幕上的坐标
            int[] location = new int[2];
            if (listViewOnScreenY ==-1){
                getLocationOnScreen(location);
                listViewOnScreenY = location[1];
            }

            //2.得到顶部轮播图在屏幕上的坐标

            topNewsView.getLocationOnScreen(location);
            int topNewsViewOnScreenY = location[1];


            return listViewOnScreenY <= topNewsViewOnScreenY;
        }else {
            return true;
        }

    }

    private void refreshStateView() {
        switch (currentStatus) {
            case PULL_DOWN_REFRESH://下拉刷新
                iv_arrow.startAnimation(downAnimation);
                tv_status.setText("下拉刷新...");
                break;
            case RELEASE_REFRESH://松手刷新
                iv_arrow.startAnimation(upAnimation);
                tv_status.setText("手动刷新...");
                break;
            case REFRESHING://正在刷新
                tv_status.setText("正在刷新...");
                pb_status.setVisibility(VISIBLE);
                iv_arrow.clearAnimation();
                iv_arrow.setVisibility(GONE);
                break;
        }
    }

    public void onRefreshFinish(boolean success) {
        if (isLoadMore) {
            //加载更多
            isLoadMore = false;
            //隐藏加载布局
            footView.setPadding(0, -footViewHeight, 0, 0);

        } else {
            tv_status.setText("下拉刷新...");
            currentStatus = PULL_DOWN_REFRESH;
            iv_arrow.clearAnimation();
            pb_status.setVisibility(GONE);
            iv_arrow.setVisibility(VISIBLE);
            //隐藏下拉刷新控件
            ll_pull_down_refresh.setPadding(0, -pullDownRefreshHeight, 0, 0);
            if (success) {
                tv_time.setText("上次更新时间：" + getSystemTime());
            }
        }
    }

    /**
     * 获取Android系统时间
     * @return
     */
    private String getSystemTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return sdf.format(new Date());
    }

    /**
     * 监听控件的刷新
     */
    public interface OnRefreshListener{
        //下拉刷新的时候回调这个方法
        public void onPullDownRefresh();

        //当加载更多的时候回调这个方法
        public void OnLoadMore();

    }

    private OnRefreshListener mOnRefreshListener;

    /**
     * 设置监听刷新，由外界设置!
     */

    public void setOnRefreshListener(OnRefreshListener l){
        this.mOnRefreshListener = l;
    }


}

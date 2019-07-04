package com.galaxy.test3.menudatailpager.tabledetailpager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.galaxy.refreshlistview.RefreshListview;
import com.galaxy.test3.R;
import com.galaxy.test3.base.MenuDetailBasePager;
import com.galaxy.test3.domain.NewsCenterPagerBean;
import com.galaxy.test3.domain.TabDetailPagerBean;
import com.galaxy.test3.utils.CacheUtils;
import com.galaxy.test3.utils.Constants;
import com.galaxy.test3.utils.LogUtil;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;

import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

public class TopicDetailPager extends MenuDetailBasePager {

    /**
     *
     */

    private ViewPager viewPager;

    private TextView tv_title;

    private LinearLayout ll_point_group;


    private ListView listView;

    private final NewsCenterPagerBean.DataBean.ChildrenBean childrenData;

    private String url;
    /**
     * 顶部轮播图新闻的数据
     */
    private List<TabDetailPagerBean.DataBean.TopnewsBean> topnews;

    //新闻列表数据的集合！
    private List<TabDetailPagerBean.DataBean.NewsBean> news;


    private TabDetailPagerListAdapter tabDetailPagerListAdapter;

    private ImageOptions imageOptions;


    //下一页的联网路径！
    private String moreUrl;

    //是否加载更多！
    private boolean isLoadMore = false;

    private PullToRefreshListView mPullRefreshListView;


    public TopicDetailPager(Context context, NewsCenterPagerBean.DataBean.ChildrenBean childrenData) {
        super(context);
        this.childrenData = childrenData;
        imageOptions = new ImageOptions.Builder()
                .setSize(DensityUtil.dip2px(100), DensityUtil.dip2px(100))
                .setRadius(DensityUtil.dip2px(5))
                // 如果ImageView的大小不是定义为wrap_content, 不要crop.
                .setCrop(true) // 很多时候设置了合适的scaleType也不需要它.
                // 加载中或错误图片的ScaleType
                //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setLoadingDrawableId(R.drawable.news_pic_default)
                .setFailureDrawableId(R.drawable.news_pic_default)
                .build();
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.topic_detail_pager, null);

        mPullRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pull_refresh_list);

//        listView = (RefreshListview) view.findViewById(R.id.listview);

        listView = mPullRefreshListView.getRefreshableView();

        /**
         * Add Sound Event Listener
         */
        SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<ListView>(context);
        soundListener.addSoundEvent(PullToRefreshBase.State.PULL_TO_REFRESH, R.raw.pull_event);
        soundListener.addSoundEvent(PullToRefreshBase.State.RESET, R.raw.reset_sound);
        soundListener.addSoundEvent(PullToRefreshBase.State.REFRESHING, R.raw.refreshing_sound);
        mPullRefreshListView.setOnPullEventListener(soundListener);

        View topnews = View.inflate(context,R.layout.topnews,null);

        viewPager = (ViewPager) topnews.findViewById(R.id.viewpager);

        tv_title = (TextView) topnews.findViewById(R.id.tv_title);

        ll_point_group = (LinearLayout) topnews.findViewById(R.id.ll_point_group);

        //把顶部的轮播图部分以头的方式添加到listView中去！
        listView.addHeaderView(topnews);

//        listView.addTopNewsView(topnews);

        //设置监听下拉刷新
//        listView.setOnRefreshListener(new MyOnRefreshListener());
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getMoreDataFromNet();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (TextUtils.isEmpty(moreUrl)){
                    //没有更多的数据
                    Toast.makeText(context,"没有更多的数据了",Toast.LENGTH_SHORT).show();
//                    listView.onRefreshFinish(false);
                    mPullRefreshListView.onRefreshComplete();
                }else {
                    getMoreDataFromNet();
                }
            }
        });

        return view;
    }

//    class MyOnRefreshListener implements RefreshListview.OnRefreshListener{
//
//        @Override
//        public void onPullDownRefresh() {
//            getDataFromNet();
//        }
//
//        @Override
//        public void OnLoadMore() {
////            Toast.makeText(context,"加载更多刷新被回调了",Toast.LENGTH_SHORT).show();
//
//            if (TextUtils.isEmpty(moreUrl)){
//                //没有更多的数据
//                Toast.makeText(context,"没有更多的数据了",Toast.LENGTH_SHORT).show();
//                listView.onRefreshFinish(false);
//            }else {
//                getMoreDataFromNet();
//            }
//
//        }
//
//    }

    private void getMoreDataFromNet() {
        RequestParams param = new RequestParams(moreUrl);
        param.setConnectTimeout(4000);
        x.http().get(param, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("加载更多联网成功！"+result);

                mPullRefreshListView.onRefreshComplete();
                //把这个放在前面
                isLoadMore = true;
                //解析数据
                processData(result);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("加载更多联网失败！"+ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("加载更多联网成功！"+cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("加载更多联网结束！");
            }
        });
    }


    @Override
    public void initData() {
        super.initData();

        url = Constants.BASE_URL + childrenData.getUrl();

        //先从缓存中读取数据
        String saveJson = CacheUtils.getString(context, url);
        if (!TextUtils.isEmpty(saveJson)) {
            processData(saveJson);
        }
        getDataFromNet();
    }

    //显示之前高亮过的位置
    private int prePosition;


    private void processData(String saveJson) {
        TabDetailPagerBean bean = pasedJson(saveJson);
        LogUtil.e(childrenData.getTitle() + "解析成功！" + bean.getData().getNews().get(0).getTitle());

        moreUrl = "";
        if (TextUtils.isEmpty(bean.getData().getMore())){
            moreUrl = "";
        }else{
            moreUrl = Constants.BASE_URL + bean.getData().getMore();
        }

        LogUtil.e("加载更多的地址"+moreUrl);
        //默认和加载更多
        if (!isLoadMore){
            //顶部轮播图
            topnews = bean.getData().getTopnews();
            //设置viewpager适配器
            viewPager.setAdapter(new TabDetailPagerTopNewsAdapter());
            //添加红点
            addPoint();

            //监听页面的改变，去设施红点变化和文字变化
            viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
            tv_title.setText(topnews.get(prePosition).getTitle());

            //准备listview对应的集合数据
            news = bean.getData().getNews();
            //设置listview的适配器！
            tabDetailPagerListAdapter = new TabDetailPagerListAdapter();
            listView.setAdapter(tabDetailPagerListAdapter);
        }else{
            //加载更多的情况
            isLoadMore = false;
            List<TabDetailPagerBean.DataBean.NewsBean> morenews = bean.getData().getNews();
            //添加到原来的集合中去
            news.addAll(morenews);
            //刷洗适配器
            tabDetailPagerListAdapter.notifyDataSetChanged();
        }




    }

    class TabDetailPagerListAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return news.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        /**
         * 和initView的功能类似！
         *
         * @param position
         * @param convertView
         * @param parent
         * @return
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_tabdetail_pager, null);
                viewHolder = new ViewHolder();
                viewHolder.iv_icon = convertView.findViewById(R.id.iv_icon);
                viewHolder.tv_title = convertView.findViewById(R.id.tv_title);
                viewHolder.tv_time = convertView.findViewById(R.id.tv_time);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            //根据位置得到数据
            TabDetailPagerBean.DataBean.NewsBean newsBean = news.get(position);
            String imgurl = Constants.BASE_URL + newsBean.getListimage();
            //请求图片
            x.image().bind(viewHolder.iv_icon,imgurl,imageOptions);
            //设置标题
            viewHolder.tv_title.setText(newsBean.getTitle());
            //设置事件
            viewHolder.tv_time.setText(newsBean.getPubdate());

            return convertView;
        }
    }

    static class ViewHolder {
        ImageView iv_icon;
        TextView tv_title;
        TextView tv_time;
    }

    private void addPoint() {
        ll_point_group.removeAllViews();//每取一次数据前，移除所有的红点

        for (int i = 0; i < topnews.size(); i++) {
            ImageView imageView = new ImageView(context);
            //设置背景选择器
            imageView.setBackgroundResource(R.drawable.point_selector);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(8), DensityUtil.dip2px(8));

            if (i == 0) {
                imageView.setEnabled(true);
            } else {
                imageView.setEnabled(false);
                params.leftMargin = DensityUtil.dip2px(8);
            }


            imageView.setLayoutParams(params);
            ll_point_group.addView(imageView);
        }
    }


    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {


        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            //1.设置文本
            tv_title.setText(topnews.get(i).getTitle());
            //2.红点高亮
            //把之前的变为灰色，当前变为红色
            ll_point_group.getChildAt(prePosition).setEnabled(false);
            ll_point_group.getChildAt(i).setEnabled(true);
            prePosition = i;

        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    }

    class TabDetailPagerTopNewsAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return topnews.size();
        }

        @NonNull
        @Override
        /**
         * 允许添加新的view，内容多变，是必须重写的一个方法！
         */
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            //动态创建图像view（参数是上下文，也就是妈）
            ImageView imageView = new ImageView(context);
            imageView.setBackgroundResource(R.drawable.home_scroll_default);
            //X,Y轴拉伸
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            //将图片添加到容器中去！
            container.addView(imageView);

            TabDetailPagerBean.DataBean.TopnewsBean topnewsBean = topnews.get(position);
            String imgurl = Constants.BASE_URL + topnewsBean.getTopimage();

            //联网请求图片
            x.image().bind(imageView, imgurl);
            return imageView;
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

    private TabDetailPagerBean pasedJson(String saveJson) {
        return new Gson().fromJson(saveJson, TabDetailPagerBean.class);
    }

    private void getDataFromNet() {
        LogUtil.e("url地址==="+url);
        RequestParams param = new RequestParams(url);
        param.setConnectTimeout(4000);
        x.http().get(param, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                //缓存数据
                CacheUtils.putString(context, url, result);
                LogUtil.e(childrenData.getTitle() + "-页面数据请求成功！" + result);

                //解析和显示数据
                processData(result);
                //隐藏下拉刷新控件-更新时间
                mPullRefreshListView.onRefreshComplete();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e(childrenData.getTitle() + "-页面数据请求失败！" + ex.getMessage());
                ////隐藏下拉刷新控件-不更新时间，只隐藏
                mPullRefreshListView.onRefreshComplete();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e(childrenData.getTitle() + "-页面数据请求取消！" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e(childrenData.getTitle() + "-页面数据请求结束！");
            }
        });
    }
}

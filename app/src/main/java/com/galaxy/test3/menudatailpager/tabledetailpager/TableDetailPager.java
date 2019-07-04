package com.galaxy.test3.menudatailpager.tabledetailpager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.galaxy.refreshlistview.RefreshListview;
import com.galaxy.test3.R;
import com.galaxy.test3.activity.NewsDetailActivity;
import com.galaxy.test3.base.MenuDetailBasePager;
import com.galaxy.test3.domain.NewsCenterPagerBean;
import com.galaxy.test3.domain.TabDetailPagerBean;
import com.galaxy.test3.domain.TianXingNewsPagerBean;
import com.galaxy.test3.utils.CacheUtils;
import com.galaxy.test3.utils.Constants;
import com.galaxy.test3.utils.LogUtil;

import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

public class TableDetailPager extends MenuDetailBasePager {

    public static final String READ_ARRAY_ID = "read_array_id";
    /**
     *
     */

    private ViewPager viewPager;

    private TextView tv_title;

    private LinearLayout ll_point_group;


    private RefreshListview listView;

//    private final NewsCenterPagerBean.DataBean.ChildrenBean childrenData;

//    private final TianXingNewsPagerBean.NewslistBean childrenData;

    private String url;
    /**
     * 顶部轮播图新闻的数据
     */
//    private List<TabDetailPagerBean.DataBean.TopnewsBean> topnews;

    //新闻列表数据的集合！
//    private List<TabDetailPagerBean.DataBean.NewsBean> news;

    private List<TianXingNewsPagerBean.NewslistBean> news;


    private TabDetailPagerListAdapter tabDetailPagerListAdapter;

    private ImageOptions imageOptions;


    //下一页的联网路径！
    private String moreUrl;

    //是否加载更多！
    private boolean isLoadMore = false;

//    private InternalHandler internalHandler;


    /**
     * 解析json数据
     * 1.使用系统的api解析
     * 2.使用第三方的框架解析，例如Gson，fastJson
//     * @param json
     * @return
     */

//    private NewsCenterPagerBean parsedJson(String json) {
//        //导入gson jar包！
//        Gson gson = new Gson();
//        //将获取到的json数据放入NewCenterPagerBean中进行适配，返回一个带有网络数据的json对象回来
//        NewsCenterPagerBean bean = gson.fromJson(json,NewsCenterPagerBean.class);
//        return bean;
//    }

    private TianXingNewsPagerBean parsedJson(String saveJson) {
        return new Gson().fromJson(saveJson, TianXingNewsPagerBean.class);
    }

    public TableDetailPager(final Context context, final String url) {
        super(context);
        this.url = url;
        LogUtil.e("本页面的天行数据请求链接（从newsmenudetailpager以构造方法传入！）是"+url);
        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("使用xutil3联网天行数据请求成功！=="+result);
                //缓存数据
                CacheUtils.putString(context,url,result);
                //获取缓存数据
                String saveJson = CacheUtils.getString(context,url);
                if(!TextUtils.isEmpty(saveJson)){
                    result = saveJson;
                }
                //处理数据
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("使用xutil3联网天行数据请求失败！=="+ex.getMessage());
                //获取缓存数据
                String saveJson = CacheUtils.getString(context,url);
                processData(saveJson);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("使用xutil3联网天行数据请求onCancelled！=="+cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("使用xutil3联网天行数据请求onFinished！");
            }
        });

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
        View view = View.inflate(context, R.layout.tabdetail_pager, null);

        listView = (RefreshListview) view.findViewById(R.id.listview);

        View topnews = View.inflate(context,R.layout.topnews,null);

        viewPager = (ViewPager) topnews.findViewById(R.id.viewpager);

        tv_title = (TextView) topnews.findViewById(R.id.tv_title);

        ll_point_group = (LinearLayout) topnews.findViewById(R.id.ll_point_group);

        //把顶部的轮播图部分以头的方式添加到listView中去！
//        listView.addHeaderView(topnews);

//        listView.addTopNewsView(topnews);

        //设置监听下拉刷新
        listView.setOnRefreshListener(new MyOnRefreshListener());

        //设置监听点击详情页
        listView.setOnItemClickListener(new MyOnItemClickListener());

        return view;
    }

    class MyOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            int realPosition = position -1;
            TianXingNewsPagerBean.NewslistBean newsBean = news.get(realPosition);
//            Toast.makeText(context,"newData==id=="+newsBean.getId()+",newData_title =="+newsBean
//            .getTitle(),Toast.LENGTH_SHORT).show();


            //1.取出保存的id
//            String idArray = CacheUtils.getString(context, READ_ARRAY_ID);//"" 1 1 2 3 1
            //2.判断是否存在，若不存在，才保存，并且刷新适配器
//            if (!idArray.contains(newsBean.getId()+"")){//3511
//                CacheUtils.putString(context,READ_ARRAY_ID,idArray+newsBean.getId()+",");
//
//                //刷新适配器
//                tabDetailPagerListAdapter.notifyDataSetChanged();
//            }
            //跳转到新闻浏览页面
            Intent intent = new Intent(context, NewsDetailActivity.class);
            intent.putExtra("url",newsBean.getUrl());
            context.startActivity(intent);

        }
    }

    class MyOnRefreshListener implements RefreshListview.OnRefreshListener{

        @Override
        public void onPullDownRefresh() {
            getDataFromNet();
        }

        @Override
        public void OnLoadMore() {
//            Toast.makeText(context,"加载更多刷新被回调了",Toast.LENGTH_SHORT).show();

            if (TextUtils.isEmpty(moreUrl)){
                //没有更多的数据
                Toast.makeText(context,"没有更多的数据了",Toast.LENGTH_SHORT).show();
                listView.onRefreshFinish(false);
            }else {
//                getMoreDataFromNet();
            }

        }

    }

//    private void getMoreDataFromNet() {
//        RequestParams param = new RequestParams(moreUrl);
//        param.setConnectTimeout(4000);
//        x.http().get(param, new Callback.CommonCallback<String>() {
//            @Override
//            public void onSuccess(String result) {
//                LogUtil.e("加载更多联网成功！"+result);
//
//                listView.onRefreshFinish(false );
//                //把这个放在前面
//                isLoadMore = true;
//                //解析数据
//                processData(result);
//
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                LogUtil.e("加载更多联网失败！"+ex.getMessage());
//            }
//
//            @Override
//            public void onCancelled(CancelledException cex) {
//                LogUtil.e("加载更多联网成功！"+cex.getMessage());
//            }
//
//            @Override
//            public void onFinished() {
//                LogUtil.e("加载更多联网结束！");
//            }
//        });
//    }


    @Override
    public void initData() {
        super.initData();

//        url = Constants.BASE_URL + childrenData.getUrl();

        //先从缓存中读取数据

        getDataFromNet();
        String saveJson = CacheUtils.getString(context, url);
        if (!TextUtils.isEmpty(saveJson)) {
            processData(saveJson);
        }
        getDataFromNet();
    }

    //显示之前高亮过的位置
    private int prePosition;


    private void processData(String saveJson) {
        LogUtil.e("这里先打印json看一下是什么样子，在parsedjson 之前");
        LogUtil.e(saveJson);

        TianXingNewsPagerBean bean = parsedJson(saveJson);
//        LogUtil.e(bean.getMsg()+ "processdata解析成功！" + bean.getNewslist().get(0).getTitle());

        moreUrl = "";
//        if (TextUtils.isEmpty(bean.getData().getMore())){
//            moreUrl = "";
//        }else{
//            moreUrl = Constants.BASE_URL + bean.getData().getMore();
//        }

//        LogUtil.e("加载更多的地址"+moreUrl);
        //默认和加载更多
        if (!isLoadMore){
            //顶部轮播图
//            topnews = bean.getData().getTopnews();
//            设置viewpager适配器
//            viewPager.setAdapter(new TabDetailPagerTopNewsAdapter());
            //添加红点
//            addPoint();

            //监听页面的改变，去设施红点变化和文字变化
//            viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
//            tv_title.setText(topnews.get(prePosition).getTitle());


            //准备listview对应的集合数据
            news = bean.getNewslist();
            if(news==null){
                LogUtil.e("我在说明news在设置适配器之前已经是null的了！");
            }
            //设置listview的适配器！
            tabDetailPagerListAdapter = new TabDetailPagerListAdapter();
            listView.setAdapter(tabDetailPagerListAdapter);
        }else{
            //加载更多的情况
            isLoadMore = false;
//            List<TabDetailPagerBean.DataBean.NewsBean> morenews = bean.getData().getNews();
            //添加到原来的集合中去
//            news.addAll(morenews);
//            刷洗适配器
//            tabDetailPagerListAdapter.notifyDataSetChanged();
        }


//        //发消息每隔3000毫秒切换一次ViewPager页面
//        if (internalHandler ==null){
//            internalHandler = new InternalHandler();
//        }
//
//        //把消息队列所有的消息和回调移除
//        internalHandler.removeCallbacksAndMessages(null);
//        internalHandler.postDelayed(new MyRunable(),3000);

    }

//    class MyRunable implements Runnable{
//
//        @Override
//        public void run() {
//            internalHandler.sendEmptyMessage(0);
//        }
//    }
//
//    class InternalHandler extends Handler{
//
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            //切换ViewPager的下一个页面
//            int item = (viewPager.getCurrentItem()+1)%topnews.size();
//            viewPager.setCurrentItem(item);
//            internalHandler.postDelayed(new MyRunable(),3000);
//
//        }
//    }






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
//                viewHolder.tv_author = convertView.findViewById(R.id.tv_author);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            //根据位置得到数据
            TianXingNewsPagerBean.NewslistBean newsBean = news.get(position);
            String imgurl = newsBean.getPicUrl();
            //请求图片
            x.image().bind(viewHolder.iv_icon,imgurl,imageOptions);
            //设置标题
            viewHolder.tv_title.setText(newsBean.getTitle());
            //设置时间
            viewHolder.tv_time.setText(newsBean.getCtime());
            //设置作者
//            viewHolder.tv_author.setText(newsBean.getDescription());

//            String idArray = CacheUtils.getString(context,READ_ARRAY_ID);
//            if (idArray.contains(newsBean.getId()+"")){
//                //设置灰色
//                viewHolder.tv_title.setTextColor(Color.GRAY);
//            }else {
//                //设置黑色
//                viewHolder.tv_title.setTextColor(Color.BLACK);
//            }

            return convertView;
        }
    }

    static class ViewHolder {
        ImageView iv_icon;
        TextView tv_title;
        TextView tv_time;
//        TextView tv_author;
    }

//    private void addPoint() {
//        ll_point_group.removeAllViews();//每取一次数据前，移除所有的红点
//
//        for (int i = 0; i < topnews.size(); i++) {
//            ImageView imageView = new ImageView(context);
//            //设置背景选择器
//            imageView.setBackgroundResource(R.drawable.point_selector);
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(8), DensityUtil.dip2px(8));
//
//            if (i == 0) {
//                imageView.setEnabled(true);
//            } else {
//                imageView.setEnabled(false);
//                params.leftMargin = DensityUtil.dip2px(8);
//            }
//
//
//            imageView.setLayoutParams(params);
//            ll_point_group.addView(imageView);
//        }
//    }


//    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
//
//
//        @Override
//        public void onPageScrolled(int i, float v, int i1) {
//
//        }
//
//        @Override
//        public void onPageSelected(int i) {
//            //1.设置文本
//            tv_title.setText(news.get(i).getTitle());
//            //2.红点高亮
//            //把之前的变为灰色，当前变为红色
//            ll_point_group.getChildAt(prePosition).setEnabled(false);
//            ll_point_group.getChildAt(i).setEnabled(true);
//            prePosition = i;
//
//        }
//
//        private boolean isDragging = false;
//
//        @Override
//        public void onPageScrollStateChanged(int i) {
//            if (i == viewPager.SCROLL_STATE_DRAGGING){
//                //拖拽
//                isDragging = true;
//                //拖拽时移除消息
//                internalHandler.removeCallbacksAndMessages(null);
//            }else if (i ==viewPager.SCROLL_STATE_SETTLING && isDragging){
//                //惯性
//                isDragging =false;
//                internalHandler.removeCallbacksAndMessages(null);
//                internalHandler.postDelayed(new MyRunable(),3000);
//            }else if (i==viewPager.SCROLL_STATE_IDLE){
//                //静止
//                isDragging = false;
//                internalHandler.removeCallbacksAndMessages(null);
//                internalHandler.postDelayed(new MyRunable(),3000);
//            }
//
//        }
//    }

//    class TabDetailPagerTopNewsAdapter extends PagerAdapter {
//
//        @Override
//        public int getCount() {
//            return topnews.size();
//        }
//
//        @NonNull
//        @Override
//        /**
//         * 允许添加新的view，内容多变，是必须重写的一个方法！
//         */
//        public Object instantiateItem(@NonNull ViewGroup container, int position) {
//            //动态创建图像view（参数是上下文，也就是妈）
//            ImageView imageView = new ImageView(context);
//            imageView.setBackgroundResource(R.drawable.home_scroll_default);
//            //X,Y轴拉伸
//            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//            //将图片添加到容器中去！
//            container.addView(imageView);
//
//            TabDetailPagerBean.DataBean.TopnewsBean topnewsBean = topnews.get(position);
//            String imgurl = Constants.BASE_URL + topnewsBean.getTopimage();
//
//            //联网请求图片
//            x.image().bind(imageView, imgurl);
//
//            imageView.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    switch (event.getAction()){
//                        case MotionEvent.ACTION_DOWN:
//                            LogUtil.e("按下");
//                            //把下地队列所有的消息和回调移除
//                            internalHandler.removeCallbacksAndMessages(null);
//                            break;
//                        case MotionEvent.ACTION_UP:
//                            LogUtil.e("离开");
//                            internalHandler.removeCallbacksAndMessages(null);
//                            internalHandler.postDelayed(new MyRunable(),3000);
//                            break;
////                        case MotionEvent.ACTION_CANCEL://取消
////                            LogUtil.e("取消");
////                            internalHandler.removeCallbacksAndMessages(null);
////                            internalHandler.postDelayed(new MyRunable(),3000);
////                            break;
//                    }
//                    return true;
//                }
//            });
//            return imageView;
//        }
//
//        @Override
//        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//            container.removeView((View) object);
//        }
//
//        @Override
//        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
//            return view == o;
//        }
//    }



    private void getDataFromNet() {
        LogUtil.e("getDataFromNet ---url地址=========="+url);
        RequestParams param = new RequestParams(url);
        param.setConnectTimeout(4000);
        x.http().get(param, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                //缓存数据
                CacheUtils.putString(context, url, result);
                LogUtil.e("-国内天行数据页面数据请求成功！" + result);

                //解析和显示数据
                processData(result);
                //隐藏下拉刷新控件-更新时间
                listView.onRefreshFinish(true);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
//                LogUtil.e(childrenData.getTitle() + "-页面数据请求失败！" + ex.getMessage());
                /*联网失败的情况下就去缓存中读取数据！*/
                String saveJson = CacheUtils.getString(context, url);
                processData(saveJson);
                ////隐藏下拉刷新控件-不更新时间，只隐藏
                listView.onRefreshFinish(false);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e( "-页面数据请求取消！" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e( "-页面数据请求结束！");
            }
        });
    }
}

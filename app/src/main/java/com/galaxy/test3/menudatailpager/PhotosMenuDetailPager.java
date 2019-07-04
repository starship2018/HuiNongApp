package com.galaxy.test3.menudatailpager;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.galaxy.test3.R;
import com.galaxy.test3.base.MenuDetailBasePager;
import com.galaxy.test3.domain.NewsCenterPagerBean;
import com.galaxy.test3.domain.PhotosMenuDetailPagerBean;
import com.galaxy.test3.utils.CacheUtils;
import com.galaxy.test3.utils.Constants;
import com.galaxy.test3.utils.LogUtil;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

public class PhotosMenuDetailPager extends MenuDetailBasePager {

    private final NewsCenterPagerBean.DataBean detailPagerData;

    @ViewInject(R.id.listview)
    private ListView listView;

    @ViewInject(R.id.gridview)
    private ListView gridview;

    private String url;

    private List<PhotosMenuDetailPagerBean.DataBean.NewsBean> news;

    public PhotosMenuDetailPager(Context context, NewsCenterPagerBean.DataBean dataBean) {
        super(context);
        this.detailPagerData = dataBean;
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.photos_menudetail_pager,null);
        x.view().inject(this,view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("组图页被初始化了！");
        url = Constants.BASE_URL + detailPagerData.getUrl();

        
        getDataFromNet();

    }

    /**
     * 使用xutils3联网数据
     */
    private void getDataFromNet() {
        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                LogUtil.e("使用xutil3联网请求成功！=="+result);
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
                LogUtil.e("使用xutil3联网请求失败！=="+ex.getMessage());
                //获取缓存数据
                String saveJson = CacheUtils.getString(context,url);
                processData(saveJson);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("使用xutil3联网请求onCancelled！=="+cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("使用xutil3联网请求onFinished！");
            }
        });
    }

    /**
     * 解析和显示数据
     * @param json
     */
    private void processData(String json) {
        PhotosMenuDetailPagerBean bean = parsedJson(json);
        LogUtil.e("图组解析成功=="+bean.getData().getNews().get(0).getTitle());

        //设置适配器
        news =  bean.getData().getNews();
        listView.setAdapter(new PhotosMenuDetailPagerAdapter());
    }

    class PhotosMenuDetailPagerAdapter extends BaseAdapter{

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

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null){
                convertView = View.inflate(context,R.layout.item_photos_menudetail_pager,null);
                viewHolder = new ViewHolder();
                viewHolder.iv_icon = convertView.findViewById(R.id.iv_icon);
                viewHolder.tv_title = convertView.findViewById(R.id.tv_title);
                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            //根据位置得到对应的数据
            PhotosMenuDetailPagerBean.DataBean.NewsBean newsBean = news.get(position);

//            viewHolder.tv_title.setText(newsBean.getTitle());
//            String imageUrl = Constants.BASE_URL + newsBean.getSmallimage();
//            loaderImager(viewHolder,imageUrl);
            return null;
        }
    }

//    private void loaderImager(final ViewHolder viewHolder, String imageurl) {
//
//        //设置tag
//        viewHolder.iv_icon.setTag(imageurl);

//    }
//    private void loaderImager(final ViewHolder viewHolder, String imageurl) {
//
//        //设置tag
//        viewHolder.iv_icon.setTag(imageurl);
//        //直接在这里请求会乱位置
//        ImageLoader.ImageListener listener = new ImageLoader.ImageListener() {
//            @Override
//            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
//                if (imageContainer != null) {
//
//                    if (viewHolder.iv_icon != null) {
//                        if (imageContainer.getBitmap() != null) {
//                            //设置图片
//                            viewHolder.iv_icon.setImageBitmap(imageContainer.getBitmap());
//                        } else {
//                            //设置默认图片
//                            viewHolder.iv_icon.setImageResource(R.drawable.home_scroll_default);
//                        }
//                    }
//                }
//            }
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                //如果出错，则说明都不显示（简单处理），最好准备一张出错图片
//                viewHolder.iv_icon.setImageResource(R.drawable.home_scroll_default);
//            }
//        };
//        VolleyManager.getImageLoader().get(imageurl, listener);
//    }


    static class ViewHolder{
        ImageView iv_icon;
        TextView tv_title;
    }

    private PhotosMenuDetailPagerBean parsedJson(String json){
        return new Gson().fromJson(json,PhotosMenuDetailPagerBean.class);
    }
}

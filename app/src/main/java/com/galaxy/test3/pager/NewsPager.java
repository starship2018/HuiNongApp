package com.galaxy.test3.pager;

import android.content.Context;
import android.graphics.Color;
import android.nfc.Tag;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.galaxy.test3.activity.MainActivity;
import com.galaxy.test3.base.BasePager;
import com.galaxy.test3.base.MenuDetailBasePager;
import com.galaxy.test3.domain.NewsCenterPagerBean;
import com.galaxy.test3.fragment.LeftMenuFragment;
import com.galaxy.test3.menudatailpager.InteracMenuDetailPager;
import com.galaxy.test3.menudatailpager.NewsMenuDetailPager;
import com.galaxy.test3.menudatailpager.PhotosMenuDetailPager;
import com.galaxy.test3.menudatailpager.TopicMenuDetailPager;
import com.galaxy.test3.utils.CacheUtils;
import com.galaxy.test3.utils.Constants;
import com.galaxy.test3.utils.LogUtil;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.common.util.FileUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * 控制页面显示内容
 */
public class NewsPager extends BasePager {
    public NewsPager(Context context) {
        super(context);
    }

    private List<NewsCenterPagerBean.DataBean> data;

    private ArrayList<MenuDetailBasePager> detailBasePagers;

    @Override
    public void initData() {
        super.initData();
        //设置隐藏！
        ib_menu.setVisibility(View.GONE);
        //1.设置标题
        tv_title.setText("新闻页");
        //2.联网请求数据，创建视图
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.BLUE);
        textView.setTextSize(30);
        //3.把子视图添加到Base Pager的FrameWork的Frame Layout中去
        fl_content.addView(textView);
        //4.绑定数据
        textView.setText("Loading...");

        //本来应该在这里进行缓存数据的载入和联网数据的更新，但是由于某些不知名的原因，我将其搬到了下面的联网的回调函数中去了！


        //联网请求数据

        //注意！在使用xutil请求数据之前，一定要在AndroidMenifest中提前进行配置 android:name=".HuiNongApplication"
//        detailBasePagers = new ArrayList<>();
//        detailBasePagers.add(new NewsMenuDetailPager(context));//新闻详情页面
        //缓存数据

        String result  = getJson("categories.json",context);
        LogUtil.e("打印本地获取的json数据"+result);
        CacheUtils.putString(context,"local_json",result);
        //获取缓存数据
        String saveJson = CacheUtils.getString(context,"local_json");
        if(!TextUtils.isEmpty(saveJson)){
            result = saveJson;
        }
//                处理数据
        processData(result);
//        getDataFromNet();
//        processData();
//        getDataFromNetByVolley();
    }

    /**
     * 读取本地json文件
     * @param fileName
     * @param context
     * @return
     */
    private static String getJson(String fileName, Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            InputStream is = context.getAssets().open(fileName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line;
            LogUtil.e("本地获取json成功！");
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();

    }
    /**
     * 使用xutils3联网数据
     */
    private void getDataFromNet() {
        RequestParams params = new RequestParams(Constants.NEW_CENTER_PAGER_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                LogUtil.e("使用xutil3联网请求成功！=="+result);
                //缓存数据
                getJson("categories.json",context);
                CacheUtils.putString(context,Constants.NEW_CENTER_PAGER_URL,result);
                //获取缓存数据
                String saveJson = CacheUtils.getString(context,Constants.NEW_CENTER_PAGER_URL);
                if(!TextUtils.isEmpty(saveJson)){
                    result = saveJson;
                }
//                处理数据
                processData(result);
            }
//
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("使用xutil3联网请求失败！=="+ex.getMessage());
                //获取缓存数据
                try{
                    String saveJson = CacheUtils.getString(context,Constants.NEW_CENTER_PAGER_URL);
                    processData(saveJson);
                }catch (Exception e){
                    Toast.makeText(context,"请检查网络连接！",Toast.LENGTH_SHORT).show();
                }

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
     * 解析json对象和显示数据
//     * @param json
     */
    private void processData(String json) {
        NewsCenterPagerBean bean = parsedJson(json);
//        这里的bean就相当于是整个的json文件了，所有的方法都是根据json的结构来的，具体可以查看json的结构图
        String title = bean.getData().get(0).getChildren().get(1).getTitle();
        LogUtil.e("使用json解析json数据成功-title=="+title);

//        给左侧菜单传递数据（标题）
        data = bean.getData();
//        得到了上下文
        MainActivity mainActivity = (MainActivity) context;
        //根据上下文得到左侧菜单
        LeftMenuFragment leftMenuFragment = mainActivity.getLeftMenuFragment();
        //添加详情页面
        detailBasePagers = new ArrayList<>();
        detailBasePagers.add(new NewsMenuDetailPager(context));//新闻详情页面
//        detailBasePagers.add(new TopicMenuDetailPager(context,data.get(0)));//专题详情页面
//        detailBasePagers.add(new PhotosMenuDetailPager(context,data.get(2)));//组图详情页面
//        detailBasePagers.add(new InteracMenuDetailPager(context));//互动详情页面
        //把数据传递给左侧菜单
        leftMenuFragment.setData(data);

    }

    /**
     * 解析json数据
     * 1.使用系统的api解析
     * 2.使用第三方的框架解析，例如Gson，fastJson
     * @param json
     * @return
     */

    private NewsCenterPagerBean parsedJson(String json) {
        //导入gson jar包！
        Gson gson = new Gson();
        //将获取到的json数据放入NewCenterPagerBean中进行适配，返回一个带有网络数据的json对象回来
        NewsCenterPagerBean bean = gson.fromJson(json,NewsCenterPagerBean.class);
        return bean;
    }

    /**
     * 根据位置切换详情页面
     * @param position
     */
    public void switchPager(int position) {
        //1.设置标题
        tv_title.setText(data.get(position).getTitle());
        //2.移除之前的内容
        fl_content.removeAllViews();
        //3.添加新的内容
        MenuDetailBasePager detailBasePager = detailBasePagers.get(position);
        View rootView = detailBasePager.rootView;
        detailBasePager.initData();//初始化数据
        fl_content.addView(rootView);

        if (position==2){
            //图组详情页面,显示按钮
            ib_switch_list_grid.setVisibility(View.VISIBLE);
        }else {
            //其他页面
            ib_switch_list_grid.setVisibility(View.GONE);
        }
    }
}
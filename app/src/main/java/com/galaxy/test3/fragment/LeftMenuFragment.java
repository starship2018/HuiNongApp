package com.galaxy.test3.fragment;

import android.arch.lifecycle.Lifecycle;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.galaxy.test3.R;
import com.galaxy.test3.activity.MainActivity;
import com.galaxy.test3.base.BaseFragment;
import com.galaxy.test3.domain.NewsCenterPagerBean;
import com.galaxy.test3.pager.NewsPager;
import com.galaxy.test3.utils.DensityUtil;
import com.galaxy.test3.utils.LogUtil;

import java.util.List;

public class LeftMenuFragment extends BaseFragment {

    //因为待会儿要把这个data显示出来，所以要先定义一下！
    private List<NewsCenterPagerBean.DataBean> data;
    private LeftmenuFragmentAdapter adapter;
    private ListView listView;
    /**
     * 记录点击的位置，为了给适配器进行调用
     */
    private int prePosition;

    //抽象类方法，一定要重写
    //外表必须有!


    @Override
    public View initView() {
        LogUtil.e("重新实现父类的initView方法！");
        listView = new ListView(context);
        listView.setPadding(0, DensityUtil.dip2px(context,40),0,0);
        listView.setDividerHeight(0);//设置分割线高度为0
        listView.setCacheColorHint(Color.TRANSPARENT);
        listView.setSelector(android.R.color.transparent);

        LogUtil.e("初始化在监听器前完成!");

        //点击事件设置在这里，而不是initData()中！
        //设置按下listView的item不变色
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             *
             * @param parent
             * @param view
             * @param position
             * @param id
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //1.记录点击的位置，以变成红色
                prePosition = position;
                adapter.notifyDataSetChanged();//刷新适配器
                //2.关闭左侧菜单
                MainActivity mainActivity = (MainActivity) context;
                mainActivity.getSlidingMenu().toggle();//开关一次！
                //3.切换到对应的详细页面：新闻，专题，图组，互动
//                switchPager(prePosition);
                //4.
            }

        });
        //同时我们的点击事件也是写在这里的！
        return listView;
    }

    private void switchPager(int position) {
        MainActivity mainActivity = (MainActivity) context;
        ContentFragment contentFragment = mainActivity.getContentFragment();
        NewsPager newsPager = contentFragment.getNewsCenterPager();
        newsPager.switchPager(position);
    }

    //可以实现，也可以不实现
    //内在可以考虑
    @Override
    public void initData() {
        super.initData();
        LogUtil.e("左侧菜单被初始化了！");
    }

    /**
     * 接受数据
     * @param data
     */
    public void setData(List<NewsCenterPagerBean.DataBean> data) {
        this.data = data;
        for (int i = 0; i < data.size(); i++) {
            LogUtil.e("title=="+data.get(i).getTitle());
        }

        //本来适配器的设置是在上面的initView()中实现的！这里我们将它改在了这里!
        LogUtil.e("初始化在实例化适配器前完成!");
        adapter = new LeftmenuFragmentAdapter();
        //设置适配器
//        listView = (ListView) initView();
        if (listView==null){
            LogUtil.e("listview为空！");

            //自己结束了函数的执行！
            return;
        }
        listView.setAdapter(adapter);
        //设置默认界面
        switchPager(prePosition);


    }

    class LeftmenuFragmentAdapter extends BaseAdapter{
        //适配器必须的实现方法！得到数据的数目，这里决定了下面的getView()会执行多少次!
        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
        //适配器必须的实现方法！适配器如何调用视图来显示！!针对上面的getCount()，执行这么多次getView()!
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //对布局文件生成相应的视图View！
            TextView textView = (TextView) View.inflate(context, R.layout.item_leftmenu,null);
            //根据数据更改文字内容！
            textView.setText(data.get(position).getTitle());
            //若当前位置id和被点击记录的id是一样的!设置红色
            textView.setEnabled(position==prePosition);
            return textView;
        }
    }
}
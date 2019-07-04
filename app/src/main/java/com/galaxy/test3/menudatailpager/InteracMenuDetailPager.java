package com.galaxy.test3.menudatailpager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.galaxy.test3.base.MenuDetailBasePager;
import com.galaxy.test3.utils.LogUtil;

public class InteracMenuDetailPager extends MenuDetailBasePager {
    private TextView textView;

    public InteracMenuDetailPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.BLUE);
        textView.setTextSize(25);
        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("互动详情页面内容被初始化了！");
        textView.setText("互动详情页面内容！");
    }
}

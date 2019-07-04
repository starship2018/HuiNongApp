package com.galaxy.test3.pager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.galaxy.test3.R;
import com.galaxy.test3.activity.AboutActivity;
import com.galaxy.test3.activity.DataAllActivity;
import com.galaxy.test3.activity.LoginAndRegisterActivity;
import com.galaxy.test3.activity.UserListActivity;
import com.galaxy.test3.base.BasePager;
import com.galaxy.test3.utils.CacheUtils;
import com.galaxy.test3.utils.DBUtils;
import com.galaxy.test3.utils.LogUtil;

import static android.support.v4.content.ContextCompat.startActivity;

public class DataPager extends BasePager {


    private View block_all;

    private View block_catagories;

    private View block_market;

    private View block_expert;

    private View block_ask;

    private View block_sanpin;

    private View block_laws;

    private View block_read;

    private View block_class;

    public DataPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        //1.设置标题
        tv_title.setText("农科中心");
        //创建出新的View
        final View userView = View.inflate(context, R.layout.datacenter, null);
        fl_content.addView(userView);

        //初始化各个模块
        block_all = userView.findViewById(R.id.data_all);
        block_ask = userView.findViewById(R.id.data_ask);
        block_catagories = userView.findViewById(R.id.data_categories);

        block_class = userView.findViewById(R.id.data_class);
        block_expert = userView.findViewById(R.id.data_expert);
        block_laws = userView.findViewById(R.id.data_law);

        block_market = userView.findViewById(R.id.data_market);
        block_read = userView.findViewById(R.id.data_read);
        block_sanpin = userView.findViewById(R.id.data_sanpin);


        block_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("title","农业统计");
                bundle.putString("url","http://222.90.83.241:9001/Userportal/dispatcher/agriculture/home");
                Intent intent = new Intent(context,DataAllActivity.class);
                intent.putExtras(bundle);
                startActivity(context,intent,Bundle.EMPTY);
            }
        });

        block_catagories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("title","农业品种");
                bundle.putString("url","http://222.90.83.241:9001/Userportal/dispatcher/seed/seedList.page");
                Intent intent = new Intent(context,DataAllActivity.class);
                intent.putExtras(bundle);
                startActivity(context,intent,Bundle.EMPTY);
            }
        });

        block_market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("title","市场行情");
                bundle.putString("url","http://sxs_jgt.54.site.veeteam.com/index.php/home/day");
                Intent intent = new Intent(context,DataAllActivity.class);
                intent.putExtras(bundle);
                startActivity(context,intent,Bundle.EMPTY);
            }
        });

        block_expert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("title","农业专家");
                bundle.putString("url","http://222.90.83.241:9001/portal/dispatcher/portalpage/portalList");
                Intent intent = new Intent(context,DataAllActivity.class);
                intent.putExtras(bundle);
                startActivity(context,intent,Bundle.EMPTY);
            }
        });

        block_ask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("title","供求信息");
                bundle.putString("url","http://222.90.83.241/");
                Intent intent = new Intent(context,DataAllActivity.class);
                intent.putExtras(bundle);
                startActivity(context,intent,Bundle.EMPTY);
            }
        });

        block_sanpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("title","三品一标");
                bundle.putString("url","http://222.90.83.241:9001/agriresources/?source=origin");
                Intent intent = new Intent(context,DataAllActivity.class);
                intent.putExtras(bundle);
                startActivity(context,intent,Bundle.EMPTY);
            }
        });

        block_laws.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("title","法律法规");
                bundle.putString("url","http://nyt.shaanxi.gov.cn/www/snflfg1196/");
                Intent intent = new Intent(context,DataAllActivity.class);
                intent.putExtras(bundle);
                startActivity(context,intent,Bundle.EMPTY);
            }
        });

        block_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("title","政策解读");
                bundle.putString("url","http://nyt.shaanxi.gov.cn/www/zcjjd6326/");
                Intent intent = new Intent(context,DataAllActivity.class);
                intent.putExtras(bundle);
                startActivity(context,intent,Bundle.EMPTY);
            }
        });

        block_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("title","法治讲堂");
                bundle.putString("url","http://nyt.shaanxi.gov.cn/www/fzdjt/");
                Intent intent = new Intent(context,DataAllActivity.class);
                intent.putExtras(bundle);
                startActivity(context,intent,Bundle.EMPTY);
            }
        });

    }
}

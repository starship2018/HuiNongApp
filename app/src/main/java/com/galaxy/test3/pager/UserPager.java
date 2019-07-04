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
import com.galaxy.test3.activity.LoginAndRegisterActivity;
import com.galaxy.test3.activity.MainActivity;
import com.galaxy.test3.activity.UserListActivity;
import com.galaxy.test3.base.BasePager;
import com.galaxy.test3.utils.CacheUtils;
import com.galaxy.test3.utils.DBUtils;
import com.galaxy.test3.utils.LogUtil;
import com.galaxy.test3.utils.User;

import static android.support.v4.content.ContextCompat.startActivity;

public class UserPager extends BasePager {

    public UserPager(Context context) {
        super(context);
    }


    public TextView username;

    public View about;

    public View query;

    public Button quit;



    @Override
    public void initData() {
        super.initData();
        //1.设置标题
        tv_title.setText("个人中心");
        //创建出新的View
        final View userView = View.inflate(context, R.layout.usercenter,null);
        fl_content.addView(userView);

        username = userView.findViewById(R.id.tv_username);
        about = userView.findViewById(R.id.v_about);
        query = userView.findViewById(R.id.v_query);
        quit = userView.findViewById(R.id.btn_quit_login);

        //取出当前登陆的用户，并进行欢迎
        String now_login_user = CacheUtils.getString(context,"who_logined");
        username.setText(now_login_user);


        //关于界面的显示
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //注意！所有的跳转都是需要在子线程中进行的！
                new Thread(new Runnable(){
                    public void run(){
                        //经过测试，这个跳转方法时完全可行的！
                        startActivity(context,new Intent(context, AboutActivity.class), Bundle.EMPTY);
                        }
                }).start();
            }
        });

        //显示当前用户数目和username


        //退出登陆
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //写入缓存，此时没有用户登陆
                CacheUtils.putBoolean(context,"is_logined",false);
                //经过测试，这个跳转方法时完全可行的！
                startActivity(context,new Intent(context, LoginAndRegisterActivity.class), Bundle.EMPTY);
                //结束掉mainactivity
                ((Activity)context).finish();


            }
        });


        //设置查询功能，使用jdbc查询当前用户的权限，若为管理员，则进行跳转，若不是，则警告劝退
        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取出当前登陆用户的用户名
                final String name = CacheUtils.getString(context,"who_logined");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        switch (DBUtils.query_is_admin(name)){
                            case 1:startActivity(context,new Intent(context, UserListActivity.class),Bundle.EMPTY);
                            break;
                            case 0:
                                Looper.prepare();
                                LogUtil.e("您不是系统管理员，无权访问！");
                                Toast.makeText(context,"您不是系统管理员，无权访问！",Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            break;
                        }
                    }
                }).start();
            }
        });
    }

}

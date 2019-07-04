package com.galaxy.test3;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.galaxy.test3.activity.GuideActivity;
import com.galaxy.test3.activity.LoginAndRegisterActivity;
import com.galaxy.test3.activity.MainActivity;
import com.galaxy.test3.utils.CacheUtils;

public class SplashActivity extends AppCompatActivity {

    public static final String START_MAIN = "start_main";
    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            next();
        }
    };
    //声明要跳转的类
    private Class<?> intent;

    private void next(){
         startActivity(new Intent(SplashActivity.this,intent));
         finish();
     }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //判断是否进入过主界面
        Boolean isStartMain = CacheUtils.getBoolean(SplashActivity.this, START_MAIN);
        //判断是否曾经登陆过
        Boolean isLogin = CacheUtils.getBoolean(SplashActivity.this,"is_logined");
        if (isStartMain){
            if (!isLogin){
                //如果进入过主界面，而且没有登陆成功过，进入登陆界面
                intent = LoginAndRegisterActivity.class;
            }else {
                //如果进入过主界面，而且成功登陆过，进入主界面
                intent = MainActivity.class;
            }
        }else {
            //若没有，则先进入引导页，再进入主界面
            intent = GuideActivity.class;
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        },2500);
        //然后记住要关闭Splash界面

    }
}

package com.galaxy.test3.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;

import android.widget.Toast;

import com.galaxy.test3.R;

import com.galaxy.test3.utils.DBUtils;
import com.galaxy.test3.utils.LogUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init(){
        EditText input_username = findViewById(R.id.et_userName);
        EditText input_psw = findViewById(R.id.et_password);
        Button btn_login = findViewById(R.id.btn_login);


        final String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";



        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取到新的手机号
                String phonenum = input_username.getText().toString().trim();
                //获取密码值
                String pwd = input_psw.getText().toString().trim();
                LogUtil.e(phonenum+"+++++"+pwd);
                LogUtil.e("获取到的手机号的长度为"+phonenum.length());
                if (phonenum.length() != 11) {
                    Toast.makeText(RegisterActivity.this,"手机号应为11位数",Toast.LENGTH_SHORT).show();
                    return;
                }else if (pwd.length()<=5){
                    Toast.makeText(RegisterActivity.this,"密码至少为6位！",Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    Pattern p = Pattern.compile(regex);
                    Matcher m = p.matcher(phonenum);
                    boolean isMatch = m.matches();

                    if (!isMatch) {
                        Toast.makeText(RegisterActivity.this,"请填入正确的手机号",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    new Thread(new Runnable(){
                        public void run(){
                            //在子线程中调用Toast必须要执行的方法！
                            Looper.prepare();
                            if (DBUtils.register(phonenum,pwd)){
                                Toast.makeText(RegisterActivity.this,"注册成功！即将返回登陆界面！",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this,LoginAndRegisterActivity.class));
                                finish();
                            }else {
                                Toast.makeText(RegisterActivity.this,"当前手机号已注册或网络错误！",Toast.LENGTH_SHORT).show();
                            }
                            Looper.loop();
                        }
                    }).start();
                }
            }
        });
    }


}

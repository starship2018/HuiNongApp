package com.galaxy.test3.activity;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.galaxy.test3.R;
import com.galaxy.test3.utils.CacheUtils;
import com.galaxy.test3.utils.DBUtils;
import com.galaxy.test3.utils.LogUtil;
import com.galaxy.test3.utils.User;

public class LoginAndRegisterActivity extends AppCompatActivity {

    boolean wantedRemenberPsw = false;

    private int login_result;
    private EditText userName;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_and_register);
        init();

    }

    /**
     * 双击退出
     */
    private long exitTime = 0;
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                //弹出提示，可以有多种方式
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void init(){
        userName = (EditText) findViewById(R.id.et_userName);
        password = (EditText) findViewById(R.id.et_password);
        ImageView unameClear = (ImageView) findViewById(R.id.iv_unameClear);
        ImageView pwdClear = (ImageView) findViewById(R.id.iv_pwdClear);
        final CheckBox rem_psw = findViewById(R.id.remember_psw);

        TextView register = findViewById(R.id.tv_register);

        Button btn_login = findViewById(R.id.btn_login);

        EditTextClearTools.addClearListener(userName,unameClear);
        EditTextClearTools.addClearListener(password,pwdClear);

        //记录密码
        rem_psw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    //记录密码(这里只是做一个记录，当核对密码成功的时候才真正的写入缓存)
                    wantedRemenberPsw = !wantedRemenberPsw;
                }
            }
        });

        //点击跳转注册页面！
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginAndRegisterActivity.this,RegisterActivity.class));

            }
        });


        //点击登陆
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取用户名
                String input_user = userName.getText().toString().trim();
                LogUtil.e("接受到的username是"+input_user);
                //获取密码
                String input_password = password.getText().toString().trim();
                LogUtil.e("接受到的psw是"+input_password);

                if (input_user.isEmpty()){
                    Toast.makeText(LoginAndRegisterActivity.this,"用户名不能为空！",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (input_password.isEmpty()){
                    Toast.makeText(LoginAndRegisterActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }




                User user = new User();
                user.setName(input_user);
                user.setPwd(input_password);

                new Thread(new Runnable(){
                    public void run(){
                        login_result = DBUtils.login(user);
                        switch (login_result){
                            case 0:
                                Looper.prepare();
                                Toast.makeText(LoginAndRegisterActivity.this,"用户名或密码错误！",Toast.LENGTH_SHORT).show();
                                Looper.loop();
                                break;
                            case 1:
                                //登陆过后进行一下记录！
                                CacheUtils.putBoolean(LoginAndRegisterActivity.this,"is_logined",true);
                                //本地缓存用户名和密码！
                                CacheUtils.putString(LoginAndRegisterActivity.this,input_user,input_password);
                                //记住当前已经登陆的用户是谁
                                CacheUtils.putString(LoginAndRegisterActivity.this,"who_logined",input_user);
                                //缓存该用户是否启用了自动记住密码服务！
                                if (wantedRemenberPsw){
                                    CacheUtils.putBoolean(LoginAndRegisterActivity.this,input_user,true);
                                }else {
                                    CacheUtils.putBoolean(LoginAndRegisterActivity.this,input_user,false);
                                }

                                startActivity(new Intent(LoginAndRegisterActivity.this,MainActivity.class));
                                finish();
                                break;
                        }
                    }
                }).start();


            }
        });

        //当出现熟悉的用户名时，自动填写密码，以及记住密码栏打勾

        userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                LogUtil.e("输入框发生了变化！");
                try {

                    //对于这种无法解决的问题，直接使用try catch 来跳过问题！
                    CacheUtils.getBoolean(LoginAndRegisterActivity.this,userName.getText().toString());
                }catch (Exception e){
                    String remembered_psw = CacheUtils.getString(LoginAndRegisterActivity.this,userName.getText().toString());
                    rem_psw.setChecked(true);
                    password.setText(remembered_psw);
                }
            }

        });
    }
}



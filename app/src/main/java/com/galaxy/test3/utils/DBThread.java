package com.galaxy.test3.utils;

import android.content.Context;
import android.content.Intent;

import java.util.Map;

class DBThread implements Runnable {
    private User user;
    private Context context;

    public void setUser(User user) {
        this.user = user;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
//        Map<String,String> result= DBUtils.login(user);
//        if (result != null && result.size() > 0) {
//            Intent intent=new Intent(this,infoActivity.class);
//            intent.putExtra("user",u);
//            context.startActivity(intent);
//        }
    }
}

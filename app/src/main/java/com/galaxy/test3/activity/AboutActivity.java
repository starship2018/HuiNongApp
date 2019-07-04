package com.galaxy.test3.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.galaxy.test3.R;

public class AboutActivity extends AppCompatActivity {

    public TextView title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);


        title = findViewById(R.id.tv_title);
        title.setText("关于我们");
    }
}

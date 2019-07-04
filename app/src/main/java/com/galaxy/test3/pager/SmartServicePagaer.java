package com.galaxy.test3.pager;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.galaxy.test3.R;
import com.galaxy.test3.base.BasePager;

import org.xutils.view.annotation.ViewInject;

import java.text.DecimalFormat;

public class SmartServicePagaer extends BasePager {

    private TextView out_fen;

    private TextView out_dao;

    private TextView out_water;

    public SmartServicePagaer(Context context) {
        super(context);
    }

    private boolean IsInputDao = false;

    @Override
    public void initData() {
        super.initData();
        //1.设置标题
        tv_title.setText("惠农生活");
        //创建出新的View
        final View calView = View.inflate(context,R.layout.calview,null);
        fl_content.addView(calView);
        RadioGroup radioGroup = calView.findViewById(R.id.radioGroup);
        EditText editText = (EditText) calView.findViewById(R.id.inputView);

        out_fen = calView.findViewById(R.id.out_fen);
        out_dao = calView.findViewById(R.id.out_dao);
        out_water = calView.findViewById(R.id.out_water);
        //给复选框设定点击事件
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                RadioButton radioButton = calView.findViewById(checkedId);
//                Toast.makeText(context,radioButton.getText(),Toast.LENGTH_SHORT).show();
                IsInputDao = !IsInputDao;
                editText.setText("");
            }
        });
        //给输入框写入事件
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //输入的长度最长为4位数，不能达到10000
                if (s.length()>=5){
                    Toast.makeText(context,"输入的最大值是9999！请重新输入！",Toast.LENGTH_SHORT).show();
                    editText.setText("");
                    return;
                }
                String input = s.toString();
                if (!input.isEmpty()){
                    ShowValue(input);
                }else {
                    ShowValue("0");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void ShowValue(String input) {
        float num = Float.parseFloat(input);
        java.text.DecimalFormat df = new DecimalFormat("#.00");
        if (IsInputDao){
            df.format(0.232);
            out_fen.setText(df.format((float) (num/1.13)));
            out_dao.setText(df.format(num));
            out_water.setText(df.format((float)(num*1.48/1.13)));
        }else {
            out_fen.setText(df.format(num));
            out_dao.setText(df.format((float) (num*1.13)));
            out_water.setText(df.format((float)(num*1.48)));
        }
    }


}

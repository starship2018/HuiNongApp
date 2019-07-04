package com.galaxy.test3.activity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.galaxy.test3.R;
import com.galaxy.test3.utils.DBUtils;
import com.galaxy.test3.utils.LogUtil;
import com.galaxy.test3.utils.User;

import java.util.ArrayList;

public class UserListActivity extends AppCompatActivity {


    public ListView result_list;

    public TextView title;

    private ArrayList<User> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        list = new ArrayList<>();

        title = findViewById(R.id.tv_title);
//        title.setText("用户列表");
        result_list = findViewById(R.id.query_result);

        //获取数据
        LogUtil.e("开始查询所有的用户！");



        //在子线程中进行联网请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                list = DBUtils.get_user_list();
                Message message = new Message();
                message.what=1;
                handler.sendMessage(message);
                LogUtil.e("我已经拿到"+list.size()+"条数据");
            }
        }).start();
        LogUtil.e("在主线程中得到的list的大小是"+list.size());
        LogUtil.e("开始设置适配器！");
//        result_list.setAdapter(new MyAdapter(this,list));

    }

    class MyAdapter extends BaseAdapter{
        private LayoutInflater mInflater;
        private ArrayList<User> mDatas;


        public MyAdapter(Context context, ArrayList<User> list){
            mInflater = LayoutInflater.from(context);
            mDatas = list;
        }

        public int getCount() {
            return mDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
//            User u = list.get(position);

            if (convertView==null){
                holder = new ViewHolder();
//                convertView = LayoutInflater.from(UserListActivity.this).inflate(R.layout.item_querylist,null);
                convertView = mInflater.inflate(R.layout.item_querylist,parent,false);
                //得到各个控件的对象
                holder.iv_number = convertView.findViewById(R.id.iv_num);
                holder.iv_name = convertView.findViewById(R.id.iv_username);

                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }

            //设置显示的具体内容
            User u = mDatas.get(position);
            holder.iv_number.setText(u.getId()+"");
            holder.iv_name.setText(u.getName());


            return convertView;
        }

        public final class ViewHolder{
            public TextView iv_number;
            public TextView iv_name;
        }
    }

    //核心步骤
    /**
     * 在子线程中获取到了联网数据，但是在子线程中并不能设置适配器，因为适配器涉及到了UI的改动，所以要转交给主线程来执行任务！
     *
     * 这个handle的意思，个人感觉就是一个处理器，自己在子线程中进行sendMessage的操作，在定义的时候，在handleMessage中定义好了处理
     * message的操作，而且这个操作是由主线程完成的！有意思!
     */
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    //设置适配器
                    result_list.setAdapter(new MyAdapter(UserListActivity.this,list));
                    title.setText("用户列表("+list.size()+"人)");
                    break;
                    default:
                        break;
            }
        }
    };
}

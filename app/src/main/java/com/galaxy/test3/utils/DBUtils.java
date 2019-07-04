package com.galaxy.test3.utils;






import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class DBUtils {

        private static String driver = "com.mysql.jdbc.Driver";//MySQL 驱动
        private static String url = "jdbc:mysql://152.136.158.112:3306/galaxy";//MYSQL数据库连接Url
        private static String user = "root";//用户名
        private static String password = "Aleo10086:";//密码

        private static Connection getConnection() {
            Connection conn = null;
            try {
                Class.forName(driver); //
                conn = DriverManager.getConnection(url,user, password);
            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
            return conn;
        }


        public static int login(User user) {
            Connection conn = getConnection();
            try {
                Statement st = conn.createStatement();
                String sql= "select * from userinfo where username ='" + user.getName()
                        + "' and password ='" + user.getPwd() + "'";
                ResultSet res = st.executeQuery(sql);
                if (res.next()){
                    conn.close();
                    st.close();
                    res.close();
                    LogUtil.e("数据库连接成功！用户名和密码验证成功！");
                    return 1;
                } else {
                    conn.close();
                    st.close();
                    res.close();
                    LogUtil.e("数据库连接成功！用户名和密码验证失败！");
                    return 0;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        }


    public static Boolean register(String username, String password) {
        Connection conn = getConnection();
        try {
            Statement st = conn.createStatement();
            String sql = "insert into userinfo(username,password) values('"+ username+ "','"+ password+ "') ";
            int res = st.executeUpdate(sql);
            if (res == 1) {
                LogUtil.e("注册mysql成功！");
                conn.close();
                st.close();
                return true;
            } else {
                LogUtil.e("注册mysql失败！");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("注册出现异常错误!");
            return false;
        }
    }

    public static int query_is_admin(String username) {
        Connection conn = getConnection();
        try {
            Statement st = conn.createStatement();
            String sql = "select * from userinfo where username='"+username+"';";

            ResultSet res = st.executeQuery(sql);
            while (res.next()){
                int isadmin = res.getInt("isadmin");
                conn.close();
                st.close();
                res.close();
                return isadmin;
            }
        } catch (Exception e) {
            LogUtil.e("发生了异常！");
            return 0;
        }
        return 0;
    }

    public static ArrayList<User> get_user_list (){

            ArrayList<User> list = new ArrayList<>();
            LogUtil.e("建立连接之前");
            Connection conn = getConnection();
            try{
                LogUtil.e("在创建实力前出状况。。。");
                Statement st = conn.createStatement();
                LogUtil.e("在执行语句之前出现了状况。。。");

                String sql = "select * from userinfo;";
                ResultSet res = st.executeQuery(sql);
                while (res.next()){
                    int id = res.getInt("id");
                    String name = res.getString("username");
                    User u = new User();
                    u.setId(id);
                    u.setName(name);
                    list.add(u);
                    LogUtil.e("我已成功拿到了数据！");
                }
                conn.close();
                st.close();
                res.close();
                return list;

            }catch (Exception e){

                LogUtil.e("查询出现异常，返回空的LIst");
                return list;
            }
    }

}

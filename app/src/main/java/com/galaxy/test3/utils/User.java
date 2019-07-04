package com.galaxy.test3.utils;

public class User {
    public int id;
    public String name;
    public String pwd;
    public int isAdmin;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIsAdmin() {
        return isAdmin;
    }

    public String getName() {
        return name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

}

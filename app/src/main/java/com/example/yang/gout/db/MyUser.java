package com.example.yang.gout.db;

import java.util.List;

import cn.bmob.v3.BmobUser;

public class MyUser extends BmobUser {
    private String address;
    private String email;
    private String  mobile;

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }
}

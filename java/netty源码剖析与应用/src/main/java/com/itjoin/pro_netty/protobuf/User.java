package com.itjoin.pro_netty.protobuf;

import com.dyuproject.protostuff.Tag;

import java.util.List;

public class User {
    @Tag(4)
    private String userName;
    @Tag(2)
    private int age;
    @Tag(3)
    private String hobbyhobby;
    @Tag(1)
    private Product p;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getHobbyhobby() {
        return hobbyhobby;
    }

    public void setHobbyhobby(String hobbyhobby) {
        this.hobbyhobby = hobbyhobby;
    }

    public Product getP() {
        return p;
    }

    public void setP(Product p) {
        this.p = p;
    }
}

package com.itjoin.pro_netty.protobuf;

import com.dyuproject.protostuff.Tag;

public class Book {

    @Tag(1)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

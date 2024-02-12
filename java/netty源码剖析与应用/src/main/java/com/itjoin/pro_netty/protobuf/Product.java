package com.itjoin.pro_netty.protobuf;

import com.dyuproject.protostuff.Tag;

public class Product {
    @Tag(1)
    private String id;

    @Tag(2)
    private Book b;

    public Book getB() {
        return b;
    }

    public void setB(Book b) {
        this.b = b;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

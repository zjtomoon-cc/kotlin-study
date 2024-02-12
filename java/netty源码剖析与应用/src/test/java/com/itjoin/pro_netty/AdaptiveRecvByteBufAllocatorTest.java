package com.itjoin.pro_netty;

import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.RecvByteBufAllocator;
import org.junit.Test;

public class AdaptiveRecvByteBufAllocatorTest {

    @Test
    public void guessTest(){
        AdaptiveRecvByteBufAllocator alloctor = new AdaptiveRecvByteBufAllocator();
        RecvByteBufAllocator.Handle handle =  alloctor.newHandle();
        System.out.println("------------开始IO读事件模拟----------------------------");
        // 读取循环开始前先重置，将读取的次数和字节数设置为0
        //totalMessages与totalBytesRead置0;
        handle.reset(null);
        System.out.println(String.format("第1次模拟读,需要分配的大小：%d", handle.guess()));
        handle.lastBytesRead(256);
        //调整下次预测值
        handle.readComplete();
        //在每次读数据时需要重置totalMessages与totalBytesRead
        handle.reset(null);
        System.out.println(String.format("第2次模拟读：需要分配的大小：%d", handle.guess()));// 读循环中缓冲大小不变
        handle.lastBytesRead(256);
        handle.readComplete();
        System.out.println("-----------连续2次读取的字节数小于默认分配的字节数--------------------------");
        handle.reset(null);
        System.out.println(String.format("第3次模拟读：需要分配的大小：%d", handle.guess()));// 读循环中缓冲大小变小
        handle.lastBytesRead(512);
        //调整下次预测值,预测值应该增加到512*2^4
        handle.readComplete();
        System.out.println("-----------读取的字节数变大--------------------------");
        handle.reset(null);
        System.out.println(String.format("第4次模拟读：需要分配的大小：%d", handle.guess()));// 读循环中缓冲大小变大
    }
}

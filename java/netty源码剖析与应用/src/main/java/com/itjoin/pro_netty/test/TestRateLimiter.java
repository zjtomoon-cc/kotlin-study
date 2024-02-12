package com.itjoin.pro_netty.test;

import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.TimeUnit;

public class TestRateLimiter {
    static RateLimiter rateLimiter = null;
    static
    {
        rateLimiter = RateLimiter.create(3.0);
    }
    public static void main(String[] args) {
        double s = rateLimiter.acquire(1);
        System.out.println("==="+s);
        s = rateLimiter.acquire(1);
        System.out.println("==="+s);
        s = rateLimiter.acquire(1);
        System.out.println("==="+s);
        s = rateLimiter.acquire(1);
        System.out.println("==="+s);
        long start = System.currentTimeMillis();
        s = rateLimiter.acquire(1);
        long end = System.currentTimeMillis();
        System.out.println("耗时=="+(end-start));
        System.out.println("==="+s);

    }
}

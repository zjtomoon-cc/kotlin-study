package com.itjoin.pro_netty;

import com.google.common.util.concurrent.RateLimiter;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class RateLimiterTest {

    static RateLimiter rateLimiter = null;
    static
    {
        rateLimiter = RateLimiter.create(5);
    }
    @Test
    public  void test01() {
        double hostTime = rateLimiter.acquire(1);
        System.out.println(hostTime);
        boolean hasAcquired = rateLimiter.tryAcquire(1, TimeUnit.SECONDS);
        System.out.println(hasAcquired);
    }
}

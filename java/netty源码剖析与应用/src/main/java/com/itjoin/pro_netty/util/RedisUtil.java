package com.itjoin.pro_netty.util;

import redis.clients.jedis.Jedis;

public class RedisUtil {

    public static Jedis jedis;
    static{
        //此处redis地址需要写到配置文件server.properties中
        jedis= new Jedis("10.203.236.18");
    }

    public static int getInt(String key){
        String value = jedis.get(key);
        if(value!=null){
            return Integer.valueOf(jedis.get(key));
        }
       return 0;
    }

    public static void  setInt(String key,String value){
        jedis.set(key,value);
    }
}

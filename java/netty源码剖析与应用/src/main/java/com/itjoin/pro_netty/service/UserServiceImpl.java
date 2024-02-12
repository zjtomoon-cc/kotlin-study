package com.itjoin.pro_netty.service;
import com.itjoin.pro_netty.annotation.Remote;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Remote
public class UserServiceImpl implements UserService{
    @Override
    public Object getUserByName(String userName) {
        System.out.println("userName==="+userName);
        return "服务器响应ok============";
    }


    public static Map<String,Integer> products = new ConcurrentHashMap<>();
    static{
        products.put("1",Integer.MAX_VALUE);
    }
    /**
     * 秒杀活动测试
     * @param productId
     * @return
     */
    @Override
    public Object testSecondSell(String productId) {
        //此处需改成redis分布式锁，由于资源有限，只采用了jvm锁
        synchronized (products){
            //此处库存也放在本地缓存，正常秒杀应该放入redis中
            Integer productVal = products.get(productId);
            if(productVal>0){
//                System.out.println("ok");
                products.put(productId,productVal-1);
                return "购买成功";
            }
        }
        return "购买失败";
    }
}

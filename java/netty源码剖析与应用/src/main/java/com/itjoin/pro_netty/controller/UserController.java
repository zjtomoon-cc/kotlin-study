package com.itjoin.pro_netty.controller;
import com.itjoin.pro_netty.annotation.Remote;
import org.springframework.stereotype.Controller;
@Controller
@Remote
public class UserController implements UserControllerI{
    //此方法用于远程调用，需要加上Remote注解
    @Remote("getUserNameById")
    public Object getUserNameById(String userId){
        //此处并未访问数据库，只是做简单输出
        System.out.println("客户端请求的用户id为======"+userId);
        return "响应结果===用户张三"+userId;
    }
}

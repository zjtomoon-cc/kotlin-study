package com.itjoin.pro_netty.controller;
import com.itjoin.pro_netty.annotation.RemoteInvoke;
import com.itjoin.pro_netty.service.UserService;
import org.springframework.stereotype.Component;
@Component
public class LoginController {
    //远程服务注解
    @RemoteInvoke
    private UserService userService;
    //通过用户名称调用用户服务获取用户信息
    public Object getUserByName(String userName){
        return userService.getUserByName(userName);
    }

    public Object testSecondSell(String productId){
        return userService.testSecondSell(productId);
    }
}

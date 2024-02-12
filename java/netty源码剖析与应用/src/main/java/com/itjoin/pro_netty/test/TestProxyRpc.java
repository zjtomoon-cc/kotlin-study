package com.itjoin.pro_netty.test;
import com.itjoin.pro_netty.controller.LoginController;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
public class TestProxyRpc {
    public static void main(String[] args) throws InterruptedException {
        //注意，此处扫描包名，不能包含服务器的启动类所在的包
        //只能包含具体测试类，以及代理类所在的包
        AnnotationConfigApplicationContext context = new
                AnnotationConfigApplicationContext(
                new String[]{"com.itjoin.pro_netty.controller",
                        "com.itjoin.pro_netty.proxy"});
        LoginController loginController= context.
                getBean(LoginController.class);
        Object result  = loginController.getUserByName("张三");
        System.out.println(result);
        Thread.sleep(100000000);
    }
}

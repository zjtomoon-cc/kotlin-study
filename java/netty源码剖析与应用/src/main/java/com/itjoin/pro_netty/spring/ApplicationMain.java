package com.itjoin.pro_netty.spring;
import org.springframework.context.annotation.
        AnnotationConfigApplicationContext;
public class ApplicationMain {
    private static volatile boolean running = true;
    public static void main(String[] args) {
        try {
            //此处扫描包名，不能包含代理类的包名
            //只需要包含service，监听器的包名即可
            AnnotationConfigApplicationContext context =
                    new AnnotationConfigApplicationContext(
                            "com.itjoin.pro_netty.controller",
                            "com.itjoin.pro_netty.spring"
                    ,"com.itjoin.pro_netty.service");
            //在jvm中增加一个关闭的钩子,当jvm关闭的时候，
            // 会执行系统中已经设置的所有通过方法addShutdownHook
            // 添加的钩子，当系统执行完这些钩子后，jvm才会关闭。
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    try {
                        context.stop();
                    } catch (Throwable t) {
                    }
                    synchronized (ApplicationMain.class) {
                        running = false;
                        ApplicationMain.class.notify();
                    }
                }
            });
            context.start();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);

        }
        System.out.println("服务器已启动====");
        synchronized (ApplicationMain.class) {
            while (running) {
                try {
                    ApplicationMain.class.wait();
                } catch (Throwable e) {
                }
            }
        }
    }
}

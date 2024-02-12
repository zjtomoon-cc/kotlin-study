package com.itjoin.pro_netty.spring;
import com.itjoin.pro_netty.annotation.Remote;
import com.itjoin.pro_netty.core.Mediator;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;
import java.util.Map;
/**
 * Spring容器初始化后,把带有@Remote的方法与其对象加载到缓存中
 * Ordered接口主要是通过getOrder()返回值来决定监听器运行的顺序
 * getOrder()返回值越小运行顺序越靠前,此处为-1
 * 是由于初始化Remote方法,需要在Netty服务器启动之前
 */
@Component
public class InitLoadRemoteMethod implements ApplicationListener<ContextRefreshedEvent> , Ordered {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        //从Spring容器中获取标有Remote注解的对象
        Map<String, Object> controllerBeans =
                contextRefreshedEvent.getApplicationContext()
                        .getBeansWithAnnotation(Remote.class);
        //遍历所有的Controller
        for(String key : controllerBeans.keySet()){
            Object bean = controllerBeans.get(key);
            //通过反射获取Remote的所有方法
            Method[] methods = bean.getClass().getDeclaredMethods();
            for (Method method : methods){
                    String methodVal = bean.getClass().
                            getInterfaces()[0].getName()
                            +"."+method.getName();
                    //把方法和bean放入包装类MethodBean中
                    Mediator.MethodBean methodBean =
                            new Mediator.MethodBean();
                    methodBean.setBean(bean);
                    methodBean.setMethod(method);
                    //最终把类名+方法名作为Key，方法+
                   // bean包装好作为value，放入到本地缓存中
                    Mediator.methodBeans.put(methodVal,methodBean);
            }
        }
    }
    @Override
    public int getOrder() {
        return -1;
    }
}

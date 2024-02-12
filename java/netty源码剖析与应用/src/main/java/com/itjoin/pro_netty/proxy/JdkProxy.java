package com.itjoin.pro_netty.proxy;

import com.alibaba.fastjson.JSONObject;
import com.itjoin.pro_netty.annotation.RemoteInvoke;
import com.itjoin.pro_netty.asyn.RequestFuture;
import com.itjoin.pro_netty.client.NettyClient;
import com.itjoin.pro_netty.zookeeper.ServerChangeWatcher;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
//@Component
public class JdkProxy implements InvocationHandler, BeanPostProcessor {
    private Field target;
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //采用netty客户端去需要去调用服务器
        RequestFuture request = new RequestFuture();
        //把接口类名+方法名组装成path
        request.setPath(target.getType().getName() + "." + method.getName());
        //设置参数,此处可以把参数改成数组
        request.setRequest(args[0]);
        //远程调用
        Object resp = NettyClient.sendRequest(request);
        Class returnType = method.getReturnType();
        if (resp == null) {
            return null;
        }
        //返回结果反序列化
        resp = JSONObject.parseObject(JSONObject.toJSONString(resp), returnType);
        return resp;
    }


    //定义获取代理对象方法
    private Object getJDKProxy(Field field) {
        this.target = field;
        //JDK动态代理只能针接口进行代理
        return Proxy.newProxyInstance(field.getType().getClassLoader(), new Class[]{field.getType()}, this);
    }

    /**
     * 在所有bean初始化完成之前，对bean中包含有RemoteInvoke注解的属性
     * ，做重新赋值
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(RemoteInvoke.class)) {
                field.setAccessible(true);
                try {
                    //对包含有RemoteInvoke注解的属性重新赋值
                    field.set(bean, getJDKProxy(field));
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }


}
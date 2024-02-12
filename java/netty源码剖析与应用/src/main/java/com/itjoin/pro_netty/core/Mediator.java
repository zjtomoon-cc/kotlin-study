package com.itjoin.pro_netty.core;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itjoin.pro_netty.asyn.RequestFuture;
import com.itjoin.pro_netty.asyn.Response;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 此类主要对Netty网络通信
 * 与业务处理逻辑类起到一个沟通的关联作用
 */
public class Mediator {
    public static Map<String, MethodBean> methodBeans;
    static{
        methodBeans = new HashMap<>();
    }
    /**
     * 请求分发处理
     * @param requestFuture
     * @return
     */
    public static Response process(RequestFuture requestFuture){
        //构建响应对象
        Response response = new Response();
        try {
            String path = requestFuture.getPath();
            //根据请求路径，从缓存中获取请求路径对应的bean和method
            MethodBean methodBean = methodBeans.get(path);
            if(methodBean!=null){
                Object bean = methodBean.getBean();
                Method method = methodBean.getMethod();
                //获取请求内容
                Object body = requestFuture.getRequest();
                //获取方法的请求参数类型，此处只支持一个参数，
                //想支持多个参数需要做响应的修改
                Class[] paramTypes = method.getParameterTypes();
                Class paramType = paramTypes[0];
                Object param = null;
                //假如参数是List类型
                if(paramType.isAssignableFrom(List.class)){
                    //采用JSONArray反序列化
                    param = JSONArray.
                            parseArray(JSONArray.toJSONString(body),paramType);
                }else if(paramType.getName().
                        equals(String.class.getName())){//假如是String类型
                    param = body;
                }else{
                    //采用JSONObject反序列化
                    param = JSONObject.
                            parseObject(JSONObject.toJSONString(body),paramType);
                }
                //采用JAVA反射，运行业务逻辑处理方法，并获取返回结果
                Object result =  method.invoke(bean,param);
                response.setResult(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.setId(requestFuture.getId());
        return response;
    }
    public static class MethodBean {
        private Object bean;
        private Method method;
        public Object getBean() {
            return bean;
        }
        public void setBean(Object bean) {
            this.bean = bean;
        }
        public Method getMethod() {
            return method;
        }
        public void setMethod(Method method) {
            this.method = method;
        }
    }
}

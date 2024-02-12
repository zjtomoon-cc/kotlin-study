package com.itjoin.pro_netty.util;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class PropertyUtil {
     private static Properties serverProperties = new Properties();
     protected static final Logger LOGGER = LoggerFactory.getLogger(PropertyUtil.class);
     static {
    	 try {
			serverProperties.load(PropertyUtil.class.getResourceAsStream("/server.properties"));
			LOGGER.error("加载配置文件成功========");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("加载配置文件失败========");
			
		}
     }
     public static  String getString(String key) {
    	 if(serverProperties!=null) {
    		 return serverProperties.getProperty(key);
    	 }
    	 return null;
     }

     public static  Integer getInt(String key) {
    	 if(serverProperties!=null) {
    		 return Integer.valueOf(serverProperties.getProperty(key));
    	 }
    	 return null;
     }
}

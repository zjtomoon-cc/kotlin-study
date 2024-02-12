package test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import dao.TestDao;
public class Test {
	public static void main(String[] args) {
		//初始化Spring容器ApplicationContext，加载配置文件
				@SuppressWarnings("resource")
				ApplicationContext appCon = 
		new FileSystemXmlApplicationContext("C:\\eclipse-workspace\\ch1_1\\src\\applicationContext.xml");
				//通过容器获取test实例
				TestDao tt = (TestDao)appCon.getBean("test");
				tt.sayHello();
	}

}

package cn.itcast.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class StatProvider {
    public static void main(String[] args) throws IOException {
    //    穿件spring容器,加载spring配置文件
        //创建spring容器，加载spring配置文件
        ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext-*.xml");
        ac.start();
        System.in.read();
    }
}

package com.kaizhi.flume.flumeinteceptor;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author WangAoQi
 * 程序入口类
 */
public class MainFlumeIntecptor implements Interceptor {

    @Override
    public void initialize() {

    }

    @Override
    public Event intercept(Event event) {
        /** 初始化配置参数 */
        Properties prop = new Properties();
        try (
                InputStream is = new FileInputStream("/home/kaizhidev/waq/servers/flume-1.7.0-bin/job/config/configuration.properties")
        ) {
            prop.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //根据类名创建对象
        Class<?> etlClass;
        BaseFlumeEtlInterceptor etl = null;
        try {
            //反射创建对象
            etlClass = Class.forName(prop.getProperty("ETLClass"));
            etl = (BaseFlumeEtlInterceptor) etlClass.newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        //进行数据清洗
        return etl.etl(event, prop);
    }


    @Override
    public List<Event> intercept(List<Event> list) {
        ArrayList<Event> interceptors = new ArrayList<>();
        for (Event event : list) {
            Event intercept1 = intercept(event);
            if (intercept1 != null) {
                interceptors.add(intercept1);
            }
        }
        return interceptors;
    }

    @Override
    public void close() {

    }

    /**
     * 程序入口
     */
    public static class Builder implements Interceptor.Builder {
        @Override
        public Interceptor build() {
            return new MainFlumeIntecptor();
        }

        @Override
        public void configure(Context context) {

        }
    }
}

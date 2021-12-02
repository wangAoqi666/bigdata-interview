package com.kaizhi.flume.flumeinteceptor;


import org.apache.flume.Event;
import java.util.Properties;

/**
 * @author WangAoQi
 * Flume拦截器  数据清洗 基类
 */
public abstract class BaseFlumeEtlInterceptor {
    /**
     * 简单的etl工作
     * @param event 事件对象
     * @param prop 配置文件
     * @return 事件对象
     */
    public abstract Event etl(Event event,Properties prop);
}

package com.kaizhi.flume.flumeinteceptor;

import com.kaizhi.flume.utils.LogUtils;
import org.apache.flume.Event;

import java.util.Map;
import java.util.Properties;

/**
 * @author WangAoQi
 * json数据清洗类
 */
public class EtlJsonInterceptor extends BaseFlumeEtlInterceptor {

    @Override
    public Event etl(Event event, Properties prop) {
        // 1 获取数据
        byte[] body = event.getBody();
        // 2 判断数据类型并向Header中赋值
        // 3 获取header
        Map<String, String> headers = event.getHeaders();
        String result = LogUtils.validateEvent(body, prop);
        //符合格式
        if (!(result == null)) {
            headers.put("dataDir",prop.getProperty("successDataDir")+result.substring(0,8));
            event.setHeaders(headers);
            return event;
        }else {
            // 不符合格式
            headers.put("dataDir",prop.getProperty("errorDataDir"));
            event.setHeaders(headers);
            return event;
        }
    }
}
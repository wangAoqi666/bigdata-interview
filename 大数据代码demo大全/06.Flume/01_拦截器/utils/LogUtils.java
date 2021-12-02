package com.kaizhi.flume.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * @author WangAoQi
 * 日志 工具类
 */
public class LogUtils {

    public static String validateEvent(byte[] body, Properties prop) {
        String jsonStr = new String(body, StandardCharsets.UTF_8);
        // 校验json
        JSONObject jsonObject = isJSONValid(jsonStr);
        if (jsonObject == null){
            return null;
        }
        // 校验是否包含指定关键字段
        if(!jsonObject.containsKey(prop.getProperty("keyField"))){
            return null;
        }
        //返回指定字段 作为HDFS存储目录
        return (String) jsonObject.get(prop.getProperty("keyField"));
    }

    /**
     * 暴力解析fastjson
     * @param jsonStr
     * @return 符合格式返回
     */
    public static JSONObject isJSONValid(String jsonStr) {
        JSONObject jsonObject;
        try {
            jsonObject = JSON.parseObject(jsonStr);
        } catch (JSONException ex) {
            try {
                jsonObject = JSON.parseObject(jsonStr);
            } catch (JSONException ex1) {
                return null;
            }
        }
        return jsonObject;
    }

}

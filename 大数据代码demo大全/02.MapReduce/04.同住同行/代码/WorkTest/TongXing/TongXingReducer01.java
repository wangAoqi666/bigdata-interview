package com.czxy.hadoop.mapreduce.WorkTest.TongXing;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.HashSet;

public class TongXingReducer01 extends Reducer<Text, Text, Text, Text> {

    private static Text _key = new Text();
    private static Text _value = new Text();

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        //去除掉同一个人的两条记录
        HashSet<String> hSet = new HashSet();
        //将value进行去重
        for (Text value : values) {
            hSet.add(value.toString());
        }
        //将酒店编号与日期进行切分  精确到日
        _key.set(key.toString().substring(0, key.toString().length() - 4));
        //拼接身份信息
        for (String value : hSet) {
            _value.set(_value.toString() + value + "#");
        }
        //前面加上@@符号 作为下次输入的分割符
        _value.set("@@" + _value);
        //如果身份信息长度大于60就说明有两个以上同行  输出结果
        if(_value.toString().length()>=60) {
            context.write(_key, _value);
        }
        //工作完初始化_value
        _value.set("");
    }
}

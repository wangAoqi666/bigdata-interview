package com.czxy.hadoop.mapreduce.WorkTest.TongXing;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class TongXingMapper02 extends Mapper<Object, Text, Text, Text> {
    private static Text value_=new Text();
    private static Text key_=new Text();
    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        //将文件切割
        String[] datas = value.toString().trim().split("@@");
        //截取同行人员
        String txry=datas[1].trim().substring(0,datas[1].lastIndexOf("#"));
        //截取同行日期
        String txrq=datas[0];
        //输出
        key_.set(txry);
        value_.set(txrq);
        System.out.println(key_.toString()+"__"+value_.toString());
        context.write(key_,value_);
    }
}

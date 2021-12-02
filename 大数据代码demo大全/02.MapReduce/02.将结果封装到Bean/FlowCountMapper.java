package com.czxy.hadoop.mapreduce.将结果封装到Bean;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * @author 550894211@qq.com
 * @version v 1.0
 * @date 2019/11/12
 */
public class FlowCountMapper extends Mapper<LongWritable, Text,Text, FlowBean> {
    Text k = new Text();
    FlowBean v = new FlowBean();
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        //1.分割
        String[] words = value.toString().split("\t");
        //2.抽取
        //手机号
        String phone = words[1];
        //上行
        String upFlow = words[3];
        //下行
        String downFlow = words[4];
        k.set(phone);
        v.setUpflow(Long.parseLong(upFlow));
        v.setDflow(Long.parseLong(downFlow));
        //3.写出

        context.write(k, v);
    }
}

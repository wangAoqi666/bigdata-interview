package com.czxy.hadoop.mapreduce.demo05;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.File;
import java.io.IOException;

/**
 * @author 550894211@qq.com
 * @version v 1.0
 * @date 2019/11/23
 */
public class ReduceJoinMapper extends Mapper<LongWritable, Text,Text,ReduceJoinBean> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        //1.获取一行数据
        String line = value.toString();
        //2.切分
        String[] strings = line.split(",");
        //3.创建一个bean对象 直接复制
        ReduceJoinBean bean = new ReduceJoinBean();
        //####判断文件名
        FileSplit fileSplit = (FileSplit) context.getInputSplit();
        String fileName = fileSplit.getPath().getName();
        if (fileName.contains("orders")){
            bean.setOid(strings[0]);
            bean.setoDate(strings[1]);
            bean.setPid(strings[2]);
            bean.setpNum(strings[3]);
            //4.输出
            context.write(new Text(strings[2]),bean );
        }else {
            bean.setpName(strings[1]);
            bean.setoNum(strings[2]);
            bean.setPrice(strings[3]);
            context.write(new Text(strings[0]),bean );
        }
    }
}

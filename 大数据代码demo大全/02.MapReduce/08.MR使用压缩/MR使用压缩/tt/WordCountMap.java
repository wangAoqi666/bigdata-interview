package com.tt;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

public class WordCountMap extends Mapper<LongWritable, Text,Text,Text> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {


        FileSplit inputSplit = (FileSplit)context.getInputSplit();
        String name = inputSplit.getPath().getName();
        //key  指的是游戏中的编号
      //key代码里是   这行数据的偏移量
         //value   一串图形
      // value     zhangsan,lisi,wangwu
        //1   将value  从text转为String

       String  datas= value.toString();
       //2  切分数据    zhangsan            lisi        wangwu
        String[] splits = datas.split(",");
        //3 遍历输出
    /*   for (int i = 0; i < splits.length; i++) {

        }*/
        for (String data : splits) {
            //输出数据
            context.write(new Text(name),new Text(data));
        }

        //zhangsan   1   输出一次（送一次）
        //lisi       1   输出一次（送一次）
        //wangwu     1   输出一次（送一次）


    }
}

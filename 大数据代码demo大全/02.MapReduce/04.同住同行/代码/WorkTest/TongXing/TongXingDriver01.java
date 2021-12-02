package com.czxy.hadoop.mapreduce.WorkTest.TongXing;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class TongXingDriver01 {
    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();

        Job job=Job.getInstance(conf,"TongXingDriver01");

        // 指定本次job运行的主类
        job.setJarByClass(TongXingDriver01.class);

        // 指定本次job的具体mapper reducer实现类
        job.setMapperClass(TongXingMapper01.class);
        job.setReducerClass(TongXingReducer01.class);

        // 指定本次job map阶段的输出数据类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        // 指定本次job reducer阶段的输出数据类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        // 指定本次job待处理数据的目录 和程序执行完输出结果存放的目录
        FileInputFormat.setInputPaths(job,"C:\\Users\\27727\\Desktop\\学习文件夹\\03.课后作业\\03.MapReduce\\10011练习\\10011.txt");
        FileOutputFormat.setOutputPath(job,new Path("TongXing1114_1_1"));
        // 提交本次job
        boolean b = job.waitForCompletion(true);
        System.exit(b ? 0 :1);
    }
}

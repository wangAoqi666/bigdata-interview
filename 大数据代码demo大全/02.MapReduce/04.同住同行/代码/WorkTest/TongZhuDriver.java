package com.czxy.hadoop.mapreduce.WorkTest;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class TongZhuDriver {
    public static void main(String[] args) throws Exception {
        //通过Job来封装本次mr的相关信息
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        //指定本次mr job jar包运行主类
        job.setJarByClass(TongZhuDriver.class);

        //指定本次mr 所用的mapper reducer类分别是什么
        job.setMapperClass(TongZhuMapper.class);
        job.setReducerClass(TongZhuReducer.class);

        //指定本次mr mapper阶段的输出  k  v类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        //指定本次mr 最终输出的 k v类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        //指定本次mr 输入的数据路径 和最终输出结果存放在什么位置
        FileInputFormat.setInputPaths(job, "E:\\wordcount\\1113");
        FileOutputFormat.setOutputPath(job, new Path("E:\\wordcount\\tongzhu1114"));
        //提交程序  并且监控打印程序执行情况
        boolean b = job.waitForCompletion(true);
        System.exit(b ? 0 : 1);
    }
}

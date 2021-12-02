package com.czxy.hadoop.mapreduce.将结果封装到Bean;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * @author 550894211@qq.com
 * @version v 1.0
 * @date 2019/11/12
 */
public class FlowCountApp  {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        //1.定义configuration
        Configuration configuration = new Configuration();
        //2.获取job
        Job job = Job.getInstance(configuration);
        //3.注册主类
        job.setJarByClass(FlowCountApp.class);
        //4.注册mapper与reduce
        job.setMapperClass(FlowCountMapper.class);
        job.setReducerClass(FlowCountReduce.class);
        //5.声明k,v类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(FlowBean.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowBean.class);
        //6.声明路径
        FileInputFormat.setInputPaths(job, new Path("phone_data .txt"));
        FileOutputFormat.setOutputPath(job, new Path("output"));
        //7.开启作业
        boolean result = job.waitForCompletion(true);
        System.exit(result?0:-1);
    }
}

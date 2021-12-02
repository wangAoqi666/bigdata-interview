package com.czxy.hadoop.mapreduce.demo05;

import com.czxy.hadoop.mapreduce.demo04.CustomPartition;
import com.czxy.hadoop.mapreduce.demo04.CustomPartitionDriver;
import com.czxy.hadoop.mapreduce.demo04.CustomPartitionMapper;
import com.czxy.hadoop.mapreduce.demo04.CustomPartitionReduce;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * @author 550894211@qq.com
 * @version v 1.0
 * @date 2019/11/23
 */
public class ReduceJoinDriver {

    public static void main(String[] args) throws Exception {
        //1.实例化configuration
        Configuration configuration = new Configuration();
        //2.创建 job
        Job job = Job.getInstance(configuration);
        //设置 主类
        job.setJarByClass(ReduceJoinDriver.class);
/*        //将partition加载到driver
        job.setPartitionerClass(CustomPartition.class);
        job.setNumReduceTasks(4);*/

        //3.设置mapper  reduce 类 输出的类型
        job.setMapperClass(ReduceJoinMapper.class);
        job.setReducerClass(ReduceJoinReudce.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(ReduceJoinBean.class);
        job.setOutputKeyClass(ReduceJoinBean.class);
        job.setOutputValueClass(NullWritable.class);
        //4.设置路径
        FileInputFormat.setInputPaths(job, new Path("C:\\Users\\27727\\Desktop\\学习文件夹\\03.课后作业\\03.MapReduce\\素材\\4\\map端join\\input"));
        FileOutputFormat.setOutputPath(job, new Path("demo05_output"));
        //5.开启作业
        boolean result = job.waitForCompletion(true);
        System.exit(result ? 0 : 1);
    }
}

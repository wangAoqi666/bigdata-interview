package com.czxy.hadoop.mapreduce.partition;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.net.URI;

/**
 * @author 550894211@qq.com
 * @version v 1.0
 * @date 2019/11/13
 */
public class Demo06PartitionMain extends Configured implements Tool {

    public static void main(String[] args) throws Exception {
        int result = ToolRunner.run(new Demo06PartitionMain(), args);
        System.out.println(result);
    }
    @Override
    public int run(String[] args) throws Exception {
        FileSystem fs = FileSystem.get(new URI("hdfs://192.168.100.109:8020"), new Configuration(), "root");
        Path outPath = new Path("hdfs://192.168.100.109:8020/aaaa/partition");
        if (fs.exists(outPath)){
            fs.delete(outPath, true);
        }
        Configuration configuration = new Configuration();
        //1.实例化JOb
        Job job = Job.getInstance(configuration, Partitioner.class.getSimpleName());
        //2.设置主类
        job.setJarByClass(Demo06PartitionMain.class);
        //3.设置输入类与输出类
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        //设置输入输出路径
        TextInputFormat.setInputPaths(job, new Path("hdfs://192.168.100.109:8020/aaaa/partition.txt"));
        TextOutputFormat.setOutputPath(job, outPath);
        //4.设置map与reduce类   k,v
        job.setMapperClass(Demo06Mapper.class);
        job.setReducerClass(Demo06Reduce.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(NullWritable.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);
        //5.设置分区
        job.setNumReduceTasks(3);
        job.setPartitionerClass(Demo06Partitioner.class);
        //6.提交作业
        boolean result = job.waitForCompletion(true);
        return result ? 0 : 1;
    }
}

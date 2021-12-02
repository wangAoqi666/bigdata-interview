package com.czxy.hadoop.mapreduce.demo01;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.net.URI;

/**
 * @author 550894211@qq.com
 * @version v 1.0
 * @date 2019/11/9
 */
public class WordCountApp {

    public static void main(String[] args) throws Exception {
        System.setProperty("HADOOP_USER_NAME", "root");
        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS", "hdfs://192.168.100.109:8020");

        //1.创建一个job
        Job job = Job.getInstance(configuration);
        job.setJarByClass(WordCountApp.class);
        //2.获取Map与Reduce处理类
        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReduce.class);
        //3.设置Mapper的Key,Value
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        //4.设置Reduce的Key,Value
        job.setOutputKeyClass(Text.class);
        job.setOutputKeyClass(IntWritable.class);
        // 如果HDFS文件目录已经存在  则先删除
        FileSystem fileSystem = FileSystem.get(new URI("hdfs://192.168.100.109:8020"), configuration, "root");
        Path outPath = new Path("/abc/output");
        if (fileSystem.exists(outPath)){
            fileSystem.delete(outPath, true);
        }

        //5.设置作业的输入和输出的路径
        FileInputFormat.setInputPaths(job, new Path("/abc/input"));
        FileOutputFormat.setOutputPath(job,outPath );
        //6.提交作业
        boolean result = job.waitForCompletion(true);
        System.exit(result ? 0 : -1);
    }
}

/**
 * mapper
 */
class WordCountMapper extends Mapper<LongWritable, Text,Text, IntWritable> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        //1.将value按照一定的分隔符进行分割
        String[] words = value.toString().split(" ");
        //2.给每个单词赋值1
        for (String word : words) {
            context.write(new Text(word), new IntWritable(1));
        }
    }
}

/**
 * reduce
 */
class WordCountReduce extends Reducer<Text, IntWritable,Text,IntWritable> {

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        //1.定义一个count 为零  用来统计单词的个数
        int count = 0;
        //2.迭代values  进行累加计算
        while (values.iterator().hasNext()) {
            IntWritable next = values.iterator().next();
            count += next.get();
        }
        //3.将结果输出
        context.write(new Text(key), new IntWritable(count));
    }
}
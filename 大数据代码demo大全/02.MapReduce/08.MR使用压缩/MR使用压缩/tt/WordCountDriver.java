package com.tt;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class WordCountDriver extends Configured implements Tool {

    @Override
    public int run(String[] args) throws Exception {
        //将已经编写好的Map    Reduce添加到计算框架中
        //1 、实例一个job
        Configuration conf =new Configuration();


        conf.set("mapreduce.map.output.compress","true");
        conf.set("mapreduce.map.output.compress.codec","org.apache.hadoop.io.compress.SnappyCodec");

        conf.set("mapreduce.output.fileoutputformat.compress","true");
        conf.set("mapreduce.output.fileoutputformat.compress.type","RECORD");
        conf.set("mapreduce.output.fileoutputformat.compress.codec","org.apache.hadoop.io.compress.SnappyCodec");





        Job job =Job.getInstance(conf,"WordCount34");

        job.setJarByClass(WordCountDriver.class);

        //2、使用job 设置读取数据(包括数据的路径)
        job.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(job,new Path("hdfs://192.168.100.211:8020/aaaaa"));

        //3、使用job 设置MAP类（map  输出的类型）
        job.setMapperClass(WordCountMap.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        //4、使用job 设置Reduce类（Reduce 输入和输出的类型）
        job.setReducerClass(WordCountReduce.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        //5、使用job 设置数据的输出路径
        job.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job,new Path("/bbbbb"));

        job.setCombinerClass(WordCountReduce.class);



        //6、返回执行状态编号
        job.setNumReduceTasks(2);
        return job.waitForCompletion(true)?0:1;
    }


    //执行job
    public static void main(String[] args) throws Exception {
        int run = ToolRunner.run(new WordCountDriver(), args);

    } 
}
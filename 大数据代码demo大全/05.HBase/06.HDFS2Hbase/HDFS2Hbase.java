package com.czxy.hbase.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

import java.io.IOException;

/**
 * @author 550894211@qq.com
 * @version v 1.0
 * @date 2019/12/19
 */
public class HDFS2Hbase {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        //1.实例configuration
        Configuration configuration = new Configuration();
        configuration.set("hbase.zookeeper.quorum", "node09:2181,node10:2181,node11:2181");
        //2.实例job
        Job job = Job.getInstance(configuration);
        job.setJarByClass(HDFS2Hbase.class);
        //3.设置mapper  输入路径
        job.setMapperClass(HDFS2HbaseMapper.class);
        job.setMapOutputKeyClass(ImmutableBytesWritable.class);
        job.setMapOutputValueClass(Put.class);
        FileInputFormat.setInputPaths(job, new Path("hdfs://node09:8020/input/hbase/abc.txt"));
        //4.设置输出reduce
        TableMapReduceUtil.initTableReducerJob("myuser3", HDFS2HbaseReduce.class, job);
        //5.开启作业
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}

class HDFS2HbaseMapper extends Mapper<LongWritable, Text, ImmutableBytesWritable, Put> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        readFile(value, context);
    }
    public static void readFile(Text value, Mapper.Context context) throws IOException, InterruptedException {
        //1.获取一行数据
        //2.切分
        String[] split = value.toString().split("\t");
        String rowkey = split[0];
        String name = split[1];
        String age = split[2];
        //3.创建put
        Put put = new Put(rowkey.getBytes());
        //4.添加数据
        put.addColumn("f1".getBytes(), "name".getBytes(), name.getBytes());
        put.addColumn("f1".getBytes(), "age".getBytes(), age.getBytes());
        //5.写出
        context.write(new ImmutableBytesWritable(rowkey.getBytes()), put);
    }
}

class HDFS2HbaseReduce extends TableReducer<ImmutableBytesWritable, Put, ImmutableBytesWritable> {
    @Override
    protected void reduce(ImmutableBytesWritable key, Iterable<Put> values, Context context) throws IOException, InterruptedException {
        //写出
        for (Put value : values) {
            context.write(key, value);
        }
    }
}



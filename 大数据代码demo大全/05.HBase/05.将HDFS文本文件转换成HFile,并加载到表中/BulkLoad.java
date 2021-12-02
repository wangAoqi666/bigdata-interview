package com.czxy.hbase.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.HFileOutputFormat2;
import org.apache.hadoop.hbase.mapreduce.LoadIncrementalHFiles;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * @author 550894211@qq.com
 * @version v 1.0
 * @date 2019/12/20
 * 需求：将我们hdfs上面的这个路径/hbase/input/user.txt的数据文件，转换成HFile格式，然后load到myuser2这张表里面去
 */
public class BulkLoad {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        //1.实例一个configuration
        Configuration conf = new Configuration();
        conf.set("hbase.zookeeper.quorum", "node09:2181,node10:2181,node11:2181");
        //2.实例一个JOB
        Job job = Job.getInstance(conf, "BulkLoad");
        //实例一个链接
        Connection connection = ConnectionFactory.createConnection(conf);
        //3.实例一个table
        Table myuser3 = connection.getTable(TableName.valueOf("myuser3"));
        //4.设置主类
        job.setJarByClass(BulkLoad.class);
        //5.设置mapper  kv
        job.setMapperClass(BulkLoadMapper.class);
        job.setMapOutputKeyClass(ImmutableBytesWritable.class);
        job.setMapOutputValueClass(Put.class);
        //6.设置输入路径
        FileInputFormat.setInputPaths(job, new Path("hdfs://node09:8020/input/hbase/abc.txt"));
        //7.设置HFile
        HFileOutputFormat2.configureIncrementalLoad(job, myuser3, connection.getRegionLocator(TableName.valueOf("myuser3")));
        //8.设置输出路径
        FileOutputFormat.setOutputPath(job, new Path("hdfs://node09:8020/input/hbase/bulkload"));
        //9.开启作业
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

    @Test
    public void loadData() throws Exception {
        Configuration conf = new Configuration();
        conf.set("hbase.zookeeper.property.clientPort", "2181");
        conf.set("hbase.zookeeper.quorum", "node09,node10,node11");

        Connection connection = ConnectionFactory.createConnection(conf);
        Admin admin = connection.getAdmin();
        Table table = connection.getTable(TableName.valueOf("myuser4"));
        LoadIncrementalHFiles load = new LoadIncrementalHFiles(conf);
        load.doBulkLoad(new Path("hdfs://node09:8020/input/hbase/bulkload"), admin, table, connection.getRegionLocator(TableName.valueOf("myuser4")));
    }
}


class BulkLoadMapper extends Mapper<LongWritable, Text, ImmutableBytesWritable, Put> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        HDFS2HbaseMapper.readFile(value, context);
    }
}




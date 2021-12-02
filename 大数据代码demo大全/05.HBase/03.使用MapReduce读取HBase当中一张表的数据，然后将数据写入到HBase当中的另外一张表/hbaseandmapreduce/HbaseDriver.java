package com.czxy.hbase.hbaseandmapreduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;

import java.io.IOException;

/**
 * @author 550894211@qq.com
 * @version v 1.0
 * @date 2019/12/18
 */
public class HbaseDriver {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        //创建job
        Configuration configuration = new Configuration();
        Job job = Job.getInstance(configuration);
        //设置主类
        job.setJarByClass(HbaseDriver.class);
        //创建scan对象
        Scan scan = new Scan();
        //设置缓存行数
        scan.setCaching(500);
        //设置块被覆盖  并不缓存
        scan.setCacheBlocks(false);
        //使用TableMapreduceUtil工具类来初始化我们的mapper
        TableMapReduceUtil.initTableMapperJob(TableName.valueOf("myuser"), scan, HbaseMapper.class, Text.class, Put.class, job);
        //使用TableMapReduceUtil工具类来处理我们的 reduce
        TableMapReduceUtil.initTableReducerJob("myuser2", HbaseReduce.class, job);
        //设置reduce个数
        job.setNumReduceTasks(1);
        //开启作业
        System.exit(job.waitForCompletion(true)?0:1);
    }
}

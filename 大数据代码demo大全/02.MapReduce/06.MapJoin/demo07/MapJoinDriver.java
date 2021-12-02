package com.czxy.hadoop.mapreduce.demo07;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author 550894211@qq.com
 * @version v 1.0
 * @date 2019/11/23
 */
public class MapJoinDriver {
    public static final String HDFS_PATH = "hdfs://192.168.100.109:8020";
    public static void main(String[] args) throws Exception {
        //1.实例化一个Configuration
        Configuration configuration = new Configuration();
        //2.将文件加载到缓存中
        DistributedCache.addCacheFile(new URI(HDFS_PATH+"/opt/mapreduce/pdts.txt"),configuration);
        //3.实例化job
        Job job = Job.getInstance(configuration);
        //4.设置主类
        job.setJarByClass(MapJoinDriver.class);
        //5.设置mapper
        job.setMapperClass(MapJoinMapper.class);
        //6.设置map  k,v
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(NullWritable.class);
        //7.设置最终的k,v
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);
        //8.设置输入输出路径
        FileInputFormat.setInputPaths(job, new Path(HDFS_PATH+"/opt/mapreduce/orders.txt"));
        FileOutputFormat.setOutputPath(job, new Path(HDFS_PATH+"/opt/mapreduce/demo07_output"));
        //9.开启作业
        boolean b = job.waitForCompletion(true);
        System.exit(b?0:1);
    }
}

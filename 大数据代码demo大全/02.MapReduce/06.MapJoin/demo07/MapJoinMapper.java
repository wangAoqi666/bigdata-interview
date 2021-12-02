package com.czxy.hadoop.mapreduce.demo07;

import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 550894211@qq.com
 * @version v 1.0
 * @date 2019/11/23
 */
public class MapJoinMapper extends Mapper<LongWritable, Text,Text, NullWritable> {
    private Map<String,String> map = new HashMap<>();
    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        //获取缓存中的文件
        URI[] uris = DistributedCache.getCacheFiles(context.getConfiguration());
        //1.获取HDFS文件系统对象
        FileSystem fs = FileSystem.get(uris[0],context.getConfiguration(),"root");
        //2.打开文件  获取输入流
        FSDataInputStream inputStream = fs.open(new Path(uris[0]));
        //3.创建一个字符串 line  用来接收读取到的一行数据
        String line = null;
        //4.读取  并以pid为key  一行数据为value 存到 map中
        while ((line = inputStream.readLine()) != null){
            String[] split = line.split(",");
            map.put(split[0],line);
        }
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        //获取orders文件传来的一行数据
        String orders = value.toString();
        //2.拼接
        String pinjiehaodeshuju = map.get(orders.split(",")[2]) + orders;
        //3.将pin拼jie接hao好de的shu数ju据 写出
        context.write(new Text(pinjiehaodeshuju), NullWritable.get());
    }
}

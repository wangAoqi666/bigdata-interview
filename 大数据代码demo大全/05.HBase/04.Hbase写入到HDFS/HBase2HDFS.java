package com.czxy.hbase.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author 550894211@qq.com
 * @version v 1.0
 * @date 2019/12/20
 */
public class HBase2HDFS {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        //1.实例configuration
        Configuration configuration = new Configuration();
        configuration.set("hbase.zookeeper.quorum", "node09:2181,node10:2181,node11:2181");
        //2.实例job
        Job job = Job.getInstance(configuration, "HBase2HDFS");
        //3.设置主类
        job.setJarByClass(HBase2HDFS.class);
        //4.设置输入路径
        TableMapReduceUtil.initTableMapperJob(TableName.valueOf("myuser3"), new Scan(), Hbase2HDFSMapper.class, Text.class, Text.class, job);
        //5.设置输出
        FileOutputFormat.setOutputPath(job, new Path("hdfs://node09:8020/input/hbase/myuser3"));
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        //6.开启作业
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}

class Hbase2HDFSMapper extends TableMapper<Text, Text> {
    @Override
    protected void map(ImmutableBytesWritable key, Result value, Context context) throws IOException, InterruptedException {
        //获取rowkey
        String rowkey = Bytes.toString(key.get());
        //获取一行数据  拿字符串拼装一下
        StringBuilder sb = new StringBuilder();
        String[] strings = new String[2];
        Arrays.stream(value.rawCells())
                .filter(cell -> "f1".equals(Bytes.toString(CellUtil.cloneFamily(cell))))
                .forEach(cell -> {
                    if ("name".equals(Bytes.toString(CellUtil.cloneQualifier(cell)))) {
                        strings[0] = Bytes.toString(CellUtil.cloneValue(cell));
                    }
                    if ("age".equals(Bytes.toString(CellUtil.cloneQualifier(cell)))) {
                        strings[1] = Bytes.toString(CellUtil.cloneValue(cell));
                    }
                });
        for (String string : strings) {
            sb.append(string).append("\t");
        }
        //写出
        context.write(new Text(rowkey), new Text(sb.toString()));
    }
}

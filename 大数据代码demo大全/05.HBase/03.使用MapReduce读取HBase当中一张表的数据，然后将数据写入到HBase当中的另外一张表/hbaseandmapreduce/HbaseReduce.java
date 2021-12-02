package com.czxy.hbase.hbaseandmapreduce;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.io.Text;

import java.io.IOException;

/**
 * @author 550894211@qq.com
 * @version v 1.0
 * @date 2019/12/18
 */
public class HbaseReduce extends TableReducer<Text, Put, ImmutableBytesWritable> {

    @Override
    protected void reduce(Text key, Iterable<Put> values, Context context) throws IOException, InterruptedException {
        for (Put value : values) {
            context.write(null, value);
        }
    }
}

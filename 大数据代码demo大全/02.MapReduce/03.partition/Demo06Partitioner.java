package com.czxy.hadoop.mapreduce.partition;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * @author 550894211@qq.com
 * @version v 1.0
 * @date 2019/11/13
 */
public class Demo06Partitioner extends Partitioner<Text, NullWritable> {
    @Override
    public int getPartition(Text text, NullWritable nullWritable, int i) {
        String[] results = text.toString().split("\t");
        System.out.println(results);
        if (Integer.parseInt(results[1]) > 1000){
            return 1;
        }else {
            return 0;
        }
    }
}

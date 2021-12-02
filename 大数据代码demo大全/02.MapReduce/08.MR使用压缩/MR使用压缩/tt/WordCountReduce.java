package com.tt;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class WordCountReduce extends Reducer<Text, Text,Text, Text> {
    //zhangsan   1,1,1,1
    //lisi       1,1,1

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            //key  是游戏中的图形（zhangsan     lisi ）
            //values   好多  1
        String  all="";
            //遍历values
        for (Text value : values) {
            all=all+value.toString()+"    ";
        }

            //输出
        context.write(key,new Text(all));

            
    }
}

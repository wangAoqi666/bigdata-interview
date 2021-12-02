package com.czxy.hadoop.mapreduce.WorkTest;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class TongZhuReducer extends Reducer<Text, Text, Text, Text> {
    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        Text newValue = new Text();
        String a = "";
        String b = "";
        for (Text val : values) {
            String[] word = val.toString().split(" ");
            a += word[0] + "   ";
            b = word[1].substring(0, 8);    //同住时间
        }
        if (a.length() > 55) {
            context.write(new Text(a + "," + b + "," + key.toString().substring(0, key.toString().lastIndexOf(" "))), newValue);
        }//同住人员超过一个人的同住人员身份证号
    }
}

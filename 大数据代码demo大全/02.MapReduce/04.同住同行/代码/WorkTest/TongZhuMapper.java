package com.czxy.hadoop.mapreduce.WorkTest;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class TongZhuMapper extends Mapper<Object, Text, Text, Text> {
    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        String[] lineDatas = value.toString().split("\\|");  //按照|线分割
        if (lineDatas[16].length() >= 8) {
            String onhotel = lineDatas[14];//入住旅馆
            String onroom = lineDatas[15];//入住房号
            String ltime = lineDatas[16]; //旅客入住时间
            String name = lineDatas[5]; //旅客姓名
            String idcode = lineDatas[11];//证件号码
            Text newKey = new Text(onhotel + " " + onroom + " " + ltime.substring(0, 8));
            context.write(newKey, new Text(name+"_"+idcode + " " + ltime));
        }
    }
}

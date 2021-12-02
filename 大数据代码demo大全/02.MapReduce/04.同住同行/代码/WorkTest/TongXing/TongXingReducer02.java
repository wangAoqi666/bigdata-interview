package com.czxy.hadoop.mapreduce.WorkTest.TongXing;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.HashSet;

public class TongXingReducer02 extends Reducer<Text, Text, Text, Text> {
    private static Text txsj=new Text();//同行时间
    private static Text _value=new Text();//

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        //去除掉相同时间的两条记录
        /** values样本数据:
         4401840159_20151231
         4401840159_20151231
         4401840159_20151231
         4401840159_20151231
         4401840159_20151231
         4401840159_20151231
         4401840159_20151231
         4401840159_20151231
         4401840159_20151231
         4401840159_20151231
         * */
        /**用来保存不重复的同行时间*/
        HashSet<String> hSet=new HashSet();
        for(Text value : values)
        {
            hSet.add(value.toString());
        }
        //定义一个标记  用来计算同行次数
        int size=0;
        //遍历集合  并将通行次数进行累加
        for(String var : hSet)
        {
            size++;
            //将同行时间进行拼接
            txsj.set(txsj.toString()+var+"  ");
        }
        //将同行次数与同行时间拼接在一起
        String tongxingxinxi=","+String.valueOf(size)+","+txsj;
        _value.set(tongxingxinxi);
        //如果同行次数大于1则输出
        if(size>1){
            context.write(key, _value);
        }
        //初始化 set集合  _value  txsj
        _value.set("");
        txsj.set("");
        hSet.clear();
    }
}

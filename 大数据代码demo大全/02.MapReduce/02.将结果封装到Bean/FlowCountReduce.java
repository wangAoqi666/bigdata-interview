package com.czxy.hadoop.mapreduce.将结果封装到Bean;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

/**
 * @author 550894211@qq.com
 * @version v 1.0
 * @date 2019/11/12
 */
public class FlowCountReduce extends Reducer<Text,FlowBean,Text,FlowBean> {
    FlowBean v = new FlowBean();
    @Override
    protected void reduce(Text key, Iterable<FlowBean> values, Context context) throws IOException, InterruptedException {
        long upFlowCount = 0;
        long downFlowCount = 0;

        Iterator<FlowBean> iterator = values.iterator();
        while (iterator.hasNext()) {
            FlowBean next = iterator.next();
            downFlowCount += next.getDflow();
            upFlowCount += next.getUpflow();
        }
        v.setDflow(downFlowCount);
        v.setUpflow(upFlowCount);
        v.setSumflow(downFlowCount+upFlowCount);
        context.write(key,v);
    }
}

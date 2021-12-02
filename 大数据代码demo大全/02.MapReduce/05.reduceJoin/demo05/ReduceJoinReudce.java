package com.czxy.hadoop.mapreduce.demo05;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * @author 550894211@qq.com
 * @version v 1.0
 * @date 2019/11/23
 *   需求:最终输出的是  一个完整的 bean
 */
public class ReduceJoinReudce extends Reducer<Text,ReduceJoinBean,ReduceJoinBean, NullWritable> {
    /**
     *
     * @param key pid
     * @param values bean的集合
     * @param context 上下文的对象
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    protected void reduce(Text key, Iterable<ReduceJoinBean> values, Context context) throws IOException, InterruptedException {
        //创建一个新的对象
        ReduceJoinBean reduceJoinBean = new ReduceJoinBean();
        //1.判断pid是否为空  -> 如果为空,获取product文件传来的bean  else  如果不为空,获取orders文件传来的bean
        for (ReduceJoinBean bean : values) {
            if (!"null".equals(bean.getPid()) && bean.getPid()!=null){
                //不为空
                reduceJoinBean.setOid(bean.getOid());
                reduceJoinBean.setoDate(bean.getoDate());
                reduceJoinBean.setPid(bean.getPid());
                reduceJoinBean.setpNum(bean.getpNum());
            }else {
                //为空
                reduceJoinBean.setpName(bean.getpName());
                reduceJoinBean.setoNum(bean.getoNum());
                reduceJoinBean.setPrice(bean.getPrice());

            }
        }
        //输出
        context.write(reduceJoinBean,NullWritable.get());
    }
}

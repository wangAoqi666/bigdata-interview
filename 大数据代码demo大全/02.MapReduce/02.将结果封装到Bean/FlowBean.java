package com.czxy.hadoop.mapreduce.将结果封装到Bean;


import lombok.Data;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @author 550894211@qq.com
 * @version v 1.0
 * @date 2019/11/12
 */

/**
 * 1.先实现WritableComparable接口
 */
@Data
public class FlowBean implements WritableComparable<FlowBean> {
    /**
     * 定义变量
     */
    private long upflow;
    private long dflow;
    private long sumflow;

    /**
     * 3.重写CompareTo方法
     *compareTo方法用于将当前对象与方法的参数进行比较。
     * 如果指定的数与参数相等返回0。
     * 如果指定的数小于参数返回 -1。
     * 如果指定的数大于参数返回 1。
     * 例如：o1.compareTo(o2);
     * 返回正数的话，当前对象（调用compareTo方法的对象o1）要排在比较对象（compareTo传参对象o2）后面，返回负数的话，放在前面。
     * @param o
     * @return
     */
    @Override
    public int compareTo(FlowBean o) {
        //倒序  从小到大
        return this.sumflow > o.getSumflow() ? -1  : 1;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeLong(upflow);
        out.writeLong(dflow);
        out.writeLong(sumflow);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        upflow = in.readLong();
        dflow = in.readLong();
        sumflow = in.readLong();
    }


}

package com.czxy.hadoop.mapreduce.WorkTest.TongXing;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TongXingMapper01 extends Mapper<Object, Text, Text, Text> {

    private static Text value_ = new Text();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
    private static Text key_ = new Text();
    static int interval=5;

    /**
     *
     * @param datetime
     * @param flags
     * @param offsets
     * @return
     * @throws ParseException
     */
    public String changetime(String datetime, int flags, int offsets) throws ParseException {
        String originalTime = datetime;
        int flag = flags;
        int offset = offsets;
        //定义一个df
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        //解析字符串为Date
        Date date = sdf.parse(originalTime);
        //实例一个Calendar
        Calendar cal = Calendar.getInstance();
        //将传过来的日期设置到Calendar中
        cal.setTime(date);
        //判断标记  如果等于1就做加法运算  如果等于0就做减法运算
        if (flag == 1) {
            //flag=1 做时间加法
            cal.add(Calendar.MINUTE, offset);
        } else if (flag == 0) {
            ////flag=0 做时间减法
            cal.add(Calendar.MINUTE, -offset);
        }
        //创建一个时间 并将修改后的时间设置进去
        Date date2 = new Date();
        date2.setTime(cal.getTimeInMillis());
        //返回转换后的字符串
        return sdf.format(date2);
    }


    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        //1.分割数据
        String[] datas = value.toString().trim().split("\\|");
        //2.判断是否是有效数据
        if (datas.length >= 17) {
            //3.判断入住时间格式是否正确
            if (datas[16].length() >= 8) {

                String rzjd = datas[14];//入住酒店
                String rzfj = datas[15];//入住房间
                String rzsj = datas[16];//入住时间
                String xm = datas[5];//姓名
                String zjhm = datas[11];//证件号码

                try {
                    String start = changetime(rzsj, 0, interval);//起始时间
                    //获取当前数据的开始时间   前5分钟
                    Date starts = sdf.parse(start);

                    String ent = changetime(rzsj, 1, interval);//结束时间
                    //获取当前数据的结束时间   后5分钟
                    Date ents = sdf.parse(ent);

                    //用结束时间减去开始时间 除以 1分钟的毫秒值  得到有多少分钟
                    long start_ent = (ents.getTime() - starts.getTime()) / 60000;

                    //遍历start_ent次并写出
                    for (int a = 0; a < start_ent; a++) {
                        //设置key为  从开始时间逐渐++
                        key_.set(rzjd + "_" + changetime(start, 1, a));
                        //设置value为 姓名+身份证号
                        value_.set(xm + "_" + zjhm);
                        //输出
                        context.write(key_, value_);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

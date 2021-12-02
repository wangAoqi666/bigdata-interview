package com.kaizhi.flink.window.time

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time

/**
 * 滑动窗口-有重叠数据
 * 按照时间来进行窗口的划分,每次窗口的滑动距离小于窗口的长度,这样数据就会有一部分重复数据
 */
object StreamingTimeSlidingWindow {
  def main(args: Array[String]): Unit = {
    //1.创建运行环境
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    //2.定义数据流来源
    val socketStream: DataStream[String] = env.socketTextStream("node01", 49999)
    //3.转换数据格式, text -> CarWc
    socketStream.map(line => WordCountCart(line.split(",")(0).toInt,line.split(",")(1).toInt))
    //4.执行统计操作,每个sensorID一个tumbling窗口,窗口的大小为5秒
      .keyBy(_.sen)
    //每2秒钟统计一次  在这个过去的10秒内  各个路口的红绿灯汽车的数量
      .timeWindow(Time.seconds(10),Time.seconds(2))
      .sum(1)
    //显示结果
      .print()
    //触发流计算
    env.execute(this.getClass.getName)

  }
  case class WordCountCart(sen: Int, cardNum: Int)
}

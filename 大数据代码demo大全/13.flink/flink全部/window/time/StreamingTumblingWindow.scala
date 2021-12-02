package com.kaizhi.flink.window.time

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time

/**
 * 翻滚窗口-无重复数据
 */
object StreamingTumblingWindow {
  def main(args: Array[String]): Unit = {
    //1.创建流环境
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    //2.定义数据流来源
    val sockerStream: DataStream[String] = env.socketTextStream("node01", 49999)
    //3.转换数据格式 text -> CarWc
    sockerStream.map(line => WordCountCart(line.split(",")(0).toInt,line.split(",")(1).toInt))
    //4.执行统计操作,每个sensorID一个tumbling窗口,窗口的大小为5秒
      .keyBy(_.sen)
      .timeWindow(Time.seconds(5))
      .sum(1)
    //5.显示统计结果
      .print()
    //6.触发流计算
    env.execute(this.getClass.getName)

  }

  case class WordCountCart(sen: Int, cardNum: Int)

}

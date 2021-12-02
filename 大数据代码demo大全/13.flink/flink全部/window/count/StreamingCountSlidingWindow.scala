package com.kaizhi.flink.window.count

import com.kaizhi.flink.window.count.StreamingCountTumblingWindow.CountCart
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._

/**
 * 有重叠数据
 * 串口长度 5  滑动距离 3
 */
object StreamingCountSlidingWindow {
  def main(args: Array[String]): Unit = {
    //1.创建运行环境
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    //2.定义数据源
    env.socketTextStream("node01",49999)
    //3.转换数据格式  text -> CarWc
      .map(line =>CountCart(line.split(",")(0).toInt,line.split(",")(1).toInt))
    //4.执行统计操作,每个sensorID一个sliding窗口,窗口大小3条数据,窗口滑动为3条数据
      .keyBy(_.sen)
      .countWindow(5,3)
      .sum(1)
    //收集3条消息时  统计最近5条数据
    //5.显示统计结果
      .print()
    //6.触发流计算
    env.execute(this.getClass
    .getName)
  }
}

package com.kaizhi.flink.window.count

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._

/**
 * 无重叠数据
 * 统计最近5条消息中 的数据
 */
object StreamingCountTumblingWindow {
  def main(args: Array[String]): Unit = {
    //1.创建运行环境
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    //2.定义数据流来源
    env.socketTextStream("node01",49999)
    //3.转换数据格式 text -> CountCart
      .map(line => CountCart(line.split(",")(0).toInt,line.split(",")(1).toInt))
    //4.执行统计操作,每个sensorID一个tumbling窗口,窗口大小为5秒
    //按照key进行收集,对应的key出现的次数打到5次作为一个结果
      .keyBy(_.sen)
    //相同的key出现三次才做一次sum聚合
      .countWindow(5)
      .sum(1)
    //5.显示统计结果
      .print()
    //6.触发流计算
    env.execute(this.getClass.getName)
  }

  case class CountCart(sen:Int,cardNum:Int)

}

package com.kaizhi.flink.window

import java.lang

import org.apache.flink.streaming.api.scala.function.WindowFunction
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

/**
 * apply方法可以进行一些自定义处理
 * 通过匿名内部类的方法来实现
 * 当有一些复杂计算时使用
 */
object WindowApply {
  def main(args: Array[String]): Unit = {
    //1.获取流处理运行环境
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    //2.构建socket流数据源,并指定IP和端口号
    env.socketTextStream("node01", 49999)
      //3.对接受的数据转换成单词元组
      .flatMap(_.split(" ").map(_ -> 1))
      //4.使用keyby进行分流(分组)
      .keyBy(_._1)
      //5.使用timeWindow指定窗口的长度(每3秒计算一次)
      .timeWindow(Time.seconds(3))
      //6.时间一个WindowFunction匿名内部类
      .apply(new WindowFunction[(String, Int), (String, Int), String, TimeWindow] {
        //在apply方法中实现聚合计算
        override def apply(key: String, w: TimeWindow, input: Iterable[(String, Int)], out: Collector[(String, Int)]): Unit = {
          val collect: (String, Int) = input.reduce((wc1, wc2) => (wc1._1, wc1._2 + wc2._2))
          out.collect(collect)
        }
      })
    //7.打印输出
      .print()
    //8.启动执行

    env.execute(this.getClass.getName)
  }
}

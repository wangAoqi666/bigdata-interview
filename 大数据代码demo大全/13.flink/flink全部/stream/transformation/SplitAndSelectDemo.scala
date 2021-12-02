package com.kaizhi.flink.stream.transformation

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._

/**
 * 演示Split和Select方法
 * Split: DataStream -> SplitStream
 * Select: SplitStream -> DataStream
 */
object SplitAndSelectDemo {
  def main(args: Array[String]): Unit = {
    //构建批处理环境
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    //设置并行度
    env.setParallelism(1)
    //加载本地集合
    val elements: DataStream[Int] = env.fromCollection(List(1, 2, 3, 4, 5, 6, 7))
    //数据分流,分为奇数和偶数
    val split_data: SplitStream[Int] = elements.split(
      (num: Int) =>
        num % 2 match {
          case 0 => List("even")
          case 1 => List("odd")
        }
    )
    //获取分流后的数据
    val even: DataStream[Int] = split_data.select("even")
    val odd: DataStream[Int] = split_data.select("odd")

    //合并数据
    val all: DataStream[Int] = split_data.select("even", "odd")
    //打印数据
    //even.print()
    //odd.print()
    all.print()
    //执行任务
    env.execute(this.getClass.getName)
  }

}

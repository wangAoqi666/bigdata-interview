package com.kaizhi.flink.stream.transformation

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.scala._

/**
 * 读取socket数据源  进行单词的计数
 */
object WordCountDemo {
  def main(args: Array[String]): Unit = {

    //    1. 获取流处理运行环境
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    //    2. 设置并行度
    env.setParallelism(1)
    //    3. 获取数据源
    val socketStream: DataStream[String] = env.socketTextStream("node01", 49999)
    //    4. 转换操作
    //      1. 以空白进行分割
    socketStream.flatMap(_.split(" "))
    //      2. 给每个单词计数1
      .map((_,1))
    //      3. 根据单词分组
      .keyBy(_._1)
    //      4. 求和
      .sum(1)
    //    5. 打印到控制台
      .print()
    //    6. 执行任务
    env.execute(this.getClass.getName)


  }
}

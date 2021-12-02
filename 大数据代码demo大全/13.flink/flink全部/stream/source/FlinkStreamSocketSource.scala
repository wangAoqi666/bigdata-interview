package com.kaizhi.flink.stream.source

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.scala._
object FlinkStreamSocketSource {
  def main(args: Array[String]): Unit = {
    //构建执行环境
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    //监控端口
    val socketDataStream: DataStream[String] = env.socketTextStream("node01", 59999)
    //转换,以空格进行分单词
    socketDataStream.flatMap(_.split(" "))
      .print()
    env.execute(this.getClass.getName)
  }
}

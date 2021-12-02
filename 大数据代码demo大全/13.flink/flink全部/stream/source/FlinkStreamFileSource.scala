package com.kaizhi.flink.stream.source

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

/**
 * 基于文件的streamSource
 */
object FlinkStreamFileSource {
  def main(args: Array[String]): Unit = {
    //创建执行环境
    val senv: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    //读取文件
    val textDataStream: DataStream[String] = senv.readTextFile("D:\\dev\\myprojects\\day_20200602_flinkDemo\\data\\sort_output\\1")
    //打印数据
    textDataStream.print()
    //执行程序
    senv.execute(this.getClass.getName)
  }

}

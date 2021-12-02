package com.kaizhi.flink

import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment}

/**
 * 读取HDFS文件中的批次数据
 */
object BatchFromHDFS {
  def main(args: Array[String]): Unit = {
    //获取flink执行环境
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    //加载数据
    val ds: DataSet[String] = env.readTextFile("hdfs://192.168.81.111:9000/demo01/20200510/sourceA.1589772504282.dat")
    //触发程序执行
    ds.print()
  }

}

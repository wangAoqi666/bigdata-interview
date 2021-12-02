package com.kaizhi.flink.sink

import akka.io.Tcp.Write
import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.api.scala._
import org.apache.flink.core.fs.FileSystem.WriteMode

/**
 * 将数据写入到本地文件中
 */
object BatchSinkFile {
  def main(args: Array[String]): Unit = {
    //1.定义环境
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    //2.定义数据
    val ds1: DataSet[Map[Int, String]] = env.fromElements(Map(1 -> "spark"), Map(2 -> "flink"))
    ds1.setParallelism(3)
    //3.写入文件到本地
    //      .writeAsText("data/test1/aa",WriteMode.OVERWRITE)
    //  4.写入到hdfs
        .writeAsText("hdfs://172.20.62.118:38020/flinkDatas/test01",WriteMode.OVERWRITE)
    env.execute("batchSinkFileDemo")
  }
}

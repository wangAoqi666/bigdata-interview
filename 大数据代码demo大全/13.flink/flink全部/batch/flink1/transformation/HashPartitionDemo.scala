package com.kaizhi.flink.batch.flink1.transformation

import org.apache.flink.api.scala.ExecutionEnvironment

/**
 * 按照指定的key记性hash分期
 */
object HashPartitionDemo {
  def main(args: Array[String]): Unit = {
    /*
    需求:
        基于以下列表数据来创建数据源，并按照hashPartition进行分区，然后输出到文件
        List(1,1,1,1,1,1,1,2,2,2,2,2)
     */
    //1.构建批处理运行环境
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    //2.设置并行度为2
    env.setParallelism(2)
    //3.加载本地集合
    import org.apache.flink.api.scala._
    val numDs: DataSet[List[Int]] = env.fromElements(List(1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2))
    //4.使用partitionByHash按照字符串的hash进行分区
    numDs.partitionByHash(_.toString())
      //5.调用writeAsText写入文件到data/partition_output目录中
      .writeAsText("data/partition_output")
    //6.打印测试
    numDs.print()

  }
}

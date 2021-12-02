package com.kaizhi.flink.batch.flink1.transformation

import org.apache.flink.api.scala.ExecutionEnvironment

/**
 * 两个DataSet取并集  不会去重
 */
object UnionDemo {
  def main(args: Array[String]): Unit = {
    /*
    需求:
      将以下数据进行取并集操作
      数据集1
      "hadoop", "hive", "flume"
      "hadoop", "hive", "spark"
      数据集2
     */
    //1. 构建批处理运行环境
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    //2. 使用 fromCollection 创建两个数据源
    import org.apache.flink.api.scala._
    val ds1: DataSet[String] = env.fromElements("hadoop", "hive", "flume")
    val ds2: DataSet[String] = env.fromElements("hadoop", "hive", "spark")
    //3. 使用 union 将两个数据源关联在一起
    //4. 打印测试
    ds1.union(ds2).print()
  }
}

package com.kaizhi.flink.transformation

import org.apache.flink.api.common.operators.Order
import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment}

/**
 * 指定字段对分区中的数据进行排序
 */
object SortPartitionDemo {
  def main(args: Array[String]): Unit = {
    /*
    需求:
      按照以下列表来创建数据集
      List("hadoop", "hadoop", "hadoop", "hive", "hive", "spark", "spark", "flink")
      对分区进行排序后，输出到文件。
     */
    //1. 构建批处理运行环境
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    //2. 使用 fromCollection 构建测试数据集
    import org.apache.flink.api.scala._
    val wdDs: DataSet[String] = env.fromCollection(List("hadoop", "hadoop", "hadoop", "hive", "hive", "spark", "spark", "flink"))
    //3. 设置数据集的并行度为 2
    wdDs.setParallelism(2)
    //4. 使用 sortPartition 按照字符串进行降序排序
    wdDs.sortPartition(_.toString,Order.DESCENDING)
    //5. 调用 writeAsText 写入文件到 data/sort_output 目录中
      .writeAsText("data/sort_output")
    //6. 启动执行
    env.execute("app")

  }
}

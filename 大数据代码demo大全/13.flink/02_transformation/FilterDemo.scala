package com.kaizhi.flink.transformation

import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment}

/**
 * 过滤出一些符合条件的元素
 */
object FilterDemo {
  def main(args: Array[String]): Unit = {
    //1.获取Flink的运行环境
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    //导入隐式转换
    import org.apache.flink.api.scala._
    //2.加载本机集合
    val wdDataSet: DataSet[String] = env.fromCollection(List("hadoop", "hive", "spark", "flink"))
    //3.使用filter执行过滤
    val resultDataSet: DataSet[String] = wdDataSet.filter(_.startsWith("h"))
    resultDataSet.print()
  }
}

package com.kaizhi.flink.transformation

import org.apache.flink.api.scala._

/*
"1,张三", "2,李四", "3,王五", "4,赵六"
 */
object mapPartitionDemo {

  case class Person(id: String, name: String)

  def main(args: Array[String]): Unit = {
    //flink环境
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    //加载本地集合
    val listDS: DataSet[String] = env.fromCollection(List("1,张三", "2,李四", "3,王五", "4,赵六"))
    //使用mappartition
    val resultDS: DataSet[Person] = listDS.mapPartition {
      text => {
        text.map(x => (x.split(",")(0), x.split(",")(1))).map(x => Person(x._1, x._2))
      }
    }
    //打印
    resultDS.print()
  }
}

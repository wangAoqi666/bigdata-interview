package com.kaizhi.flink.transformation

import org.apache.flink.api.scala.ExecutionEnvironment

/**
 * 去重
 */
object DistinctDemo {
  def main(args: Array[String]): Unit = {
    /*
    需求:
        请将以下元组数据，使用 distinct 操作去除重复的单词
        ("java" , 1) , ("java", 2) ,("scala" , 1)
        去重得到
        ("java", 1), ("scala", 1)
     */
    //1.获取flink的运行环境
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    //2.加载本地集合
    import org.apache.flink.api.scala._
    val wdDS: DataSet[(String, Int)] = env.fromCollection(List(("java", 1), ("java", 2), ("scala", 1)))
    //3.使用distinct使用哪个字段去重
    wdDS.distinct(0).print()
  }
}

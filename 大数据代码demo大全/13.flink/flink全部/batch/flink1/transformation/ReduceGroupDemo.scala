package com.kaizhi.flink.batch.flink1.transformation

import org.apache.flink.api.scala.ExecutionEnvironment

/**
 * 先在数据所在节点中分组并聚合
 */
object ReduceGroupDemo {
  def main(args: Array[String]): Unit = {
    /*
    需求:
        请将以下元组数据，下按照单词使用 groupBy 进行分组，再使用 reduceGroup 操作进行单词计数
        ("java" , 1) , ("java", 1) ,("scala" , 1)  
     */
    //1.获取flink的环境
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    //2.加载本机集合
    import org.apache.flink.api.scala._
    val wdDS: DataSet[(String, Int)] = env.fromCollection(List(("java", 1), ("java", 1), ("scala", 1)))
    //3.使用groupby进行分组
    //4.使用reducegroup对每个分组进行统计
    wdDS.groupBy(_._1)
      .reduceGroup(iter => iter.reduce((wc1, wc2) => (wc1._1, wc1._2 + wc2._2))).print()
  }

}

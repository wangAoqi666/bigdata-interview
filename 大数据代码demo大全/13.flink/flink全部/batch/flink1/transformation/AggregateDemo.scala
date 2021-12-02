package com.kaizhi.flink.batch.flink1.transformation

import org.apache.flink.api.java.aggregation.Aggregations
import org.apache.flink.api.scala.ExecutionEnvironment

/**
 * 按照内置的方式进行聚合
 * aggregate只能作用在元组上
 * 例如 : SUM/MIN/MAX
 */
object AggregateDemo {
  def main(args: Array[String]): Unit = {
   /*
   需求:
       请将以下元组数据，使用 aggregate 操作进行单词统计
       ("java" , 1) , ("java", 1) ,("scala" , 1)
    */
    //1.构建flink运行环境
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    //2.加载本地集合
    import org.apache.flink.api.scala._
    val wdDS: DataSet[(String, Int)] = env.fromCollection(List(("java", 1), ("java", 1), ("scala", 1)))
    //3.使用groupby按照单词进行分组
    //4.使用aggregate对每个分组进行SUM统计  打印
    wdDS.groupBy(0)
      //todo 注意  使用aggregate时  groupby使用索引值或者索引名  不可使用_._1
      .aggregate(Aggregations.SUM,1)
      .print()
  }
}

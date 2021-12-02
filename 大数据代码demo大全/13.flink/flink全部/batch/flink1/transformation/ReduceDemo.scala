package com.kaizhi.flink.batch.flink1.transformation

import org.apache.flink.api.scala.ExecutionEnvironment

/**
 * 可以对一个dataset或者一个group来进行聚合计算,最终聚合成一个元素
 */
object ReduceDemo {
  def main(args: Array[String]): Unit = {
    /*
    需求1:
          请将以下元组数据，使用 reduce 操作聚合成一个最终结果
          ("java" , 1) , ("java", 1) ,("java" , 1)
          将上传元素数据转换为 ("java",3)
     */
    //1.获取flink的执行环境
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    //2.加载本机集合
    import org.apache.flink.api.scala._
    val mapDS: DataSet[(String, Int)] = env.fromCollection(List(("java", 1), ("java", 1), ("java", 1)))
    //3.使用reduce聚合
    //4.打印
    mapDS.reduce((r1, r2) => {
      (r1._1, r1._2 + r2._2)
    }).print()

    /*
    需求2:
        请将以下元组数据，下按照单词使用 groupBy 进行分组，再使用 reduce 操作聚合成一个最终结果
        ("java" , 1) , ("java", 1) ,("scala" , 1)  
        转换为
         ("java", 2), ("scala", 1)
     */
    val map2DS: DataSet[(String, Int)] = env.fromCollection(List(("java", 1), ("java", 1), ("scala", 1)))
    map2DS.groupBy(_._1).reduce((r1,r2) => (r1._1,r1._2+r2._2)).print()

  }
}

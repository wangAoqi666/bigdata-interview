package com.kaizhi.flink.batch.flink1.transformation

import org.apache.flink.api.scala.ExecutionEnvironment

/**
 * 使用join可以将两个DataSet连接起来
 */
object JoinDemo {

  //2.创建两个样例类保存数据
  // 学科Subject（学科ID、学科名字）
  case class Subject(id: Int, name: String)

  // 成绩Score（唯一ID、学生姓名、学科ID、分数）
  case class Score(id: Int, name: String, subjectId: Int, score: Double)

  def main(args: Array[String]): Unit = {
    /*
    需求:
      在 资料\测试数据源 中，有两个csv文件，有一个为 score.csv ，一个为 subject.csv ，分别保存了成绩数据以及
学科数据。
      需要将这两个数据连接到一起，然后打印出来。
     */
    //1.构建flink运行环境
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    //3.分别使用readCSVFile加载数据源 并指定泛型
    import org.apache.flink.api.scala._
    val scoreDS: DataSet[Score] = env.readCsvFile("data\\join\\input\\score.csv")
    val subDs: DataSet[Subject] = env.readCsvFile("data\\join\\input\\subject.csv")
    //4.使用join连接两个DataSet,并使用where、equalto方法设定关联条件
    scoreDS.join(subDs).where(2).equalTo(0).print()



  }


}

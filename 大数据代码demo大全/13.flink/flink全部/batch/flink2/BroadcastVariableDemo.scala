package com.kaizhi.flink.batch.flink2

import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.api.scala._
import org.apache.flink.configuration.Configuration

/**
 * 广播变量
 */
object BroadcastVariableDemo {
  def main(args: Array[String]): Unit = {
    //1. 获取批处理运行环境
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    //2. 分别创建两个数据集
    val stuDs: DataSet[(Int, String)] = env.fromCollection(List((1, "张三"), (2, "李四"), (3, "王五")))
    val scoreDS: DataSet[(Int, String, Int)] = env.fromCollection(List((1, "语文", 50), (2, "数学", 70), (3, "英文", 86)))
    //3. todo 1.使用 RichMapFunction 对 成绩 数据集进行map转换
    val resultDs: DataSet[(String, String, Int)] = scoreDS.map((new RichMapFunction[(Int, String, Int), (String, String, Int)] {
      var bc_studentList: List[(Int, String)] = _
      //重写`open`方法  获取广播数据  todo 3.获取广播数据
      override def open(parameters: Configuration): Unit = {
        import scala.collection.JavaConverters._
         bc_studentList= getRuntimeContext.getBroadcastVariable[(Int, String)]("bc_student")
          .asScala
          .toList
      }
      //在map方法中使用广播进行转换
      override def map(value: (Int, String, Int)): (String, String, Int) = {
        //获取学生ID
        val stuID: Int = value._1
        //过滤出和学生ID相同的内容
        val tuples: List[(Int, String)] = bc_studentList.filter((x: (Int, String)) => {x._1 == stuID})
        //构建元组
        (tuples.head._2, value._2, value._3)
      }
    }))
      //todo 2.使用withBroadcastSet进行广播数据
      .withBroadcastSet(stuDs, "bc_student")

    //打印测试
    resultDs.print()

  }

}

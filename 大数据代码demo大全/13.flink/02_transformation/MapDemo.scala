package com.kaizhi.flink.transformation

import org.apache.flink.api.scala.ExecutionEnvironment

/**
 * 使用map操作，将以下数据
 * "1,张三", "2,李四", "3,王五", "4,赵六"
 * 转换为一个scala的样例类。
 */
object MapDemo {

  def main(args: Array[String]): Unit = {
   //1.创建flink运行环境
   val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    //2.导入隐式转换
    import org.apache.flink.api.scala._
    //3.加载本地集合
    val listDS: DataSet[String] = env.fromElements("1,张三", "2,李四", "3,王五", "4,赵六")
    //4.进行map转换
    val mapDS: DataSet[String] = listDS.map(_ + "222")
    //5.触发程序
    mapDS.print()
  }
}

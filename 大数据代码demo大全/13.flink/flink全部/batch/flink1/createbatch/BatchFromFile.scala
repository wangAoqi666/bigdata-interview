package com.kaizhi.flink.batch.flink1.createbatch

import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment}
import org.apache.flink.configuration.Configuration

/**
 * 读取文件中的批次数据
 */
object BatchFromFile {


  def main(args: Array[String]): Unit = {
    //读取文本数据
    //readTextFile()
    //读取CSV数据
    //readCSVFile
    //遍历目录,递归查询
    readFolder
  }

  /**
   * 读取CSV文件
   */
  def readCSVFile: Unit = {
    //获取flink的执行环境
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    //用于映射CSV文件的样例类
    case class Student(id: Int, name: String)
    //加载数据
    import org.apache.flink.api.scala._
    val ds: DataSet[Student] = env.readCsvFile[Student]("student.csv")
    ds.print()
  }

  /**
   * 读取文件(可以为指定的压缩文件)
   */
  def readTextFile(): Unit = {
    //获取flink执行环境  env
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    //加载文本数据
    val ds: DataSet[String] = env.readTextFile("D:\\words\\flink\\flink基础\\资料\\测试数据源\\wordcount.txt.gz")
    //触发程序执行
    ds.print()
  }

  /**
   * 递归遍历目录
   */
  def readFolder: Unit = {
    //初始化环境
    val env = ExecutionEnvironment.getExecutionEnvironment
    val parameters = new Configuration
    // recursive.file.enumeration 开启递归
    parameters.setBoolean("recursive.file.enumeration", true)
    val result = env.readTextFile("D:\\dev").withParameters(parameters)
    //触发程序执行
    result.print()

  }

}

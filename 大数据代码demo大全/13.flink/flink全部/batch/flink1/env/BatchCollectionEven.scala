package com.kaizhi.flink.batch.flink1.env

import java.util.Date

import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.api.scala._

/**
 * local环境
 */
object BatchCollectionEven {
  def main(args: Array[String]): Unit = {
    //本机环境
    //    localEnv
    //java集合环境
    collectionEnvironmentTest()
  }

  /**
   * 使用集合环境
   */
  def collectionEnvironmentTest() = {
    //开始时间
    val start_time: Long = new Date().getTime
    //todo 初始化本地环境
    val env: ExecutionEnvironment = ExecutionEnvironment.createCollectionsEnvironment
    val list: DataSet[String] = env.fromCollection(List("1", "2"))
    list.print()
    //结束时间
    val end_time: Long = new Date().getTime
    println(end_time - start_time)
  }

  /**
   * 默认情况下，启动的 本地线程数 与计算机的 CPU个数 相同。
   */
  def localEnv() = {
    //开始时间
    val start_time: Long = new Date().getTime
    //todo 初始化本地环境
    val env: ExecutionEnvironment = ExecutionEnvironment.createLocalEnvironment()
    val list: DataSet[String] = env.fromCollection(List("1", "2"))
    list.print()
    //结束时间
    val end_time: Long = new Date().getTime
    println(end_time - start_time) // 单位毫秒
  }
}

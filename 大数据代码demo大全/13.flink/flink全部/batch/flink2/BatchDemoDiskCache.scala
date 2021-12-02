package com.kaizhi.flink.batch.flink2

import java.io.File
import java.util

import org.apache.commons.io.FileUtils
import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.api.scala._
import org.apache.flink.configuration.Configuration


/**
 * 分布式缓存
 */
object BatchDemoDiskCache {
  def main(args: Array[String]): Unit = {
    //获取运行环境
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    //隐式转换
    //注册文件
    env.registerCachedFile("D:\\dev\\myprojects\\day_20200602_flinkDemo\\data\\sort_output\\1", "b.txt")

    val data: DataSet[String] = env.fromElements("a", "b", "c", "d")
    val result: DataSet[String] = data.map(new RichMapFunction[String, String] {
      //读取数据
      override def open(parameters: Configuration): Unit = {
        super.open(parameters)
        //访问数据
        val myFile: File = getRuntimeContext.getDistributedCache.getFile("b.txt")
        val iter: util.Iterator[String] = FileUtils.readLines(myFile).iterator()
        while (iter.hasNext) {
          println("iter: " + iter.next())
        }
      }
      override def map(in: String): String = {
        in
      }
    })

    result.print()



  }

}

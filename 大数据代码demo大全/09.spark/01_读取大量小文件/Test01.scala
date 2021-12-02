package com.czxy.organizeTheCode

import org.apache.spark.{SparkConf, SparkContext}

object Test01 {
  def main(args: Array[String]): Unit = {
    //1.实例sparkContext
    val conf = new SparkConf().setAppName("随便设置").setMaster("local[*]")
    val sc = new SparkContext(conf)
    //2.设置控制台打印日志级别  方便查看结果数据
    sc.setLogLevel("WARN")
    //3.读取大量小文件   传递目录 会将目录下的所有文件作为RDD
    val fileRDD = sc.wholeTextFiles("D:\\dev\\workspace04\\day20200407_spark_day03_01")
    //4.看一下结果   key是文件的绝对路径  value是文件的内容
    fileRDD.foreach{
      case (a,b) => println(s"文件名:${a} + 文件内容:${b}")
    }
  }
}

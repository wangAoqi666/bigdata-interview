package com.czxy.day0409

import java.util.Properties

import org.apache.spark.sql.SparkSession

object Test02 {
  def main(args: Array[String]): Unit = {
    //1.实例一个SparkSession
    val spark = SparkSession.builder()
      .master("local[*]")
      .appName("mysql")
      .getOrCreate()
    //2.设置参数 url table properties
    val url = "jdbc:mysql://node09:3306/bigdata?characterEncoding=UTF-8"
    val table = "housedata"
    val prop = new Properties()
    prop.setProperty("user","root")
    prop.setProperty("password","123456")
    //3.读取mysql中的数据 返回df
    val dataFrame = spark.read.jdbc(url, table, prop)
    //4.创建临时视图
    dataFrame.createOrReplaceTempView("housedata")
    val frameRDD = spark.sql("select count(*) from housedata where title like '东花市北里%' ").rdd
    //5.执行sparkSQL并返回一个RDD并打印
    println(frameRDD.collect().toList)
  }

}

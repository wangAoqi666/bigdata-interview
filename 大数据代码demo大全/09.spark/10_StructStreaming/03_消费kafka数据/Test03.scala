package com.czxy.day0416.homework

import org.apache.spark.sql.SparkSession

/**
 * 消费kafka中的数据  计算wordcount
 */
object Test03 {

  def main(args: Array[String]): Unit = {
    //1.创建SparkSession
    val spark = SparkSession.builder()
      .master("local[*]")
      .appName("wc")
      .getOrCreate()
    //2.设置日志级别
    spark.sparkContext.setLogLevel("WARN")
    //3.读取数据
    val kafkaData = spark.readStream.format("kafka")
      .option("kafka.bootstrap.servers", "node09:9092,node10:9092,node11:9092")
      .option("subscribe", "stu1")
      .load()
    //4.逻辑计算  并输出
    import spark.implicits._
    kafkaData.selectExpr("CAST( key AS String)","CAST(value AS String)")
      .as[(String,String)]
      .flatMap(_._2.split(" "))
      .groupBy("value")
      .count()
      .sort($"count".desc)
      .writeStream
      .format("console")
      .outputMode("complete")
      .start()
      .awaitTermination()
  }

}

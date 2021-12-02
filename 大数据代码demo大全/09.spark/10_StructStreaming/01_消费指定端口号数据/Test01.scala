package com.czxy.day0416.homework

import org.apache.spark.sql.SparkSession

/**
 * 监控socket数据 实现WordCount
 */
object Test01 {
  def main(args: Array[String]): Unit = {
    //1.创建SparkSession
    val spark = SparkSession.builder()
      .master("local[*]")
      .appName("wc")
      .getOrCreate()
    spark.sparkContext.setLogLevel("WARN")
    //2.介入读取最新数据
    val socketData = spark.readStream
      .format("socket")
      .option("host", "node09")
      .option("port", "9999")
      .load()
    //3.根据业务编写代码
    import spark.implicits._
    socketData.as[String]
      .flatMap(_.split(" "))
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

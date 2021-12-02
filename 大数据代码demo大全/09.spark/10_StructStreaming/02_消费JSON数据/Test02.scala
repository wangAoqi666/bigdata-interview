package com.czxy.day0416.homework

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.StructType

/**
 * 读取json  统计年龄小于25岁的人 各个 年龄 的个数
 */
object Test02 {
  def main(args: Array[String]): Unit = {
    //1.创建SparkSession
    val spark = SparkSession.builder()
      .appName("wc")
      .master("local[*]")
      .getOrCreate()
    //2.设置日志级别
    spark.sparkContext.setLogLevel("WARN")
    //3.读取数据  设置数据的结构 schema
    val schema = new StructType().add("name", "String")
      .add("age", "Integer")
    val fileData = spark.readStream
      .schema(schema).json("F:\\01.编程\\06.日常\\学习文件夹\\03.课后作业\\09.spark\\4.7号练习题30道\\")
    //4.计算数据 并输出
    import spark.implicits._
    fileData.filter($"age" < 25 )
      .groupBy("age")
      .count()
      .sort($"count".desc)
      .writeStream
      .format("console")
      .outputMode("complete")
      .start()
      .awaitTermination()
  }

}

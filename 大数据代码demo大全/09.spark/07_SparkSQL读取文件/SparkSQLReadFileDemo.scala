package com.czxy.day0410


import java.text.SimpleDateFormat
import java.util.Date

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

object SparkSQLReadFileDemo {

  case class Cityenvironment(
                              time: String,
                              cityname: String,
                              aqi: Double,
                              pm2_5: Double,
                              pm10: Double,
                              so2: Double,
                              no2: Double,
                              co: Double,
                              o3: Double,
                              yicixing: String
                            )

  def main(args: Array[String]): Unit = {
    //1.获取SparkSession
    val spark = SparkSession.builder()
      .appName("sparkSqlReadFile")
      .master("local[*]")
      .getOrCreate()
    //2.获取SparkContext
    val sc = spark.sparkContext
    val fileRDD = sc.textFile("D:\\dev\\workspace04\\day20200407_spark_day03_01\\cityenvironment.txt")

    val cityRDD: RDD[Cityenvironment] = fileRDD.map(_.split("\t"))
      .filter { a =>
        a.length == 10 && a(0) != "" && a(1) != "" && a(2) != "" && a(3) != "" && a(4) != "" && a(5) != "" && a(6) != "" && a(7) != "" && a(8) != "" && a(9) != "" && a(9) != "—"
      }.map(a => Cityenvironment(
      a(0),
      a(1),
      a(2).toDouble,
      a(3).toDouble,
      a(4).toDouble,
      a(5).toDouble,
      a(6).toDouble,
      a(7).toDouble,
      a(8).toDouble,
      a(9)
    ))
    //导入隐示转换
    import spark.implicits._
    //cityRDD.foreach(println)
    //3.转换数据
    val df = cityRDD.toDF
    df.show(10)
    //4.注册临时表
     df.createOrReplaceTempView("city")
    //5.写SparkSQL
     spark.sql("select * from city").show()
  }

}

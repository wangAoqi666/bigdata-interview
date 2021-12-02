package com.czxy.day0408

import java.sql.DriverManager

import org.apache.spark.rdd.JdbcRDD
import org.apache.spark.{SparkConf, SparkContext}

object MysqlRDD {

  def main(args: Array[String]): Unit = {

    //1.创建spark配置信息
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("JdbcRDD")

    //2.创建SparkContext
    val sc = new SparkContext(sparkConf)
    sc.setLogLevel("WARN")
    //3.定义连接mysql的参数  todo 设置连接参数
    val driver = "com.mysql.jdbc.Driver"
    val url = "jdbc:mysql://node09:3306/bigdata?characterEncoding=UTF-8"
    val userName = "root"
    val passWd = "123456"

    //创建JdbcRDD todo 创建JDBCRDD  第一个参数是上下文对象  第二个是连接  第三个是SQL 第四个是下限  5上限  6分区  7返回的元组
    val rdd = new JdbcRDD(sc, () => {
      Class.forName(driver)
      DriverManager.getConnection(url, userName, passWd)
    },
      "select `oid`,`posname`,`ordernum`,`paychannel`,`paymethod`,`posid`,`money`,`paytime`,`ordstatus`,`recstate` from `paydata` where money >= ? and money <= ?;",
      0,
      999999999,
      8,
      r => (r.getString(1), r.getString(2), r.getString(3), r.getString(4), r.getString(5), r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10))
    )
    //打印最后结果T
    // TODO: 在这里对RDD进行操作即可
    sc.stop()
  }
}
package com.czxy.day0408

import org.apache.spark.{SparkConf, SparkContext}

/**
 * 使用这段代码
 *    1.更改读取的文件路径
 *    2.
 */
object Test01 {
  def main(args: Array[String]) {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("mysqlApp")
    val sc = new SparkContext(sparkConf)
    //读取数据
    // TODO: 更改读取文件的路径
    val fileRDD = sc.textFile("D:\\dev\\workspace04\\day20200407_spark_day03_01\\paydata.txt")
    //清洗数据
    //todo 对数据进行 清洗
    val filterMap = fileRDD.map(_.split("\t")).filter(_.size >= 10)
    //向mysql中添加
    filterMap.foreachPartition(insertData)
  }

  /**
   * 以分区为单位  向mysql中插入数据
   * @param list
   */
  def insertData(list: Iterator[Array[String]]): Unit = {
    //注册驱动
    Class.forName ("com.mysql.jdbc.Driver").newInstance()
    //获取链接  todo 设置驱动  修改数据库地址  用户名  密码
    val conn = java.sql.DriverManager.getConnection("jdbc:mysql://node09:3306/bigdata?characterEncoding=UTF-8", "root", "123456")
    //插入数据
    list.foreach(data => {
      //写SQL  todo 修改SQL
      val ps = conn.prepareStatement("INSERT INTO `paydata`(`oid`,`posname`,`ordernum`,`paychannel`,`paymethod`,`posid`,`money`,`paytime`,`ordstatus`,`recstate`) VALUES(?,?,?,?,?,?,?,?,?,?)")
      // todo 设置参数
      println(data(0))
      ps.setString(1, data(0))
      ps.setString(2, data(1))
      ps.setString(3, data(3))
      ps.setString(4, data(4))
      ps.setString(5, data(5))
      ps.setString(6, data(6))
      ps.setString(7, data(7))
      ps.setString(8, data(8))
      ps.setString(9, data(9))
      ps.setString(10, data(10))
      //执行sql
      ps.executeUpdate()
    })
    //关闭连接
    conn.close()
  }

}

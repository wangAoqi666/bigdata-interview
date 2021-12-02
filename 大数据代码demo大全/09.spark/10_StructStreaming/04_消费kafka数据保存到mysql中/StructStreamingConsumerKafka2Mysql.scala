package com.czxy.day0416

import java.sql.{Connection, DriverManager, PreparedStatement}
import org.apache.spark.sql.{ForeachWriter, Row, SparkSession}

/**
 * 消费kafka中的数据  计算wordcount
 */
object StructStreamingConsumerKafka2Mysql {

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
    var intoMysql = new IntoMysql("jdbc:mysql://localhost:3306/day20200414?characterEncoding=UTF-8","root","root")
    import spark.implicits._
    kafkaData.selectExpr("CAST( key AS String)","CAST(value AS String)")
      .as[(String,String)]
      .flatMap(_._2.split(" "))
      .groupBy("value")
      .count()
      .sort($"count".desc)
      .writeStream
      .foreach(intoMysql)
      .outputMode("complete")
      .start()
      .awaitTermination()
  }



}

class IntoMysql(url:String,username:String,password:String) extends ForeachWriter[Row] with Serializable{
  var connection:Connection = _ //_表示占位符,后面会给变量赋值
  var preparedStatement: PreparedStatement = _
  //开启连接
  override def open(partitionId: Long, version: Long): Boolean = {
    connection = DriverManager.getConnection(url, username, password)
    true
  }

  /*
  CREATE TABLE `t_word` (
      `id` int(11) NOT NULL AUTO_INCREMENT,
      `word` varchar(255) NOT NULL,
      `count` int(11) DEFAULT NULL,
      PRIMARY KEY (`id`),
      UNIQUE KEY `word` (`word`)
    ) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;
   */
  //replace INTO `bigdata`.`t_word` (`id`, `word`, `count`) VALUES (NULL, NULL, NULL);
  //处理数据--存到MySQL
  override def process(row: Row): Unit = {
    val word: String = row.get(0).toString
    val count: String = row.get(1).toString
    println(word+":"+count)
    //REPLACE INTO:表示如果表中没有数据这插入,如果有数据则替换
    //注意:REPLACE INTO要求表有主键或唯一索引
    val sql = "REPLACE INTO `t_word` (`id`, `word`, `count`) VALUES (NULL, ?, ?);"
    preparedStatement = connection.prepareStatement(sql)
    preparedStatement.setString(1,word)
    preparedStatement.setInt(2,Integer.parseInt(count))
    preparedStatement.executeUpdate()
  }

  //关闭资源
  override def close(errorOrNull: Throwable): Unit = {
    if (connection != null){
      connection.close()
    }
    if(preparedStatement != null){
      preparedStatement.close()
    }
  }
}
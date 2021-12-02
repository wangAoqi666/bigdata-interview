package com.kaizhi.flink.stream.sink

import java.sql.{Connection, DriverManager, PreparedStatement}

import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.sink.{RichSinkFunction, SinkFunction}
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._

/**
 * 将本地集合插入到mysql中
 */
object DataSink_Mysql {
  def main(args: Array[String]): Unit = {
    //创建流环境
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    //准备数据
    val value: DataStream[(Int, String, String, String)] = env.fromCollection(List(
      (10, "dazhuang", "1234567", "大壮"),
      (11, "erya", "123456", "二丫"),
      (12, "sanpang", "123456", "三胖")
    ))
    //添加sink
    value.addSink(new Mysql_Sink)
    //触发流执行
    env.execute(this.getClass.getName)
  }
}

//自定义落地的mysqlsink
class Mysql_Sink extends RichSinkFunction[(Int, String, String, String)] {
  private var connection: Connection = _
  private var ps: PreparedStatement = _

  override def open(parameters: Configuration): Unit = {
    //加载驱动
    Class.forName("com.mysql.cj.jdbc.Driver")
    //创建连接
    connection = DriverManager.getConnection("jdbc:mysql://node02:33306/flink_demo","root","3edcVFR$")
    //获取执行语句
    ps = connection.prepareStatement("replace into stu01 values(?,?,?,?);")
  }

  override def invoke(value: (Int, String, String, String), context: SinkFunction.Context[_]): Unit = {
    //插入数据
    try {
      ps.setInt(1, value._1)
      ps.setString(2, value._2)
      ps.setString(3, value._3)
      ps.setString(4, value._4)
      //执行
      ps.executeUpdate()
    } catch {
      case e:Exception => println(e.getMessage)
    }
  }

  override def close(): Unit = {
    //关闭资源
    if (connection!= null)connection.close()
    if (ps!=null) ps.close()
  }
}
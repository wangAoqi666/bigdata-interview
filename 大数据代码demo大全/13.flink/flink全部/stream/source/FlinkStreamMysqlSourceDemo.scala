package com.kaizhi.flink.stream.source

import java.sql.{Connection, DriverManager, PreparedStatement, ResultSet}

import org.apache.flink.streaming.api.functions.source.{RichSourceFunction, SourceFunction}
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._

/**
 * mysql 数据源  创建 flinkStream
 */
object FlinkStreamMysqlSourceDemo {
  def main(args: Array[String]): Unit = {
    //构建环境
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    //设置并行度
    env.setParallelism(1)
    //添加mysql数据源
    val source: DataStream[(Long, String, Int)] = env.addSource(new MySql_source)
    //打印结果
    source.print()
    //执行任务
    env.execute(this.getClass.getName)
  }

  class MySql_source extends RichSourceFunction[(Long, String, Int)] {

    override def run(sourceContext: SourceFunction.SourceContext[(Long, String, Int)]): Unit = {
      //1.加载驱动
      Class.forName("com.mysql.jdbc.Driver")
      //2.链接mysql
      val conn: Connection = DriverManager.getConnection("jdbc:mysql://node02:33306/bigdata", "root", "3edcVFR$")
      //3.创建PreparedStatement
      val ps: PreparedStatement = conn.prepareStatement("select sbbh,hphm,after_frequency from analysis_of_illegal_operations")
      //4.执行SQL
      val queryResult: ResultSet = ps.executeQuery()
      //5.遍历结果
      while (queryResult.next()){
        val id: Long = queryResult.getLong("sbbh")
        val name: String = queryResult.getString("hphm")
        val age: Int = queryResult.getInt("after_frequency")
        //收集结果
        sourceContext.collect((id,name,age))
      }
    }

    override def cancel(): Unit = ???
  }

}

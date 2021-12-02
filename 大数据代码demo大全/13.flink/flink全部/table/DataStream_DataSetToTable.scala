package com.kaizhi.flink.table

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.table.api.TableEnvironment
import org.apache.flink.table.api.scala.StreamTableEnvironment
import org.apache.flink.api.scala._
import org.apache.flink.core.fs.FileSystem
import org.apache.flink.table.sinks.CsvTableSink

/**
 * 以 流 处理方式，加载下列数据，并注册为表，查询所有数据，写入到CSV文件中。
 *
 * id product amount
 * 1 beer 3
 * 2 diaper 4
 * 3 rubber 2
 */
object DataStream_DataSetToTable {
  //订单样例类  用来保存数据
  case class Order(id:Int,product: String,amount:Int)
  def main(args: Array[String]): Unit = {
    //1.获取流处理环境
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    //2.获取TableEnvironment
    val tableEnv: StreamTableEnvironment = TableEnvironment.getTableEnvironment(env)
    //3.加载本地集合
    val listDStream: DataStream[Order] = env.fromCollection(List(
      Order(1, "beer", 3),
      Order(2, "diaper", 4),
      Order(3, "rubber", 2)
    ))
    //4.根据数据注册表
    tableEnv.registerDataStream("t1",listDStream)
    //5.执行SQL
    tableEnv.sqlQuery("select * from t1")
    //6.写入CSV文件中
      .writeToSink(new CsvTableSink("./data/flinkTableWrite.csv",",",1,FileSystem.WriteMode.OVERWRITE))
    //7.执行任务
    env.execute(this.getClass.getName)
  }

}

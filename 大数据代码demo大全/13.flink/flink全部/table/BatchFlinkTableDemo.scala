package com.kaizhi.flink.table

import java.util.Date

import com.kaizhi.flink.stream.sink.Mysql_Sink
import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.api.scala._
import org.apache.flink.core.fs.FileSystem
import org.apache.flink.table.api.{Table, TableEnvironment}
import org.apache.flink.table.api.scala.BatchTableEnvironment
import org.apache.flink.table.sinks.TableSink
import org.apache.flink.types.Row

/**
 * 使用Flink SQL统计用户消费订单的总金额、最大金额、最小金额、订单总数。
 */
object BatchFlinkTableDemo {

  /**
   * 订单样例类
   *
   * @param id         订单id
   * @param username   用户名
   * @param createTime 订单创建时间
   * @param money      订单金额
   */
  case class Order(id: Int, username: String, createTime: String, money: Double)

  def main(args: Array[String]): Unit = {
    /*
    数据源
    (1,"zhangsan","2018-10-20 15:30",358.5),
    (2,"zhangsan","2018-10-20 16:30",131.5),
    (3,"lisi","2018-10-20 16:30",127.5),
    (4,"lisi","2018-10-20 16:30",328.5),
    (5,"lisi","2018-10-20 16:30",432.5),
    (6,"zhaoliu","2018-10-20 22:30",451.0),
    (7,"zhaoliu","2018-10-20 22:30",362.0),
    (8,"zhaoliu","2018-10-20 22:30",364.0),
    (9,"zhaoliu","2018-10-20 22:30",341.0)
     */
    //1.获取flink执行环境
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    //2.加载本地集合
    val orderDSet: DataSet[Order] = env.fromCollection(List(Order(1, "zhangsan", "2018-10-20 15:30", 358.5),
      Order(2, "zhangsan", "2018-10-20 16:30", 131.5),
      Order(3, "lisi", "2018-10-20 16:30", 127.5),
      Order(4, "lisi", "2018-10-20 16:30", 328.5),
      Order(5, "lisi", "2018-10-20 16:30", 432.5),
      Order(6, "zhaoliu", "2018-10-20 22:30", 451.0),
      Order(7, "zhaoliu", "2018-10-20 22:30", 362.0),
      Order(8, "zhaoliu", "2018-10-20 22:30", 364.0),
      Order(9, "zhaoliu", "2018-10-20 22:30", 341.0)))
    //3.获取flinktbale
    val tableEnv: BatchTableEnvironment = TableEnvironment.getTableEnvironment(env)
    //4.添加DataSet数据源
    tableEnv.registerDataSet("orders", orderDSet)
    //5.计算
    val table: Table = tableEnv.sqlQuery("select username,sum(money) 总和,min(money) 最小金额,sum(money) 最大金额 from orders group by username")
    table.printSchema()
    //6.打印
    tableEnv.toDataSet[(String,Double,Double,Double)](table)
        .writeAsText("./data/20200716_01",FileSystem.WriteMode.OVERWRITE)
    //7.执行程序
    env.execute(this.getClass.getName)
  }
}

package com.kaizhi.flink.table


import java.util.UUID
import java.util.concurrent.TimeUnit

import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.source.{RichSourceFunction, SourceFunction}
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.table.api.{Table, TableEnvironment}
import org.apache.flink.api.scala._
import org.apache.flink.table.api.scala._
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks
import org.apache.flink.streaming.api.watermark.Watermark
import org.apache.flink.table.api.scala.StreamTableEnvironment
import org.apache.flink.table.shaded.org.apache.commons.lang3.time.FastDateFormat
import org.apache.flink.types.Row

import scala.util.Random

/*
注意事项:
    1. 要使用流处理的SQL，必须要添加水印时间
    2. 使用 registerDataStream 注册表的时候，使用 ' 来指定字段
    3. 注册表的时候，必须要指定一个rowtime，否则无法在SQL中使用窗口
    4. 必须要导入 import org.apache.flink.table.api.scala._ 隐式参数
    5. SQL中使用 tumble(时间列名, interval '时间' sencond) 来进行定义窗口
 */
/**
 * stream流SQL
 */
object StreamFlinkTableDemo {

  /*
  需求:
    使用Flink SQL来统计5秒内 用户的 订单总数、订单的最大金额、订单的最小金额。
   */
  //创建一个订单样例类Order,包含四个字段(订单ID,用户ID,订单金额,时间戳)
  case class Order(id: String, userId: Int, money: Long, createTime: Long)

  def main(args: Array[String]): Unit = {
    //获取流处理运行环境
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    //设置处理时间为EventTime
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    //获取Table运行环境
    val tableEnv: StreamTableEnvironment = TableEnvironment.getTableEnvironment(env)
    //创建一个自定义数据源
    val orderStream: DataStream[Order] = env.addSource(new RichSourceFunction[Order] {
      override def run(ctx: SourceFunction.SourceContext[Order]): Unit = {
        //使用for循环生成1000个订单
        for (i <- 0 until 1000) {
          //随机生成订单ID(UUID)
          val id: String = UUID.randomUUID().toString
          //随机生成用户ID(1-2)
          val userId: Int = Random.nextInt(3)
          //随机生成订单金额
          val money: Int = Random.nextInt(101)
          //时间戳为当期系统时间
          val timestamp: Long = System.currentTimeMillis()
          //收集数据
          ctx.collect(Order(id, userId, money, timestamp))
          //每隔1秒生成一个订单
          TimeUnit.SECONDS.sleep(1)
        }
      }

      override def cancel(): Unit = {

      }
    })
    //添加水印,允许延迟2秒
    val watermarkDataStream: DataStream[Order] = orderStream.assignTimestampsAndWatermarks(new AssignerWithPeriodicWatermarks[Order] {
      var currentTimestamp: Long = _
      //允许延迟2秒
      var delayTime = 2000

      //生成一个水印数据
      override def getCurrentWatermark: Watermark = {
        //减去两秒钟,表示让window窗口延迟两秒计算
        val watermark = new Watermark(currentTimestamp - delayTime)
        val formater: FastDateFormat = FastDateFormat.getInstance("HH:mm:ss")
        println(s"水印时间: ${formater.format(watermark.getTimestamp)}，事件时间： ${formater.format(currentTimestamp)}, 系统时间：${formater.format(System.currentTimeMillis())}")
        watermark
      }

      //表示从Order中获取对应的时间戳
      override def extractTimestamp(element: Order, previousElementTimestamp: Long): Long = {
        //获取从order中获取对应的时间戳
        val timestamp: Long = element.createTime
        currentTimestamp = Math.max(currentTimestamp, timestamp)
        //表示时间轴不会往前推,不能因为魔蝎数据延迟了,导致整个window数据的得不到计算
        currentTimestamp
      }
    })
    //使用registerDataStream注册表,并分别指定字段,还要指定rowtime字段
    tableEnv.registerDataStream("t_order",watermarkDataStream,'id,'userId,'money,'createTime.rowtime)
    //使用tableEnv.sqlQuery语句
    val table: Table = tableEnv.sqlQuery(
      """
        |select userId,count(1) as totalCount,max(money) as maxMoney,min(money) as minMoney
        |from t_order
        |group by tumble(createTime, interval '5' second), userId
        |""".stripMargin)
    //将SQL的执行结果转换成DataStream再打印出来
    table.printSchema()
    val retractStream: DataStream[(Boolean, Row)] = tableEnv.toRetractStream[Row](table)
    retractStream.print()
    //启动流处理程序

    env.execute(this.getClass.getName)

  }
}

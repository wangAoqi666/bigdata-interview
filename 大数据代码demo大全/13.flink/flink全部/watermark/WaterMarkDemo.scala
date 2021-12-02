package com.kaizhi.flink.watermark

import java.util.concurrent.TimeUnit
import java.util.{Date, UUID}

import org.apache.commons.lang3.time.FastDateFormat
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks
import org.apache.flink.streaming.api.functions.source.{RichSourceFunction, SourceFunction}
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.watermark.Watermark
import org.apache.flink.streaming.api.windowing.time.Time

import scala.util.Random

/**
 * 计算5秒内,用户的订单总额
 * 订单数据(订单ID--UUID,用户ID,时间戳,订单金额)
 * 要求添加水印来解决网络延迟问题
 */
object WaterMarkDemo {

  //3.创建一个订单样例类`Order`,包含四个字段(订单ID,用户ID,订单金额,时间戳)
  case class Order(orderId: String, userId: Int, money: Long, timestamp: Long)

  def main(args: Array[String]): Unit = {
    //1.创建流处理运行环境
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    //2.设置处理时间为`EventTime`
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    //4.创建一个自定义数据源
    env.addSource(new RichSourceFunction[Order] {
      var isRunning = true

      override def run(sourceContext: SourceFunction.SourceContext[Order]): Unit = {
        while (isRunning) {
          //随机生成订单
          val order: Order = Order(UUID.randomUUID().toString, Random.nextInt(3), Random.nextInt(101), new Date().getTime)
          sourceContext.collect(order)
          //每隔1秒生成一个订单
          TimeUnit.SECONDS.sleep(1)
        }
      }

      override def cancel(): Unit = {
        isRunning = false
      }
    })
    //5.添加水印
      .assignTimestampsAndWatermarks(new AssignerWithPeriodicWatermarks[Order] {
        var currentTimestamp = 0L
        var delayTime = 2000

        override def getCurrentWatermark: Watermark = {
          //允许延迟2秒
          //在获取水印方法中,打印水印时间、当前事件时间和当前系统时间
          val watermark = new Watermark(currentTimestamp - delayTime)
          val dataFormat: FastDateFormat = FastDateFormat.getInstance("HH:mm:ss")
          println(s"当前水印时间:${dataFormat.format(watermark.getTimestamp)},当前事件时间:${dataFormat.format(System.currentTimeMillis())}")
          watermark
        }

        override def extractTimestamp(element: Order, previousElementTimestamp: Long): Long = {
          val timestamp: Long = element.timestamp
          currentTimestamp = Math.max(currentTimestamp,timestamp)
          currentTimestamp
        }
      })
      //6.按照用户进行分流
      .keyBy(_.userId)
      //7.设置5秒的聚合计算
      .timeWindow(Time.seconds(5))
      //8.进行聚合计算
      .reduce((order1,order2) =>{
        Order(order1.orderId,order2.userId,order1.money+order2.money,0)
      })
    //9.打印结果数据
      .print()
    //10.启动执行流处理
    env.execute(this.getClass.getName)
  }
}

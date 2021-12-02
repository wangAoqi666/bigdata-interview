package com.kaizhi.flink.stream.source

import java.util.UUID
import java.util.concurrent.TimeUnit

import org.apache.flink.streaming.api.functions.source.{RichSourceFunction, SourceFunction}
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._

import scala.util.Random

/**
 * 自定义数据源
 * 每一秒钟随机生成一条订单信息(订单ID,用户ID,订单金额,时间戳)
 */
object FlinkStreamCustomDataSourceDemo {

  //创建一个订单的样例类Order,包含四个字段(订单ID,用户ID,订单金额,时间戳)
  case class Order(id: String, userID: Int, money: Long, createTime: Long)

  def main(args: Array[String]): Unit = {
    /*
    要求:
      随机生成订单ID（UUID）
      随机生成用户ID（0-2）
      随机生成订单金额（0-100）
      时间戳为当前系统时间
     */
    //1.创建流处理运行环境
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    //2.创建一个自定义数据源
    val orderStream: DataStream[Order] = env.addSource(new RichSourceFunction[Order] {
      override def run(ctx: SourceFunction.SourceContext[Order]): Unit = {
        //使用for循环生成1000个订单
        for (i <- 0 until 1000) {
          //生成订单ID
          val id: String = UUID.randomUUID().toString
          //用户id
          val userId: Int = Random.nextInt(3)
          //随机生成订单金额
          val money: Int = Random.nextInt(101)
          //时间戳为当前系统时间
          val timestamp: Long = System.currentTimeMillis()
          //收集数据
          ctx.collect(Order(id, userId, money, timestamp))
          //每一秒生成一个订单
          TimeUnit.SECONDS.sleep(1)
        }
      }

      override def cancel(): Unit = ()
    })
    //打印数据
    orderStream.print()

    //开启程序
    env.execute(this.getClass.getName)

  }
}

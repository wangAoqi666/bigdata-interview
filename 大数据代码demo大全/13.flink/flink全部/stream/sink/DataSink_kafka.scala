package com.kaizhi.flink.stream.sink

import java.util.Properties

import com.kaizhi.flink.stream.source.FlinkStreamMysqlSourceDemo.MySql_source
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011

/**
 * 读mysql中的数据到kafka
 */
object DataSink_kafka {
  def main(args: Array[String]): Unit = {
    //1.创建流式环境
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    //2.设置并行度
    env.setParallelism(1)
    //3.添加自定义mysql数据源
    val source: DataStream[(Long, String, Int)] = env.addSource(new MySql_source)
    //4.转换元组数据为字符串
    val strStream: DataStream[String] = source.map(line => line._1 + line._2 + line._3)
    //5.构建FlinkKafkaProduce011
    val properties = new Properties()
    properties.setProperty("bootstrap.servers","node01:39092,node02:39092,node03:39092")
    val sink = new FlinkKafkaProducer011[String]("20200703test", new SimpleStringSchema(), properties)
    //6.添加sink
    strStream.addSink(sink)
    //7.执行任务
    env.execute(this.getClass.getName)
  }
}

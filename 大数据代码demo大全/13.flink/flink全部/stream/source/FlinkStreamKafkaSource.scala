package com.kaizhi.flink.stream.source

import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer010, FlinkKafkaConsumer011}
import org.apache.kafka.clients.CommonClientConfigs

/**
 * kafka数据源生成FlinkStream
 */
object FlinkStreamKafkaSource {
  def main(args: Array[String]): Unit = {
    //1. 创建流处理环境
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    //2. 指定kafka数据流的相关信息
    val kafkaCluster = "172.20.62.117:39092,172.20.62.118:39092,172.20.62.119:39092"
    val kafkaTopicName = "20200703test"
    //3. 创建kafka数据流
    val properties = new Properties()
    properties.setProperty(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, kafkaCluster)
    properties.setProperty("group.id","2020070300")
    val kafka010 = new FlinkKafkaConsumer011[String](kafkaTopicName, new
        SimpleStringSchema(), properties)
    //4. 添加数据源addSource(kafka010)
    val text: DataStream[String] = env.addSource(kafka010)
    //5. 打印数据
    text.print()
    //6. 执行任务
    env.execute("flink-kafka-wordcunt")
  }
}

package day25

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.dstream.{DStream, InputDStream, ReceiverInputDStream}
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

object SparkStreamingKafka {


  def main(args: Array[String]): Unit = {
    //1 创建sparkConf
    var conf =new SparkConf().setMaster("local[*]").setAppName("SparkStremingDemo1")
    //2 创建一个sparkcontext
    var sc =new SparkContext(conf)
    sc.setLogLevel("WARN")
    //3 创建streamingcontext
    var ssc=new   StreamingContext(sc,Seconds(5))
        //设置缓存数据的位置
    ssc.checkpoint("./TmpCount")

   var  kafkaParams= Map[String, Object](
     "bootstrap.servers" -> "node01:9092,node02:9092,node03:9092",
     "key.deserializer" -> classOf[StringDeserializer],
     "value.deserializer" -> classOf[StringDeserializer],
     "group.id" -> "SparkKafkaDemo",
     //earliest:当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，从头开始消费
     //latest:当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，消费新产生的该分区下的数据
     //none:topic各分区都存在已提交的offset时，从offset后开始消费；只要有一个分区不存在已提交的offset，则抛出异常
     //这里配置latest自动重置偏移量为最新的偏移量,即如果有偏移量从偏移量位置开始消费,没有偏移量从新来的数据开始消费
     "auto.offset.reset" -> "latest",
     //false表示关闭自动提交.由spark帮你提交到Checkpoint或程序员手动维护
     "enable.auto.commit" -> (false: java.lang.Boolean)

   )
    //4 接收kafka数据并根据业务逻辑进行计算

    //                                设置位置策略   均衡，
    //kafkaDatas  含有key和value
    //key是kafka成产数据时指定的key（可能为空）
    //value是真实的数据（100%有数据）
    val kafkaDatas: InputDStream[ConsumerRecord[String, String]] =
      KafkaUtils.createDirectStream[String,String](
        ssc,
        LocationStrategies.PreferConsistent,
        ConsumerStrategies.Subscribe[String,String](Array("18BD34"),kafkaParams))

    val kafkaWordOne: DStream[(String, Int)] = kafkaDatas.flatMap(z=>z.value().split(" ")).map((_,1))
    val wordCounts: DStream[(String, Int)] = kafkaWordOne.reduceByKeyAndWindow((x:Int, y:Int)=>x+y,Seconds(10),Seconds(5))
    wordCounts.print()

    //5 开启计算任务
    ssc.start()
    //6 等待关闭
    ssc.awaitTermination()
  }
}

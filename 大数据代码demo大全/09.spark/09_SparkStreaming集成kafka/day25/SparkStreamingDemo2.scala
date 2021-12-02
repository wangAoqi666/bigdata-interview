package day25

import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

object SparkStreamingDemo2 {

  def tmpSum(currentValues:Seq[Int], historyValue:Option[Int] ):Option[Int] ={
    // currentValues当前值
    // historyValue历史值
    //hadoop 1,1,1   hive 1,1  hbase 1
    //hadoop 1,1  hive 1,1  hbase 1
    val result: Int = currentValues.sum + historyValue.getOrElse(0)
    Some(result)
  }

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




    //4 接收数据并根据业务逻辑进行计算
    val socketDatas: ReceiverInputDStream[String] = ssc.socketTextStream("node01",9999)
    val WordOne: DStream[(String, Int)] = socketDatas.flatMap(a=>a.split(" ")).map(x=>(x,1))
    //hadoop hadoop hadoop hive hive hbase
    //hadoop 1,1,1   hive 1,1  hbase 1
    val WordAllCount: DStream[(String, Int)] = WordOne.reduceByKeyAndWindow((x:Int,y:Int)=>x+y,Seconds(10),Seconds(5))
      WordAllCount.print()


    //5 开启计算任务
    ssc.start()
    //6 等待关闭
    ssc.awaitTermination()
  }
}

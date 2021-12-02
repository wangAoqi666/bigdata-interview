package com.czxy.day0408

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.hadoop.hbase.util.Bytes

object HBaseSpark {

  def main(args: Array[String]): Unit = {

    //创建spark配置信息
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("JdbcRDD")

    //创建SparkContext
    val sc = new SparkContext(sparkConf)

    //构建HBase配置信息
    val conf: Configuration = HBaseConfiguration.create()
    // TODO: 修改Zookeeper的地址
    conf.set("hbase.zookeeper.quorum", "hadoop102,hadoop103,hadoop104")
    // TODO: 修改读取的表名
    conf.set(TableInputFormat.INPUT_TABLE, "rddtable")

    //从HBase读取数据形成RDD
    val hbaseRDD: RDD[(ImmutableBytesWritable, Result)] = sc.newAPIHadoopRDD(
      conf,
      //设置读取数据的存储格式
      classOf[TableInputFormat],
      //设置K的类型
      classOf[ImmutableBytesWritable],
      //设置value的类型
      classOf[Result])

    val count: Long = hbaseRDD.count()
    println(count)

    //对hbaseRDD进行处理
    hbaseRDD.foreach {
          //进行模式匹配
      case (_, result) =>
        // TODO: 根据result  求出结果
        val key: String = Bytes.toString(result.getRow)
        val name: String = Bytes.toString(result.getValue(Bytes.toBytes("info"), Bytes.toBytes("name")))
        val color: String = Bytes.toString(result.getValue(Bytes.toBytes("info"), Bytes.toBytes("color")))
        println("RowKey:" + key + ",Name:" + name + ",Color:" + color)
    }

    //关闭连接
    sc.stop()
  }

}
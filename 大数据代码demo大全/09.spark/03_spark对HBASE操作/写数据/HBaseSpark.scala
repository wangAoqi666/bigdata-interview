package com.czxy.day0408

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.{HBaseConfiguration, HColumnDescriptor, HTableDescriptor, TableName}
import org.apache.hadoop.hbase.client.{HBaseAdmin, Put, Result}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapred.TableOutputFormat
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.mapred.JobConf

object HBaseSpark {
  /**
   * todo 在使用这段代码时  需要将hbase安装包中的hbase-site.xml拉到项目目录下
   * @param args
   */
  def main(args: Array[String]) {
    //获取Spark配置信息并创建与spark的连接
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("HBaseApp")
    val sc = new SparkContext(sparkConf)

    //创建HBaseConf
    val conf = HBaseConfiguration.create()
    //创建作业的配置
    val jobConf = new JobConf(conf)
    //设置作业的参数
    jobConf.setOutputFormat(classOf[TableOutputFormat])
    // TODO: 设置输出的表名
    jobConf.set(TableOutputFormat.OUTPUT_TABLE, "fruit_spark")

    //构建Hbase表描述器  todo 设置表名
    val fruitTable = TableName.valueOf("fruit_spark")
    //构造一个表描述符，指定一个TableName对象
    val tableDescr = new HTableDescriptor(fruitTable)
    // TODO: 设置列族
    tableDescr.addFamily(new HColumnDescriptor("info".getBytes))

    //创建Hbase表
    val admin = new HBaseAdmin(conf)
    //判断hbase表是否存在  如果创建则删除
    if (admin.tableExists(fruitTable)) {
      admin.disableTable(fruitTable)
      admin.deleteTable(fruitTable)
    }
    //创建表
    admin.createTable(tableDescr)

    //定义往Hbase插入数据的方法
    def convert(triple: (Int, String, Int)) = {
      //创建put对象
      val put = new Put(Bytes.toBytes(triple._1))
      //添加数据
      put.addImmutable(Bytes.toBytes("info"), Bytes.toBytes("name"), Bytes.toBytes(triple._2))
      put.addImmutable(Bytes.toBytes("info"), Bytes.toBytes("price"), Bytes.toBytes(triple._3))
      (new ImmutableBytesWritable, put)
    }

    //创建一个RDD
    val initialRDD = sc.parallelize(List((1,"apple",11), (2,"banana",12), (3,"pear",13)))

    //将RDD内容写到HBase
    val localData = initialRDD.map(convert)
    localData.saveAsHadoopDataset(jobConf)
  }

}
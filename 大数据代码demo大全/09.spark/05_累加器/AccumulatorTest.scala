package com.czxy.day0408

import org.apache.spark.rdd.RDD
import org.apache.spark.{Accumulator, SparkConf, SparkContext}
object AccumulatorTest {
  def main(args: Array[String]): Unit = {
    //1.设置spark上下文对选哪个
    val conf: SparkConf = new SparkConf().setAppName("wc").setMaster("local[*]")
    val sc: SparkContext = new SparkContext(conf)
    //设置目录级别
    sc.setLogLevel("WARN")

    //使用scala集合完成累加
    var counter1: Int = 0;
    var data = List(1,2,3)
    data.foreach(x => counter1 += x )
    //结果是6  没毛病
    println(counter1)//6

    println("+++++++++++++++++++++++++")

    //使用RDD进行累加
    var counter2: Int = 0;
    //分布式集合的[1,2,3]
    val dataRDD: RDD[Int] = sc.parallelize(data)
    //累加
    dataRDD.foreach(x => counter2 += x)
    //结果是 0
    println(counter2)//0
    //注意：上面的RDD操作运行结果是0
    //因为foreach中的函数是传递给Worker中的Executor执行,用到了counter2变量
    //而counter2变量在Driver端定义的,在传递给Executor的时候,各个Executor都有了一份counter2
    //最后各个Executor将各自个x加到自己的counter2上面了,和Driver端的counter2没有关系

    //那这个问题得解决啊!不能因为使用了Spark连累加都做不了了啊!
    //如果解决?---使用累加器
    //todo  实例累加器
    val counter3: Accumulator[Int] = sc.accumulator(0)
    // 对累加器进行累加
    dataRDD.foreach(x => counter3 += x)
    // 结果就是 1 + 2 + 3  =  6
    println(counter3)//6
  }

}

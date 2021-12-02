package com.kaizhi.flink2

import org.apache.flink.api.common.JobExecutionResult
import org.apache.flink.api.common.accumulators.IntCounter
import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.api.scala._
import org.apache.flink.configuration.Configuration

/**
 * 累加器
 */
object AccumulatorDemo {
  def main(args: Array[String]): Unit = {
    //获取执行环境
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    val data: DataSet[String] = env.fromCollection(List("a", "b", "c", "d"))
    val res: DataSet[String] = data.map(new RichMapFunction[String, String] {
      //1. 定义累加器
      val numLines = new IntCounter()

      override def open(parameters: Configuration): Unit = {
        super.open(parameters)
        //2. 测试累加器
        getRuntimeContext.addAccumulator("num-lines", this.numLines)
      }

      var sum = 0

      override def map(in: String): String = {
        //如果并行度为1,使用普通的累加求和即可,但是设置多个并行度,则普通的累加求和结果就不准了
        sum = sum + 1
        println(s"sum: $sum")
        this.numLines.add(1)
        in
      }
    }
    ).setParallelism(1)
    //res.print()
    res.writeAsText("data\\count0")
    val job: JobExecutionResult = env.execute("BatchDemoCounterScala")
    //3. 获取累加器
    val num: Int = job.getAccumulatorResult[Int]("num-lines")
    println(s"num: $num")
  }
}

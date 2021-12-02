package com.kaizhi.flink.transformation

import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment}

/**
 * flink也会产生  数据倾斜  的时候
 * 使用Rebalance会使用轮询的方式将数据均匀打散,这是处理数据倾斜最好的选择
 */
object RebalanceDemo {
  def main(args: Array[String]): Unit = {
    //1. 构建批处理运行环境
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    //2. 使用 env.generateSequence 创建0-100的并行数据
    import org.apache.flink.api.scala._
    val numDataSet: DataSet[Long] = env.generateSequence(0, 100)
    //3. 使用 fiter 过滤出来 大于8 的数字
    numDataSet.rebalance()//todo 调用Rebalance  避免数据倾斜
      .filter(_ > 8)
      //4. 使用map操作传入 RichMapFunction ，将当前子任务的ID和数字构建成一个元组
      .map(new RichMapFunction[Long, (Long, Long)] {
        override def map(in: Long): (Long, Long) = {
          //todo 在RichMapFunction中可以使用`getRuntimeContext.getIndexOfThisSubtask`获取子任务序号
          (getRuntimeContext.getIndexOfThisSubtask, in)
        }
      })
      //打印
      //5. 打印测试
      .print()

  }
}

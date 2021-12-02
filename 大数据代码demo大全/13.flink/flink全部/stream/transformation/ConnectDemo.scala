package com.kaizhi.flink.stream.transformation



import java.util.concurrent.TimeUnit

import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.scala._

/**
 * Connect 用来将两个DataStream组装成一个 ConnectedStreams 。它用了两个泛型，即不要求两个dataStream的
 * element是同一类型。这样我们就可以把不同的数据组装成同一个结构
 */
object ConnectDemo {
  def main(args: Array[String]): Unit = {
    /*
    需求:
        读取两个不同类型的数据源，使用connect进行合并打印
     */
    //创建流式环境
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    //添加两个自定义数据源
    val text1: DataStream[Long] = env.addSource(new MyLongSourceScala)
    val text2: DataStream[String] = env.addSource(new MyStringSourceScala)
    //使用connect合并两个数据流,创建ConnectedStream对象
    val connStream: ConnectedStreams[Long, String] = text1.connect(text2)
    //遍历ConnectedStream对象,转换为DataStream
    val result: DataStream[Any] = connStream.map(line1 => {
      line1
    }, line2 => {
      line2
    })
    //打印输出设置并行度为1
    result.print().setParallelism(1)
    //执行任务
    env.execute(this.getClass.getName)
  }

  /**
   * 创建自定义并行度为1的source
   * 实现从1开始递增数字
   */
  class MyLongSourceScala extends SourceFunction[Long] {
    var count = 1L
    var isRunning = true
    override def run(sourceContext: SourceFunction.SourceContext[Long]): Unit = {
      while (isRunning){
        sourceContext.collect(count)
        count+=1
        TimeUnit.SECONDS.sleep(1)
      }
    }

    override def cancel(): Unit = {
      isRunning = false
    }
  }

  /**
   * 创建自定义并行度为1的source
   * 实现从1开始产生递增字符串
   */
  class MyStringSourceScala extends SourceFunction[String]{
    var count = 1L
    var isRunning = true
    override def run(sourceContext: SourceFunction.SourceContext[String]): Unit = {
      while(isRunning){
        sourceContext.collect("str_"+count)
        count += 1
        TimeUnit.SECONDS.sleep(1)
      }
    }

    override def cancel(): Unit = {
      isRunning = false
    }
  }


}

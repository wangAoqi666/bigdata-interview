package com.kaizhi.flink.highlevel

import java.util
import java.util.concurrent.TimeUnit

import org.apache.flink.api.java.tuple.Tuple
import org.apache.flink.runtime.state.filesystem.FsStateBackend
import org.apache.flink.streaming.api.{CheckpointingMode, TimeCharacteristic}
import org.apache.flink.streaming.api.checkpoint.ListCheckpointed
import org.apache.flink.streaming.api.functions.source.{RichSourceFunction, SourceFunction}
import org.apache.flink.streaming.api.scala.{DataStream, KeyedStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.scala.function.WindowFunction
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks
import org.apache.flink.streaming.api.watermark.Watermark
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.util.Collector

//样例类 --> 发送数据形式
case class SEvent(id: Long, name: String, info: String, count: Int)

/**
 * 自定义数据源,继承RichSourceFunction
 * 时间run方法和cancel方法
 */
class SEventSourceWithChk extends RichSourceFunction[SEvent] {
  var isRunning = true
  //source算子的逻辑  即:每秒钟向流图中注入10000个元组
  override def run(sourceContext: SourceFunction.SourceContext[SEvent]): Unit = {
    while (isRunning){
      for (i <- 0 until 10000){
        sourceContext.collect(SEvent(1,"hello-"+i,"test-info",1))
      }
      //休眠一秒
      TimeUnit.SECONDS.sleep(1)
    }
  }

  //任务取消时使用
  override def cancel(): Unit = {
    isRunning = false
  }
}

/**
 * 该段代码是流图定义代码，具体实现业务流程，另外，代码中窗口的触发时间使用了eventtime
 */
object FlinkEventTimeAPIChkMain {
  def main(args: Array[String]): Unit = {
    //1.流处理环境
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    //2.开启checkpoint,接个时间为6s
    env.enableCheckpointing(3000)
    //3.设置checkpoint的位置
    env.setStateBackend(new FsStateBackend("file:///D:/tmp/test_checkpoint"))
    //设置checkpoint模式 EXACTLY_ONCE 为默认值 这句可以忽略
    env.getCheckpointConfig.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE)
    //4.设置处理时间为  事件时间
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    //5.添加数据源
    val source: DataStream[SEvent] = env.addSource(new SEventSourceWithChk)
    //6.添加水印支持
    val watemarkDS: DataStream[SEvent] = source.assignTimestampsAndWatermarks(new AssignerWithPeriodicWatermarks[SEvent] {
      //设置watermark
      override def getCurrentWatermark: Watermark = {
        new Watermark(System.currentTimeMillis())
      }

      //给每个元组打上时间戳
      override def extractTimestamp(t: SEvent, l: Long): Long = {
        System.currentTimeMillis()
      }
    })
    //7.keyby分组
    val keybyDS: KeyedStream[SEvent, Tuple] = watemarkDS.keyBy("id")
    //8.设置滑动窗口,窗口时间为4s
    keybyDS.timeWindow(Time.seconds(60),Time.seconds(3))
      //9.指定自定义窗口
      .apply(new WindowStatisticWithChk)
    //10.打印结果
      .print()
    //11.执行任务
    env.execute(this.getClass.getName)
  }
}

/**
 * 该数据在算子制作快照时用于保存到目前为止算子记录的数据条数
 * 用户自定义状态
 */
class UDFState extends Serializable{
  private var count =  0L
  //设置用户自定义状态
  def  setState(s:Long) = count = s
  //获取用户自定义状态
  def getState = count
}

/**
 * 该段代码是window算子的代码  每当触发计算时统计窗口中元组数量
 * 自定义window 继承 windowFunction
 */
class WindowStatisticWithChk extends WindowFunction[SEvent,Long,Tuple,TimeWindow] with ListCheckpointed[UDFState]{
  private  var total = 0L
  /**
   * window算子的实现逻辑  即:统计window中元组的数量
   * @param key  keyby的类型
   * @param window 窗口
   * @param input 输入类型
   * @param out 输出类型
   */
  override def apply(key: Tuple, window: TimeWindow, input: Iterable[SEvent], out: Collector[Long]): Unit = {
    var count = 0L
    for (event <- input){
      count += 1L
    }
    total += count
    out.collect(count)

  }

  /**
   * 从自定义快照中恢复状态
   * @param state
   */
  override def restoreState(state: util.List[UDFState]): Unit = {
    val udfState: UDFState = state.get(0)
    total = udfState.getState.toInt
  }

  /**
   * 制定自定义状态快照
   * @param l  唯一单调递增的数字
   * @param l1 master触发checkpoint的时间戳
   * @return
   */
  override def snapshotState(l: Long, l1: Long): util.List[UDFState] = {
    val udfList: util.ArrayList[UDFState] = new util.ArrayList[UDFState]
    val udfState = new UDFState
    udfState.setState(total)
    udfList.add(udfState)
    udfList
  }

}

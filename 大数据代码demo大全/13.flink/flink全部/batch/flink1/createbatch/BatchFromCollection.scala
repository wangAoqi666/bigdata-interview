package com.kaizhi.flink.batch.flink1.createbatch

import org.apache.flink.api.scala.ExecutionEnvironment

import scala.collection.mutable
import scala.collection.mutable.{ArrayBuffer, ListBuffer}

/**
 * 读取本地集合的批次数据
 */
object BatchFromCollection {
  def main(args: Array[String]): Unit = {

    //获取flink执行环境
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    //导入隐式转换
    import org.apache.flink.api.scala._
    //0.用element创建DataSet(formElements)
    val ds0: DataSet[String] = env.fromElements("spark", "flink")
    //1.用Tuple创建DataSet(formElements)
    val ds1: DataSet[(String, Int)] = env.fromElements(("zhangsan", 18), ("lisi", 19))
    //2.使用Array创建DataSet
    val ds2: DataSet[Array[Int]] = env.fromElements(Array(1, 2, 3, 4))
    //3.使用ArrayBuffer创建DataSet
    val ds3: DataSet[ArrayBuffer[Int]] = env.fromElements(ArrayBuffer(2, 3, 4, 5))
    //4.用list创建DataSet
    val ds4: DataSet[List[Int]] = env.fromElements(List(1, 2, 3, 4))
    //5.用ListBuffer创建DataSet
    val ds5: DataSet[ListBuffer[Int]] = env.fromElements(ListBuffer(2, 3, 4, 5))
    //6.用Vector创建DataSet
    val ds6: DataSet[Vector[Int]] = env.fromElements(Vector(1, 2, 3))
    //7.用Queue创建DataSet
    val ds7: DataSet[mutable.Queue[Int]] = env.fromElements(mutable.Queue(1, 2, 3))
    //8.用Stack创建DataSet
    val ds8: DataSet[mutable.Stack[Int]] = env.fromElements(mutable.Stack(1, 2, 3))
    //9.用Stream创建DataSet(Stream相当于lazy List,避免在中间过程中生成不必要的集合)
    val ds9: DataSet[Stream[Int]] = env.fromElements(Stream(1, 2, 3))
    //10.用Seq创建DataSet
    val ds10: DataSet[mutable.Seq[Int]] = env.fromElements(mutable.Seq(1, 2, 3))
    //11.用Set创建DataSet
    val ds11: DataSet[mutable.Set[Int]] = env.fromElements(mutable.Set(1, 1, 1, 2))
    //12.用Iterable创建DataSet
    val ds12: DataSet[mutable.Iterable[Int]] = env.fromElements(mutable.Iterable(1, 1, 1, 3))
    //13.用ArraySeq创建DataSet
    val ds13: DataSet[mutable.ArraySeq[Int]] = env.fromElements(mutable.ArraySeq(1, 1, 1, 4))
    //14.用ArrayStack创建DataSet
    val ds14: DataSet[mutable.ArrayStack[Int]] = env.fromElements(mutable.ArrayStack(1, 1, 3, 4))
    //15.用Map创建DataSet
    val ds15: DataSet[mutable.Map[Any, Any]] = env.fromElements(mutable.Map(1 -> 2, "1" -> "1"))
    //16.用Range创建DataSet
    val ds16: DataSet[Int] = env.fromCollection(1 to 2)
    //17.用fromElements创建DataSet
    val ds17: DataSet[Long] = env.generateSequence(1, 2)
  }

}

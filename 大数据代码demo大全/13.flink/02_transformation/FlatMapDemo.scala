package com.kaizhi.flink.transformation

import org.apache.flink.api.scala.ExecutionEnvironment


/*
张三,中国,江西省,南昌市
李四,中国,河北省,石家庄市
Tom,America,NewYork,Manhattan
 转换成
 (张三,中国)
 (张三,中国,江西省)
 (张三,中国,江西省,江西省)
 (李四,中国)
 (李四,中国,河北省)
 (李四,中国,河北省,河北省)
(Tom,America)
(Tom,America,NewYork)
(Tom,America,NewYork,NewYork)
 */


object FlatMapDemo {
  def main(args: Array[String]): Unit = {
    //1.构建flink运行环境
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    //2.导入隐式转换
    import org.apache.flink.api.scala._
    //3.加载本地数据
    val listDS: DataSet[String] = env.fromCollection(List(
      "张三,中国,江西省,南昌市",
      "李四,中国,河北省,石家庄市",
      "Tom,America,NewYork,Manhattan"
    ))
    //4.使用flatmap
    val resultDS: DataSet[Product] = listDS.flatMap(text => {
      val fieldArr: Array[String] = text.split(",")
      List(
        (fieldArr(0), fieldArr(1)),
        (fieldArr(0), fieldArr(1), fieldArr(2)),
        (fieldArr(0), fieldArr(1), fieldArr(2), fieldArr(3))
      )
    })
    //5.执行
    resultDS.print()

  }
}

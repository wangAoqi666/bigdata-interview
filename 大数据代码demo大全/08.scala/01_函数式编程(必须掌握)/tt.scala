package day17

import scala.io.StdIn

object tt {

  def main(args: Array[String]): Unit = {

    var A0=Array(1,2,3,4,5)
    var A1=Array(6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,1,2,3,4,5)
    var A2=Array("zhangsan lisi wnagwu","hadoop hive hbase","biejing hanghai nanjing")

    //输出集合大小
    println(A1.length)

    //遍历输出每一个数据

    var aa=A1.map(x=>x*2)
           aa.map(println)    // aa.map(a=>println(a))    aa.map(println(_))
    var aaa=A1.map(x=>x*2).map(y=>y-1).map(r=>r+6).map(u=>{u-10}).map(println)

    //只保留大于10 的数据
    var bb=A1.filter(x=>if(x>10) true else false)
    bb.map(println)

    A1.filterNot(x=>if(x>10) true else false).foreach(println)




    //计算总和
    var sum=A1.reduce((a,b)=>a+b)
    //println(sum)
    /*
        6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,1,2,3,4,5
    1   a=6   b=7  =>13
    2   a=13  b=8  =>21
    3   a=21  b=9  =>30
    4   a=3-  b=10  =>40
    5
    6
    7
    8
     */
    var sum1=A1.reduce(_+_)
    //println(sum)

    /*
    6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,1,2,3,4,5
      1   _1=6   _2=7  =>13
      2   _1=13  _2=8  =>21
      3   _1=21  _2=9  =>30
      4   _1=3-  _2=10  =>40
      5
      6
      7
      8
       */

    //计算总积
    var sum2=A0.reduce((a,b)=>a*b)   //    var sum=A0.reduce(_*_)
    //println(sum)


    //升序
    A1.sortBy(x=>x).foreach(println)

    //降序
    A1.sortBy(a=> -a).map(println)

    //多个抽成一个
    /*flatten  将数组中的数组（嵌套数组）    抽成一个数据
    Array(Array(1,2,3),Array(4,5,6),Array（7,8,9）)   => Array（1,2,3,4,5,6,7,8,9）


    */
    //var A2=Array("zhangsan lisi wnagwu","hadoop hive hbase","biejing hanghai nanjing")
    // val aaaaaaa: Array[String] = "zhangsan lisi wnagwu".split(" ")

    val A22: Array[String] = A2.flatMap(x=>x.split(" "))
   // A22.foreach(println)


    //奇数偶数分开
     val pp: (Array[Int], Array[Int]) = A1.partition( { x => x % 2 == 0})
    println(pp._1.mkString(","))     // return  2,4
    println(pp._2.mkString(","))     // return  1,3,5


  }
}

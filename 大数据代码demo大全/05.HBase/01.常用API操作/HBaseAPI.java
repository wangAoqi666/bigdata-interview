package com.czxy.hbase.demo02;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author 550894211@qq.com
 * @version v 1.0
 * @date 2019/12/17
 */
public class HBaseAPI {
    private Connection connection;
    /**
     * 初始化连接
     */
    @Before
    public void init() throws IOException {
        Configuration conf = new Configuration();
        conf.set("hbase.zookeeper.quorum", "node09:2181,node10:2181,node11:2181");
        connection = ConnectionFactory.createConnection(conf);
    }



    /**
     * 通过scan进行全表扫描
     */
    @Test
    public void findTableAll() {
        try (
                //获取表
                Table myuser = connection.getTable(TableName.valueOf("myuser"))
        ) {
                //创建scan对象
            Scan scan = new Scan();
            //进行全表扫描
            ResultScanner scanner = myuser.getScanner(scan);
            for (Result result : scanner) {
                System.out.println("rowkey: "+Bytes.toString(result.getRow()));
                //获取rowkey下的cells
                Cell[] cells = result.rawCells();
               //打印一下
                printCells(cells);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过startRowKey和endRowKey进行扫描查询
     */
    @Test
    public void findTableByStartkeyAndEndkey() {
        try (  //获取表
               Table myuser = connection.getTable(TableName.valueOf("myuser"))
        ) {
            //创建Scan对象 指定startrowkey与endrowkey
            Scan scan = new Scan();
            scan.setStartRow("0002".getBytes());
            scan.setStopRow("0006".getBytes());
            //扫描数据
            ResultScanner scanner = myuser.getScanner(scan);
            for (Result result : scanner) {
                Cell[] cells = result.rawCells();
                for (Cell cell : cells) {
                    System.out.println(Bytes.toString(CellUtil.cloneFamily(cell)));
                    System.out.println(Bytes.toString(CellUtil.cloneQualifier(cell)));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 按照rowkey查询指定列族下面的指定列的值
     */
    @Test
    public void findMyuserByRowkeyAndQualifier() {
        try (
                //获取表
                Table myuser = connection.getTable(TableName.valueOf("myuser"))
        ) {
            //创建Get  指定rowk与列组与列
            Get get = new Get("0002".getBytes());
            get.addColumn("f2".getBytes(), "sex".getBytes());
            //获取数据
            Result result = myuser.get(get);
            //打印数据
            for (Cell cell : result.rawCells()) {
                System.out.println(Bytes.toString(CellUtil.cloneFamily(cell)));
                System.out.println(Bytes.toString(CellUtil.cloneQualifier(cell)));
                System.out.println(Bytes.toString(CellUtil.cloneValue(cell)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 按照rowkey进行查询获取所有列的所有值
     */
    @Test
    public void findMyuserByRowkey() {
        try (
                //获取表
                Table myuser = connection.getTable(TableName.valueOf("myuser"));
        ) {
            //创建get对象  指定列族
            Get get = new Get("0002".getBytes());
            //获取数据
            Result result = myuser.get(get);
            //获取所有cell
            Cell[] cells = result.rawCells();
            for (Cell cell : cells) {
                //列族
                System.out.println(Bytes.toString(CellUtil.cloneFamily(cell)));
                //列
                System.out.println(Bytes.toString(CellUtil.cloneQualifier(cell)));
                //值
                if (Bytes.toString(CellUtil.cloneQualifier(cell)).equals("id") || Bytes.toString(CellUtil.cloneQualifier(cell)).equals("age")) {
                    System.out.println(Bytes.toInt(CellUtil.cloneValue(cell)));
                } else {
                    System.out.println(Bytes.toString(CellUtil.cloneValue(cell)));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化一批数据  方便查询
     */
    @Test
    public void initData() {
        try (
                //获取表
                Table table = connection.getTable(TableName.valueOf("myuser"))
        ) {
            //创建数据
            //创建put对象，并指定rowkey
            Put put = new Put("0002".getBytes());
            put.addColumn("f1".getBytes(), "id".getBytes(), Bytes.toBytes(1));
            put.addColumn("f1".getBytes(), "name".getBytes(), Bytes.toBytes("曹操"));
            put.addColumn("f1".getBytes(), "age".getBytes(), Bytes.toBytes(30));
            put.addColumn("f2".getBytes(), "sex".getBytes(), Bytes.toBytes("1"));
            put.addColumn("f2".getBytes(), "address".getBytes(), Bytes.toBytes("沛国谯县"));
            put.addColumn("f2".getBytes(), "phone".getBytes(), Bytes.toBytes("16888888888"));
            put.addColumn("f2".getBytes(), "say".getBytes(), Bytes.toBytes("helloworld"));

            Put put2 = new Put("0003".getBytes());
            put2.addColumn("f1".getBytes(), "id".getBytes(), Bytes.toBytes(2));
            put2.addColumn("f1".getBytes(), "name".getBytes(), Bytes.toBytes("刘备"));
            put2.addColumn("f1".getBytes(), "age".getBytes(), Bytes.toBytes(32));
            put2.addColumn("f2".getBytes(), "sex".getBytes(), Bytes.toBytes("1"));
            put2.addColumn("f2".getBytes(), "address".getBytes(), Bytes.toBytes("幽州涿郡涿县"));
            put2.addColumn("f2".getBytes(), "phone".getBytes(), Bytes.toBytes("17888888888"));
            put2.addColumn("f2".getBytes(), "say".getBytes(), Bytes.toBytes("talk is cheap , show me the code"));


            Put put3 = new Put("0004".getBytes());
            put3.addColumn("f1".getBytes(), "id".getBytes(), Bytes.toBytes(3));
            put3.addColumn("f1".getBytes(), "name".getBytes(), Bytes.toBytes("孙权"));
            put3.addColumn("f1".getBytes(), "age".getBytes(), Bytes.toBytes(35));
            put3.addColumn("f2".getBytes(), "sex".getBytes(), Bytes.toBytes("1"));
            put3.addColumn("f2".getBytes(), "address".getBytes(), Bytes.toBytes("下邳"));
            put3.addColumn("f2".getBytes(), "phone".getBytes(), Bytes.toBytes("12888888888"));
            put3.addColumn("f2".getBytes(), "say".getBytes(), Bytes.toBytes("what are you 弄啥嘞！"));

            Put put4 = new Put("0005".getBytes());
            put4.addColumn("f1".getBytes(), "id".getBytes(), Bytes.toBytes(4));
            put4.addColumn("f1".getBytes(), "name".getBytes(), Bytes.toBytes("诸葛亮"));
            put4.addColumn("f1".getBytes(), "age".getBytes(), Bytes.toBytes(28));
            put4.addColumn("f2".getBytes(), "sex".getBytes(), Bytes.toBytes("1"));
            put4.addColumn("f2".getBytes(), "address".getBytes(), Bytes.toBytes("四川隆中"));
            put4.addColumn("f2".getBytes(), "phone".getBytes(), Bytes.toBytes("14888888888"));
            put4.addColumn("f2".getBytes(), "say".getBytes(), Bytes.toBytes("出师表你背了嘛"));

            Put put5 = new Put("0005".getBytes());
            put5.addColumn("f1".getBytes(), "id".getBytes(), Bytes.toBytes(5));
            put5.addColumn("f1".getBytes(), "name".getBytes(), Bytes.toBytes("司马懿"));
            put5.addColumn("f1".getBytes(), "age".getBytes(), Bytes.toBytes(27));
            put5.addColumn("f2".getBytes(), "sex".getBytes(), Bytes.toBytes("1"));
            put5.addColumn("f2".getBytes(), "address".getBytes(), Bytes.toBytes("哪里人有待考究"));
            put5.addColumn("f2".getBytes(), "phone".getBytes(), Bytes.toBytes("15888888888"));
            put5.addColumn("f2".getBytes(), "say".getBytes(), Bytes.toBytes("跟诸葛亮死掐"));


            Put put6 = new Put("0006".getBytes());
            put6.addColumn("f1".getBytes(), "id".getBytes(), Bytes.toBytes(5));
            put6.addColumn("f1".getBytes(), "name".getBytes(), Bytes.toBytes("xiaobubu—吕布"));
            put6.addColumn("f1".getBytes(), "age".getBytes(), Bytes.toBytes(28));
            put6.addColumn("f2".getBytes(), "sex".getBytes(), Bytes.toBytes("1"));
            put6.addColumn("f2".getBytes(), "address".getBytes(), Bytes.toBytes("内蒙人"));
            put6.addColumn("f2".getBytes(), "phone".getBytes(), Bytes.toBytes("15788888888"));
            put6.addColumn("f2".getBytes(), "say".getBytes(), Bytes.toBytes("貂蝉去哪了"));
            //添加数据
            List<Put> puts = Arrays.asList(put, put2, put3, put4, put5, put6);
            table.put(puts);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 向表中添加数据
     */
    @Test
    public void addData() throws IOException {
        try (
                //1.获取表
                Table myuser = connection.getTable(TableName.valueOf("myuser"));
        ) {
            //2.创建数据
            Put put = new Put("0001".getBytes());
            put.addColumn("f1".getBytes(), "name".getBytes(), "zhangsan".getBytes());
            put.addColumn("f1".getBytes(), "age".getBytes(), "18".getBytes());
            //3.添加数据
            myuser.put(put);
        }
    }

    /**
     * 创建表myuser
     */
    @Test
    public void createTable() {
        try (
                //获取Admin
                Admin admin = connection.getAdmin()
        ) {
            //1.设置表名称
            HTableDescriptor hTableDescriptor = new HTableDescriptor(TableName.valueOf("myuser"));
            //2.添加列族
            hTableDescriptor.addFamily(new HColumnDescriptor("f1"));
            hTableDescriptor.addFamily(new HColumnDescriptor("f2"));
            //3.创建表
            admin.createTable(hTableDescriptor);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 为了方便测试 打印出所有的cell单元的信息
     * @param cells
     */
    public static void printCells(Cell[] cells) {
        for (Cell cell : cells) {
            System.out.println("------------华丽的分割线------------");
            System.out.println("列族: "+Bytes.toString(CellUtil.cloneFamily(cell)));
            System.out.println("列: "+Bytes.toString(CellUtil.cloneQualifier(cell)));
            if ("id".equals(Bytes.toString(CellUtil.cloneQualifier(cell))) || "age".equals(Bytes.toString(CellUtil.cloneQualifier(cell)))){
                System.out.println("值: "+Bytes.toInt(CellUtil.cloneValue(cell)));
            }else {
                System.out.println("值: "+Bytes.toString(CellUtil.cloneValue(cell)));
            }
           /* if (Bytes.toString(CellUtil.cloneQualifier(cell)).equals("id") || Bytes.toString(CellUtil.cloneQualifier(cell)).equals("age")) {
                System.out.println(Bytes.toInt(CellUtil.cloneValue(cell)));
            } else {
                System.out.println(Bytes.toString(CellUtil.cloneValue(cell)));
            }*/
            System.out.println("------------华丽的分割线------------");
        }
    }

    /**
     * 为了方便测试 打印出所有的result单元的信息
     * @param resultScanner
     */
    public static void printScanner(ResultScanner resultScanner){
        for (Result result : resultScanner) {
            System.out.println("rowkey: "+Bytes.toString(result.getRow())+"=======================================");
            printCells(result.rawCells());
        }
    }


    @After
    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

package com.czxy.hbase.demo02;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import java.io.IOException;

/**
 * @author 550894211@qq.com
 * @version v 1.0
 * @date 2019/12/17
 * hbase过滤器api使用
 */
public class HBaseFilterAPI {
    private Connection connection;
    private Table myuser;

    /**
     * 初始化连接
     */
    @Before
    public void init() throws IOException {
        Configuration conf = new Configuration();
        conf.set("hbase.zookeeper.quorum", "node09:2181,node10:2181,node11:2181");
        connection = ConnectionFactory.createConnection(conf);
        myuser = connection.getTable(TableName.valueOf("myuser"));
    }

    /**
     * 删除整张表
     */
    @Test
    public void deleteTable() {
        try (
                Admin admin = connection.getAdmin()
        ) {
            admin.disableTable(TableName.valueOf("day20191217_01"));
            admin.deleteTable(TableName.valueOf("day20191217_01"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据rowkey删除一行数据
     */
    @Test
    public void deleteByRowkey() throws IOException {
        Delete delete = new Delete("0002".getBytes());
        myuser.delete(delete);
    }

    /**
     * 多个过滤器
     */
    @Test
    public void manyFilter() throws IOException {
        //1.创建scan
        Scan scan = new Scan();
        //2.创建过滤器集合
        SingleColumnValueFilter singleColumnValueFilter = new SingleColumnValueFilter("f1".getBytes(), "name".getBytes(), CompareFilter.CompareOp.EQUAL, "刘备".getBytes());
        PrefixFilter prefixFilter = new PrefixFilter("0002".getBytes());
        FilterList filterList = new FilterList();
        filterList.addFilter(singleColumnValueFilter);
        filterList.addFilter(prefixFilter);
        scan.setFilter(filterList);
        //3.查询数据
        ResultScanner scanner = myuser.getScanner(scan);
        //4.打印出来
        HBaseAPI.printScanner(scanner);
    }

    /**
     * rowkey前缀过滤器PrefixFilter
     */
    @Test
    public void prefixFilter() throws IOException {
        //创建scan
        Scan scan = new Scan();
        //添加过滤器
        scan.setFilter(new PrefixFilter("0002".getBytes()));
        //查询数据
        ResultScanner scanner = myuser.getScanner(scan);
        //打印
        HBaseAPI.printScanner(scanner);
    }

    /**
     * 过滤掉指定的列
     */
    @Test
    public void singleColumnValueExcludeFilter() throws IOException {
        //创建scan
        Scan scan = new Scan();
        //添加过滤器
        scan.setFilter(new SingleColumnValueExcludeFilter("f2".getBytes(), "name".getBytes(), CompareFilter.CompareOp.EQUAL, "刘备".getBytes()));
        //查询数据
        ResultScanner scanner = myuser.getScanner(scan);
        //打印
        HBaseAPI.printScanner(scanner);
    }

    /**
     * 单列值过滤器
     * SingleColumnValueFilter会返回满足条件的整列值的所有字段
     */
    @Test
    public void singleColumnValueFilter() throws IOException {
        //创建scan对象
        Scan scan = new Scan();
        //添加过滤器
        scan.setFilter(new SingleColumnValueFilter("f1".getBytes(), "name".getBytes(), CompareFilter.CompareOp.EQUAL, "刘备".getBytes()));
        //扫描数据
        ResultScanner scanner = myuser.getScanner(scan);
        //打印出来
        HBaseAPI.printScanner(scanner);
    }

    /**
     * 列值过滤器ValueFilter
     * 查询所有列当中包含8的数据
     */
    @Test
    public void valueFilter() throws IOException {
        //创建Scan对象
        Scan scan = new Scan();
        //添加过滤器
        scan.setFilter(new ValueFilter(CompareFilter.CompareOp.EQUAL, new SubstringComparator("8")));
        //获取数据
        ResultScanner scanner = myuser.getScanner(scan);
        //打印出来
        for (Result result : scanner) {
            HBaseAPI.printCells(result.rawCells());
        }
    }

    /**
     * 列过滤器QualifierFilter
     * 只查询name列的值
     *
     * @throws IOException
     */
    @Test
    public void qualifierFilter() throws IOException {
        //创建scan对象
        Scan scan = new Scan();
        //设置列过滤器
        scan.setFilter(new QualifierFilter(CompareFilter.CompareOp.EQUAL, new SubstringComparator("name")));
        //获取数据
        ResultScanner scanner = myuser.getScanner(scan);
        //打印出来
        for (Result result : scanner) {
            HBaseAPI.printCells(result.rawCells());
        }
    }


    /**
     * 列族过滤器FamilyFilter
     * 查询比f2列族小的所有的列族内的数据
     */
    @Test
    public void familyFilter() throws IOException {
        //创建scan对象
        Scan scan = new Scan();
        //添加过滤器
        scan.setFilter(new FamilyFilter(CompareFilter.CompareOp.EQUAL, new SubstringComparator("f1")));
        //查询
        ResultScanner scanner = myuser.getScanner(scan);
        //打印
        for (Result result : scanner) {
            HBaseAPI.printCells(result.rawCells());
        }
    }

    /**
     * hbase行键过滤器RowFilter
     * 通过RowFilter过滤比rowKey  0003小的所有值出来
     */
    @Test
    public void rowFilter() throws IOException {
        //创建scan对象
        Scan scan = new Scan();
        //添加条件
        scan.setFilter(new RowFilter(CompareFilter.CompareOp.LESS_OR_EQUAL, new BinaryComparator("0003".getBytes())));
        //查询数据
        ResultScanner scanner = myuser.getScanner(scan);
        //打印
        HBaseAPI.printScanner(scanner);
    }


    /**
     * 释放资源
     */
    @After
    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (myuser != null) {
            try {
                myuser.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

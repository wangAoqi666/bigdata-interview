package com.czxy.hbase.hbaseandmapreduce;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author 550894211@qq.com
 * @version v 1.0
 * @date 2019/12/18
 * <p>
 * <p>
 * 需求：读取HBase当中一张表的数据，然后将数据写入到HBase当中的另外一张表当中去。注意：我们可以使用TableMapper与TableReducer来实现从HBase当中读取与写入数据
 */
public class HbaseMapper extends TableMapper<Text, Put> {
    /**
     *
     * @param key  rowkey
     * @param value 一行数据
     * @param context 上下文对象
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    protected void map(ImmutableBytesWritable key, Result value, Context context) throws IOException, InterruptedException {
        //获取rowkey
        String rowkey = Bytes.toString(key.get());
        //创建put对象
        Put put = new Put(key.get());
        //获取cell集合
        Cell[] cells = value.rawCells();
        Arrays.stream(cells)
                .filter(cell -> "f1".equals(Bytes.toString(CellUtil.cloneFamily(cell))))
                .filter(cell -> "name".equals(Bytes.toString(CellUtil.cloneQualifier(cell))))
                .filter(cell -> "age".equals(Bytes.toString(CellUtil.cloneQualifier(cell))))
                .forEach(cell -> {
                    try {
                        put.add(cell);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        if(!put.isEmpty()){
            context.write(new Text(rowkey),put);
        }
    }

}

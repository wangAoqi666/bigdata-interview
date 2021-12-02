package com.czxy;

import com.google.common.io.Files;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;


import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Properties;

import static com.google.common.io.Files.readLines;

public class KafkaProducerDemo {
        public static void main(String[] args) throws IOException {
            // TODO: 2020/3/22 模拟生产者，请写出代码向18BD-50主题中生产数据test0-test99
            //1.获取默认配置
            Properties props = new Properties();
            props.put("bootstrap.servers", "node09:9092,node10:9092,node11:9092");
            props.put("acks", "all");
            props.put("retries", 0);
            props.put("batch.size", 16384);
            props.put("linger.ms", 1);
            props.put("buffer.memory", 33554432);
            props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            //自定义分区
            props.put("partitioner.class", "com.czxy.KafkaCustomPartitioner");
            //	手动提交每条数据
            //2.实例一个生产者
            KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(props);
            //3.模拟生产
            //请把给出的文件写入到kafka中，根据数据id进行分区，id为奇数的发送到一个分区中，偶数的发送到另一个分区
            //读取数据
            List<String> lines1 = readLines(new File("D:\\dev\\workspace04\\day20200413_spark_day04\\output1\\part-00000"), Charset.defaultCharset());
            List<String> lines2 = readLines(new File("D:\\dev\\workspace04\\day20200413_spark_day04\\output1\\part-00001"), Charset.defaultCharset());
            lines1.forEach(line -> kafkaProducer.send(new ProducerRecord<>("rng_comment" , line)));


            //4.关闭连接
            kafkaProducer.close();
    }
}

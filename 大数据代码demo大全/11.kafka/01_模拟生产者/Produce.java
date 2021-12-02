package com.czxy.test10;

import com.czxy.utils.KafkaUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * @author 27727
 */
public class Produce {
    public static void main(String[] args) {
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
        //	手动提交每条数据
        //2.实例一个生产者
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(produceProperties);
        //3.模拟生产
        //数据分发策略为轮询方式发送到每个分区中
        for (int i = 0; i < 100; i++) {
            kafkaProducer.send(new ProducerRecord<>("18BD-50" , "test" + i));
        }
        //4.关闭连接
        kafkaProducer.close();
    }
}

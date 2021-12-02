package com.czxy.test10;

import com.czxy.utils.KafkaUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

/**
 * @author 27727
 */
public class Consumer {
    public static void main(String[] args) {
        // TODO: 2020/3/22 模拟消费者，请写出代码把18BD-50主题中的0和2号分区的数据消费掉 ，打印输出到控制台
        //1.获取默认配置
        Properties props = new Properties();
        props.put("bootstrap.servers", "node09:9092,node10:9092,node11:9092");
        props.put("group.id", "test");
        //以下两行代码 ---消费者自动提交offset值
        props.put("enable.auto.commit", isCommitOffset);
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        //设置自动提交offset的周期
        props.put("auto.commit.interval.ms", "1000");
        return props;
        //设置当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，从头开始消费
        consumerProperties.put("auto.offset.reset", "earliest");
        //2.实例消费者
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(consumerProperties);
        //3.设置消费主题
        TopicPartition topicPartition0 = new TopicPartition("18BD-50", 0);
        TopicPartition topicPartition2 = new TopicPartition("18BD-50", 2);
        kafkaConsumer.assign(Arrays.asList(topicPartition0,topicPartition2));
        //消费指定分区0和分区2中的数据，并且设置消费0分区的数据offerset值从0开始，消费2分区的数据offerset值从10开始
        kafkaConsumer.seek(topicPartition0,0);
        kafkaConsumer.seek(topicPartition2,10);
        //4.死循环消费
        while (true){
            KafkaUtils.printConsumerData(kafkaConsumer.poll(1000));
        }
    }
}

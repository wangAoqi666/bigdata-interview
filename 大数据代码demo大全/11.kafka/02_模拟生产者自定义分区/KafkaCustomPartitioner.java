package com.czxy;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import java.util.Map;


public class KafkaCustomPartitioner implements Partitioner {
	@Override
	public void configure(Map<String, ?> configs) {
	}

	@Override
	public int partition(String topic, Object arg1, byte[] keyBytes, Object arg3, byte[] arg4, Cluster cluster) {
//		根据数据id进行分区，id为奇数的发送到一个分区中，偶数的发送到另一个分区
		int i = Integer.parseInt(arg3.toString().split("\t")[0]);
		return i%2==1?0:1;
	}

	@Override
	public void close() {
		
	}

}
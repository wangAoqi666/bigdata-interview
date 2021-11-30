package com.heibaiying.wordcount;

import com.heibaiying.wordcount.component.CountBolt;
import com.heibaiying.wordcount.component.DataSourceSpout;
import com.heibaiying.wordcount.component.SplitBolt;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.topology.TopologyBuilder;

public class ClusterWordCountApp {

    public static void main(String[] args) {
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("DataSourceSpout", new DataSourceSpout());
        // 指明将 DataSourceSpout 的数据发送到 SplitBolt 中处理
        builder.setBolt("SplitBolt", new SplitBolt()).shuffleGrouping("DataSourceSpout");
        //  指明将 SplitBolt 的数据发送到 CountBolt 中 处理
        builder.setBolt("CountBolt", new CountBolt()).shuffleGrouping("SplitBolt");

        // 使用StormSubmitter提交Topology到服务器集群
        try {
            StormSubmitter.submitTopology("ClusterWordCountApp",  new Config(), builder.createTopology());
        } catch (AlreadyAliveException | InvalidTopologyException | AuthorizationException e) {
            e.printStackTrace();
        }
    }

}

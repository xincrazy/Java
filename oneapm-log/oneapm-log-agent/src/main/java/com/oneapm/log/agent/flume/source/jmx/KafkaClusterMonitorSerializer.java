package com.oneapm.log.agent.flume.source.jmx;

import org.apache.flume.Context;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.net.InetAddress;
import java.util.*;

/**
 * Created by tang on 16-8-4.
 */
public class KafkaClusterMonitorSerializer extends  AbstractJMXSerializer{

    /**
     * @see com.oneapm.log.agent.flume.source.jmx.AbstractJMXSerializer#build()
     */
    @Override
    public void build() {
        try {
            //BytesInSpeedPerMinute
            addKeyValue(new ObjectName("kafka.server:type=BrokerTopicMetrics,name=BytesInPerSec"),
                    "OneMinuteRate","BytesInSpeedPerMinute");
            //BytesOutSpeedPerMinute
            addKeyValue(new ObjectName("kafka.server:type=BrokerTopicMetrics,name=BytesOutPerSec"),
                    "OneMinuteRate","BytesOutSpeedPerMinute");
            addKeyValue(new ObjectName("kafka.server:type=ReplicaManager,name=UnderReplicatedPartitions"),
                    "Value","UnderReplicatedPartitions");
            //MessagesInSpeedPerMinute
            addKeyValue(new ObjectName("kafka.server:type=BrokerTopicMetrics,name=MessagesInPerSec"),
                    "OneMinuteRate","MessagesInSpeedPerMinute");
            //刷文件耗时，单位ms
            addKeyValue(new ObjectName("kafka.log:type=LogFlushStats,name=LogFlushRateAndTimeMs"),
                    "95thPercentile","LogFlushMs95Percent");
        } catch (MalformedObjectNameException e) {
            log.error("{}",
                    e);
        }
    }

    @Override
    protected List<String> formateMetrics(Map<String, Object> metrics, Context ctx) {
        String hostname = "unknow";
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        }catch (Exception e) {
            e.printStackTrace();
        }
        final List<String> result = new ArrayList<>();
        for (String m: metrics.keySet()) {
            Map<String, Object> metric = new HashMap<>();
            metric.put("timestamp", System.currentTimeMillis());
            metric.put("hostname",hostname);
            metric.put("servicename","KafkaClusterMonitor");
            metric.put("servicetype","kafkaClusterMonitor");
            metric.put("metricname",m);
            metric.put("metricdata",metrics.get(m));
            result.add(jsonFormatter.apply(metric));

        }

        return result;
    }
}

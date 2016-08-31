package com.oneapm.log.agent.flume.parser.factory;

import com.oneapm.log.agent.flume.metric.akka.AkkaCheckerMetrics;
import com.oneapm.log.agent.flume.metric.druid.v073.DruidRealtimeNodeBatchDataSegmentAnnouncerMetrics;
import com.oneapm.log.agent.flume.metric.redis.v32.RedisMetrics;
import com.oneapm.log.agent.flume.metric.system.linux.SystemUsageMetrics;
import com.oneapm.log.agent.flume.parser.Parser;
import com.oneapm.log.agent.flume.parser.ai.consumer.v402.AIConsumerParser;
import com.oneapm.log.agent.flume.parser.akka.AkkaLoggingParser;
import com.oneapm.log.agent.flume.parser.common.CommonLoggingParser;
import com.oneapm.log.agent.flume.parser.druid.v073.DruidBatchDataSegmentAnnouncerMetricsParser;
import com.oneapm.log.agent.flume.parser.druid.v073.DruidLoggingEmitterMetricsParser;
import com.oneapm.log.agent.flume.parser.eswrapper.EsSinkWrapperParser;
import com.oneapm.log.agent.flume.parser.jvm.hotspot.JvmParser;
import com.oneapm.log.agent.flume.parser.kafka.v08.topic.KafkaTopicMonitorParser;
import com.oneapm.log.agent.flume.parser.redis.v32.RedisParser;
import com.oneapm.log.agent.flume.parser.system.linux.ProcessAliveCheckerParser;
import com.oneapm.log.agent.flume.parser.system.linux.SystemUsageParser;
import com.oneapm.log.agent.flume.parser.tomcat.TomcatLogParser;
import com.oneapm.log.agent.flume.parser.transform.TransformParser;

import java.util.Map;

public class LogParserFactory {

    private static LogParserFactory instance;


    public static LogParserFactory getInstance() {
        if (instance == null) {
            instance = new LogParserFactory();
        }

        return instance;
    }
    @Deprecated
    public Parser getParser(String metric) {
        for (AkkaCheckerMetrics akkaMetric : AkkaCheckerMetrics
                .values()) {
            if (akkaMetric.getAkkaCheckerContext().equals(metric)) {
                return AkkaLoggingParser.getParser();
            }
        }

        for (DruidRealtimeNodeBatchDataSegmentAnnouncerMetrics currentMetric : DruidRealtimeNodeBatchDataSegmentAnnouncerMetrics
                .values()) {
            if (currentMetric.getMetricContext().equals(metric)) {
                return DruidBatchDataSegmentAnnouncerMetricsParser.getParser();
            }
        }

        // System Usage getParser
        for (SystemUsageMetrics currentSytemUsage : SystemUsageMetrics.values()) {
            if (currentSytemUsage.getSystemUsageContext().equals(metric)) {
                return SystemUsageParser.getParser();
            }
        }

        //redis monitor getParaser
        for (RedisMetrics currentRedis : RedisMetrics.values()) {
            if (currentRedis.getRedisContext().equals(metric)) {
                return RedisParser.getParser();
            }
        }
        return null;
    }

    public Parser getParser() {
        // String name = Parser.
        return TransformParser.getParser();
    }

    public Parser getParserByClassName(Class<?> clazz) throws Exception {
        String name = clazz.getSimpleName();
        switch (name) {
            case "TransformInterceptor":
                return TransformParser.getParser();
            default:
                throw new Exception("can not found this class's parser");
        }

    }

    public Parser getParserByClass(Class<?> clazz, Map<String, String> map) throws Exception {
        String name = clazz.getSimpleName();
        switch (name) {
            case "CommonLogInterceptor":
                return CommonLoggingParser.getParser();
            case "TransformInterceptor":
                return TransformParser.getParser();
            case "EsSinkWrapperInterceptor":
                String indexName = map.get("indexName");
                String indexType = map.get("indexType");
                return EsSinkWrapperParser.getParser(indexName,indexType);
            case "ProcessAliveCheckerInterceptor":
                String processName = map.get("processName");
                String processKeyword = map.get("processKeyword");
                return ProcessAliveCheckerParser.getParser(processName, processKeyword);
            case "JvmInterceptor":
                String appName = map.get("appName");
                return JvmParser.getParser(appName);
            case "CassandraLogInterceptor":
                String version = map.get("version");
                switch (version) {
                    case "21":
                        return com.oneapm.log.agent.flume.parser.cassandra.v21.CassandraLogParser
                                .getParser(map);
                    case "37":
                        return com.oneapm.log.agent.flume.parser.cassandra.v37.CassandraLogParser
                                .getParser(map);
                    default:
                        throw new Exception("can not found this class's parser");
                }
            case "TomcatLogInterceptor":
                return TomcatLogParser.getParser(map.get("logType"));
            case "AiConsumerInterceptor":
                return AIConsumerParser.getParser();
            case "KafkaTopicMonitorInterceptor":
                String topic = map.get("topic");
                String zookeeperConnect = map.get("zookeeperConnect");
                String consumerGroup = map.get("consumerGroup");
                String clusterName = map.get("clusterName");
                return  KafkaTopicMonitorParser.getParser(topic,zookeeperConnect,consumerGroup,clusterName);
            case "SystemUsageInterceptor":
                return SystemUsageParser.getParser();
            case "DruidLogInterceptor":
                return DruidLoggingEmitterMetricsParser.getParser(map.get("druidNode"), map.get("tolerableRequestTime"));
            default:
                throw new Exception("can not found this class's parser");
        }
    }
}

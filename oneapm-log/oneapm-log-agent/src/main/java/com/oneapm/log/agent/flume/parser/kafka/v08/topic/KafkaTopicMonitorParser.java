package com.oneapm.log.agent.flume.parser.kafka.v08.topic;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oneapm.log.agent.flume.parser.Parser;
import com.oneapm.log.common.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class KafkaTopicMonitorParser implements Parser {

    private Logger logger = LoggerFactory.getLogger(KafkaTopicMonitorParser.class);
    private String topic;
    private String zookeeperConnect;
    private String consumerGroup;
    private String clusterName;

    public static Map<Integer, KafkaTopicMonitorParser> parserInstancePool = new HashMap<Integer, KafkaTopicMonitorParser>();

    public KafkaTopicMonitorParser(String topic,String zookeeperConnect,String consumerGroup,String clusterName){
        this.topic = topic;
        this.zookeeperConnect = zookeeperConnect;
        this.consumerGroup = consumerGroup;
        this.clusterName = clusterName;
    }

	public List<String> getKafkaTopicMonitorData(String topic,String zookeeperConnect,String consumerGroup) {
		String[] arr = new String[]{"--zkconnect="+zookeeperConnect,"--group="+consumerGroup,"--topic="+topic};
        List<String> metaData = new ArrayList<>();
        try {
            KafkaTopicMonitor kafkaTopicMonitor = new KafkaTopicMonitor();
            metaData.addAll(kafkaTopicMonitor.main(arr));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return metaData;
	}

	@Override
	public String parser(String content) {
        List<String> rowData = getKafkaTopicMonitorData(topic,zookeeperConnect,consumerGroup);
        if(rowData!=null) {
            String ts = String.valueOf(System.currentTimeMillis());
            String[] metricNames = {"offset", "logsize", "lag"};
            List<String> kafkaMetricData = new ArrayList<String>();
            for (String oneLineData : rowData) {
                oneLineData += (",clusterName:" + clusterName + ",timestamp:" + ts);
                LinkedHashMap<String, String> eventHashMap = JsonUtil.getMap(oneLineData);
                String oneLineDataJson = JsonUtil.getJacksonString(eventHashMap);
                Map<String, String> oneLineDataMap = null;
                try {
                    oneLineDataMap = new ObjectMapper().readValue(oneLineDataJson, new TypeReference<HashMap<String, String>>() {
                    });
                } catch (IOException e) {
                    logger.error("convert map error", e);
                }
                /**
                 * hostname->clusterName
                 * servicetype->topic_consumergroup
                 * subservicetype->pid
                 */
                Map<String, Object> kafkaMetricBase = new LinkedHashMap<>();
                kafkaMetricBase.put("timestamp", oneLineDataMap.get("timestamp"));
                kafkaMetricBase.put("hostname", oneLineDataMap.get("clusterName"));
                kafkaMetricBase.put("servicename", "kafkaMonitor");
                kafkaMetricBase.put("servicetype", oneLineDataMap.get("group") + "_" + oneLineDataMap.get("topic"));
                kafkaMetricBase.put("subservicetype", oneLineDataMap.get("pid"));
                ObjectMapper mapper = new ObjectMapper();
                for (String metricName : metricNames) {
                    Map<String, Object> kafkaMetric = new LinkedHashMap<>();
                    kafkaMetric.putAll(kafkaMetricBase);
                    kafkaMetric.put("metricname", metricName);
                    kafkaMetric.put("metricdata", Integer.valueOf(oneLineDataMap.get(metricName)));
                    String kafkaMetricJson = null;
                    try {
                        kafkaMetricJson = mapper.writeValueAsString(kafkaMetric);
                    } catch (JsonProcessingException e) {
                        logger.error("convert to json error", e);
                    }
                    kafkaMetricData.add(kafkaMetricJson);
                }
            }
            return String.valueOf(kafkaMetricData);
        }else {
            logger.error("get kafka data from scala error");
            return "error to find kafka service";
        }
	}

	public static KafkaTopicMonitorParser getParser(String topic,String zookeeperConnect,String consumerGroup,String clusterName) {
        int hashCode = topic.hashCode() ^ zookeeperConnect.hashCode()^consumerGroup.hashCode()^clusterName.hashCode();
        if (parserInstancePool.get(hashCode) != null) {
            return parserInstancePool.get(hashCode);
        } else {
            KafkaTopicMonitorParser parser = new KafkaTopicMonitorParser(topic,zookeeperConnect,consumerGroup,clusterName);
            parserInstancePool.put(Integer.valueOf(hashCode),parser);
            return parser;
        }
    }

}
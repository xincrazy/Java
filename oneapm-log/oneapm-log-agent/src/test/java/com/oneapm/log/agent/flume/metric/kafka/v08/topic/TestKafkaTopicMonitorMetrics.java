package com.oneapm.log.agent.flume.metric.kafka.v08.topic;

import org.junit.Assert;
import org.junit.Test;



public class TestKafkaTopicMonitorMetrics{
	
	@Test
	public void testGetKafkaTopicMonitorContext(){
		KafkaTopicMonitorMetrics e1 = KafkaTopicMonitorMetrics.KAFKA_TOPIC_MONITOR;
		String result = e1.getKafkaTopicMonitorContext();
		Assert.assertEquals("kafka/topicMonitor", result);
	}
	
	@Test 
	public void testTravEnum(){
		for(KafkaTopicMonitorMetrics e1 :KafkaTopicMonitorMetrics.values()){
			Assert.assertNotNull(e1.toString());
		}
	}
}
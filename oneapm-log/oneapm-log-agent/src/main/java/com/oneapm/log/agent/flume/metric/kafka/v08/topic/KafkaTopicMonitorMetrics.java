package com.oneapm.log.agent.flume.metric.kafka.v08.topic;

public enum KafkaTopicMonitorMetrics {
	KAFKA_TOPIC_MONITOR("kafka/topicMonitor");
	private String context;

	public String getKafkaTopicMonitorContext() {
		return this.context;
	}

	KafkaTopicMonitorMetrics(String context) {
		this.context = context;
	}
}

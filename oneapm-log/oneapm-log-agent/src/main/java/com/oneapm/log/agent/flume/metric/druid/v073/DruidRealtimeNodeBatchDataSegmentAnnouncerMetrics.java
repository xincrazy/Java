package com.oneapm.log.agent.flume.metric.druid.v073;

public enum DruidRealtimeNodeBatchDataSegmentAnnouncerMetrics {

	UNANNOUNCING_SEGMENT("Unannouncing segment");

	private String context;

	public String getMetricContext() {
		return this.context;
	}

	DruidRealtimeNodeBatchDataSegmentAnnouncerMetrics(String context) {
		this.context = context;
	}

}

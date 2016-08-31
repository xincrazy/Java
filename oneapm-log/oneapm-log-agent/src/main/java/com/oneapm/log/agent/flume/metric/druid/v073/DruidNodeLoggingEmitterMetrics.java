package com.oneapm.log.agent.flume.metric.druid.v073;

public enum DruidNodeLoggingEmitterMetrics {

	EVENT_THROWNAWAY("events/thrownAway"),
	EVENT_UNPARSEABLE("events/unparseable"),
	EVENT_PROCESSED("events/processed"),
	COORDINATOR_SEGMENT_SIZE("coordinator/segment/size"),
	COORDINATOR_SEGMENT_COUNT("coordinator/segment/count"),
	CACHE_DELTA_HITRATE("cache/delta/hitRate"),
	CACHE_TOTAL_HITRATE("cache/total/hitRate"),
	REQUEST_TIME("request/time");

	private String context;

	public String getMetricContext() {
		return this.context;
	}

	DruidNodeLoggingEmitterMetrics(String context) {
		this.context = context;
	}

}

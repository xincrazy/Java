package com.oneapm.log.agent.flume.metric.redis.v32;

public enum RedisMetrics {
	REDISINFO("redis/info");
	private String context;

	public String getRedisContext() {
		return this.context;
	}

	RedisMetrics(String context) {
		this.context = context;
	}
}

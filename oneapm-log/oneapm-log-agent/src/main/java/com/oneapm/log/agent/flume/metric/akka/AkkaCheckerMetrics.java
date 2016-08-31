package com.oneapm.log.agent.flume.metric.akka;

public enum AkkaCheckerMetrics {
	AKKA_COMMIT_CHECKER("event/commit/speed"),
	AKKA_COMMIT_BATCH_CHECKER("event/commitbatch/speed"),
	AKKA_SUCCESS_BATCH_CHECKER("event/successbatch/speed"),
	AKKA_FAILURE_BATCH_CHECKER("event/failurebatch/speed"),
	AKKA_KAFKA_CHECKER("kafka/consume/speed");
	private String context;

	public String getAkkaCheckerContext() {
		return this.context;
	}

	AkkaCheckerMetrics(String context) {
		this.context = context;
	}
}

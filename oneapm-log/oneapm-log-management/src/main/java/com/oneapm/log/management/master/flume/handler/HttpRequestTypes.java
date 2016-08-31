package com.oneapm.log.management.master.flume.handler;

public enum HttpRequestTypes {
	DEPLOY_AGENT("/deploy_agent"),DEPLOY_AGENTS("/deploy_agents");
	private String context;
	public String getHttpRequestType() {
		return this.context;
	}

	HttpRequestTypes(String context) {
		this.context = context;
	}
}

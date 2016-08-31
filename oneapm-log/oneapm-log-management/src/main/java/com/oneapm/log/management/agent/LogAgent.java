package com.oneapm.log.management.agent;

import lombok.Data;

@Data
public abstract class LogAgent {

	private String uuid;

	private String hostName;

	private int port;

	private String userName;

	private String password;

	private String flumeHomePath;

	private String confFilePath;

	private boolean alive;

	private String startTime;

	public String getUuid() {
		return hostName + ":" + confFilePath;
	}

}

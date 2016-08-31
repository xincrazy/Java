package com.oneapm.log.management.master.operation.shell;

public class AgentsParameter {
	private String agentHostName;

	private String agentUserName;

	private String agentPassword;

	private String agentInstallationPath;

	public void setAgentHostName(String agentHostName) {
		this.agentHostName = agentHostName;
	}

	public String getAgentHostName() {
		return this.agentHostName;
	}

	public void setAgentUserName(String agentUserName) {
		this.agentUserName = agentUserName;
	}

	public String getAgentUserName() {
		return this.agentUserName;
	}

	public void setAgentPassword(String agentPassword) {
		this.agentPassword = agentPassword;
	}

	public String getAgentPassword() {
		return this.agentPassword;
	}

	public void setAgentInstallationPath(String agentInstallationPath) {
		this.agentInstallationPath = agentInstallationPath;
	}

	public String getAgentInstallationPath() {
		return this.agentInstallationPath;
	}

}
package com.oneapm.log.management.master;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.PropertiesConfiguration;

import com.oneapm.log.management.agent.LogAgent;

public abstract class LogManager {

	public static String LOG_MANAGER_TYPE = "flume";
	public static int LOG_MANAGER_DEFAULT_PORT = 9660;
	protected Map<String, LogAgent> logAgentList = new HashMap<String, LogAgent>();

	public abstract void start();

	public abstract void stop();

	public abstract void restart();

	public abstract void deployAgent(PropertiesConfiguration config);

	public abstract void startAgent(PropertiesConfiguration config);

	public abstract String healthReporting();

	public abstract boolean checkAgentAlive();

	// TODO add a new agent into logAgentList
	public void addAgent(LogAgent logAgent) {

	}

	// TODO remove a agent from logAgentList
	public void removeAgent(String logAgentUuid) {

	}

	// TODO update logAgentList with latest agents status
	public void updateLogAgentList() {

	}

}

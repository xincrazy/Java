package com.oneapm.log.management.master.factory;

import com.oneapm.log.management.master.LogManager;
import com.oneapm.log.management.master.flume.FlumeLogManager;

public class LogManagerFactory {

	private static LogManagerFactory instance;

	public static LogManagerFactory getInstance() {
		if (instance == null) {
			instance = new LogManagerFactory();
		}

		return instance;
	}

	public LogManager getLogManager(String logManagerType) {
		if (logManagerType.equals(FlumeLogManager.LOG_MANAGER_TYPE)
				|| logManagerType == null) {
			return new FlumeLogManager();
		} else
			return null;
	}
}

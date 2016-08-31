package com.oneapm.log.management.util;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class LogUtil {

	public static String LOG_MASTER_LOG_NAME = "logmaster";
	private static String LOG4J_PROP_PATH = "conf/log4j.properties";

	static {
		PropertyConfigurator.configure(LOG4J_PROP_PATH);
	}

	private static Logger LOG;

	public static Logger getLogger() {
		if (LOG == null)
			LOG = Logger.getLogger(LOG_MASTER_LOG_NAME);

		return LOG;
	}

}

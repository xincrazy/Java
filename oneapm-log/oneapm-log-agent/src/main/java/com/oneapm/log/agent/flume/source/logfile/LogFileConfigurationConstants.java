package com.oneapm.log.agent.flume.source.logfile;

public class LogFileConfigurationConstants {

	/**
	 * Number of lines to read at a time
	 */
	public static final String CONFIG_LOG_FILE_PATH = "logFilePath";

	/**
	 * Number of lines to read at a time
	 */
	public static final String CONFIG_BATCH_SIZE = "batchSize";
	public static final int DEFAULT_BATCH_SIZE = 20;

	/**
	 * Amount of time to wait, if the buffer size was not reached, before to
	 * data is pushed downstream: : default 3000 ms
	 */
	public static final String CONFIG_BATCH_TIME_OUT = "batchTimeout";
	public static final long DEFAULT_BATCH_TIME_OUT = 3000l;

	/**
	 * Charset for reading input
	 */
	public static final String CHARSET = "charset";
	public static final String DEFAULT_CHARSET = "UTF-8";

	/**
	 * Grok Pattern
	 */
	public static final String CONFIG_GROK_PATTERN = "grokPattern";

	/**
	 * Log type
	 */
	public static final String CONFIG_LOG_TYPE = "logType";

	/**
	 * Log Subtype1
	 */
	public static final String CONFIG_SUBLOG1_TYPE = "subType1";

	/**
	 * Log Subtype2
	 */
	public static final String CONFIG_SUBLOG2_TYPE = "subType2";
	/**
	 * Log Subtype3
	 */
	public static final String CONFIG_SUBLOG3_TYPE = "subType3";
}

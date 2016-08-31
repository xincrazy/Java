package com.oneapm.log.common.constants;

public interface GrokConstants {
	
	// Default put the log.grok file under flume_home/conf dir
	public static final String DEFAULT_GROK_PATTERN_FILE_PATH = "log.grok";
	
	public static final String DEFAULT_GROK_NOT_MATCH_RESULT = "{}";
	
	// Key name of Grok output
	public static final String DEFAULT_GROK_KEY_NAME_CODELOCATION = "codelocation";
	public static final String DEFAULT_GROK_KEY_NAME_DAY = "day";
	public static final String DEFAULT_GROK_KEY_NAME_HOUR = "hour";
	public static final String DEFAULT_GROK_KEY_NAME_MILL = "mills";
	public static final String DEFAULT_GROK_KEY_NAME_MINUTE = "minute";
	public static final String DEFAULT_GROK_KEY_NAME_MONTH = "month";
	public static final String DEFAULT_GROK_KEY_NAME_YEAR = "year";
	public static final String DEFAULT_GROK_KEY_NAME_SECOND = "second";
	public static final String DEFAULT_GROK_KEY_NAME_TIMESTAMP = "timestamp";
	public static final String DEFAULT_GROK_KEY_NAME_LOGLEVEL = "loglevel";
	public static final String DEFAULT_GROK_KEY_NAME_LOGMSG = "logmessage";
	
}

package com.oneapm.log.agent.flume.parser.druid;

public interface DruidLogExtractor {
	public String getLogTimeRaw(String content);

	public String getLogTime(String content);

	public String getLogTimeUnixTimeStamp(String content);

	public String[] getLogArray(String content);

	public String[] getLogArrayByEvent(String content);

	public String getLogMillsNum(String[] logArray);

	public String getLogLevel(String[] logArray);

	public String getLogType(String[] logArray);

	public String getLogSource(String[] logArray);
}

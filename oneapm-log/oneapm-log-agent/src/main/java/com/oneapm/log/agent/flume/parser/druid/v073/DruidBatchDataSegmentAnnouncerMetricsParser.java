package com.oneapm.log.agent.flume.parser.druid.v073;

import java.util.LinkedHashMap;

import com.oneapm.log.agent.flume.parser.Parser;
import com.oneapm.log.agent.flume.parser.druid.DruidLogExtractor;
import com.oneapm.log.common.utils.FormatUnixTimeStampUtil;
import com.oneapm.log.common.utils.HostNameUtil;
import com.oneapm.log.common.utils.JsonUtil;
import com.oneapm.log.common.utils.StringUtil;
import com.oneapm.log.common.utils.TimeUtil;

public class DruidBatchDataSegmentAnnouncerMetricsParser implements Parser, DruidLogExtractor {

	private static DruidBatchDataSegmentAnnouncerMetricsParser parser;

	public static DruidBatchDataSegmentAnnouncerMetricsParser getParser() {
		if (parser == null)
			parser = new DruidBatchDataSegmentAnnouncerMetricsParser();

		return parser;
	}

	public LinkedHashMap<String, String> getMap(String content) {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		String[] logArray = getLogArrayByEvent(content);
		map.put("timestamp", getTimeStamp(logArray));
		map.put("hostname", HostNameUtil.getHostName());
		map.put("dataSource", getDateSource(logArray));
		map.put("metric", getMetric());
		map.put("value", getValue());
		return map;
	}

	@Override
	public String getLogTimeRaw(String content) {
		int index = content.indexOf(",");
		String time = content.substring(0, index);
		return time;
	}

	/**
	 * Type: logtime
	 */
	@Override
	public String getLogTime(String content) {
		String a = getLogTimeUnixTimeStamp(content);
		String b = getLogMillsNum(getLogArray(content));
		String result = StringUtil.addToString(a, b);
		return result;
	}

	@Override
	public String getLogTimeUnixTimeStamp(String content) {
		String string = getLogTimeRaw(content);
		String result = FormatUnixTimeStampUtil.getUnixTimeStamp(string);
		return result;
	}

	@Override
	public String[] getLogArray(String content) {
		return StringUtil.getArray(content, " ");
	}

	@Override
	public String[] getLogArrayByEvent(String content) {
		String[] temp = StringUtil.getArray(content, "[\\s]?Unannouncing segment[\\s]?");
		String[] beforeEvent = getLogArray(temp[0]);
		String[] afterEvent = StringUtil.getArray(temp[1], "[\\s]?at path[\\s]?");
		int i = 0;
		String result[] = new String[beforeEvent.length + afterEvent.length];
		for (i = 0; i < beforeEvent.length; i++)
			result[i] = beforeEvent[i];
		for (i = 0; i < afterEvent.length; i++)
			result[beforeEvent.length + i] = afterEvent[i];
		return result;
	}

	@Override
	public String getLogMillsNum(String[] logArray) {
		String num = logArray[1].split(",")[1];
		return num;
	}

	@Override
	public String getLogLevel(String[] logArray) {
		String level = logArray[2];
		return level;
	}

	@Override
	public String getLogType(String[] logArray) {
		String type = logArray[3];
		return type;
	}

	@Override
	public String getLogSource(String[] logArray) {
		String logSource = logArray[4];
		String result = logSource.substring(1, logSource.length() - 1);
		return result;
	}

	/**
	 * Type: dataSource
	 */
	public String getDateSource(String[] logArray) {
		String string = logArray[5];
		string = string.substring(1, string.length() - 1);
		String[] array = string.split("_");

		String result = "";
		for (int i = 0; i < array.length; i++) {
			String a = array[i];
			if (a.contains("T") && a.contains("Z") && a.contains("-") && a.contains(":"))
				break;
			result += a + "_";
		}
		result = result.substring(0, result.length() - 1);
		return result;
	}

	/**
	 * metric
	 */
	public String getMetric() {
		return "Unannouncing segment";
	}

	/**
	 * Type: value
	 */
	public String getValue() {
		return "1";
	}

	/**
	 * Type: timeStamp
	 */
	public String getTimeStamp(String[] logArray) {
		String string = logArray[5];
		string = string.substring(1, string.length() - 1);
		String[] array = string.split("_");
		String result = "";
		for (int i = 0; i < array.length; i++) {
			String a = array[i];
			if (a.contains("T") && a.contains("Z") && a.contains("-") && a.contains(":")) {
				result = a;
				break;
			}
		}
		return TimeUtil.formatTZ(result, "yyyy-MM-dd'T'HH:mm:ss.SSS");
	}

	@Override
	public String parser(String content) {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map = getMap(content);
		return JsonUtil.getJacksonString(map);
	}

}
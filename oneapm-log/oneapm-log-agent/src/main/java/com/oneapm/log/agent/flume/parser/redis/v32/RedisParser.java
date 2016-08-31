package com.oneapm.log.agent.flume.parser.redis.v32;

import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.oneapm.log.agent.flume.parser.Parser;
import com.oneapm.log.common.utils.JsonUtil;

public class RedisParser implements Parser {

	private static RedisParser parser;

	public static RedisParser getParser() {
		if (parser == null) {
			parser = new RedisParser();
		}
		return parser;
	}

	public String getRawRedisData(String content) {
		String[] string = content.split("\\s");
		String result = "";
		for (String string2 : string) {
			if (string2.charAt(string2.length() - 1) == ',') {
				result += string2;
			}
		}
		return result;
	}

	public String getUsedMemory(String string) {
		String usedMemory = "";
		Matcher m = Pattern.compile(".*used_memory:([0-9]+),.*").matcher(string);
		if (m.find()) {
			usedMemory = m.group(1);
		}
		return usedMemory;
	}

	public String getUsedMemoryUsage(String string) {
		String usedMemory = getUsedMemory(string);
		String totalSystemMemory = "";
		Matcher m = Pattern.compile(".*total_system_memory:([0-9]+),.*").matcher(string);
		if (m.find()) {
			totalSystemMemory = m.group(1);
		}
		long usedMemoryLong = Long.parseLong(usedMemory);
		long totalSystemMemoryLong = Long.parseLong(totalSystemMemory);
		DecimalFormat df = new DecimalFormat("0.00 ");
		double result = (1.0 * usedMemoryLong / totalSystemMemoryLong);
		return df.format(result);
	}

	public String getHits(String string) {
		String hits = "";
		Matcher m = Pattern.compile(".*keyspace_hits:([0-9]+),.*").matcher(string);
		if (m.find()) {
			hits = m.group(1);
		}
		return hits;
	}

	public String getHitRate(String string) {
		String missed = "";
		String hits = getHits(string);
		Matcher m = Pattern.compile(".*keyspace_misses:([0-9]+),.*").matcher(string);
		if (m.find()) {
			missed = m.group(1);
		}
		long hitsLong = Long.parseLong(hits);
		long missedLong = Long.parseLong(missed);
		DecimalFormat df = new DecimalFormat("0.00 ");
		double result = hitsLong * 1.0 / (hitsLong + missedLong);
		return df.format(result);
	}

	public String getKeysSum(String string) {
		String keys = "";
		Matcher m = Pattern.compile(".*keys=([0-9]+),.*").matcher(string);
		if (m.find()) {
			keys = m.group(1);
		}
		return keys;
	}

	public LinkedHashMap<String, String> getRedisMap(String content) {
		String string = getRawRedisData(content);
		String UsedMemory = getUsedMemory(string);
		String UsedMemoryUsage = getUsedMemoryUsage(string);
		String Hits = getHits(string);
		String HitRate = getHitRate(string);
		String KeysSum = getKeysSum(string);
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("keys", KeysSum);
		map.put("usedMemory", UsedMemory);
		map.put("usedMemoryUsage", UsedMemoryUsage);
		map.put("hits", Hits);
		map.put("hitRate", HitRate);
		return map;
	}

	@Override
	public String parser(String content) {

		return JsonUtil.getJacksonString(getRedisMap(content));

	}

}

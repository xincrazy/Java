package com.oneapm.log.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.LinkedHashMap;

public class JsonUtil {

	/**
	 * 将形如"feed":"metrics","timestamp":"2016-05-11T09:14:36.641Z"的字符串中的key-
	 * value提取出来,存入linkedhashmap。
	 * 2016/5/19修改,对形如不含引号的字符串：feed:metrics,timestamp:2016-05-11T09:14:36.
	 * 641Z同样适用。
	 *
	 * 2016/06/03修改，将双引号改为全部去掉
	 *
	 * @param string
	 * @return 一个LinkedHashMap类型的对象
	 */
	public static LinkedHashMap<String, String> getMap(String string) {
		LinkedHashMap<String, String> map = new LinkedHashMap<>();
		String[] keyValueArrays = StringUtil.getArray(string, ",");

		String keyValuePairs[] = new String[2];
		boolean isUser = false;
		for (String keyValue : keyValueArrays) {
		  if(keyValue.contains("\"")){
			  keyValue =keyValue.replaceAll("\"", "");
			  int index = keyValue.indexOf(":");
			  keyValuePairs[0] = keyValue.substring(0, index);
			  keyValuePairs[1] = keyValue.substring(index + 1);
			} else {
			  int index = keyValue.indexOf(":");
			  keyValuePairs[0] = keyValue.substring(0, index);
			  keyValuePairs[1] = keyValue.substring(index + 1);
			}
            if (isUser && !"log_timestamp".equals(keyValuePairs[0])) {
                map.put("dataSource", keyValuePairs[1]);
                isUser = false;
				break;
            } else {
                map.put(keyValuePairs[0], keyValuePairs[1]);
            }
            if ("value".equals(keyValuePairs[0])) {
                // user的数据在value之后， 所以当你碰到value的时候 表示下一个是user的
                isUser = true;
            }
		}
		return map;
	}

	/**
	 * 传入一个LinkedHashMap对象,将Map对象转换成一个JSON格式的字符串,该LinkedHashMap对象没有嵌套,
	 * 如果传入的对象含有嵌套,用getJacksonDeepString方法.
	 *
	 * @param map
	 * @return 一个Json字符串
	 */
	public static String getJacksonString(LinkedHashMap<String, String> map) {
		ObjectMapper mapper = new ObjectMapper();
		String result = "Can not transfom map to jacson!";
		try {
			result = mapper.writeValueAsString(map);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 将含有嵌套的LinkedHashMap类型转换成一个Json格式。
	 *
	 * @param map
	 *            LinkedHashMap类型
	 * @return 一个Json字符串
	 */
	public static String getJacksonDeepString(LinkedHashMap<String, Object> map) {
		ObjectMapper mapper = new ObjectMapper();
		String result = "Can not transfom map to jacson!";
		try {
			result = mapper.writeValueAsString(map);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void main(String[] args) {
		String string = " \"feed\":\"metrics\",\"timestamp\":\"2016-05-11T09:14:36.641Z\",\"service\":\"realtime\",\"host\":\"10.165.113.43:8084\",\"metric\":\"events/unparseable\",\"value\":\"23\",\"user2\":\"druid_metric_1day\"  ";
		LinkedHashMap<String, String> map = getMap(string);
		for (String string2 : map.keySet()) {
			System.out.println(string2 + ":" + map.get(string2));
		}
		String result = getJacksonString(map);
		System.out.println(result);

	}
}
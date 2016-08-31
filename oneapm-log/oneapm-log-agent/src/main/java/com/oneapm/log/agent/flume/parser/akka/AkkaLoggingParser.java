package com.oneapm.log.agent.flume.parser.akka;

import com.oneapm.log.agent.flume.parser.Parser;
import com.oneapm.log.common.constants.GrokConstants;
import com.oneapm.log.common.grok.GrokBean;
import com.oneapm.log.common.utils.TimeUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static com.oneapm.log.common.grok.GrokBean.mapper;

public class AkkaLoggingParser implements Parser {

	private static final Logger logger = LoggerFactory.getLogger(AkkaLoggingParser.class);

	private static AkkaLoggingParser parser;

	public static AkkaLoggingParser getParser() {
		if (parser == null) {
			parser = new AkkaLoggingParser();
		}
		return parser;
	}

	/**
	 * Get the json from the log for elasticsearch
	 * @param content
	 *    The content represents the log
	 * @return
	 *    The return is the json for elasticsearch
     */
	public String getDataArray(String content) {

		logger.debug("Start parsing the content");
		String resultJson=null ;
		GrokBean gb = new GrokBean(
				this.getClass().getResource("/").getPath() + GrokConstants.DEFAULT_GROK_PATTERN_FILE_PATH,
				"%{AKKALOG:akka}");

		AkkaLogResult akkaLogResult = gb.matchLog(content).getObject(AkkaLogResult.class);
		if (akkaLogResult == null) {
			logger.debug("Grok match result is null...");
			return "";
		}
		try{
			String jsonString=akkaLogResult.getLogmessage();
			logger.debug("Grok logmessage "+jsonString);
			resultJson=convertJson(jsonString);

		}catch (NullPointerException e){
            e.printStackTrace();
		}
		return resultJson;
	}

	/**
	 * @param grobJson
	 * @return
     */
	public String convertJson(String grobJson){
		String resultJson=null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			LinkedHashMap hashMap = new LinkedHashMap();
			HashMap<String, String> hashmap = mapper.readValue(grobJson, HashMap.class);

			hashMap.put("timestamp", TimeUtil.formatTZ(hashmap.get("timestamp"), "yyyy-MM-dd HH:mm:ss"));
			hashMap.put("hostname",hashmap.get("host"));
			hashMap.put("servicename",hashmap.get("service"));
			hashMap.put("servicetype","akka");
			hashMap.put("metricname",hashmap.get("metric"));
			hashMap.put("metricdata", Double.parseDouble(String.valueOf(hashmap.get("value"))));
			resultJson=mapper.writeValueAsString(hashMap);
			logger.debug("The result content " + resultJson);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resultJson;
	}


	/**
	 * Private class used to store the Grok match result.
	 */

	@ToString
	@Getter
	@Setter
	private static class AkkaLogResult {
		String akka;
		String codelocation;
		String day;
		String hour;
		String loglevel;
		String logmessage;
		String mills;
		String minute;
		String month;
		String second;
		String threadname;
		String timestamp;
		String year;
	}


	@Override
	public String parser(String content) {
		
		return getDataArray(content);
	}

}

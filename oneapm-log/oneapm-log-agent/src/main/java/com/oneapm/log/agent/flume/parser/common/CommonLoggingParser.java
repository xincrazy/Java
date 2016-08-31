package com.oneapm.log.agent.flume.parser.common;

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

public class CommonLoggingParser implements Parser {

	private static final Logger logger = LoggerFactory.getLogger(CommonLoggingParser.class);

	private static CommonLoggingParser parser;

	public static CommonLoggingParser getParser() {
		if (parser == null) {
			parser = new CommonLoggingParser();
		}
		return parser;
	}

	@Override
	public String parser(String content) {
		
		return content;
	}

}

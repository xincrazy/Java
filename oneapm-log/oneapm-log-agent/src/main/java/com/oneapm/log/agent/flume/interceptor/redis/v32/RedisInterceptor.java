package com.oneapm.log.agent.flume.interceptor.redis.v32;

import java.util.Iterator;
import java.util.List;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;

import com.google.common.base.Charsets;
import com.oneapm.log.agent.flume.interceptor.Constants;
import com.oneapm.log.agent.flume.parser.Parser;
import com.oneapm.log.agent.flume.parser.factory.LogParserFactory;
import com.oneapm.log.common.utils.StringUtil;

public class RedisInterceptor implements Constants, Interceptor {

	public static final String REDIS_TYPES="redisTypes";
	public static final String DEFUALT_REDIS_TYPE = "redis/info";
	public String[] redis;
	
	public RedisInterceptor(String redisTypes){
		this.redis = StringUtil.getArray(redisTypes, INTERCEPTOR_DEFAULT_REDIS_SEPERATOR);
	}
	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub

	}

	@Override
	public Event intercept(Event event) {
		String eventBodyStr = new String(event.getBody(), Charsets.UTF_8);
		String matchedRedis = StringUtil.containStr(eventBodyStr, redis);
		Parser parser = null;
		if (matchedRedis != null) {
			parser = LogParserFactory.getInstance().getParser(matchedRedis);
		}
		if (parser != null) {
			String parserResult = parser.parser(eventBodyStr);
			event.setBody(parserResult.getBytes(Charsets.UTF_8));
		} else
			event.setBody("NO parser is found!".getBytes(Charsets.UTF_8));
		return event;
	}

	@Override
	public List<Event> intercept(List<Event> events) {
		for (Iterator<Event> iterator = events.iterator(); iterator.hasNext();) {

			Event next = iterator.next();
			intercept(next);

			if (next == null) {
				iterator.remove();
			}
		}
		return events;
	}
	public static class RedisInterceptorBuilder implements Interceptor.Builder{
		private String redisTypes;
		@Override
		public void configure(Context content) {
			redisTypes = content.getString(REDIS_TYPES,DEFUALT_REDIS_TYPE);
		}

		@Override
		public Interceptor build() {
			return new RedisInterceptor(redisTypes);
		}
		
	}

}

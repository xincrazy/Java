package com.oneapm.log.agent.flume.interceptor.redis.v32;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.event.EventBuilder;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Test.None;

import com.oneapm.log.common.utils.StringUtil;


public class TestRedisInterceptor {

	public static final String REDIS_TYPES="redisTypes";
	public static final String DEFUALT_REDIS_TYPE = "redis/info";
	public String[] redis;
	public static final String INTERCEPTOR_DEFAULT_REDIS_SEPERATOR=",";
	private String redisTypes;

	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test(expected=None.class)
	public void testClose(){
		String redisTypes = "redisTypes1,redisTypes2";
		RedisInterceptor redisInterceptor = new RedisInterceptor (redisTypes);
		redisInterceptor.close();
		redisInterceptor.initialize();
	}
	
	@Test
	public void testInterceptEvent() {
		String redisTypes = "events/thrownAway,redisTypes2";
		RedisInterceptor redisInterceptor = new RedisInterceptor (redisTypes);
		redis = StringUtil.getArray(redisTypes, INTERCEPTOR_DEFAULT_REDIS_SEPERATOR);
		Event event = EventBuilder.withBody("hello", Charset.forName("UTF-8"));
		Event event1 = EventBuilder.withBody("redisTypes2", Charset.forName("UTF-8"));
		Event result = redisInterceptor.intercept(event);	
		Assert.assertNotNull(result);
		Event result1 = redisInterceptor.intercept(event1);	
		Assert.assertNotNull(result1);
	}
	
	@Test
	public void testInterceptListOfEvent() {
		String redisTypes = "redisTypes,redisTypes2";
		RedisInterceptor redisInterceptor = new RedisInterceptor (redisTypes);
		Event event = EventBuilder.withBody("redisTypes", Charset.forName("UTF-8"));
		List<Event> events = new ArrayList<Event>();
		events.add(event);
		List<Event> result =redisInterceptor.intercept(events);
		Assert.assertNotNull(result);
	}
	
	@Test
	public void testConfigure(){
		Context content = new Context();
		RedisInterceptor.RedisInterceptorBuilder redisInterceptorBuilder = new RedisInterceptor .RedisInterceptorBuilder();
		redisInterceptorBuilder.configure(content);
		redisTypes = content.getString(REDIS_TYPES,DEFUALT_REDIS_TYPE);
		Assert.assertNotNull(redisTypes);
		redisInterceptorBuilder.build();
		RedisInterceptor redis = new RedisInterceptor(redisTypes);
		Assert.assertNotNull(redis);
	}

}

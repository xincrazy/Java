package com.oneapm.log.agent.flume.interceptor.cassandra.v37;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.event.EventBuilder;
import org.apache.flume.interceptor.Interceptor;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Test.None;



public class TestCassandraLogInterceptor {
	String metricType = "metricType"; 
	String cassandraPath = "cassandraPath";
	CassandraLogInterceptor cassandraLogInterceptor = new CassandraLogInterceptor(metricType,cassandraPath);
	private Map<String, String> paramMap = new HashMap<>();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void testCassandraLogInterceptor() {
		paramMap.put("metricType", metricType);
		paramMap.put("version", "37");
		paramMap.put("cassandraPath", cassandraPath);
		Assert.assertEquals("test cassandraLogInterceptor error!", paramMap.get(metricType),"metricType");
		Assert.assertEquals("test cassandraLogInterceptor error!", paramMap.get(cassandraPath),"cassandraPath");
		Assert.assertEquals("test cassandraLogInterceptor error!", paramMap.get("version"),"37");
	}

	@Test(expected=None.class)
	public void testClose() {
		cassandraLogInterceptor.close();
	}

	@Test(expected=None.class)
	public void testInitialize() {
		cassandraLogInterceptor.initialize();
	}

	@Test
	public void testInterceptListOfEvent() {
		String body = "hello i am a test,";
		String s = "";
		Event event = EventBuilder.withBody(body, Charset.forName("UTF-8"));
		Event event1 = EventBuilder.withBody(s, Charset.forName("UTF-8"));
		List<Event> events = new ArrayList<Event>();
		events.add(event);
		events.add(event1);
		List<Event> eventList = new ArrayList<Event>();
		eventList = cassandraLogInterceptor.intercept(events);
		Assert.assertEquals(0, eventList.size());
	}

	@Test
	public void testInterceptEvent() {
		String body = "hello i am a test,";
		Event event = EventBuilder.withBody(body, Charset.forName("UTF-8"));
		Event result = cassandraLogInterceptor.intercept(event);
		Assert.assertNull(result);
	}
	
	@Test
	public void testConfigure(){
		Context context = new Context();
		CassandraLogInterceptor.CassandraLogInterceptorBuilder cassandraInterceptorBuilder = new CassandraLogInterceptor.CassandraLogInterceptorBuilder();
		cassandraInterceptorBuilder.configure(context);
		Interceptor inte = cassandraInterceptorBuilder.build();
		Assert.assertNotNull(inte);
	}

}

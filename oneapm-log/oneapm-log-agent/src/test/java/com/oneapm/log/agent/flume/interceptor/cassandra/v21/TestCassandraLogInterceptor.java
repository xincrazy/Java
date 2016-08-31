package com.oneapm.log.agent.flume.interceptor.cassandra.v21;

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
		paramMap.put("version", "21");
		paramMap.put("cassandraPath", cassandraPath);
		Assert.assertEquals("test cassandraLogInterceptor error! ", paramMap.get(metricType),"metricType");
		Assert.assertEquals("test cassandraLogInterceptor error! ", paramMap.get("version"),"21");
		Assert.assertEquals("test cassandraLogInterceptor error! ", paramMap.get(cassandraPath),"cassandraPath");
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
		Event event = EventBuilder.withBody("hello#",Charset.forName("UTF-8"));
		List<Event> events = new ArrayList<>();
		events.add(event);
		List<Event> event1 = cassandraLogInterceptor.intercept(events);
		Assert.assertNotNull(event1);
		Assert.assertEquals(0, event1.size());
	}

	@Test
	public void testInterceptEvent() {
		paramMap.put("metricType", metricType);
		paramMap.put("version", "21");
		paramMap.put("cassandraPath", cassandraPath);
		Event event = EventBuilder.withBody("Event#thrownAway",Charset.forName("UTF-8"));
		Event event1 = cassandraLogInterceptor.intercept(event);
		Assert.assertNull(event1);
	}

	@Test
	public void testConfigure(){
		CassandraLogInterceptor. CassandraLogInterceptorBuilder cassandraLogInterceptorBuilder
			= new CassandraLogInterceptor. CassandraLogInterceptorBuilder();
		Context context = new Context();
		cassandraLogInterceptorBuilder.configure(context);
		Interceptor inte = cassandraLogInterceptorBuilder.build();
		Assert.assertNotNull(inte);	
	}
}
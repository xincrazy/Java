package com.oneapm.log.agent.flume.interceptor.transform;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.event.EventBuilder;
import org.apache.flume.interceptor.Interceptor;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Test.None;


public class TestTransformInterceptor {
	
	TransformInterceptor transformInterceptor = new TransformInterceptor();


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test(expected=None.class)
	public void testClose() {
		transformInterceptor.close();
		transformInterceptor.initialize();
	}
	
	@Test
	public void testInterceptEvent() {
		String string =
		        "{\"key\":\"system\",\"eventCategory\":\"RawMetricEvent\",\"metrics\":{\"iZ25979ub3rZ#&maxDiskUsage\":\"0.45\"},\"ttl\":\"2400\",\"timestamp\":\"1465368478064\"}";
		Event event = EventBuilder.withBody( string,Charset.forName("UTF-8"));
		Event result = transformInterceptor.intercept(event);
		Assert.assertNotNull(result);
	}
	
	@Test
	public void testInterceptListOfEvent() {
		String string =
		        "{\"key\":\"system\",\"eventCategory\":\"RawMetricEvent\",\"metrics\":{\"iZ25979ub3rZ#&maxDiskUsage\":\"0.45\"},\"ttl\":\"2400\",\"timestamp\":\"1465368478064\"}";
		Event event = EventBuilder.withBody(string,Charset.forName("UTF-8"));
		List<Event> events = new ArrayList<Event>();
		events.add(event);
		List<Event> result = transformInterceptor.intercept(events);
		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
	}

	@Test
	public void  testConfigure()
	{
		TransformInterceptor.TransformInterceptorBuilder transformInterceptorBuilder = new TransformInterceptor.TransformInterceptorBuilder();
		Context content = new Context();
		transformInterceptorBuilder.configure(content);
		Assert.assertNotNull(transformInterceptorBuilder);
		Interceptor trans = transformInterceptorBuilder.build();
		Assert.assertNotNull(trans);
	}
}

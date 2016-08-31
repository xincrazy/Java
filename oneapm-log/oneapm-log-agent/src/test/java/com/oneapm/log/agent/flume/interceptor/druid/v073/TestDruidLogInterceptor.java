package com.oneapm.log.agent.flume.interceptor.druid.v073;

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

import com.oneapm.log.common.utils.StringUtil;

public class TestDruidLogInterceptor {
	
	public static final String DRUID_METRIC_TYPES = "druidMetricTypes";
	public static final String DEFAULT_DRUID_METRIC_TYPE = "events/thrownAway";
	String INTERCEPTOR_DEFAULT_METRICS_SEPERATOR = ",";
	private Map<String, String> paramMap = new HashMap<>();
	private String[] druidMetrics;
	
	String druidMetricTypes = "druidMetricTypes";
	String druidNode = "druidNode";
	String tolerableRequestTime = "tolerableRequestTime";
	DruidLogInterceptor druidLogInterceptor = new DruidLogInterceptor(druidMetricTypes,druidNode,tolerableRequestTime); 
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void testDruidLogInterceptor() {
		
		druidMetrics = StringUtil.getArray(druidMetricTypes, INTERCEPTOR_DEFAULT_METRICS_SEPERATOR);
		Assert.assertEquals("druidMetricTypes", druidMetrics[0]);
		paramMap.put("druidNode", druidNode);
		paramMap.put("tolerableRequestTime", tolerableRequestTime);
		Assert.assertEquals("druidNode",paramMap.get(druidNode));
		Assert.assertEquals("tolerableRequestTime",paramMap.get(tolerableRequestTime));
	}
	
	@Test(expected=None.class)
	public void testClose() {
		druidLogInterceptor.close();
	}

	@Test(expected=None.class)
	public void testInitialize() {
		druidLogInterceptor.initialize();
	}
	

	@Test
	public void testInterceptEvent() {
		String str = "";
		Event event = EventBuilder.withBody(str,Charset.forName("UTF-8"));
		Event result = druidLogInterceptor.intercept(event);
		Assert.assertNull(result);
	}

	@Test
	public void testInterceptListOfEvent() {
		String str = "{\"feed\":\"metrics\",\"timestamp\":\"2016-05-11T09:14:36.640Z\",\"service\""
				+ ":\"realtime\",\"host\":\"10.165.113.43:8084\",\"metric\":\"events/thrownAway\","
				+ "\"value\":8889,\"user2\":\"druid_metric_1day\"}";
		Event event1 = EventBuilder.withBody(str,Charset.forName("UTF-8"));
		List<Event> events = new ArrayList<Event>();
		events.add(event1);
		List<Event> result = druidLogInterceptor.intercept(events);
		Assert.assertNotNull(result);
		Assert.assertEquals(0, result.size());
	}
	
	@Test
	public void testCongigure(){
		DruidLogInterceptor.DruidLogInterceptorBuilder druidLogInterceptorBuilder = new DruidLogInterceptor.DruidLogInterceptorBuilder();
		Context context = new Context();
		druidLogInterceptorBuilder .configure(context);
		druidMetricTypes = context.getString(DRUID_METRIC_TYPES, DEFAULT_DRUID_METRIC_TYPE);
		Assert.assertNotNull(druidMetricTypes);
		Interceptor dru = druidLogInterceptorBuilder.build();
		Assert.assertNotNull(dru);	
	}
	
	

}

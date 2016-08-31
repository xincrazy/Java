package com.oneapm.log.agent.flume.interceptor.druid.v073;

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

public class TestDruidLogInterceptorBak {

	public static final String DRUID_METRIC_TYPE = "druidMetricType";
	public static final String DEFAULT_DRUID_METRIC_TYPE = "events/thrownAway";
	String INTERCEPTOR_DEFAULT_METRICS_SEPERATOR = ",";
	 private String druidMetricType;

	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void testClose() {
		String druidMetricType="helloworldiamhere";
		DruidLogInterceptorBak  druidLogInterceptorBak = new  DruidLogInterceptorBak(druidMetricType);
		druidLogInterceptorBak.close();
		
	}


	@Test(expected=None.class)
	public void testInitialize() {
		String druidMetricType="helloworldiamhere";
		DruidLogInterceptorBak  druidLogInterceptorBak = new  DruidLogInterceptorBak(druidMetricType);
		druidLogInterceptorBak.initialize();
	}
	
	@Test
	public void testInterceptEvent() {
		String druidMetricType="helloworldiamhere";
		DruidLogInterceptorBak  druidLogInterceptorBak = new  DruidLogInterceptorBak(druidMetricType);
		Event event = EventBuilder.withBody(DRUID_METRIC_TYPE,Charset.forName("UTF-8"));
		Event ev = druidLogInterceptorBak.intercept(event);
		Assert.assertNotNull(ev);
	}

	@Test
	public void testInterceptListOfEvent() {
		String druidMetricType = "hello";
		DruidLogInterceptorBak  druidLogInterceptorBak = new  DruidLogInterceptorBak(druidMetricType);		
		Event event1 = EventBuilder.withBody("hello",Charset.forName("UTF-8"));
		Event event2 = EventBuilder.withBody("druidMetricType",Charset.forName("UTF-8"));
		List<Event> events = new ArrayList<Event>();
		events.add(event1);
		events.add(event2);
		List<Event> event =druidLogInterceptorBak.intercept(events);
		Assert.assertNotNull(event);
		Assert.assertEquals(2, event.size());
	}
	
	@Test
	public void testConfigure(){
		DruidLogInterceptorBak.DruidLogInterceptorBuilder druidLogInterceptorBuilder = new DruidLogInterceptorBak .DruidLogInterceptorBuilder();
		Context context = new Context();
		druidLogInterceptorBuilder .configure(context);
		druidMetricType = context.getString(DRUID_METRIC_TYPE,DEFAULT_DRUID_METRIC_TYPE);
		Assert.assertNotNull(druidMetricType);
		druidLogInterceptorBuilder.build();
		DruidLogInterceptorBak dru = new DruidLogInterceptorBak(druidMetricType);
		Assert.assertNotNull(dru);
	}

}

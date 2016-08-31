package com.oneapm.log.agent.flume.interceptor.akka;

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

import com.oneapm.log.common.utils.StringUtil;


public class TestAkkaLogInterceptor {
	
	public static final String INTERCEPTOR_DEFAULT_METRICS_SEPERATOR = ",";
	String akkaMetricTypes = "AkkaMetricTypes1,akkaMetrics2";
	AkkaLogInterceptor akkaLogInterceptor = new AkkaLogInterceptor(akkaMetricTypes);
	private String[] akkaMetrics;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void testAkkaLogInterceptor() {
		
		akkaMetrics = StringUtil.getArray(akkaMetricTypes, INTERCEPTOR_DEFAULT_METRICS_SEPERATOR);
		Assert.assertEquals("test AkkaLogInterceptor error! ", akkaMetrics[0],"AkkaMetricTypes1");
		Assert.assertEquals("test AkkaLogInterceptor error! ", akkaMetrics[1],"akkaMetrics2");
	}

	@Test(expected=None.class)
	public void testClose() {
		akkaLogInterceptor .close();
	}

	@Test(expected=None.class)
	public void testInitialize() {
		akkaLogInterceptor .initialize();
	}

	@Test
	public void testInterceptEvent() {
		Event event = EventBuilder.withBody("AkkaMetricTypes1",Charset.forName("UTF-8"));
		event = akkaLogInterceptor.intercept(event);
		Event event1 = EventBuilder.withBody("hello",Charset.forName("UTF-8"));
		event1 = akkaLogInterceptor.intercept(event1);
		Assert.assertNotNull(event);
		Assert.assertNull(event1);
	}

	@Test
	public void testInterceptListOfEvent() {
		Event event = EventBuilder.withBody("AkkaMetricTypes1",Charset.forName("UTF-8"));
		Event event2 = EventBuilder.withBody("akkaMetrics2",Charset.forName("UTF-8"));
		Event event3 = EventBuilder.withBody("hello",Charset.forName("UTF-8"));
		List<Event> events = new ArrayList<Event>();
		events.add(event);
		events.add(event2);
		events.add(event3);
		List<Event> event1 = akkaLogInterceptor.intercept(events);
		Assert.assertNotNull(event1);
		Assert.assertEquals(2, event1.size());
	}
	
	@Test
	public void testConfigure(){
		AkkaLogInterceptor.AkkaLogInterceptorBuilder akkaLogInterceptorBuilder = new AkkaLogInterceptor.AkkaLogInterceptorBuilder();
		Context context = new Context();
		akkaLogInterceptorBuilder.configure(context);
		Interceptor inte = akkaLogInterceptorBuilder.build();
		Assert.assertNotNull(inte);
	}

}
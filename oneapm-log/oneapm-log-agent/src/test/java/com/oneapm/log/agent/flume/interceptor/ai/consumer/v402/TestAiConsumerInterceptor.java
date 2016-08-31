package com.oneapm.log.agent.flume.interceptor.ai.consumer.v402;

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

public class TestAiConsumerInterceptor {
	
	AiConsumerInterceptor aiConsumerInterceptor = new AiConsumerInterceptor();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test(expected=None.class)
	public void testClose() {
		aiConsumerInterceptor.close();
	}

	@Test(expected=None.class)
	public void testInitialize() {
		aiConsumerInterceptor.initialize();
	}

	@Test
	public void testInterceptEvent() {
		Event event = EventBuilder.withBody("Event/thrownAway",Charset.forName("UTF-8"));
		aiConsumerInterceptor.intercept(event);
	}

	@Test
	public void testInterceptListOfEvent() {
		Event event = EventBuilder.withBody("Event/thrownAway",Charset.forName("UTF-8"));
		List<Event> events = new ArrayList<>();
		events.add(event);
		List<Event> event1 = aiConsumerInterceptor.intercept(events);
		Assert.assertNotNull(events);
		Assert.assertEquals(0,event1.size() );
	}

	@Test
	public void Confiure(){
		AiConsumerInterceptor.AiConsumerInterceptorBuilder aiConsumerInterceptorBuilder = new AiConsumerInterceptor.AiConsumerInterceptorBuilder();
		Context content = new Context();
		aiConsumerInterceptorBuilder.configure(content);
		Interceptor inte = aiConsumerInterceptorBuilder.build();
		Assert.assertNotNull(inte);	
	}
}
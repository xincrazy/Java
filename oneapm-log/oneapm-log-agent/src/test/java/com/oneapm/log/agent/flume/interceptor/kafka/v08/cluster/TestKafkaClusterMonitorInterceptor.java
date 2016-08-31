package com.oneapm.log.agent.flume.interceptor.kafka.v08.cluster;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.flume.Event;
import org.apache.flume.event.EventBuilder;
import org.apache.flume.interceptor.Interceptor;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Test.None;

public class TestKafkaClusterMonitorInterceptor{
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}
	
	@Test(expected=None.class)
	public void testInitialize() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
	InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Class c = Class.forName("com.oneapm.log.agent.flume.interceptor.kafka.v08.cluster.KafkaClusterMonitorInterceptor");
		Constructor c0 = c.getDeclaredConstructor();
		c0.setAccessible(true);		
		KafkaClusterMonitorInterceptor kafka=(KafkaClusterMonitorInterceptor)c0.newInstance();
		kafka.initialize();
		kafka.close();
	}
	
	@Test
	public void testIntercept() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
	InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Class c = Class.forName("com.oneapm.log.agent.flume.interceptor.kafka.v08.cluster.KafkaClusterMonitorInterceptor");
		Constructor c0 = c.getDeclaredConstructor();
		c0.setAccessible(true);		
		KafkaClusterMonitorInterceptor kafka=(KafkaClusterMonitorInterceptor)c0.newInstance();
		Event event = EventBuilder.withBody("hello", Charset.defaultCharset());
		Event eventresult = kafka.intercept(event);
		Assert.assertNotNull(eventresult);
		List<Event> events = new ArrayList<>();
		events.add(event);
		List<Event> resultList = kafka.intercept(events);
		Assert.assertNotNull(resultList);
	}  
	
	@Test
	public void testConfigure(){
		KafkaClusterMonitorInterceptor.KafkaClusterMonitorInterceptorBuilder kafkacluster 
		= new KafkaClusterMonitorInterceptor.KafkaClusterMonitorInterceptorBuilder();
		 Interceptor result = kafkacluster.build();
		Assert.assertNotNull(result);
		
		
	}
}